package com.valantic.cec.sprykerplugin.services;

import com.intellij.openapi.project.Project;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.model.chatgpt.ChatGptPromptInterface;

public interface ChatGptExecuterInterface {
    void execute(Project project, ChatGptPromptInterface prompt, Context context);
}
