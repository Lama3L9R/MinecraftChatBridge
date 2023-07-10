package icu.lama.minecraft.chatbridge.core;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import icu.lama.minecraft.chatbridge.core.loader.BridgePlugin;
import icu.lama.minecraft.chatbridge.core.loader.reflection.ClassLoaderEx;
import icu.lama.minecraft.chatbridge.core.proxy.minecraft.IMinecraftServerProxy;
import icu.lama.minecraft.chatbridge.core.proxy.platform.IPlatformProxy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class MinecraftChatBridge {
    private static final Map<String, IPlatformProxy> platforms = new HashMap<>();
    private static final HashMap<String, BridgePlugin> loadedPlugins = new HashMap<>();
    private static final HashMap<String, ClassLoader> classLoaders = new HashMap<>(); // use for hold all classloader reference | avoid class being GC-ed
    private static File dataRoot;
    private static Config config = null; // volatile + sync lock
    private static IMinecraftServerProxy minecraft;
    private static ErrorCallback onError;

    public static void init(@NotNull File dataFolder, @NotNull IMinecraftServerProxy minecraft) {
        init(dataFolder, minecraft, new ErrorCallback() {
            @Override public void onError(Throwable e, BridgePlugin source) {
                System.err.println("A runtime error was found on " + source.getIdentifier());
                e.printStackTrace();
            }

            @Override public void onError(Throwable e, IPlatformProxy platform) {
                System.err.println("A runtime error was found on " + platform.getPlatformName());
                e.printStackTrace();
            }
        });
    }

    public static void init(@NotNull File dataFolder,
                            @NotNull IMinecraftServerProxy minecraft,
                            @NotNull ErrorCallback errorHandler) {

        MinecraftChatBridge.onError = errorHandler;
        MinecraftChatBridge.minecraft = minecraft;
        MinecraftChatBridge.dataRoot = dataFolder;

        if (!dataFolder.exists()) {
            if (!dataFolder.mkdir()) {
                throw new RuntimeException("Failed to create directory: " + dataFolder.getAbsolutePath());
            }
        }

        reloadConfig();

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

        saveConfig(new File(dataRoot, "core.conf"));

        ArrayList<BridgePlugin> pendingInits = new ArrayList<>(loadedPlugins.values()); // Prevent concurrent modification
        pendingInits.sort(Comparator.comparingInt(BridgePlugin::getPriority));
        pendingInits.forEach(BridgePlugin::init);
    }

    /**
     * Register a platform bridge
     * @param instance bridge instance
     */
    public static void register(IPlatformProxy instance) {
        platforms.put(instance.getPlatformName(), instance);
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
    public static void throwException(Throwable e, @NotNull BridgePlugin source) {
        Objects.requireNonNull(source);

        onError.onError(e, source);
    }

    /**
     * Throw a runtime exception. Should always be called by Platform Bridge
     * @param e exception
     * @param source exception source
     */
    @Deprecated public static void throwException(Throwable e, @NotNull IPlatformProxy source) {
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

    public static BridgePlugin loadPlugin(String name) {
        return loadPlugin(new File(dataRoot, "plugins/" + name));
    }


    public static void unloadPlugin(String identifier) {
        if (!loadedPlugins.containsKey(identifier)) {
            return;
        }

        loadedPlugins.get(identifier).callFinalize();

        loadedPlugins.remove(identifier);
        classLoaders.remove(identifier);

        System.gc(); // force gc to remove classes
    }

    public static void unloadPlugin(BridgePlugin plugin) {
        plugin.callFinalize();

        loadedPlugins.remove(plugin.getIdentifier());
        classLoaders.remove(plugin.getIdentifier());

        System.gc(); // force gc to remove classes
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

    public static IMinecraftServerProxy getMinecraftProxy() {
        return minecraft;
    }

    public static ErrorCallback getErrorHandler() {
        return onError;
    }

    public static File getDataRoot() {
        return dataRoot;
    }

    public static void reloadConfig() {
        var configFile = new File(dataRoot, "core.conf");
        if (!configFile.exists()) {
            saveConfig(configFile);
        }

        var userConfig = ConfigFactory.parseFile(configFile);
        config = config != null ? userConfig.withFallback(config) : userConfig;
        config = config.withFallback(ConfigFactory.parseResourcesAnySyntax(MinecraftChatBridge.class.getClassLoader(), "defaults.conf"));
    }
}
