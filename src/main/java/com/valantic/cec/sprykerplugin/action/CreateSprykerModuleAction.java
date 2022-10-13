package com.valantic.cec.sprykerplugin.action;

import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsContexts.Command;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.jetbrains.rd.util.reactive.KeyValuePair;
import com.valantic.cec.sprykerplugin.icons.ValanticIcons;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.model.TwigTreeNode;
import com.valantic.cec.sprykerplugin.services.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

import static com.intellij.openapi.util.NlsContexts.*;

public class CreateSprykerModuleAction extends CreateElementActionBase {

    public CreateSprykerModuleAction()
    {
        super("Create New Spryker Module", "Create new Spryker module.", ValanticIcons.moduleIcon);
    }

    @Override
    protected void invokeDialog(@NotNull Project project, @NotNull PsiDirectory directory, @NotNull Consumer<? super PsiElement[]> elementsConsumer) {
        this.project = project;
        MyInputValidator validator = new MyInputValidator(project, directory);
        String moduleName = Messages.showInputDialog(
                "Set module name",
                "Input Module Name",
                Messages.getQuestionIcon(),
                "",
                validator
        );

        if (moduleName == null || moduleName.isEmpty()) {
            return;
        }
        elementsConsumer.accept(validator.getCreatedElements());
    }

    @Override
    protected PsiElement @NotNull [] create(@NotNull String moduleName, PsiDirectory directory) throws Exception {
        if (directory.findSubdirectory(moduleName) != null) {
            Messages.showErrorDialog(this.project, "Module $moduleName already exists!", "Error");

            return PsiElement.EMPTY_ARRAY;
        }

        PsiDirectory moduleDirectory  = this.createSprykerModule(directory, moduleName);

        Context context = this.project.getService(ContextBuilderInterface.class).
                createContextFromProjectAndFilePath(
                        this.project,
                        moduleDirectory.getVirtualFile().getPath(),
                        true);

        return this.createModuleClasses(context, moduleDirectory);
    }

    @Override
    protected @DialogTitle String getErrorTitle() {
        return "Error Creating a New Spryker Module!";
    }

    @Override
    protected @Command @NotNull String getActionName(@NotNull PsiDirectory directory, @NotNull String newName) {
        return "Create Spryker Module";
    }

    private PsiElement[] createModuleClasses(Context context, PsiDirectory directory) {
        ArrayList<PsiElement> createdElements = new ArrayList<>();

        KeyValuePair<String, ArrayList<TwigTreeNode>> nodes = this.project.getService(TwigResourcesInterface.class).
                getPathsToTwigResourcesAndSprykerDirectoriesForContext(context);

        createElementsByNodes(directory, createdElements, nodes);

        return createdElements.toArray(new PsiElement[0]);
    }

    private void createClassesForDirectory(Context context, PsiDirectory directory, ArrayList<PsiElement> createdElements) {
        KeyValuePair<String, ArrayList<TwigTreeNode>> nodes = this.project.getService(TwigResourcesInterface.class).
                getPathsToTwigResourcesAndSprykerDirectoriesForContext(context);

        createElementsByNodes(directory, createdElements, nodes);
    }

    private void createElementsByNodes(
            PsiDirectory moduleDirectory,
            ArrayList<PsiElement> createdElements,
            KeyValuePair<String, ArrayList<TwigTreeNode>> nodes
    ) {
        for (TwigTreeNode node : nodes.getValue()) {
            if (node.getType().equals(TwigResources.DIRECTORY_NODE_TYPE)) {
                createDirectoryByTwigTreeNode(moduleDirectory, createdElements, node);
            } else {
                createFileByTwigTreeNode(moduleDirectory, createdElements, node);
            }

        }
    }

    private void createFileByTwigTreeNode(PsiDirectory psiDirectory, ArrayList<PsiElement> createdElements, TwigTreeNode node) {
        String templateName = node.getName();
        Context context = this.project.getService(ContextBuilderInterface.class).
                createContextFromProjectAndFilePath(
                        this.project,
                        psiDirectory.getVirtualFile().getPath(),
                        true);
        FileWriterInterface writer = project.getService(FileWriterInterface.class);
        String templatePathString = getTemplatePathString(context, templateName);

        String resultFileName = project.getService(FileNameGeneratorInterface.class).
                getResultFileNameFromTemplateNameAndContext(templateName, context);

        try {
            PsiFile file = writer.createFileFromTemplate(context,
                    psiDirectory,
                    templatePathString,
                    resultFileName
            );
            if (file != null) {
                createdElements.add(file);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }



    private void createDirectoryByTwigTreeNode(PsiDirectory moduleDirectory, ArrayList<PsiElement> createdElements, TwigTreeNode node) {
        PsiDirectory dir = createSubDirectory(moduleDirectory, node.getName());
        createdElements.add(dir);
        Context context = this.project.getService(ContextBuilderInterface.class).
                createContextFromProjectAndFilePath(
                        this.project,
                        dir.getVirtualFile().getPath(),
                        true);
        createClassesForDirectory(context, dir, createdElements);
    }

    private String matchProjectName(PsiDirectory directory) {
        PsiDirectory projectDir = directory.getParent();
        if (projectDir == null) {
            throw new RuntimeException("Wrong directory to create a new module!");
        }
        return projectDir.getName();
    }

    private PsiDirectory createSprykerModule(PsiDirectory directory, String moduleName) {
        return this.project.getService(FileWriterInterface.class).createDirectory(directory, moduleName);
    }

    private PsiDirectory createSubDirectory(PsiDirectory directory, String subDirName) {
        return this.project.getService(FileWriterInterface.class).createDirectory(directory, subDirName);
    }

    protected String getTemplatePathString(Context context, String templateFileName) {
        String path = "templates/";
        if (context.getApplicationName() != null) {
            path += context.getApplicationName() + "/";
        }

        if (context.getInnerPath() != null) {
            path += context.getInnerPath() + "/";
        }
        path += templateFileName;
        return path;
    }

    private Project project;
}
