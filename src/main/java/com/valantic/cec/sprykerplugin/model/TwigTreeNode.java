package com.valantic.cec.sprykerplugin.model;

public class TwigTreeNode {
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

    public TwigTreeNode[] getContents() {
        return contents;
    }

    public void setContents(TwigTreeNode[] contents) {
        this.contents = contents;
    }

    public TwigTreeNode(String type, String name, TwigTreeNode[] contents) {
        this.type = type;
        this.name = name;
        this.contents = contents;
    }

    private String type;
    private String name;
    private TwigTreeNode[] contents;
}
