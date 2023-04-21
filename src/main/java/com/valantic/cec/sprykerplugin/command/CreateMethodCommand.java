package com.valantic.cec.sprykerplugin.command;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.*;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.services.ContextBuilderInterface;
import com.valantic.cec.sprykerplugin.services.PhpClassStringGeneratorInterface;
import com.valantic.cec.sprykerplugin.services.datatransfer.DocBlockItem;

import java.util.ArrayList;

/**
 * This class is derived from the class AddMissingFactoryMethodsCommand from the
 * Spryker plugin https://github.com/tobi812/idea-php-spryker-plugin
 */

public class CreateMethodCommand {

    public void createFactoryMethods(Project project, PsiElement element) {

        VirtualFile file = element.getContainingFile().getVirtualFile();
        if (file == null) {
            return;
        }
        Context context = project.getService(ContextBuilderInterface.class).createContextFromProjectAndFilePath(project, file.getPath(), file.isDirectory());

        PsiDirectory directory = element.getContainingFile().getContainingDirectory();

        if (directory == null) {
            return;
        }

        PhpClass mainClass = PhpPsiUtil.getParentOfClass(element, PhpClass.class);
        if (mainClass == null) {
            return;
        }

        ArrayList<PhpClass> phpClasses = this.findPhpClasses(directory);

        for (PhpClass phpClass : phpClasses) {
            if (phpClass.isAbstract() || phpClass.isTrait() || phpClass.isInterface() || phpClass.equals(mainClass)) {
                continue;
            }
            Method method = mainClass.findMethodByName(new StringBuffer("create" + phpClass.getName()));
            if (method != null) {
                continue;
            }

            PhpClassStringGeneratorInterface generator = project.getService(PhpClassStringGeneratorInterface.class);
            String factoryMethod = generator.getFactoryMethodString(phpClass);
            method = PhpPsiElementFactory.createMethod(project, factoryMethod);
            PhpDocComment comment = this.createDocComment(phpClass, project);

            PsiElement mainClassParent = mainClass.getParent();

            if (!mainClass.getNamespaceName().equals(phpClass.getNamespaceName())) {
                PhpUseList useStatement = null;
                if (mainClassParent instanceof PhpPsiElement) {
                    useStatement = this.getUseStatement(mainClassParent, project, phpClass);
                }

                if (useStatement != null) {
                    mainClassParent.addAfter(useStatement, mainClass.getParent().getFirstChild());
                }
            }

            if (comment != null) {
                mainClass.addBefore(comment, mainClass.getLastChild());
            }

            mainClass.addBefore(method, mainClass.getLastChild());
        }
    }

    private PhpDocComment createDocComment(PhpClass phpClass, Project project) {
        ArrayList<DocBlockItem> docBlockItems = new ArrayList<>();

        String returnType = phpClass.getFQN();
        PhpClass[] interfaces = phpClass.getImplementedInterfaces();
        if (interfaces.length > 0) {
            PhpClass firstInterface  = interfaces[0];
            if (firstInterface != null) {
                returnType = firstInterface.getFQN();
            }
        }

        docBlockItems.add(new DocBlockItem("return", returnType));

        return PhpPsiElementFactory.createFromText(
                project,
                PhpDocComment.class,
                project.getService(PhpClassStringGeneratorInterface.class).getDocBlockString(docBlockItems)
        );
    }

    private PhpUseList getUseStatement(PsiElement mainClassParent, Project project, PhpClass phpClass) {
        String alias = null;
        for (PsiElement child : mainClassParent.getChildren()) {
            if (!(child instanceof PhpUseList)) {
                continue;
            }

            for (PhpUse use :  ((PhpUseList) child).getDeclarations()) {
                if (use.getFQN().equals(phpClass.getFQN())) {
                    return null;
                }

                if (use.getName().equals(phpClass.getName())) {
                    alias = phpClass.getName() + "Alias";
                }
            }
        }
        return PhpPsiElementFactory.createUseStatement(project, phpClass.getFQN(), alias);
    }

    private ArrayList<PhpClass> findPhpClasses(PsiDirectory directory) {
        ArrayList<PhpClass> phpClasses = new ArrayList<>();

        for (PsiDirectory subdirectory : directory.getSubdirectories()) {
            phpClasses.addAll(this.findPhpClasses(subdirectory));
        }

        for (PsiFile file : directory.getFiles()) {
            PhpClass moduleClass = PhpPsiUtil.findAllClasses(file).iterator().next();

            if (moduleClass != null) {
                phpClasses.add(moduleClass);
            }
        }

        return phpClasses;
    }

    public void createMethodFromTemplate(Project project, PsiElement element) {

    }
}
