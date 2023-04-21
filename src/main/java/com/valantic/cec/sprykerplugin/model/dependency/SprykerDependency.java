package com.valantic.cec.sprykerplugin.model.dependency;

import java.util.Arrays;
import java.util.StringTokenizer;

public class SprykerDependency {
    private String moduleName;
    private String dependencyType;

    private String constantPropertyName;

    private static String[] validDependencyTypes = {"CLIENT", "FACADE", "SERVICE", "QUERY_CONTAINER"};

    private SprykerDependency(String constantPropertyName, String moduleName, String dependencyType){

        this.moduleName = moduleName;
        this.dependencyType = dependencyType;
        this.constantPropertyName = constantPropertyName;
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

        return new SprykerDependency(dependencyProviderConstant, moduleName, dependencyType);
    }

    private static String stringToCamelCaseStartingWithUpperCase(String text) {
        return  text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    public String getMethodName() {

        return "add" + this.moduleName + stringToCamelCaseStartingWithUpperCase(this.dependencyType);
    }

    private static boolean isValidDependencyType(String dependencyTypeCandidate) {
        return (Arrays.asList(validDependencyTypes).contains(dependencyTypeCandidate));
    }


    public String getMethodBody() {
        return "$container->set(static::" + this.constantPropertyName + ", function (Container $container) {\n" +
                "    return $container->getLocator()-> " + this.toCamelCaseStartingWithLowerCase(this.moduleName) + "()->" + this.dependencyType.toLowerCase() + "();\n" +
                "});\n" +
                "\n" +
                "return $container;";
    }

    private String toCamelCaseStartingWithLowerCase(String moduleName) {
        return moduleName.substring(0, 1).toLowerCase() + moduleName.substring(1);
    }
}
