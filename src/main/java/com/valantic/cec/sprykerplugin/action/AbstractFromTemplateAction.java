package com.valantic.cec.sprykerplugin.action;

import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.NlsActions;
import com.valantic.cec.sprykerplugin.model.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class AbstractFromTemplateAction extends AnAction {
    protected Boolean enabled;
    protected String templateFileName;
    protected Context context;

    public AbstractFromTemplateAction(
            @Nullable @NlsActions.ActionDescription String description,
            @Nullable Icon icon,
            Context context,
            String templateFileName,
            Boolean enabled
    ) {
        super( description, description, icon);
        this.templateFileName = templateFileName;
        this.context = context;
        this.enabled = enabled;
    }

    @Override
    public abstract void actionPerformed(@NotNull AnActionEvent e);

    @NotNull
    protected String getTemplatePathString() {
        String path = "templates/";
        if (this.context.getApplicationName() != null) {
            path += this.context.getApplicationName() + "/";
        }

        if (this.context.getInnerPath() != null) {
            path += this.context.getInnerPath() + "/";
        }
        path += this.templateFileName;
        return path;
    }

    /**
     * Only make this action visible when text is selected.
     *
     * @param e Event object holding information about what is going on
     */
    @Override
    public void update(AnActionEvent e) {

        e.getPresentation().setEnabledAndVisible(this.enabled);
    }
}
