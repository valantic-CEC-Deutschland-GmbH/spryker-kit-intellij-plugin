package com.valantic.cec.sprykerplugin.services;

import com.github.hypfvieh.util.StringUtil;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.valantic.cec.sprykerplugin.services.datatransfer.DocBlockItem;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * derived from PhpClassRenderer
 * from the Spryker plugin https://github.com/tobi812/idea-php-spryker-plugin
 */

public class PhpClassStringGenerator implements PhpClassStringGeneratorInterface {
    @Override
    public String getFactoryMethodString(PhpClass phpClass) {
        String method = "public function create";
        method += phpClass.getName();
        method += "()\n{\n";
        String returnStatement = MessageFormat.format("    return new {0}(", phpClass.getName());
        method += returnStatement;

        Method constructor = phpClass.getConstructor();
        if (constructor != null) {
            method += this.getConstructorCallString(constructor, phpClass);
        }
        method += "\n     );\n}";

        return method;
    }

    @Override
    public String getDocBlockString(ArrayList<DocBlockItem> docBlockItems) {
        if (docBlockItems.isEmpty()) {
            return "";
        }

        StringBuilder docBlock = new StringBuilder("/**\n");
        for (DocBlockItem docBlockItem : docBlockItems) {
            ArrayList<String> docBlockElements = new ArrayList<>();
            docBlockElements.add(docBlockItem.tag);
            docBlockElements.add(docBlockItem.returnType);

            if (docBlockItem.value != null) {
                docBlockElements.add(docBlockItem.value);
            }

            docBlock.append(MessageFormat.format(" * {0}\n", StringUtil.join(" ", docBlockElements)));
        }

        docBlock.append(" */\n");

        return docBlock.toString();
    }

    private String getConstructorCallString(Method constructor, PhpClass phpClass) {
        ArrayList<String> methodCalls = new ArrayList<>();
        Parameter[] parameters = constructor.getParameters();
        for (Parameter parameter : parameters) {
            if (parameter.getDeclaredType().isEmpty()) {
                methodCalls.add(this.createMethodCallString(
                        parameter.getName().replaceFirst(parameter.getName().substring(0, 1), parameter.getName().substring(0, 1).toUpperCase(Locale.ROOT))));
                continue;
            }

            String classNamespace = this.extractModuleNamespace(phpClass.getNamespaceName());
            String parameterNamespace = this.extractModuleNamespace(parameter.getDeclaredType().toString());

            String [] tokens = parameter.getLocalType().toString().split("\\\\");
            String parameterType = "";
            if (tokens.length > 0) {
                parameterType = tokens[tokens.length - 1];
            }


            var methodPrefix = "create";
            if (!classNamespace.equals(parameterNamespace)) {
                methodPrefix = "get";
            }

            methodCalls.add(this.createMethodCallString(parameterType, methodPrefix));
        }

        return StringUtil.join( ",", methodCalls);
    }

    private String extractModuleNamespace(String namespaceName) {
        String preparedNamespace = namespaceName.replaceAll("^\\\\", "");
        String[] namespaceSegments = preparedNamespace.split("\\\\");
        String appName = namespaceSegments[1];
        int occurence = 3;

        if (appName.equals("Zed")) {
            occurence = 4;
        }

        int index = this.indexOf(preparedNamespace, "\\", occurence);

        return preparedNamespace.substring(0, index).replaceAll("\\\\$", "");

    }

    private int indexOf(String string, String subString, int occurence)
    {
        int i = 0;
        int match = 0;
        while (i < occurence) {
            match = string.indexOf(subString, match) + 1;
            i++;
        }

        return match;
    }

    private @NotNull String createMethodCallString(String className) {
        return this.createMethodCallString(className, "create");


    }
    private @NotNull String createMethodCallString(@NotNull String className, String methodPrefix) {
        String preparedClassName = className
                .replaceAll("Interface$", "")
                .replaceAll("^Abstract", "");

        return MessageFormat.format("\n$this->{0}{1}()", methodPrefix, preparedClassName);
    }
}
