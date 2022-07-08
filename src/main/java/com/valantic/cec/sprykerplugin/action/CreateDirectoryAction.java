package com.valantic.cec.sprykerplugin.action;

import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsActions;
import com.intellij.psi.PsiDirectory;
import com.valantic.cec.sprykerplugin.icons.ValanticIcons;
import com.valantic.cec.sprykerplugin.model.Context;
import com.valantic.cec.sprykerplugin.services.FileWriterInterface;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class CreateDirectoryAction extends AnAction {
    public CreateDirectoryAction(
            @Nullable @NlsActions.ActionDescription String description,
            Context context,
            String dirName
    ) {
        super( description, description, ValanticIcons.folderIcon);
        this.dirName = dirName;
        this.context = context;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final IdeView view = getIdeView(e);
        if (view == null) {
            return;
        }
        final Project project = e.getProject();

        final PsiDirectory dir = view.getOrChooseDirectory();
        if (dir == null || project == null) return;

        FileWriterInterface writer = project.getService(FileWriterInterface.class);

        writer.createDirectory(dir, this.dirName);
    }

    /**
     * Only make this action visible when text is selected.
     *
     * @param e Event object holding information about what is going on
     */
    @Override
    public void update(AnActionEvent e) {

        e.getPresentation().setEnabledAndVisible(true);
    }

    @Nullable
    private IdeView getIdeView(@NotNull AnActionEvent e) {
        return e.getData(LangDataKeys
                .IDE_VIEW);
    }



    private final String dirName;
    private final Context context;
}
