package com.valantic.cec.sprykerplugin.forms;

import javax.swing.*;

public class GetModuleNameUI {
    private JTextField tfModuleName;
    private JPanel rootPanel;

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public String getModuleName() {
        return tfModuleName.getText();
    }

}
