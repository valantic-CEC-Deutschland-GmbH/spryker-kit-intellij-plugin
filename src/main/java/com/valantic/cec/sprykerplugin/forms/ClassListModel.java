package com.valantic.cec.sprykerplugin.forms;

import com.jetbrains.php.lang.psi.elements.PhpClass;

import javax.swing.*;
import java.util.ArrayList;

public class ClassListModel extends AbstractListModel<PhpClass> {

    public ClassListModel(ArrayList<PhpClass> list) {
        this.list = list;
    }

    private final ArrayList<PhpClass> list;

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public PhpClass getElementAt(int index) {
        return list.get(index);
    }
}
