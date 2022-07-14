package com.valantic.cec.sprykerplugin.action;

import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.valantic.cec.sprykerplugin.icons.ValanticIcons;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.services.FileWriterInterface;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

//import javax.swing.*;

public class CreateFileFromTemplateAction extends AbstractFromTemplateAction {
    private String resultFileName = "";

    public CreateFileFromTemplateAction(
            String description,
            Context context,
            String templateFileName,
            String resultFileName,
            Boolean enabled
    ) {
        super(description, ValanticIcons.fileIcon, context, templateFileName, enabled);
        this.resultFileName = resultFileName;
    }
    public CreateFileFromTemplateAction(
            String description,
            Boolean enabled
    ) {
        super(description, ValanticIcons.fileIcon, null, null, enabled);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {

        final IdeView view = getIdeView(e);
        if (view == null) {
            return;
        }

        final Project project = e.getProject();

        final PsiDirectory dir = view.getOrChooseDirectory();
        if (dir == null || project == null) return;

        String templatePathString = getTemplatePathString();
        FileWriterInterface writer = project.getService(FileWriterInterface.class);

        try {
            writer.createFileFromTemplate(context, dir, templatePathString, this.resultFileName);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    @Nullable
    private IdeView getIdeView(@NotNull AnActionEvent e) {
        return e.getData(LangDataKeys
                .IDE_VIEW);
    }

}

