package com.valantic.cec.sprykerplugin.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Supports storing the application settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
        name = "com.valantic.cec.sprykerplugin.config.state",
        storages = @Storage("VAC-SprykerSettingsPlugin.xml")
)
public class ApplicationSettingsState implements PersistentStateComponent<ApplicationSettingsState> {

    public String projectNamespace = "Pyz";

    public static ApplicationSettingsState getInstance() {
        if (ApplicationManager.getApplication() != null) {
            return ApplicationManager.getApplication().getService(ApplicationSettingsState.class);
        }
        return null;
    }
    @Nullable
    @Override
    public ApplicationSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ApplicationSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}