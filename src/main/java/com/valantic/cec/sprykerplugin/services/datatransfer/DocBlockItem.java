package com.valantic.cec.sprykerplugin.services.datatransfer;

public class DocBlockItem {

    public DocBlockItem(String tag, String returnType) {
        this(tag, returnType, null);
    }

    public DocBlockItem(String tag, String returnType, String value) {
        this.tag = tag;
        this.returnType = returnType;
        this.value = value;
    }

    public String tag;
    public String returnType;
    public String value;
}
