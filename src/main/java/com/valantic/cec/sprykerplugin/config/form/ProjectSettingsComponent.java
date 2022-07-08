package com.valantic.cec.sprykerplugin.config.form;

import javax.swing.*;

public class ProjectSettingsComponent {
    private JPanel rootPanel;
    private JLabel prjNamespaceLbl;
    private JTextField tFProjectNamespace;

    public ProjectSettingsComponent(String projectNamespace) {
        this.prjNamespaceLbl.setText("Enter project namespace: ");
        this.tFProjectNamespace.setText(projectNamespace);
    }

    public JPanel getRootPanel() {
        return this.rootPanel;
    }

    public String getProjectNamespace() {
        return this.tFProjectNamespace.getText();
    }

}
