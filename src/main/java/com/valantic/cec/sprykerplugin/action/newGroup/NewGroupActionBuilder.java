package com.valantic.cec.sprykerplugin.action.newGroup;

import com.intellij.openapi.actionSystem.AnAction;
import com.jetbrains.rd.util.reactive.KeyValuePair;
import com.valantic.cec.sprykerplugin.action.CreateDirectoryAction;
import com.valantic.cec.sprykerplugin.action.CreateSprykerModuleAction;
import com.valantic.cec.sprykerplugin.action.CreateFileFromTemplateAction;
import com.valantic.cec.sprykerplugin.action.CreateMethodFromTemplateAction;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.model.TwigTreeNode;
import com.valantic.cec.sprykerplugin.services.FileNameGeneratorInterface;
import com.valantic.cec.sprykerplugin.services.TwigResources;
import com.valantic.cec.sprykerplugin.services.TwigResourcesInterface;
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

        if (resourcesMap == null) {
            return new AnAction[]{
                    new CreateFileFromTemplateAction("No class to generate", false)
            };
        }
        return getContextMenuAction(context, resourcesMap);
    }

    @NotNull
    private AnAction[] getContextMenuAction(Context context, KeyValuePair<String, ArrayList<TwigTreeNode>> resourcesMap) {
        AnAction[] menuAction;
        if(context.getClassName() != null) {
            menuAction = getCreateMethodActions(context, resourcesMap);
        } else if (context.getModuleName() != null) {
            AnAction[] fileActions = getCreateFileActions(context, resourcesMap);
            AnAction[] dirActions = getCreateDirectoryActions(context, resourcesMap);
            menuAction = Arrays.copyOf(fileActions, fileActions.length + dirActions.length);
            System.arraycopy(dirActions, 0, menuAction, fileActions.length, dirActions.length);
        } else {
            menuAction = getCreateBundleAction(context, resourcesMap);
        }

        return menuAction;
    }

    private AnAction[] getCreateDirectoryActions(Context context, KeyValuePair<String, ArrayList<TwigTreeNode>> resourcesMap) {
        ArrayList<TwigTreeNode> twigTreeNodes = resourcesMap.getValue();
        ArrayList<CreateDirectoryAction> actions = new ArrayList<>();

        AnAction[] menuAction = new AnAction[0];

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
        menuAction = actions.toArray(menuAction);
        return menuAction;
    }

    private AnAction[] getCreateBundleAction(Context context, KeyValuePair<String, ArrayList<TwigTreeNode>> resourcesMap) {
        AnAction[] ret = new AnAction[1];
        ret[0] = (new CreateSprykerModuleAction());
        return ret;
    }

    private AnAction[] getCreateMethodActions(Context context, KeyValuePair<String, ArrayList<TwigTreeNode>> resourcesMap) {
        ArrayList<TwigTreeNode> methodTemplates = resourcesMap.getValue();

        int length = methodTemplates.size();
        AnAction[] menuAction = new AnAction[length];

        int i = 0;
        for (TwigTreeNode templateNode : methodTemplates )
        {
            String resultMethodName = templateNode.getName().replace(".twig","")
                    .replace(context.getApplicationName(), "");
            menuAction[i++] = new CreateMethodFromTemplateAction("Create " + resultMethodName,
                    context,
                    templateNode.getName(),
                    true);
        }
        return menuAction;
    }

    @NotNull
    private AnAction[] getCreateFileActions(Context context, KeyValuePair<String, ArrayList<TwigTreeNode>> resourcesMap) {
        ArrayList<TwigTreeNode> twigTreeNodes = resourcesMap.getValue();
        ArrayList<CreateFileFromTemplateAction> actions = new ArrayList<>();

        AnAction[] menuAction = new AnAction[0];

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
        menuAction = actions.toArray(menuAction);
        return menuAction;
    }

}
