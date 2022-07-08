package com.valantic.cec.sprykerplugin.config.form;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ApplicationSettingsComponent {
    private final JPanel myMainPanel;
    private final JBTextField myProjectNamespaceText = new JBTextField();

    public ApplicationSettingsComponent() {
        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Enter project namespace: "), myProjectNamespaceText, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return myProjectNamespaceText;
    }

    @NotNull
    public String getProjectNamespaceText() {
        return myProjectNamespaceText.getText();
    }

    public void setProjectNamespaceTextText(@NotNull String newText) {
        myProjectNamespaceText.setText(newText);
    }
}
