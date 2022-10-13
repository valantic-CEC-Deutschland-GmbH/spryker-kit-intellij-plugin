package com.valantic.cec.sprykerplugin.action.intention;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.valantic.cec.sprykerplugin.command.CreateMethodCommand;
import org.jetbrains.annotations.NotNull;

/**
 * Derived from AddMissingFactoryMethodsAction from the
 * Spryker plugin https://github.com/tobi812/idea-php-spryker-plugin
 */
public class CreateMissingFactoryMethodsIntentionAction extends PsiElementBaseIntentionAction {

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
        CommandProcessor.getInstance().executeCommand(project, new Runnable() {
            @Override
            public void run() {
                CreateMethodCommand command = project.getService(CreateMethodCommand.class);
                command.createFactoryMethods(project, element);
            }
        },"Create missing Spryker Factory Methods", null);
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        PhpClass phpClass = PhpPsiUtil.getParentByCondition(element, PhpClass.INSTANCEOF);
        if (phpClass == null) return false;
        return this.isFactoryClass(phpClass);
    }

    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return "Spryker plugin";
    }

    @Override
    public @IntentionName @NotNull String getText() {
        return "Create missing factory methods";
    }

    private boolean isFactoryClass(PhpClass phpClass) {
        PhpClass currentClass = phpClass.getSuperClass();
        while (currentClass != null) {
            if (currentClass.getFQN().contains("AbstractFactory")) {
                return true;
            }
            currentClass = currentClass.getSuperClass();
        }
        return false;
    }
}
