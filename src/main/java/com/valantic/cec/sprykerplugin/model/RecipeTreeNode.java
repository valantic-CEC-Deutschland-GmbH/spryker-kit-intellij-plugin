package com.valantic.cec.sprykerplugin.model;

import java.util.Dictionary;

public class RecipeTreeNode {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RecipeTreeNode[] getContents() {
        return contents;
    }

    public void setContents(RecipeTreeNode[] contents) {
        this.contents = contents;
    }

    public RecipeTreeNode(String type, String name, RecipeTreeNode[] contents, Dictionary<String, Object> argumentCollection) {
        this.type = type;
        this.name = name;
        this.contents = contents;
        this.argumentCollection = argumentCollection;
    }

    private String type;
    private String name;
    private String baseTemplate;
    private Dictionary<String, Object> argumentCollection;
//    module:
//    inherit: true
//
//    organization:
//    inherit: true
//    default: Spryker
//
//    template:
//    value: Zed/ZedDependencyProvider.php.twig
//
//    extends:
//    value: \Spryker\Zed\Kernel\AbstractBundleDependencyProvider
//
//    targetFilename:
//    value: "{{ module }}DependencyProvider.php"
//
//    targetPath:
//    value: "{{ moduleRoot }}/src/{{ organization }}/Zed/{{ module }}/"
    private RecipeTreeNode[] contents;
}
