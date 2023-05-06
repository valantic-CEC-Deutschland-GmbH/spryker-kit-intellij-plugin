package com.valantic.cec.sprykerplugin.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextParser {

    private static final String START_CLASS_NAME = "//start-class-name:";
    private static final String END_CLASS_NAME = "//end-class-name";
    private static final String START_SOURCE_CODE = "//start-source-code:";
    private static final String END_SOURCE_CODE = "//end-source-code";

    public static List<String> getClassNames(String text) {
        List<String> classNames = new ArrayList<>();
        Pattern pattern = Pattern.compile(START_CLASS_NAME + "(.*?)" + END_CLASS_NAME);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            classNames.add(matcher.group(1));
        }
        return classNames;
    }

    public static List<String> getSourceCode(String text) {
        List<String> sourceCode = new ArrayList<>();
        Pattern pattern = Pattern.compile(START_SOURCE_CODE + "(.*?)" + END_SOURCE_CODE, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            sourceCode.add(matcher.group(1));
        }
        return sourceCode;
    }

}