package com.valantic.cec.sprykerplugin.config.form;

import com.intellij.openapi.project.Project;
import com.valantic.cec.sprykerplugin.model.chatgpt.ChatGptPrompt;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Objects;

public class ProjectSettingsComponent {
    private JPanel rootPanel;
    private JLabel prjNamespaceLbl;
    private JTextField tFProjectNamespace;

    private JTextField tfOpenAiApiKey;
    private JLabel openAiApiKeyLbl;

    private JTable tPrompts;
    private JButton btnDelete;
    private JButton btnAdd;
    private JScrollPane sPprompts;
    private JTextField tFModel;
    private JLabel modlelLbl;

    private DefaultTableModel promptsTableModel;

    private Project project;

    public ProjectSettingsComponent(String projectNamespace, String openAiApiKey, ArrayList<ChatGptPrompt> prompts, String model, Project project) {
        this.prjNamespaceLbl.setText("Enter project namespace: ");
        this.tFProjectNamespace.setText(projectNamespace);
        this.tfOpenAiApiKey.setText(openAiApiKey);
        this.promptsTableModel = new DefaultTableModel(promptsTo2DArray(prompts), new String[]{"Context", "Prompt", "Prompt Type", "Intention Text"});
        this.tPrompts.setModel(promptsTableModel);
        this.tPrompts.setShowGrid(true);

        this.tFModel.setText(model);

        this.btnAdd.addActionListener(e -> {
            promptsTableModel.addRow(new Object[]{"", "", ""});
        });

        this.btnDelete.addActionListener(e -> {
            int selectedRow = tPrompts.getSelectedRow();
            if (selectedRow != -1) {
                promptsTableModel.removeRow(selectedRow);
            }
        });
        this.tPrompts.setSize(500, 500);
        this.tPrompts.setVisible(true);
        this.project = project;
    }

    private Object[][] promptsTo2DArray(ArrayList<ChatGptPrompt> prompts) {
        prompts.removeIf(Objects::isNull);
        int size = prompts.size();

        Object[][] prompts2DArray = new Object[size][4];

        for (int i = 0; i < size; i++) {
            if (prompts.get(i) == null) {
                continue;
            }
            prompts2DArray[i][0] = prompts.get(i).getNecessaryContextString();
            prompts2DArray[i][1] = prompts.get(i).getPrompt();
            prompts2DArray[i][2] = prompts.get(i).getPromptType();
            prompts2DArray[i][3] = prompts.get(i).getIntentionText();
        }
        return prompts2DArray;
    }

    public JPanel getRootPanel() {
        return this.rootPanel;
    }

    public String getProjectNamespace() {
        return this.tFProjectNamespace.getText();
    }

    public String getOpenAiApiKey() {
        return this.tfOpenAiApiKey.getText();
    }

    public String getModel() {
        return this.tFModel.getText();
    }

    public ArrayList<ChatGptPrompt> getPrompts() {
        int numPrompts = promptsTableModel.getRowCount();

        ArrayList<ChatGptPrompt> prompts = new ArrayList<>();
        for (int i = 0; i < numPrompts; i++) {
            prompts.add(
                new ChatGptPrompt(
                    (promptsTableModel.getValueAt(i, 0) != null)? promptsTableModel.getValueAt(i, 0).toString() : "",
                    (promptsTableModel.getValueAt(i, 1) != null)? promptsTableModel.getValueAt(i, 1).toString() : "",
                    (promptsTableModel.getValueAt(i, 2) != null)? promptsTableModel.getValueAt(i, 2).toString() : "",
                    (promptsTableModel.getValueAt(i, 3) != null)? promptsTableModel.getValueAt(i, 3).toString() : ""
                )
            );
        }

        return prompts;
    }
}
