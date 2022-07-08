package com.valantic.cec.sprykerplugin.action.newGroup;

import com.intellij.openapi.actionSystem.AnAction;
import com.valantic.cec.sprykerplugin.model.Context;

public interface NewGroupActionBuilderInterface {

    AnAction[] createActionsForContext(Context context);
}
