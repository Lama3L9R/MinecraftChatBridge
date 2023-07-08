package icu.lama.minecraft.chatbridge.core;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import icu.lama.minecraft.chatbridge.core.loader.BridgePlugin;
import icu.lama.minecraft.chatbridge.core.loader.reflection.ClassLoaderEx;
import icu.lama.minecraft.chatbridge.core.loader.reflection.InstanceBoundCall;
import icu.lama.minecraft.chatbridge.core.proxy.minecraft.IMinecraftServerProxy;
import icu.lama.minecraft.chatbridge.core.proxy.platform.IPlatformProxy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MinecraftChatBridge {
    private static final Map<String, IPlatformProxy> platforms = new HashMap<>();
    private static final HashMap<String, BridgePlugin> loadedPlugins = new HashMap<>();
    private static final HashMap<String, ClassLoader> classLoaders = new HashMap<>();
    private static Config config; // volatile + sync lock
    private static IMinecraftServerProxy minecraft;

    private static ErrorCallback onError;

    public static void init(@NotNull File dataFolder, @NotNull IMinecraftServerProxy minecraft) throws Throwable {
        init(dataFolder, minecraft, (exception, source) -> {
            System.err.println("A runtime error was found on " + source.getPlatformName());
            exception.printStackTrace();
        });
    }

    public static void init(@NotNull File dataFolder,
                            @NotNull IMinecraftServerProxy minecraft,
                            @NotNull ErrorCallback errorHandler) throws Throwable {

        onError = errorHandler;

        if (!dataFolder.exists()) {
            if (!dataFolder.mkdir()) {
                throw new RuntimeException("Failed to create directory: " + dataFolder.getAbsolutePath());
            }
        }

        var configFile = new File(dataFolder, "core.conf");
        if (!configFile.exists()) {
            saveConfig(configFile);
        }

        var userConfig = ConfigFactory.parseFile(configFile);
        config = config != null ? userConfig.withFallback(config) : userConfig;

        var platformDirectory = new File(dataFolder, "plugins");
        if (!platformDirectory.exists()) {
            if (!platformDirectory.mkdir()) {
                throw new RuntimeException("Failed to create directory: " + platformDirectory.getAbsolutePath());
            }
        }

        // Stream API is async so for thread safe I have to avoid use it.
        // Arrays.stream(platformDirectory.listFiles())
        //         .filter(it -> it.getName().endsWith(".jar"))
        //         .forEach(MinecraftChatBridge::loadPlugin);

        for (var it : platformDirectory.listFiles()) {
            if (it.getName().endsWith(".jar")) {
                loadPlugin(it); // Sync, Slower but thread safe
            }
        }

        saveConfig(configFile);

        loadedPlugins.values().forEach(it -> {
            it.getInitializers().forEach(InstanceBoundCall::call);
        });
    }

    /**
     * Register a platform bridge
     * @param instance bridge instance
     * @param name name
     */
    public static void register(IPlatformProxy instance, String name) {
        platforms.put(name, instance);
    }

    /**
     * Get all platforms with name
     * @return all platforms with name
     */
    public static Map<String, IPlatformProxy> getPlatforms() {
        return platforms;
    }

    /**
     * Get platform by name
     * @param name platform name
     * @return platform instance
     */
    public static @Nullable IPlatformProxy getPlatform(String name) {
        return platforms.get(name);
    }

    /**
     * Throw a runtime exception. Should always be called by Platform Bridge
     * @param e exception
     * @param source exception source
     */
    public static void throwException(Throwable e, @NotNull IPlatformProxy source) {
        Objects.requireNonNull(source);

        onError.onError(e, source);
    }


    public static BridgePlugin loadPlugin(File file) {
        if (file.getName().endsWith(".jar")) {
            try { // TODO Migrate to custom classloader: ClassLoaderEx to improve performance
                var cl = new ClassLoaderEx(MinecraftChatBridge.class.getClassLoader(), file);
                cl.initPlugin();

                loadedPlugins.put(cl.getName(), cl.getPlugin());
                classLoaders.put(cl.getName(), cl);

                return cl.getPlugin();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }


    public static void unloadPlugin(String identifier) {
        loadedPlugins.remove(identifier);
        classLoaders.remove(identifier);
    }

    public static void unloadPlugin(BridgePlugin plugin) {
        loadedPlugins.remove(plugin.getIdentifier());
        classLoaders.remove(plugin.getIdentifier());
    }

    public static Collection<BridgePlugin> getLoadedPlugins() {
        return loadedPlugins.values();
    }

    public static BridgePlugin getPlugin(String identifier) {
        return loadedPlugins.get(identifier);
    }

    public static Config getConfig() {
        return config;
    }

    public static void updateConfig(Config config) {
        MinecraftChatBridge.config = config;
    }

    public static void saveConfig(File configFile) {
        try {
            if (!configFile.exists()) {
                if (configFile.createNewFile()) {
                    throw new RuntimeException("Failed to create config file!");
                }
            }

            var fw = new FileWriter(configFile);
            fw.write(config.root().render(ConfigRenderOptions
                    .defaults()
                    .setJson(false)
                    .setOriginComments(false)
                    .setComments(false)
                    .setFormatted(true)));
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
