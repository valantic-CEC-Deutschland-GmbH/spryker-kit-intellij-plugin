package com.valantic.cec.sprykerplugin.forms;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;

public class ClassSelectionDialog extends DialogWrapper {

    private final ClassListModel classesList;

    private ClassDialogUI ui;

    public ClassSelectionDialog(@Nullable Project project, ClassListModel classesList) {
        super(project);
        setTitle("Class Selector");
        this.classesList = classesList;
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        ui = new ClassDialogUI(classesList);
        return ui.getRootPanel();
    }

    public ArrayList<PhpClass> getSelectedClasses() {
        return ui.getSelectedClasses();
    }
}
