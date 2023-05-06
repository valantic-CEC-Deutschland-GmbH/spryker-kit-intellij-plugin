package com.valantic.cec.sprykerplugin.model;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiManagerImpl;
import com.jetbrains.php.lang.PhpFileType;

import java.io.IOException;
import java.util.List;

public class ClassCreator {

    public static PsiFile createClasses(List<String> classNames, List<String> sourceCode, Project project) throws IOException {
        PsiDirectory psiRootDir = PsiManagerImpl.getInstance(project).findDirectory(project.getBaseDir().findChild("src"));

        PsiFile file = null;
        for (int i = 0; i < classNames.size(); i++) {
            String className = classNames.get(i);
            String code = sourceCode.get(i);
            String namespace = className.substring(0, className.lastIndexOf("\\"));
            String classNameWithoutNamespace = className.substring(className.lastIndexOf("\\") + 1);
            String pathToFile = className.replace("\\", "/");
            String [] subDirectories = pathToFile.split("/");
            PsiDirectory psiNamespaceDir = psiRootDir;
            for (String subDirectory : subDirectories) {
                if (subDirectory.contains(".php") || subDirectory.isBlank() || subDirectory.isEmpty()) {
                    continue;
                }
                assert psiNamespaceDir != null;
                PsiDirectory newDir = psiNamespaceDir.findSubdirectory(subDirectory);
                if (newDir == null) {
                    newDir = psiNamespaceDir.createSubdirectory(subDirectory);
                }
                psiNamespaceDir = newDir;
            }

            String fileName = classNameWithoutNamespace;
            if (!fileName.contains(".php")) {
                fileName = fileName + ".php";
            }
            PsiFile psiFile = PsiFileFactory.getInstance(project).createFileFromText(fileName, PhpFileType.INSTANCE, code);
            psiNamespaceDir.add(psiFile);

            file = psiFile;
            file.clearCaches();
            file.subtreeChanged();
        }

        return file;
    }

}