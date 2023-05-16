package com.valantic.cec.sprykerplugin.model.chatgpt;

public interface ChatGptPromptInterface {
    String getPrompt();

    String getPromptType();

    String getNecessaryContextString();

    String getIntentionText();
}
