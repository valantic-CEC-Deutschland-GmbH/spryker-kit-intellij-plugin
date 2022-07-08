package com.valantic.cec.sprykerplugin.services;

import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.valantic.cec.sprykerplugin.model.Context;

import java.io.IOException;

public interface FileWriterInterface {
    PsiFile createFileFromTemplate(Context context, PsiDirectory fileDirectory, String templatePathString, String resultFileName) throws IOException;

    PsiDirectory createDirectory(PsiDirectory fileDirectory, String subDirectoryName);
}
