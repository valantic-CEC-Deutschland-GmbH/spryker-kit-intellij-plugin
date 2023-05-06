package com.valantic.cec.sprykerplugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.valantic.cec.sprykerplugin.icons.ValanticIcons;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.model.chatgpt.ChatGptPromptInterface;
import com.valantic.cec.sprykerplugin.services.ChatGptExecuter;
import com.valantic.cec.sprykerplugin.services.ChatGptExecuterInterface;
import org.jetbrains.annotations.NotNull;

public class ChatGptContextAction extends AnAction {
    private final ChatGptPromptInterface prompt;
    private final Context context;

    public ChatGptContextAction(ChatGptPromptInterface prompt, Context context) {
        super(prompt.getPromptType() == null ? "ChatGpt Action" : prompt.getPromptType(), prompt.getPromptType() == null ? "ChatGpt action" : prompt.getPromptType(), ValanticIcons.folderIcon);
        this.prompt = prompt;
        this.context = context;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        assert prompt != null;
        context.getProject().getService(ChatGptExecuterInterface.class).execute(context.getProject(), prompt, context);
    }
}
