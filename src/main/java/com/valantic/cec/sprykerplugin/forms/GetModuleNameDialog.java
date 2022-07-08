package com.valantic.cec.sprykerplugin.forms;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GetModuleNameDialog extends DialogWrapper {

    public GetModuleNameDialog(Project project)
    {
        super(project);

        init();
    }

    public String getModuleName() {
        return moduleNameUI.getModuleName();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        moduleNameUI = new GetModuleNameUI();

        return moduleNameUI.getRootPanel();
    }

    private GetModuleNameUI moduleNameUI;
}
