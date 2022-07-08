package com.valantic.cec.sprykerplugin.services;

import com.jetbrains.rd.util.reactive.KeyValuePair;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.model.TwigTreeNode;

import java.util.ArrayList;

public interface TwigResourcesInterface {
    KeyValuePair<String, ArrayList<TwigTreeNode>> getPathsToTwigResourcesForContext(Context context);

    KeyValuePair<String, ArrayList<TwigTreeNode>> getPathsToTwigResourcesAndSprykerDirectoriesForContext(Context context);
}
