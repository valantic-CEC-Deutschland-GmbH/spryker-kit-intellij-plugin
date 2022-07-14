package com.valantic.cec.sprykerplugin.config;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.NlsContexts;
import com.valantic.cec.sprykerplugin.config.form.ApplicationSettingsComponent;
import com.valantic.cec.sprykerplugin.services.ApplicationSettingsState;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ApplicationSettingsConfigurable implements Configurable {

    private ApplicationSettingsComponent applicationSettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "SprykerKit Settings";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return applicationSettingsComponent.getPreferredFocusedComponent();
    }

    @Override
    public @Nullable JComponent createComponent() {
        ApplicationSettingsState settings = ApplicationSettingsState.getInstance();
        if (settings == null) {
            throw new RuntimeException("Settings isn't accessible.");
        }

        applicationSettingsComponent = new ApplicationSettingsComponent();
        applicationSettingsComponent.setProjectNamespaceTextText(settings.projectNamespace);
        return applicationSettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        ApplicationSettingsState settings = ApplicationSettingsState.getInstance();
        if (settings == null) {
            throw new RuntimeException("Settings isn't accessible.");
        }

        return !applicationSettingsComponent.getProjectNamespaceText().equals(settings.projectNamespace);
    }

    @Override
    public void apply() {
        ApplicationSettingsState settings = ApplicationSettingsState.getInstance();
        if (settings == null) {
            throw new RuntimeException("Settings isn't accessible.");
        }

        settings.projectNamespace = applicationSettingsComponent.getProjectNamespaceText();
    }

    @Override
    public void reset() {
        ApplicationSettingsState settings = ApplicationSettingsState.getInstance();
        if (settings == null) {
            throw new RuntimeException("Settings isn't accessible.");
        }

        applicationSettingsComponent.setProjectNamespaceTextText(settings.projectNamespace);
    }

    @Override
    public void disposeUIResources() {
        applicationSettingsComponent = null;
    }
}
