package com.valantic.cec.sprykerplugin.services;

import com.jetbrains.rd.util.reactive.KeyValuePair;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.model.RecipeTreeNode;
import com.valantic.cec.sprykerplugin.model.TwigTreeNode;

import java.util.ArrayList;

interface RecipeRepositoryInterface {
    KeyValuePair<String, ArrayList<RecipeTreeNode>> getPathsToRecipesForContext(Context context);

    KeyValuePair<String, ArrayList<RecipeTreeNode>> getPathsToRecipesAndSprykerDirectoriesForContext(Context context);
}
