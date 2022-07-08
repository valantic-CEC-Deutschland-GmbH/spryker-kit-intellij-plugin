package com.valantic.cec.sprykerplugin.forms;

import javax.swing.*;
import java.util.List;

public class FactoryMethodParametersUI {
    public JPanel getRootPanel() {
        return rootPanel;
    }

    public String getClassName() {
        return tfClassName.getText();
    }
    public boolean getIsDependency() {
        return cbIsDependency.isSelected();
    }

    public List<String> getListDependencies() {
        return listDependencies.getSelectedValuesList();
    }
    private JLabel lblClassName;
    private JTextField tfClassName;
    private JCheckBox cbIsDependency;
    private JLabel lblDependencies;
    private JList<String> listDependencies;

    private JPanel rootPanel;
}
