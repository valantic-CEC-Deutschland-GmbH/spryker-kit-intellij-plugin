package mock;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBus;
import org.jetbrains.annotations.SystemIndependent;
import org.picocontainer.PicoContainer;

public class ProjectMock<T> implements Project {

    public T serviceToReturn;

    public ProjectMock(T serviceToReturn) {
        this.serviceToReturn = serviceToReturn;
    }

    public T getService(Class theClass) {
        return this.serviceToReturn;
    }

    @Override
    public @org.jetbrains.annotations.NotNull String getName() {
        return null;
    }

    @Override
    public VirtualFile getBaseDir() {
        return null;
    }

    @Override
    public @org.jetbrains.annotations.Nullable
    @SystemIndependent String getBasePath() {
        return null;
    }

    @Override
    public @org.jetbrains.annotations.Nullable VirtualFile getProjectFile() {
        return null;
    }

    @Override
    public @org.jetbrains.annotations.Nullable
    @SystemIndependent String getProjectFilePath() {
        return null;
    }

    @Override
    public @org.jetbrains.annotations.Nullable VirtualFile getWorkspaceFile() {
        return null;
    }

    @Override
    public @org.jetbrains.annotations.NotNull String getLocationHash() {
        return null;
    }

    @Override
    public void save() {

    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    @Override
    public boolean isDefault() {
        return false;
    }

    @Override
    public <T> T getComponent(@org.jetbrains.annotations.NotNull Class<T> interfaceClass) {
        return null;
    }

    @Override
    public @org.jetbrains.annotations.NotNull PicoContainer getPicoContainer() {
        return null;
    }

    @Override
    public @org.jetbrains.annotations.NotNull MessageBus getMessageBus() {
        return null;
    }

    @Override
    public boolean isDisposed() {
        return false;
    }

    @Override
    public @org.jetbrains.annotations.NotNull Condition<?> getDisposed() {
        return null;
    }

    @Override
    public void dispose() {

    }

    @Override
    public <T> @org.jetbrains.annotations.Nullable T getUserData(@org.jetbrains.annotations.NotNull Key<T> key) {
        return null;
    }

    @Override
    public <T> void putUserData(@org.jetbrains.annotations.NotNull Key<T> key, @org.jetbrains.annotations.Nullable T value) {

    }
}
