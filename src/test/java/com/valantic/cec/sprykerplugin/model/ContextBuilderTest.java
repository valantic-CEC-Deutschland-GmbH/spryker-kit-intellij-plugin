package com.valantic.cec.sprykerplugin.model;

import com.valantic.cec.sprykerplugin.services.ContextBuilder;
import com.valantic.cec.sprykerplugin.services.ContextBuilderInterface;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ContextBuilderTest {
    @Test
    public void testContextBuildingWithAFilePath()
    {
        ContextBuilderInterface builder = new ContextBuilder(null);
        String filePath = "/home/user/projects/PhpStormProjects/project/src/Pyz/Zed/MyModule/innerPath/Validation/MyModuleConfig.php";

        Context context = builder.createContextFromFilePath(filePath, false);

        assertEquals("Application should have been Zed!", "Zed", context.getApplicationName());
        assertEquals("module name should have been MyModule!", "MyModule", context.getModuleName());
        assertEquals("Inner path isn't correct!", "innerPath/Validation", context.getInnerPath());
        assertEquals("class name should have been MyModuleConfig!", "MyModuleConfig.php", context.getClassName());
        String expectedDirPath = filePath.substring(0, filePath.lastIndexOf("/"));
        assertEquals("filePath is wrong", expectedDirPath, context.getDirPath());
    }
}
