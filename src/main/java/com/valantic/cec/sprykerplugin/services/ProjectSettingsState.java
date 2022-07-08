package com.valantic.cec.sprykerplugin.services;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Supports storing the project settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
        name = "com.valantic.cec.sprykerplugin.settings.ProjectSettingsState",
        storages = @Storage("PluginProjectSettings.xml")
)
public class ProjectSettingsState implements PersistentStateComponent<ProjectSettingsState> {

    public String projectNamespace = "Pyz";

    public ProjectSettingsState() {

    }

    @Override
    public @Nullable ProjectSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ProjectSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }


}
