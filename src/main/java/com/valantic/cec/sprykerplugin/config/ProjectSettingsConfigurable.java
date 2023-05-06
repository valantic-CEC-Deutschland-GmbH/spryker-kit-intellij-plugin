package com.valantic.cec.sprykerplugin.config;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.valantic.cec.sprykerplugin.config.form.ProjectSettingsComponent;
import com.valantic.cec.sprykerplugin.model.chatgpt.ChatGptPromptInterface;
import com.valantic.cec.sprykerplugin.services.ProjectSettingsState;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;

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
        ProjectSettingsState projectSettings = project.getService(ProjectSettingsState.class);
        String projectNamespace = projectSettings.projectNamespace;
        String openAiApiKey = projectSettings.openAiApiKey;
        String model = projectSettings.model;
        ArrayList<ChatGptPromptInterface> prompts = project.getService(ProjectSettingsState.class).prompts;

        component = new ProjectSettingsComponent(projectNamespace, openAiApiKey, prompts, model, project);
        return component.getRootPanel();
    }

    @Override
    public boolean isModified() {
        ProjectSettingsState settings = project.getService(ProjectSettingsState.class);
        return !component.getProjectNamespace().equals(settings.projectNamespace)
            || !component.getOpenAiApiKey().equals(settings.openAiApiKey)
            || !component.getPrompts().equals(settings.prompts)
            || !component.getModel().equals(settings.model);
    }

    @Override
    public void apply() throws ConfigurationException {
        project.getService(ProjectSettingsState.class).projectNamespace = component.getProjectNamespace();
        project.getService(ProjectSettingsState.class).openAiApiKey = component.getOpenAiApiKey();
        project.getService(ProjectSettingsState.class).prompts = component.getPrompts();
        project.getService(ProjectSettingsState.class).model = component.getModel();
    }
}
