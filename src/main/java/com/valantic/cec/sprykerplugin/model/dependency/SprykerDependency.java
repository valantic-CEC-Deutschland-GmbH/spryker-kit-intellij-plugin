package com.valantic.cec.sprykerplugin.model.dependency;

import java.util.Arrays;
import java.util.StringTokenizer;

public class SprykerDependency {
    private String moduleName;
    private String dependencyType;

    private static String[] validDependencyTypes = {"CLIENT", "FACADE", "SERVICE", "QUERY_CONTAINER"};

    private SprykerDependency(String moduleName, String dependencyType){

        this.moduleName = moduleName;
        this.dependencyType = dependencyType;
    }

    public static SprykerDependency fromString(String dependencyProviderConstant) {
        StringTokenizer tokenizer = new StringTokenizer(dependencyProviderConstant, "_");

        String moduleName = null;
        String dependencyType = null;

        if (tokenizer.countTokens() < 2) {
            return null;
        }

        while (tokenizer.hasMoreTokens()) {
            if (dependencyType == null) {
                dependencyType = tokenizer.nextToken();
                if (dependencyType.equals("QUERY")) {
                    if (!tokenizer.hasMoreTokens()) {
                        return null;
                    }
                    String container = tokenizer.nextToken();
                    if (!container.equals("CONTAINER")) {
                        return null;
                    }
                    dependencyType += "_" + container;
                }
                if (!isValidDependencyType(dependencyType)) {
                    return null;
                }
                continue;
            }
            String text = tokenizer.nextToken().toUpperCase();
            moduleName = (moduleName != null) ? moduleName + stringToCamelCaseStartingWithUpperCase(text) : stringToCamelCaseStartingWithUpperCase(text);
        }

        return null;
    }

    private static String stringToCamelCaseStartingWithUpperCase(String text) {
// todo
        return  text.charAt(0) + text.substring(1).toLowerCase();
    }

    public String getMethodName() {

        return "add" + this.moduleName + this.dependencyType;
    }

    private static boolean isValidDependencyType(String dependencyTypeCandidate) {
        if (Arrays.asList(validDependencyTypes).contains(dependencyTypeCandidate)) {
            return true;
        }
        return false;
    }
}
