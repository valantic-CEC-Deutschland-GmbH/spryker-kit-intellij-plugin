package com.valantic.cec.sprykerplugin.forms;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public class ZedBusinessFactoryMethodParameterDialog extends DialogWrapper {

    private FactoryMethodParametersUI ui;
    private JPanel centerPanel;

    private JTextField classNameField;

    private JCheckBox checkIsDependency;

    private JList<String> dependenciesList;

    private final String[] possibleDependencies;

    public ZedBusinessFactoryMethodParameterDialog(@Nullable Project project, String[] possibleDependencies) {
        super(project);
        setTitle("Create Business Factory Method");
        this.possibleDependencies = possibleDependencies;
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        ui = new FactoryMethodParametersUI();
        return ui.getRootPanel();

    }

    public String getClassNameToCreateOrGet() {
        return this.ui.getClassName();
    }

    public boolean isProvidedDependency() {
        return this.ui.getIsDependency();
    }

    /**
     *
     * @return List of Dependencies for the constructor
     */
    public List<String> getListOfDependenciesForCreation() {
        return  this.ui.getListDependencies();
    }
}
