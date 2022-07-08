package mock;

import com.intellij.diagnostic.ActivityCategory;
import com.intellij.openapi.extensions.PluginDescriptor;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBus;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.SystemIndependent;
import org.picocontainer.PicoContainer;

import java.util.Map;

public class ProjectMock<T> implements Project {

    public T serviceToReturn;

    public ProjectMock(T serviceToReturn) {
        this.serviceToReturn = serviceToReturn;
    }

    public T getService(@NotNull Class theClass) {
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
    public <T> T @NotNull [] getComponents(@NotNull Class<T> baseClass) {
        return null;
    }

    @Override
    public @org.jetbrains.annotations.NotNull PicoContainer getPicoContainer() {
        return null;
    }

    @Override
    public boolean isInjectionForExtensionSupported() {
        return false;
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
    public <T> T instantiateClassWithConstructorInjection(@NotNull Class<T> aClass, @NotNull Object key, @NotNull PluginId pluginId) {
        return null;
    }

    @Override
    public @NotNull RuntimeException createError(@NotNull Throwable error, @NotNull PluginId pluginId) {
        return null;
    }

    @Override
    public @NotNull RuntimeException createError(@NotNull @NonNls String message, @NotNull PluginId pluginId) {
        return null;
    }

    @Override
    public @NotNull RuntimeException createError(@NotNull @NonNls String message, @Nullable Throwable error, @NotNull PluginId pluginId, @Nullable Map<String, String> attachments) {
        return null;
    }

    @Override
    public @NotNull <T> Class<T> loadClass(@NotNull String className, @NotNull PluginDescriptor pluginDescriptor) throws ClassNotFoundException {
        return null;
    }

    @Override
    public @NotNull ActivityCategory getActivityCategory(boolean isExtension) {
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
