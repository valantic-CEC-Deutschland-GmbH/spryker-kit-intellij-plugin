package com.valantic.cec.sprykerplugin.services;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.constants.SprykerConstants;

public class ContextBuilder implements ContextBuilderInterface {

    private final String searchProjectString;
    private Project project;

    public ContextBuilder(Project project) {
        this.project = project;
        if (this.project != null) {

            this.searchProjectString = "src/" + project.getService(ProjectSettingsState.class).projectNamespace + "/";
        }
        else {
            ApplicationSettingsState settingsState = ApplicationSettingsState.getInstance();
            if (settingsState != null) {
                this.searchProjectString = "src/" + settingsState.projectNamespace + "/";
            } else {
                this.searchProjectString = "src/" + SprykerConstants.PROJECTNAMESPACE + "/";
            }
        }
    }

    public Context createContextFromFilePath(String filePath, boolean isDirectory) {
        String applicationName = getApplicationNameFromPath(filePath);
        String moduleName = getModuleNameFromPath(filePath, applicationName);
        String innerPath = getInnerPathFromPath(filePath, applicationName, moduleName, isDirectory);
        String className = null;
        if (!isDirectory) {
            className = getClassNameFromPath(filePath, applicationName, moduleName, innerPath);
        }

        String dirName = isDirectory? filePath : filePath.substring(0, filePath.lastIndexOf("/"));

        return new Context(applicationName, moduleName, innerPath, className, dirName, null);
    }

    @Override
    public Context createContextFromAnActionEvent(AnActionEvent event) {
        if (event == null) return null;
        VirtualFile file = event.getDataContext().getData(LangDataKeys.VIRTUAL_FILE);
        Project project = event.getProject();
        if (file == null) return null;
        String filePath = file.getPath();
        boolean isDirectory = file.isDirectory();
        return createContextFromProjectAndFilePath(project, filePath, isDirectory);
    }

    @Override
    public Context createContextFromProjectAndFilePath(Project project, String filePath, boolean isDirectory) {
        String applicationName = getApplicationNameFromPath(filePath);
        String moduleName = getModuleNameFromPath(filePath, applicationName);
        String innerPath = getInnerPathFromPath(filePath, applicationName, moduleName, isDirectory);
        String className = null;
        if (!isDirectory) {
            className = getClassNameFromPath(filePath, applicationName, moduleName, innerPath);
        }

        String dirName = isDirectory? filePath : filePath.substring(0, filePath.lastIndexOf("/"));

        return new Context(applicationName, moduleName, innerPath, className, dirName, project);
    }

    private String getInnerPathFromPath(String filePath, String applicationName, String moduleName, boolean isDirectory) {
        if (applicationName == null || moduleName == null) {
            return null;
        }
        int indexOfSrcPyz = filePath.indexOf(this.searchProjectString);

        int indexOfStartOfLayerNameApplication = indexOfSrcPyz +
                (this.searchProjectString + applicationName + "/" + moduleName + "/").length();

        if (indexOfStartOfLayerNameApplication >= filePath.length()) {
            return null;
        }
        int indexOfEndOfLayerName = filePath.lastIndexOf("/");

        if ((indexOfEndOfLayerName == -1) || isDirectory) {
            indexOfEndOfLayerName = filePath.length();
        }

        if (indexOfEndOfLayerName < indexOfStartOfLayerNameApplication) {
            return null;
        }
        return filePath.substring(indexOfStartOfLayerNameApplication, indexOfEndOfLayerName);
    }

    private String getClassNameFromPath(String filePath, String applicationName, String moduleName, String layerName) {
        if (applicationName == null || moduleName == null)  {
            return null;
        }

        int indexOfSrcPyz = filePath.indexOf(this.searchProjectString);

        String layerPart = (layerName != null) ? (layerName) : "";

        int indexOfStartOfClassNameApplication = indexOfSrcPyz +
                (this.searchProjectString + applicationName + "/" + moduleName + "/" + layerPart + "/").length();

        if (indexOfStartOfClassNameApplication >= filePath.length()) {
            return null;
        }
        int indexOfEndOfClassName = filePath.indexOf("/", indexOfStartOfClassNameApplication);
        if (indexOfEndOfClassName == -1) {
            indexOfEndOfClassName = filePath.length();
        }

        return filePath.substring(indexOfStartOfClassNameApplication, indexOfEndOfClassName);
    }

    private String getModuleNameFromPath(String filePath, String applicationName) {
        if (applicationName == null) {
            return null;
        }
        int indexOfSrcPyz = filePath.indexOf(this.searchProjectString);

        int indexOfStartOfModuleNameApplication = indexOfSrcPyz +
                (this.searchProjectString + applicationName + "/").length();

        if (indexOfStartOfModuleNameApplication >= filePath.length()) {
            return null;
        }

        int indexOfEndOfModuleName = filePath.indexOf("/", indexOfStartOfModuleNameApplication);
        if (indexOfEndOfModuleName == -1) {
            indexOfEndOfModuleName = filePath.length();
        }

        return filePath.substring(indexOfStartOfModuleNameApplication, indexOfEndOfModuleName);
    }

    private String getApplicationNameFromPath(String filePath) {
        int indexOfSrcPyz = filePath.indexOf(this.searchProjectString);

        int startOfApplicationeName = indexOfSrcPyz + this.searchProjectString.length();
        int indexOfEndOfApplicationName = filePath.indexOf("/", startOfApplicationeName);

        if (indexOfEndOfApplicationName == -1) {
            indexOfEndOfApplicationName = filePath.length();
        }

        String applicationName = filePath.substring(startOfApplicationeName, indexOfEndOfApplicationName);

        // Check whether its a spryker application Name
        if (!SprykerConstants.LIST_OF_SPRYKER_APPLICATION_NAMES.contains(applicationName)) {
            return null;
        }

        return applicationName;
    }
}
