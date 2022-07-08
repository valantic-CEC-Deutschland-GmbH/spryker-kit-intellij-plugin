package com.valantic.cec.sprykerplugin.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsActions;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.PhpCodeUtil;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.PhpFileImpl;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import com.valantic.cec.sprykerplugin.constants.SprykerConstants;
import com.valantic.cec.sprykerplugin.forms.ZedBusinessFactoryMethodParameterDialog;
import com.valantic.cec.sprykerplugin.icons.ValanticIcons;
import com.valantic.cec.sprykerplugin.model.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateMethodFromTemplateAction extends AbstractFromTemplateAction {

    private String className;
    private boolean isProvidedDependency    ;
    private List<String> listOfDependencies;

    public CreateMethodFromTemplateAction(
            @Nullable @NlsActions.ActionDescription String description,
            Context context,
            String methodName,
            boolean enabled
    ) {
        super(description, ValanticIcons.methodIcon, context, methodName, enabled);
        this.context = context;
        this.methodName = methodName;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String templatePathString = getTemplatePathString();

        ZedBusinessFactoryMethodParameterDialog dialog = new ZedBusinessFactoryMethodParameterDialog(e.getProject(), new String[]{"TestDependency"});
        boolean exitOk = dialog.showAndGet();

        if (exitOk) {
            className = dialog.getClassNameToCreateOrGet();
            isProvidedDependency = dialog.isProvidedDependency();
            listOfDependencies = dialog.getListOfDependenciesForCreation();
        } else {
            return;
        }
        try {
            writeMethodFromTemplate(templatePathString);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void writeMethodFromTemplate(String templatePathString) throws IOException
    {
        PebbleEngine engine = new PebbleEngine.Builder().build();

        Map<String, Object> pebbleContext = new HashMap<>();

        pebbleContext.put("organization", SprykerConstants.PROJECTNAMESPACE);
        pebbleContext.put("module", context.getModuleName());
        pebbleContext.put("className", this.className);
        pebbleContext.put("factoryMethod", this.getMethodName());
        pebbleContext.put("dependencyMethods", new String[] {"createSomeThing", "getSomethingClient", "getEntityManager"});

        PebbleTemplate compiledTemplate = engine.getTemplate(templatePathString);

        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);

        compiledTemplate.evaluate(writer, pebbleContext);
        writer.flush();
        String methodString = stringWriter.toString();

        File file = new File(context.getDirPath() + "/" + context.getClassName());

        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file);
        if (virtualFile == null) {
            return;
        }
        final String finalMethodString = methodString;

        Project proj = context.getProject();

        PsiFile psiFile = PsiManager.getInstance(context.getProject()).findFile(virtualFile);
        if (psiFile == null) {
            return;
        }
        PhpFile phpFile = new PhpFileImpl(psiFile.getViewProvider());
        PhpClass phpClass = PsiTreeUtil.findChildOfType(phpFile, PhpClass.class);
        if (phpClass == null) {
            return;
        }
        final Editor editor = FileEditorManager.getInstance(proj).openTextEditor(
                new OpenFileDescriptor(proj, phpClass.getContainingFile().getVirtualFile()), true);
        if(editor == null) {
            return;
        }

        PsiDocumentManager.getInstance(proj).doPostponedOperationsAndUnblockDocument(editor.getDocument());
        PsiDocumentManager.getInstance(proj).commitDocument(editor.getDocument());

        final int insertPos = getMethodInsertPosition(phpClass);

        WriteCommandAction.writeCommandAction(context.getProject(), psiFile)
                .withGroupId("Create Command").run( () ->
                        {

                            if(psiFile.isWritable())
                            {

                                assert phpClass != null;
                                PsiElement method = PhpCodeUtil.createMethodFromTemplate(phpClass, proj, finalMethodString);
                                if (method == null) {
                                    return;
                                }
                                editor.getDocument().insertString(insertPos, "\n" + method.getText());
                                final int endPos = insertPos + method.getText().length() + "\n".length();

                                PsiDocumentManager.getInstance(proj).commitDocument(editor.getDocument());

                                CodeStyleManager.getInstance(proj).reformatText(phpClass.getContainingFile(), insertPos, endPos);
                                PsiDocumentManager.getInstance(proj).commitDocument(editor.getDocument());
                                Method insertedMethod = phpClass.findMethodByName(getMethodName());
                                if(insertedMethod != null) {
                                    editor.getCaretModel().moveToOffset(insertedMethod.getTextRange().getStartOffset());
                                    editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
                                }

                            }
                        }
                );
    }

    private int getMethodInsertPosition(PhpClass phpClass) {
        int insertPos = -1;

        int methodEndPos = -1;
        PsiElement method = PsiTreeUtil.findChildOfType(phpClass, Method.class);
        if (method != null) {
            methodEndPos = method.getTextRange().getEndOffset();
            insertPos = methodEndPos;
        }

        if (insertPos == -1) {
            PsiElement lastChild = phpClass.getLastChild();
            int lastChildPos = lastChild.getTextOffset();
            insertPos = lastChildPos -1;
        }
        return insertPos;
    }

    private String getMethodName() {
        String prefix = isProvidedDependency ? "get" : "create";
        return prefix + this.className;
    }

    private final String methodName;

}
