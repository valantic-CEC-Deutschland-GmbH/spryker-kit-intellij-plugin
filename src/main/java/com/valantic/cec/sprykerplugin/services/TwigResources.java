package com.valantic.cec.sprykerplugin.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.intellij.openapi.project.Project;
import com.jetbrains.rd.util.reactive.KeyValuePair;
import com.valantic.cec.sprykerplugin.constants.SprykerConstants;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.model.TwigTreeNode;
import com.valantic.cec.sprykerplugin.resources.FileResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TwigResources implements TwigResourcesInterface {

    private static final String METHOD_IDENTIFIER = "Method";
    private final Map<String, ArrayList<TwigTreeNode>> map = new HashMap<>();


    private final static String twigResourcesTreePath = "template-map.json";
    public final static String DIRECTORY_NODE_TYPE = "directory";

    protected static TwigResources instance = null;

    private Project project;


    //ToDo: Alles im Ordner != src ist modulspezifisch
    public TwigResources(Project project) {
        this.project = project;

        String jsonString = FileResource.readFileContentFromResources(twigResourcesTreePath);
        JsonArray arr = null;
        Gson gson = new Gson();
        try {
            arr = gson.fromJson(jsonString, JsonArray.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        if (arr == null) {
            return;
        }
        TwigTreeNode[] nodes = getTwigTreeNodesFromArray(arr);

        generateMapFromNodes(nodes);
    }

    private void generateMapFromNodes(TwigTreeNode[] nodes) {
        if (nodes[0].getName().equals("templates/")) {

            ProjectSettingsState state = this.project.getService(ProjectSettingsState.class);


            String projectNamespace = SprykerConstants.PROJECTNAMESPACE;

            if (state != null) {
                projectNamespace = state.projectNamespace;
            } else {
                ApplicationSettingsState appState = ApplicationSettingsState.getInstance();
                if (appState != null) {
                    projectNamespace = appState.projectNamespace;
                }
            }


            String currentPath = "src/" + projectNamespace + "/";
            addContentsToMap(currentPath, nodes[0].getContents() );
        }
    }

    private void addContentsToMap(String currentPath, TwigTreeNode[] contents) {
        for (TwigTreeNode node : contents) {
            addNodeToMap(currentPath, node);
            if (node.getType().equals(DIRECTORY_NODE_TYPE)) {
                String newCurrentPath = currentPath + node.getName() + "/";
                if (node.getContents() != null) {
                    addContentsToMap(newCurrentPath, node.getContents());
                }
            }
        }
    }

    private void addNodeToMap(String currentPath, TwigTreeNode node) {
        ArrayList<TwigTreeNode> resourceFileList = this.map.get(currentPath);
        if (resourceFileList == null) {
            resourceFileList = new ArrayList<>();
        }

        String name = node.getName();
        String pathToUse = currentPath;

        if (name.startsWith(METHOD_IDENTIFIER)) {
            pathToUse += METHOD_IDENTIFIER;
        }

        resourceFileList.add(node);

        this.map.put(pathToUse, resourceFileList);
    }

    private TwigTreeNode createTreeNodeFromJSONObject(Object obj) {
        JsonObject jsonObj = (JsonObject) obj;

        String type = (jsonObj.get("type") != null) ? jsonObj.get("type").getAsString() : null;
        String name = (jsonObj.get("name") != null) ? jsonObj.get("name").getAsString() : null;
        Object contentsObj = jsonObj.get("contents");

        TwigTreeNode[] contents = null;
        if (contentsObj != null) {
            contents = getTwigTreeNodesFromArray(contentsObj);
        }

        return new TwigTreeNode(type, name, contents);
    }

    private TwigTreeNode[] getTwigTreeNodesFromArray(Object array) {
        JsonArray arr = (JsonArray) array;

        int length = arr.size();
        TwigTreeNode[] nodes = new TwigTreeNode[length];
        int i = 0;
        for (Object obj : arr) {
            if (i >= length) {
                break;
            }
            TwigTreeNode node = createTreeNodeFromJSONObject(obj);
            nodes[i++] = node;
        }

        return nodes;
    }

    @Override
    public KeyValuePair<String, ArrayList<TwigTreeNode>> getPathsToTwigResourcesForContext(Context context)
    {
        KeyValuePair<String, ArrayList<TwigTreeNode>> result = this.getPathsToTwigResourcesAndSprykerDirectoriesForContext(context);
        if (result == null) {
            return null;
        }
        ArrayList<TwigTreeNode> list = result.getValue();

        if (list != null) {
            this.removeDirectoriesFromTwigTreeNodeList(list);
        }

        return result;
    }

    @Override
    public KeyValuePair<String, ArrayList<TwigTreeNode>> getPathsToTwigResourcesAndSprykerDirectoriesForContext(Context context)
    {
        String applicationName = context.getApplicationName();
        String innerPath = context.getInnerPath();
        ApplicationSettingsState state = ApplicationSettingsState.getInstance();
        String projectNamespace = SprykerConstants.PROJECTNAMESPACE;
        if (state != null) {
            projectNamespace = state.projectNamespace;
        }
        String path = "src/" + projectNamespace + "/";

        if (applicationName != null) {
            path += applicationName + "/";
        }

        if (innerPath != null) {
            path += innerPath + "/";
        }

        if (context.getClassName() != null) {
            path += "Method";
        }

        if (this.map.containsKey(path)) {
            return new KeyValuePair<>(path, this.map.get(path));
        }
        return null;
    }

    private void removeDirectoriesFromTwigTreeNodeList(ArrayList<TwigTreeNode> list) {
        ArrayList<TwigTreeNode> nodesToRemove = new ArrayList<>();
        for (TwigTreeNode node: list) {
            if (node.getType().equals(DIRECTORY_NODE_TYPE)) {
                nodesToRemove.add(node);
            }
        }
        for (TwigTreeNode node: nodesToRemove) {
            list.remove(node);
        }
    }
}
