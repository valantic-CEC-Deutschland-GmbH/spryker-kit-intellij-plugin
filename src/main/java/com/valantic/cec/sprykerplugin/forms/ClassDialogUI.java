package com.valantic.cec.sprykerplugin.forms;

import com.intellij.ui.components.JBList;
import com.jetbrains.php.lang.psi.elements.PhpClass;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ClassDialogUI {

    public ClassDialogUI(ClassListModel listModel) {
        this.listModel = listModel;
    }

    private final ClassListModel listModel;
    private JList<PhpClass> listClasses;
    private JPanel rootPanel;

    public JComponent getRootPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
        listClasses = new JBList<>(listModel);
        listClasses.setSelectionInterval(0,listModel.getSize()-1);
    }

    public ArrayList<PhpClass> getSelectedClasses() {
        List<PhpClass> list = listClasses.getSelectedValuesList();

        return new ArrayList<>(list);
    }
}
