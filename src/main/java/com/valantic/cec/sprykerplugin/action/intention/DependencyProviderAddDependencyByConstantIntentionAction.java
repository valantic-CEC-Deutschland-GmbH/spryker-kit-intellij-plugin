package com.valantic.cec.sprykerplugin.action.intention;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.impl.ClassConstImpl;
import com.jetbrains.rd.util.reactive.KeyValuePair;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.model.TwigTreeNode;
import com.valantic.cec.sprykerplugin.model.dependency.SprykerDependency;
import com.valantic.cec.sprykerplugin.services.ContextBuilderInterface;
import com.valantic.cec.sprykerplugin.services.TwigResourcesInterface;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DependencyProviderAddDependencyByConstantIntentionAction extends PsiElementBaseIntentionAction {
    @Override
    public @IntentionName @NotNull String getText() {
        return "DependencyProviderAddDependencyByConstantIntentionAction";
    }

    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return "Spryker plugin";
    }


    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        VirtualFile file = element.getContainingFile().getVirtualFile();
        if (file == null) {
            return false;
        }

        PhpClass phpClass = PhpPsiUtil.getParentByCondition(element, PhpClass.INSTANCEOF);
        if (phpClass == null) {
            return false;
        }
        if (!phpClass.getName().endsWith("DependencyProvider")) {
            return false;
        }

        if (!(element.getParent() instanceof ClassConstImpl)) {
            return false;
        }
        ClassConstImpl constant = (ClassConstImpl) element.getParent();

        SprykerDependency dependency = SprykerDependency.fromString(constant.getName());

        if (dependency == null) {
            return false;
        }

        String methodName = dependency.getMethodName();

        Context context = project.getService(ContextBuilderInterface.class).createContextFromProjectAndFilePath(project, file.getPath(), file.isDirectory());


        return true;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {

    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }
}
