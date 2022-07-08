package com.valantic.cec.sprykerplugin.config;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.valantic.cec.sprykerplugin.config.form.ProjectSettingsComponent;
import com.valantic.cec.sprykerplugin.services.ProjectSettingsState;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ProjectSettingsConfigurable implements Configurable {

    private ProjectSettingsComponent component;
    private final Project project;

    public ProjectSettingsConfigurable(Project project) {
        this.project = project;
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "SprykerKit Project Settings";
    }

    @Override
    public @Nullable JComponent createComponent() {
        String projectNamespace = project.getService(ProjectSettingsState.class).projectNamespace;
        component = new ProjectSettingsComponent(projectNamespace);
        return component.getRootPanel();
    }

    @Override
    public boolean isModified() {
        ProjectSettingsState settings = project.getService(ProjectSettingsState.class);
        return !component.getProjectNamespace().equals(settings.projectNamespace);
    }

    @Override
    public void apply() throws ConfigurationException {
        project.getService(ProjectSettingsState.class).projectNamespace = component.getProjectNamespace();
    }
}
