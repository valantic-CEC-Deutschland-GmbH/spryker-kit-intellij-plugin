package com.valantic.cec.sprykerplugin.services;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.valantic.cec.sprykerplugin.model.Context;

public interface ContextBuilderInterface {

    Context createContextFromFilePath(String filePath, boolean isDirectory);

    Context createContextFromAnActionEvent(AnActionEvent event);

    Context createContextFromProjectAndFilePath(Project project, String filePath, boolean isDirectory);
}
