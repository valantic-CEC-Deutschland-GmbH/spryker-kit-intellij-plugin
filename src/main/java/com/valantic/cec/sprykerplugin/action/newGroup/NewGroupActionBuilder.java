package com.valantic.cec.sprykerplugin.action.newGroup;

import com.intellij.openapi.actionSystem.AnAction;
import com.jetbrains.rd.util.reactive.KeyValuePair;
import com.valantic.cec.sprykerplugin.action.*;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.model.TwigTreeNode;
import com.valantic.cec.sprykerplugin.model.chatgpt.ChatGptPromptInterface;
import com.valantic.cec.sprykerplugin.services.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class NewGroupActionBuilder implements NewGroupActionBuilderInterface {

    @Override
    public AnAction[] createActionsForContext(Context context)
    {
        try {
            return getActionsByContext(context);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return new AnAction[]{
                new CreateFileFromTemplateAction("No class to generate", false)
        };
    }

    private AnAction[] getActionsByContext(Context context) throws URISyntaxException, IOException {
        if (context.getApplicationName() == null) {
            return new AnAction[]{
                    new CreateFileFromTemplateAction("No class to generate", false)
            };
        }
        TwigResourcesInterface resources = context.getProject().getService(TwigResourcesInterface.class);

        KeyValuePair<String, ArrayList<TwigTreeNode>> resourcesMap = resources.getPathsToTwigResourcesAndSprykerDirectoriesForContext(context);

        ArrayList<ChatGptPromptInterface> matchingPrompts = getMatchingPrompts(context);

        if ((resourcesMap == null) && (matchingPrompts.isEmpty())) {
            return new AnAction[]{
                    new CreateFileFromTemplateAction("No class to generate", false)
            };
        }
        return getContextMenuAction(context, resourcesMap, matchingPrompts);
    }

    private ArrayList<ChatGptPromptInterface> getMatchingPrompts(Context context) {
        ProjectSettingsState projectSettings =  context.getProject().getService(ProjectSettingsState.class);

        ArrayList<ChatGptPromptInterface> prompts = projectSettings.prompts;

        ArrayList<ChatGptPromptInterface> matchingPrompts = new ArrayList<>();

        for (ChatGptPromptInterface prompt : prompts) {
            if (prompt == null) {
                continue;
            }
            Context necessaryContext = context.getProject()
                    .getService(ContextBuilderInterface.class)
                .createContextFromContextString(prompt.getNecessaryContextString());

            if ((necessaryContext != null) && necessaryContext.matches(context)) {
                matchingPrompts.add(prompt);
            }
        }

        return matchingPrompts;
    }

    @NotNull
    private AnAction[] getContextMenuAction(Context context, KeyValuePair<String, ArrayList<TwigTreeNode>> resourcesMap, ArrayList<ChatGptPromptInterface> matchingPrompts) {
        ArrayList<AnAction> actions = new ArrayList<AnAction>();

        if (resourcesMap != null) {
            actions.addAll(getTwigBasedActions(context, resourcesMap, actions));
        }
        if (matchingPrompts != null) {
            actions.addAll(getPromptsActions(context, matchingPrompts));
        }

        int size = actions.size();
        AnAction[] ret = new AnAction[size];

        return actions.toArray(ret);
    }

    @NotNull
    private ArrayList<AnAction> getTwigBasedActions(Context context, KeyValuePair<String, ArrayList<TwigTreeNode>> resourcesMap, ArrayList<AnAction> actions) {
        if(context.getClassName() != null) {
            actions = getCreateMethodActions(context, resourcesMap);

        } else if (context.getModuleName() != null) {
            actions.addAll(getCreateFileActions(context, resourcesMap));
            actions.addAll(getCreateDirectoryActions(context, resourcesMap));
        } else {
            actions.addAll(getCreateBundleAction(context, resourcesMap));
        }
        return actions;
    }

    private ArrayList<AnAction> getPromptsActions(Context context, ArrayList<ChatGptPromptInterface> matchingPrompts) {
        ArrayList<AnAction> actions = new ArrayList<>();

        for (ChatGptPromptInterface prompt : matchingPrompts) {
            actions.add(new ChatGptContextAction(prompt, context));
        }

        return actions;
    }

    private ArrayList<AnAction> getCreateDirectoryActions(Context context, KeyValuePair<String, ArrayList<TwigTreeNode>> resourcesMap) {
        ArrayList<TwigTreeNode> twigTreeNodes = resourcesMap.getValue();
        ArrayList<AnAction> actions = new ArrayList<>();

        int i = 0;
        for (TwigTreeNode twigTreeNode : twigTreeNodes) {
            String resultDirName = twigTreeNode.getName();
            if (twigTreeNode.getType().equals(TwigResources.DIRECTORY_NODE_TYPE)
                    && !(new File(context.getDirPath() + "/" + resultDirName).exists()))
            {
                actions.add(new CreateDirectoryAction("Create " + resultDirName + " Directory",
                        context,
                        twigTreeNode.getName())
                );
            }
        }
        return actions;
    }

    private ArrayList<AnAction> getCreateBundleAction(Context context, KeyValuePair<String, ArrayList<TwigTreeNode>> resourcesMap) {
        ArrayList<AnAction> ret = new ArrayList<>(1);
        ret.add(new CreateSprykerModuleAction());

        return ret;
    }

    private ArrayList<AnAction> getCreateMethodActions(Context context, KeyValuePair<String, ArrayList<TwigTreeNode>> resourcesMap) {
        ArrayList<TwigTreeNode> methodTemplates = resourcesMap.getValue();

        int length = methodTemplates.size();
        ArrayList<AnAction> actions = new ArrayList<>(length);

        int i = 0;
        for (TwigTreeNode templateNode : methodTemplates )
        {
            String resultMethodName = templateNode.getName().replace(".twig","")
                    .replace(context.getApplicationName(), "");
            actions.add(new CreateMethodFromTemplateAction("Create " + resultMethodName,
                    context,
                    templateNode.getName(),
                    true));
        }
        return actions;
    }

    @NotNull
    private ArrayList<AnAction> getCreateFileActions(Context context, KeyValuePair<String, ArrayList<TwigTreeNode>> resourcesMap) {
        ArrayList<TwigTreeNode> twigTreeNodes = resourcesMap.getValue();
        ArrayList<AnAction> actions = new ArrayList<>();

        int i = 0;
        for (TwigTreeNode twigTreeNode : twigTreeNodes)
        {
            String resultFileName = context.getProject().getService(FileNameGeneratorInterface.class).
                    getResultFileNameFromTemplateNameAndContext(twigTreeNode.getName(), context);

            if (twigTreeNode.getType().equals(TwigResources.DIRECTORY_NODE_TYPE)
                    || new File(context.getDirPath() + "/" + resultFileName).exists()
            ) {
                continue;
            }

            actions.add(new CreateFileFromTemplateAction("Create " + resultFileName,
                    context,
                    twigTreeNode.getName(),
                    resultFileName,
                    true)
            );
        }

        return actions;
    }

}
