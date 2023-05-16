package com.valantic.cec.sprykerplugin.model.chatgpt;

import java.io.Serial;
import java.io.Serializable;

public class ChatGptPrompt implements ChatGptPromptInterface, Serializable {
    @Serial
    public String necessaryContextString;

    @Serial
    private String prompt;

    @Serial
    private String promptType;

    private String intentionText;

    public ChatGptPrompt() {
    }

    public ChatGptPrompt(String necessaryContextString, String prompt, String promptType, String intentionText) {
        this.necessaryContextString = necessaryContextString;
        this.prompt = prompt;
        this.promptType = promptType;
        this.intentionText = intentionText;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setPromptType(String promptType) {
        this.promptType = promptType;
    }

    public void setNecessaryContextString(String necessaryContextString) {
        this.necessaryContextString = necessaryContextString;
    }

    @Override
    public String getPrompt() {
        return prompt;
    }

    @Override
    public String getPromptType() {
        return promptType;
    }

    @Override
    public String getNecessaryContextString() {
        return necessaryContextString;
    }

    public String getIntentionText() {
        return intentionText;
    }

    public void setIntentionText(String intentionText) {
        this.intentionText = intentionText;
    }
}
