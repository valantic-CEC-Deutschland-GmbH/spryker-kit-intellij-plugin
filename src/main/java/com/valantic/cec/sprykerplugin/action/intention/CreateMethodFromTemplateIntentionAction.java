package com.valantic.cec.sprykerplugin.action.intention;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.rd.util.reactive.KeyValuePair;
import com.valantic.cec.sprykerplugin.command.CreateMethodCommand;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.model.TwigTreeNode;
import com.valantic.cec.sprykerplugin.services.TwigResources;
import com.valantic.cec.sprykerplugin.services.ContextBuilderInterface;
import com.valantic.cec.sprykerplugin.services.TwigResourcesInterface;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CreateMethodFromTemplateIntentionAction extends PsiElementBaseIntentionAction {
    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
        CommandProcessor.getInstance().executeCommand(project, new Runnable() {
            @Override
            public void run() {
                CreateMethodCommand command = project.getService(CreateMethodCommand.class);
                command.createMethodFromTemplate(project, element);
            }
        },"Add Spryker Method", null);

    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        VirtualFile file = element.getContainingFile().getVirtualFile();
        if (file == null) {
            return false;
        }
        Context context = project.getService(ContextBuilderInterface.class).createContextFromProjectAndFilePath(project, file.getPath(), file.isDirectory());
        KeyValuePair<String, ArrayList<TwigTreeNode>> templates = project.getService(TwigResourcesInterface.class).getPathsToTwigResourcesForContext(context);

        return templates != null;
    }

    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return "Spryker plugin";
    }

    @Override
    public @IntentionName @NotNull String getText() {
        return "Create method from template for current context";
    }
}
