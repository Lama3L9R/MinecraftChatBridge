package icu.lama.minecraft.chatbridge.core.loader;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import icu.lama.minecraft.chatbridge.core.MinecraftChatBridge;
import icu.lama.minecraft.chatbridge.core.loader.reflection.ClassLoaderEx;
import icu.lama.minecraft.chatbridge.core.loader.reflection.InstanceBoundCall;
import icu.lama.minecraft.chatbridge.core.proxy.platform.IPlatformProxy;

import java.io.File;
import java.util.List;
import java.util.Map;

public class BridgePlugin extends JarPlugin {
    private final List<IPlatformProxy> platforms;
    private final List<InstanceBoundCall> initializers;
    private final List<InstanceBoundCall> finalizers;
    private final Config pluginConfig;
    private final int priority;
    private PluginStatus pluginStatus = PluginStatus.LOADED;

    public BridgePlugin(ClassLoaderEx classLoader,
                        File file) {
        super(classLoader.getNamedLoadedClasses(), classLoader.getName(), file);

        this.platforms = classLoader.getPlatforms();
        this.initializers = classLoader.getInitializers();
        this.finalizers = classLoader.getFinalizers();
        this.priority = classLoader.getPluginMarker().priority();

        pluginConfig = MinecraftChatBridge.getConfig().withFallback(ConfigFactory.parseResourcesAnySyntax(classLoader, "defaults.conf"));
        MinecraftChatBridge.updateConfig(pluginConfig);
    }

    public BridgePlugin(ClassLoader cl, Map<String, Class<?>> classes, String identifier, File file, List<IPlatformProxy> platforms, List<InstanceBoundCall> initializers, List<InstanceBoundCall> finalizers, int priority) {
        super(classes, identifier, file);

        this.platforms = platforms;
        this.initializers = initializers;
        this.finalizers = finalizers;
        this.priority = priority;

        platforms.forEach(MinecraftChatBridge::register);

        pluginConfig = MinecraftChatBridge.getConfig().withFallback(ConfigFactory.load(cl, "defaults.conf"));
        MinecraftChatBridge.updateConfig(pluginConfig);
    }

    public void init() {
        try {
            this.initializers.forEach(InstanceBoundCall::call);
            pluginStatus = PluginStatus.INITIALIZED;
        } catch (Throwable ex) {
            pluginStatus = PluginStatus.FAILED;
            MinecraftChatBridge.getErrorHandler().onError(ex, this);
        }
    }

    public void callFinalize() {
        try {
            this.finalizers.forEach(InstanceBoundCall::call);
        } catch (Throwable ex) {
            MinecraftChatBridge.getErrorHandler().onError(ex, this);
            System.out.println("WARNING: Runtime error was caught in finalizer! Plugin may not be fully unloaded! It is recommended to restart your server!");
        }
    }

    public List<IPlatformProxy> getPlatforms() {
        return platforms;
    }

    public List<InstanceBoundCall> getInitializers() {
        return initializers;
    }

    public Config getPluginConfig() {
        return pluginConfig;
    }

    public PluginStatus getPluginStatus() {
        return pluginStatus;
    }

    public int getPriority() {
        return priority;
    }
}
