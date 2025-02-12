package com.valantic.cec.sprykerplugin.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import com.valantic.cec.sprykerplugin.model.ClassCreator;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.model.TextParser;
import com.valantic.cec.sprykerplugin.model.chatgpt.ChatGptPromptInterface;
import okhttp3.OkHttpClient;
import org.codehaus.groovy.classgen.asm.BinaryDoubleExpressionHelper;
import retrofit2.Retrofit;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatGptExecuter implements ChatGptExecuterInterface {
    public static final String END_SOURCE_CODE = "[end-source-code]";
    private OpenAiService openAiService;

    private Project project;
    private StringBuilder buffer;

    public ChatGptExecuter(Project project) {
        this.project = project;
        ProjectSettingsState state = project.getService(ProjectSettingsState.class);

        ObjectMapper mapper = OpenAiService.defaultObjectMapper();
        OkHttpClient client = OpenAiService.defaultClient(state.openAiApiKey, Duration.ofMinutes(1))
            .newBuilder()
            .build();
        Retrofit retrofit = OpenAiService.defaultRetrofit(client, mapper);

        OpenAiApi api = retrofit.create(OpenAiApi.class);
        this.openAiService  = new OpenAiService(api);
    }

    @Override
    public void execute(Project project, ChatGptPromptInterface prompt, Context context) {
        ProjectSettingsState state = project.getService(ProjectSettingsState.class);
        final List<ChatMessage> messages = new ArrayList<>();

        String message = getMessageFromPromptAndContext(prompt, context);

        System.out.println("Prompt used:" + message);

        final ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), message);
        messages.add(userMessage);

        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
            .messages(messages)
            .model(state.model)
            .build();

        this.buffer = new StringBuilder();

        ArrayList<String> errorMessages = new ArrayList<>();
        this.openAiService.streamChatCompletion(completionRequest).forEach(completion -> {
            String str = completion.getChoices().get(0).getMessage().getContent();

            if (str != null) buffer.append(str);
            if (buffer.length() > 10) {
                int index = buffer.lastIndexOf(END_SOURCE_CODE);
                if (index > 0) {
                    try {
                        createClasses(buffer.toString());
                    } catch (Exception e) {
                        errorMessages.add(e.getMessage());
                    }
                    buffer.delete(0, index + END_SOURCE_CODE.length());
                }
            }
        });

        if (errorMessages.size() > 0) {
            messages.clear();
            message = "Error while creating classes: \n";
            for (String errorMessage : errorMessages) {
                message += errorMessage + "\n";
            }
            final ChatMessage userMessage2 = new ChatMessage(ChatMessageRole.USER.value(), message);
            messages.add(userMessage);

            completionRequest = ChatCompletionRequest.builder()
                .messages(messages)
                .model(state.model)
                .build();
        }
//        this.openAiService.createChatCompletion(completionRequest).getChoices().forEach(choice -> {
//            createClasses(choice.getMessage());
//        });
    }

    private void createClasses(String content) {

        List<String> classNames = TextParser.getClassNames(content);
        List<String> sourceCode = TextParser.getSourceCode(content);

        for (int i = 0; i < classNames.size(); i++) {
            System.out.println("Class name: " + classNames.get(i));
            System.out.println("Source code: " + sourceCode.get(i));
        }

        WriteCommandAction.writeCommandAction(this.project)
            .withGroupId("Create Command")
            .run(() -> {
                try {
                    PsiFile file = ClassCreator.createClasses(classNames, sourceCode, this.project);
                    file.navigate(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
    }

    private String getMessageFromPromptAndContext(ChatGptPromptInterface prompt, Context context) {
        String projectName = this.project.getService(ProjectSettingsState.class).projectNamespace;

        return prompt.getPrompt()
            .replace("<%ProjectName%>", projectName)
            .replace("<%ApplicationName%>", context.getApplicationName())
            .replace("<%ModuleName%>", context.getModuleName())
            .replace("<%ClassName%>", context.getClassName())
            .replace("<%InnerPath%>", context.getInnerPath());
    }
}
