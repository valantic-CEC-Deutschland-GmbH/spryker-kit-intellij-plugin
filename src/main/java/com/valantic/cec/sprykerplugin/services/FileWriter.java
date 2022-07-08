package com.valantic.cec.sprykerplugin.services;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import com.valantic.cec.sprykerplugin.constants.SprykerConstants;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.resources.FileResource;
import com.valantic.cec.sprykerplugin.twig.SprykCompatibilityPebbleExtension;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FileWriter implements FileWriterInterface{

    private Project project;

    public FileWriter(Project project) {
        this.project = project;
    }

    @Override
    public PsiFile createFileFromTemplate(Context context, PsiDirectory fileDirectory, String templatePathString, String resultFileName) throws IOException {

        if (fileDirectory.findFile(resultFileName) != null) {
            throw new IOException(String.format("File %s already exists.", resultFileName));
        }
        String content = null;
        if (
                !templatePathString.contains(".php.twig") && !templatePathString.contains(".yml.twig")
                && !templatePathString.contains(".md.twig") && !templatePathString.contains(".json.twig")
                && !templatePathString.contains(".xml.twig")
        ) {
            content = FileResource.readFileContentFromResources(templatePathString);
        } else {
            try {
                content = getStringFromTemplate(context, templatePathString);
            }
            catch (PebbleException e) {
                e.printStackTrace();
            }
        }
        try {
            return createPsiFileWithContent(fileDirectory, resultFileName, content);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param fileDirectory
     * @param resultFileName
     * @param content
     * @return
     * @throws IOException
     */
    @Nullable
    private PsiFile createPsiFileWithContent(PsiDirectory fileDirectory, String resultFileName, String content) throws IOException {

        WriteCommandAction.writeCommandAction(this.project)
                .withGroupId("Create Command")
                .run(() -> {
                    PsiFile file = fileDirectory.createFile(resultFileName);
                    file.getVirtualFile().setBinaryContent(content.getBytes(StandardCharsets.UTF_8));
                    file.clearCaches();
                    file.subtreeChanged();
                });

        return fileDirectory.findFile(resultFileName);
    }

    /**
     * @param context
     * @param templatePathString
     * @return
     * @throws IOException
     */
    private String getStringFromTemplate(Context context, String templatePathString) throws IOException {
        PebbleEngine engine = new PebbleEngine.Builder().extension(new SprykCompatibilityPebbleExtension()).build();
        ApplicationSettingsState settings = ApplicationSettingsState.getInstance();
        String projectNamespace = SprykerConstants.PROJECTNAMESPACE;
        if (settings != null) {
            projectNamespace = settings.projectNamespace;
        }

        PebbleTemplate compiledTemplate = engine.getTemplate(templatePathString);
        Map<String, Object> pebbleContext = new HashMap<>();
        pebbleContext.put("organization", projectNamespace);
        pebbleContext.put("module", context.getModuleName());
        pebbleContext.put("controller", context.getModuleName());

        String className = (context.getClassName() != null) ? context.getClassName() : context.getModuleName();
        pebbleContext.put("className", className);

        pebbleContext.put("subDirectory", context.getInnerPath());
        pebbleContext.put("interfaceName", className + "Interface");
        pebbleContext.put("constructorArguments", "");

        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);
        compiledTemplate.evaluate(writer, pebbleContext);
        String phpText = stringWriter.toString();
        return phpText;
    }

    public PsiDirectory createDirectory(PsiDirectory fileDirectory, String subDirectoryName) {
        WriteCommandAction.writeCommandAction(this.project)
                .withGroupId("Create Command")
                .run( () -> {
                    fileDirectory.createSubdirectory(subDirectoryName);
                });

        return fileDirectory.findSubdirectory(subDirectoryName);
    }
}
