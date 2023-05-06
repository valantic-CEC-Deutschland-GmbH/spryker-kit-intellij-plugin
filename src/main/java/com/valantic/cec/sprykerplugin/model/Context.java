package com.valantic.cec.sprykerplugin.model;

import com.intellij.openapi.project.Project;
import com.valantic.cec.sprykerplugin.services.ProjectSettingsState;

public class Context {

    public Context(String projectOrVendorName, String applicationName, String moduleName, String innerPath, String className, String dirPath, Project project) {
        this.projectOrVendorName = projectOrVendorName;
        this.applicationName = applicationName;
        this.moduleName = moduleName;
        this.innerPath = innerPath;
        this.className = className;
        this.dirPath = dirPath;
        this.project = project;
    }

    public Context(
            String applicationName,
            String moduleName,
            String innerPath,
            String className,
            String dirPath,
            Project project
    ) {
        this((project != null) ? project.getService(ProjectSettingsState.class).projectNamespace : "Pyz", applicationName, moduleName, innerPath, className, dirPath, project);
    }

    public String getProjectOrVendorName() {
        return projectOrVendorName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getInnerPath() {
        return innerPath;
    }

    public String getClassName() {
        return className;
    }

    public String getDirPath() {
        return this.dirPath;
    }

    public final Project getProject() { return project; }

    private String projectOrVendorName;

    private final String applicationName; //Zed, Yves, Glue, Service or Client

    private final String moduleName;

    private final String innerPath;

    private final String className;

    private final String dirPath;

    private final Project project;

    public boolean matches(Context context) {
        return (this.applicationName.equals(context.getApplicationName()) || (this.applicationName.equals("Any")))
                && (this.moduleName.equals(context.getModuleName()) || (this.moduleName.equals("Any")))
                && (this.innerPath.equals(context.getInnerPath()) || (this.innerPath.equals("Any")))
                && (this.className.equals(context.getClassName()) || (this.className.equals("Any")))
                && (this.dirPath.equals(context.getDirPath()) || (this.dirPath.equals("Any")));
    }

    public CharSequence getClassSource() {

        return null;
    }
}
