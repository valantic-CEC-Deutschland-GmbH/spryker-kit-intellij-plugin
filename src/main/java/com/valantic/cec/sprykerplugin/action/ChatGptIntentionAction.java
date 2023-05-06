package com.valantic.cec.sprykerplugin.action;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.PhpFileImpl;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.model.chatgpt.ChatGptPrompt;
import com.valantic.cec.sprykerplugin.model.chatgpt.ChatGptPromptInterface;
import com.valantic.cec.sprykerplugin.services.ChatGptExecuter;
import com.valantic.cec.sprykerplugin.services.ContextBuilderInterface;
import com.valantic.cec.sprykerplugin.services.ProjectSettingsState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChatGptIntentionAction extends PsiElementBaseIntentionAction {

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {

        ProjectSettingsState projectSettings = project.getService(ProjectSettingsState.class);
        if (projectSettings.openAiApiKey != null && projectSettings.prompts != null) {
            VirtualFile file = element.getContainingFile().getVirtualFile();

            if (file == null) {
                return;
            }
            PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
            if (psiFile == null) {
                return;
            }
            PhpFile phpFile = new PhpFileImpl(psiFile.getViewProvider());
            PhpClass phpClass = PsiTreeUtil.findChildOfType(phpFile, PhpClass.class);
            if (phpClass == null) {
                return;
            }
            String nameSpaceName = phpClass.getNamespaceName();

            Context context = project.getService(ContextBuilderInterface.class).createContextFromNameSpaceName(project, nameSpaceName, phpClass.getName(), file.getPath());
            ChatGptPromptInterface prompt = getMatchingPrompt(projectSettings.prompts, context);

            assert prompt != null;
            project.getService(ChatGptExecuter.class).execute(project, prompt, context);
        }
    }

    private ChatGptPromptInterface getMatchingPrompt(ArrayList<ChatGptPromptInterface> prompts, Context context) {
        for (ChatGptPromptInterface prompt : prompts) {
            Context necessaryContext = context.getProject()
                .getService(ContextBuilderInterface.class)
                .createContextFromContextString(prompt.getNecessaryContextString());
            if (necessaryContext.matches(context)) {
                return prompt;
            }
        }
        return null;
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        ProjectSettingsState projectSettings = project.getService(ProjectSettingsState.class);
        if (projectSettings.openAiApiKey != null && projectSettings.prompts != null) {
            VirtualFile file = element.getContainingFile().getVirtualFile();

            if (file == null) {
                return false;
            }
            PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
            if (psiFile == null) {
                 return false;
            }
            PhpFile phpFile = new PhpFileImpl(psiFile.getViewProvider());
            PhpClass phpClass = PsiTreeUtil.findChildOfType(phpFile, PhpClass.class);
            if (phpClass == null) {
                return false;
            }
            String nameSpaceName = phpClass.getNamespaceName();

            Context context = project.getService(ContextBuilderInterface.class).createContextFromNameSpaceName(project, nameSpaceName, phpClass.getName(), file.getPath());
            if (hasPromptForContext(projectSettings.prompts, context)) {
                return true;
            }
        }

        return false;
    }

    private boolean hasPromptForContext(ArrayList<ChatGptPromptInterface> prompts, Context context) {
        for (ChatGptPromptInterface prompt : prompts) {
            Context necessaryContext = context.getProject()
                .getService(ContextBuilderInterface.class)
                .createContextFromContextString(prompt.getNecessaryContextString());
            if (necessaryContext.matches(context)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return "Spryker plugin";
    }
}
