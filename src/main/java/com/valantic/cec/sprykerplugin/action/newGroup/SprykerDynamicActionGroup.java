package com.valantic.cec.sprykerplugin.action.newGroup;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.services.ContextBuilder;
import com.valantic.cec.sprykerplugin.services.ContextBuilderInterface;
import org.jetbrains.annotations.NotNull;

public class SprykerDynamicActionGroup extends ActionGroup {

    @NotNull
    @Override
    public AnAction @NotNull [] getChildren(AnActionEvent event) {
        ContextBuilderInterface builder = new ContextBuilder(event.getProject());

        Context context = builder.createContextFromAnActionEvent(event);
        if (context == null) {
            return new AnAction[0];
        }
        NewGroupActionBuilderInterface actionBuilder = new NewGroupActionBuilder();

        AnAction[] children = actionBuilder.createActionsForContext(context);

        if (children != null) {
            event.getPresentation().setEnabledAndVisible(true);
        }
        if (children == null) {
            children = new AnAction[0];
        }

        return children;
    }


    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(true);
    }
}
