package com.valantic.cec.sprykerplugin.services;

import com.valantic.cec.sprykerplugin.model.Context;
import org.jetbrains.annotations.NotNull;

public interface FileNameGeneratorInterface {

    @NotNull String getResultFileNameFromTemplateNameAndContext(String templateName, Context context);
}
