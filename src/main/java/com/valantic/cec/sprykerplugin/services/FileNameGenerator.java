package com.valantic.cec.sprykerplugin.services;

import com.valantic.cec.sprykerplugin.constants.SprykerConstants;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.twig.UnderscoredFilter;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;

public class FileNameGenerator implements FileNameGeneratorInterface {
    @Override
    @NotNull
    public String getResultFileNameFromTemplateNameAndContext(String templateName, Context context) {

        if (templateName.startsWith("$")) {
            return templateName.replaceFirst(Matcher.quoteReplacement("$"), "").replace(".twig", "");
        }

        int firstDot = templateName.indexOf('.');
        int secondDot = templateName.lastIndexOf('.');

        if (secondDot <= firstDot) {
            return templateName;
        }

        String targetFileType = templateName.toLowerCase().substring(firstDot
                , secondDot);

        switch (targetFileType) {
            case ".xml":
                return getResultFileNameForSchemaOrTransfer(templateName, context);

            case ".yml":
                return getResultFileNameForValidationOrCodeCeption(templateName, context);

            case ".php":
                return getResultFileNameForPhp(templateName, context);
        }

        return templateName;
    }

    private String getResultFileNameForValidationOrCodeCeption(String templateName, Context context) {
        if (templateName.toLowerCase().contains("validation") ) {
            return "" + new UnderscoredFilter().apply(context.getModuleName(), null, null, null, 0)
                    + "."
                    + templateName.toLowerCase().replace(".twig", "");
        }

        return "" + templateName.replace(".twig", "");
    }

    private String getResultFileNameForPhp(String templateName, Context context) {
        String resultFileName = templateName
                .replace(".twig", "");

        return  context.getModuleName() + resultFileName;
    }

    private String getResultFileNameForSchemaOrTransfer(String templateName, Context context) {
        String projectName = null;
        if (templateName.contains("schema") ) {
            ProjectSettingsState state = context.getProject().getService(ProjectSettingsState.class);
            //ApplicationSettingsState state = ApplicationSettingsState.getInstance();
            projectName = (state != null)? state.projectNamespace.toLowerCase() : SprykerConstants.PROJECTNAMESPACE.toLowerCase();
        }

        return ((projectName != null) ? (projectName + "_") : "")
                        + (String) new UnderscoredFilter().apply(context.getModuleName(), null, null, null, 0)
                        + "."
                        + templateName.toLowerCase().replace(".twig", "");
    }


}
