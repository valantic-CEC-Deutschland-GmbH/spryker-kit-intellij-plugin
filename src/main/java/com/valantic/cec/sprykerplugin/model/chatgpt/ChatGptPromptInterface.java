package com.valantic.cec.sprykerplugin.model.chatgpt;

import com.valantic.cec.sprykerplugin.model.Context;

public interface ChatGptPromptInterface {
    String getPrompt();

    String getPromptType();

    String getNecessaryContextString();
}
