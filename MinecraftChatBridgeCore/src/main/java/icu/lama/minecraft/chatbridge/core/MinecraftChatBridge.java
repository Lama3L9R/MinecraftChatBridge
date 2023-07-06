package icu.lama.minecraft.chatbridge.core;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import icu.lama.minecraft.chatbridge.core.loader.*;
import icu.lama.minecraft.chatbridge.core.proxy.minecraft.IMinecraftServerProxy;
import icu.lama.minecraft.chatbridge.core.proxy.platform.IPlatformProxy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Collectors;

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
            if (dataFolder.mkdir()) {
                throw new RuntimeException("Failed to create directory: " + dataFolder.getAbsolutePath());
            }
        }

        var configFile = new File(dataFolder, "core.conf");
        if (!configFile.exists()) {
            saveConfig(configFile);
        }

        var userConfig = ConfigFactory.parseFile(configFile);
        config = userConfig.withFallback(config);

        var platformDirectory = new File(dataFolder, "plugins");
        if (!platformDirectory.exists()) {
            if (platformDirectory.mkdir()) {
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
                var cl = new URLClassLoader(new URL[] { file.toURI().toURL() });

                Field classesField = ClassLoader.class.getDeclaredField("classes");
                classesField.setAccessible(true);

                Vector<Class<?>> classes =  (Vector<Class<?>>) classesField.get(Thread.currentThread().getContextClassLoader());
                var namedClasses = new HashMap<String, Class<?>>();
                classes.forEach(it -> namedClasses.put(it.getName(), it));

                String name = Math.random() + System.currentTimeMillis() + "UNNAMED";
                List<InstanceBoundCall> initializers = new ArrayList<>();
                List<IPlatformProxy> pluginPlatforms = new ArrayList<>();

                for (var it : classes) { // Autoload @Platform and @Initializer
                    Plugin platformMarker = (Plugin) it.getAnnotation(Plugin.class);
                    if (platformMarker != null) {
                        name = platformMarker.name();
                        IPlatformProxy instance;

                        try { // Load / Create instance
                            var instanceField = Arrays.stream(it.getDeclaredFields()).filter(f -> "INSTANCE".equalsIgnoreCase(f.getName())).findFirst();
                            if (instanceField.isPresent()) {
                                if ((instanceField.get().getModifiers() & Modifier.STATIC) != 0 &&
                                        (instanceField.get().getModifiers() & Modifier.PUBLIC) != 0) {
                                    instance = (IPlatformProxy) instanceField.get().get(null);
                                } else {
                                    throw new RuntimeException("Illegal INSTANCE Field! It must be public and static");
                                }
                            } else {
                                var constructor = Arrays.stream(it.getConstructors()).filter(c -> c.getParameterCount() == 0).findFirst();
                                if (constructor.isPresent()) {
                                    instance = (IPlatformProxy) constructor.get().newInstance();
                                } else {
                                    throw  new RuntimeException("No idea how to construct your class! Please consider add a instance field (public static) named INSTANCE.");
                                }
                            }
                        } catch (ClassCastException ex) {
                            throw new RuntimeException("Illegal platform marker on " + it.getName() + ". Marker can only mark IPlatformProxy", ex);
                        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }

                        if (platformMarker.type() == PluginType.PLATFORM_PROXY) {
                            pluginPlatforms.add(instance);
                            platforms.put(instance.getPlatformName(), instance);
                        }

                        initializers.addAll(Arrays.stream(it.getDeclaredMethods())
                                .filter(m -> m.getAnnotation(Initializer.class) != null)
                                .map(m -> new InstanceBoundCall(m, instance))
                                .collect(Collectors.toList())
                        );
                    }


                }

                var plugin = new BridgePlugin(cl, namedClasses, name, file, pluginPlatforms, initializers);

                loadedPlugins.put(name, plugin);
                classLoaders.put(name, cl);

                return plugin;
            } catch (IOException | IllegalAccessException | NoSuchFieldException e) {
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
        if (!configFile.exists()) {
            try {
                if (configFile.createNewFile()) {
                    throw new RuntimeException("Failed to create config file!");
                }

                var fw = new FileWriter(configFile);
                fw.write(config.root().render(ConfigRenderOptions
                        .defaults()
                        .setJson(false)
                        .setComments(true)
                        .setFormatted(true)));
                fw.flush();
                fw.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
