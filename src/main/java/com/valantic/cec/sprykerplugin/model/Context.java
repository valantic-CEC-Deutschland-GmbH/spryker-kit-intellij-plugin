package com.valantic.cec.sprykerplugin.model;

import com.intellij.openapi.project.Project;

public class Context {

    public Context(
            String applicationName,
            String moduleName,
            String innerPath,
            String className,
            String dirPath,
            Project project
    ) {
        this.applicationName = applicationName;
        this.moduleName = moduleName;
        this.innerPath = innerPath;
        this.className = className;
        this.dirPath = dirPath;
        this.project = project;
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

    private final String applicationName; //Zed, Yves, Glue, Service or Client

    private final String moduleName;

    private final String innerPath;

    private final String className;

    private final String dirPath;

    private final Project project;
}
