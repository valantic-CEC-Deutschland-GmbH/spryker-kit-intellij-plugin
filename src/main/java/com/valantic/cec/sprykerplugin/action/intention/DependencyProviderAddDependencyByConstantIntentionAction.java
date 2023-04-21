package com.valantic.cec.sprykerplugin.action.intention;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.impl.ClassConstImpl;
import com.valantic.cec.sprykerplugin.model.dependency.SprykerDependency;
import org.jetbrains.annotations.NotNull;

public class DependencyProviderAddDependencyByConstantIntentionAction extends PsiElementBaseIntentionAction {
    @Override
    public @IntentionName @NotNull String getText() {
        return "Create method for adding the dependency.";
    }

    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return "SprykerKit plugin";
    }


    /**
     * find out if current psi element is a public constant in a class that extends Spryker DependencyProvider
     * And check if a method related to the name of the constant exists in the same class
     * If that kind of method doesn't exist return true
     *
     * @param project the project in which the availability is checked.
     * @param editor  the editor in which the intention will be invoked.
     * @param element the element under caret.
     *
     * @return
     */
    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        VirtualFile file = element.getContainingFile().getVirtualFile();
        if (file == null) {
            return false;
        }

        PhpClass phpClass = PhpPsiUtil.getParentOfClass(element, PhpClass.class);
        if (phpClass == null) {
            return false;
        }
        if (!phpClass.getName().endsWith("DependencyProvider")) {
            return false;
        }

        if (!(element.getParent() instanceof ClassConstImpl constantProperty)) {
            return false;
        }

        SprykerDependency dependency = SprykerDependency.fromString(constantProperty.getName());

        if (dependency == null) {
            return false;
        }

        String methodName = dependency.getMethodName();
        if (methodName == null) {
            return false;
        }

        // check if method exists
        if (phpClass.findMethodByName(methodName) != null) {
            return false;
        }

        return true;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
        VirtualFile file = element.getContainingFile().getVirtualFile();
        if (file == null) {
            return;
        }

        PhpClass phpClass = PhpPsiUtil.getParentOfClass(element, PhpClass.class);
        if (phpClass == null) {
            return;
        }
        if (!phpClass.getName().endsWith("DependencyProvider")) {
            return;
        }

        if (!(element.getParent() instanceof ClassConstImpl constantProperty)) {
            return;
        }

        SprykerDependency dependency = SprykerDependency.fromString(constantProperty.getName());

        if (dependency == null) {
            return;
        }

        String methodName = dependency.getMethodName();
        if (methodName == null) {
            return;
        }

        // add method to class
        String methodBody = dependency.getMethodBody();
        String methodString = createMethodString(methodName, methodBody);
        Method method = PhpPsiElementFactory.createMethod(project, methodString);

        phpClass.addBefore(method, phpClass.getLastChild());
    }

    private String createMethodString(String methodName, String methodBody) {
        return "public function " + methodName + "(Container $container): Container {\n" + methodBody + "\n}";
    }
}
