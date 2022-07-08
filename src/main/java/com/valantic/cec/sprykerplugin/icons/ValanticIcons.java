// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.valantic.cec.sprykerplugin.icons;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public class ValanticIcons {
    public static final Icon Sdk_default_icon = IconLoader.findIcon("/3360-16x16.png", ValanticIcons.class.getClassLoader());
    public static final Icon folderIcon = AllIcons.Actions.NewFolder;
    public static final Icon moduleIcon = AllIcons.Actions.ModuleDirectory;
    public static final Icon fileIcon = AllIcons.Actions.AddFile;
    public static final Icon methodIcon = AllIcons.Actions.New;

}