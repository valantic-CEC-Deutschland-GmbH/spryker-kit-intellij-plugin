package com.valantic.cec.sprykerplugin.model;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.impl.ProjectImpl;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBus;
import com.jetbrains.rd.util.reactive.KeyValuePair;
import com.valantic.cec.sprykerplugin.services.ProjectSettingsState;
import com.valantic.cec.sprykerplugin.services.TwigResources;
import mock.ProjectMock;
import org.jetbrains.annotations.SystemIndependent;
import org.junit.Assert;
import org.junit.Test;
import org.picocontainer.PicoContainer;

import java.util.ArrayList;

public class TwigResourcesTest {
    @Test
    public void testLoadingResources() {
        Project projMock =  new ProjectMock<ProjectSettingsState>(
                new ProjectSettingsState()
        );
        TwigResources resource = new TwigResources(
            projMock
        );

        Context context = new Context("Zed", null, null, null, "/data/src/Pyz/Zed", projMock);
        KeyValuePair<String, ArrayList<TwigTreeNode>> pair = resource.getPathsToTwigResourcesForContext(context);

        Assert.assertEquals("Expected the Key Path to resources would be correct", "src/Pyz/Zed/", pair.getKey());
    }
}
