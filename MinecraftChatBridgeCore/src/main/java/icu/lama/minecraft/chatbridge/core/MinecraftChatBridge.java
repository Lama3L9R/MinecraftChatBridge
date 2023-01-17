package icu.lama.minecraft.chatbridge.core;

import icu.lama.minecraft.chatbridge.core.binding.IBindingDatabase;
import icu.lama.minecraft.chatbridge.core.config.ChatBridgeConfiguration;
import icu.lama.minecraft.chatbridge.core.config.PlatformConfiguration;
import icu.lama.minecraft.chatbridge.core.minecraft.IMinecraftBridge;
import icu.lama.minecraft.chatbridge.core.platform.IPlatformBridge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;

public class MinecraftChatBridge {
    private static ChatBridgeConfiguration config;

    private static ClassLoader classLoader;

    private static final Map<String, IPlatformBridge> platforms = new HashMap<>();

    private static IMinecraftBridge minecraftBridge;

    private static ErrorCallback onError = (exception, source) -> {
        System.err.println("A runtime error was found on " + source.getPlatformName());
        exception.printStackTrace();
    };

    private static final PlatformReceiveCallback platformReceiveCallback = (platform, name, msg) -> {
        UUID uuid = null;
        if (platform.getBindingDatabase() != null) {
            uuid = platform.getBindingDatabase().getBinding(name);
        }
        minecraftBridge.send(name, uuid, platform, msg);
    };

    public static final MinecraftReceiveCallback minecraftReceiveCallback = (name, uuid, msg) -> platforms.forEach((pName, platform) -> {
        IBindingDatabase database = platform.getBindingDatabase();
        String bName = name;
        if (database != null) {
            bName = database.getName(uuid);
        }

        platform.send(bName, uuid, msg);
    });

    /**
     * Init minecraft chat bridge.
     *
     * @param coreConf chat bridge configurations
     * @param platformConf platform configurations
     * @param bridge minecraft bridge instance (aka this method caller)
     */
    public static void init(@NotNull ChatBridgeConfiguration coreConf,
                            @NotNull Map<String, PlatformConfiguration> platformConf,
                            @NotNull IMinecraftBridge bridge) throws Exception {
        init(coreConf, platformConf, bridge, null);
    }

    /**
     * Init minecraft chat bridge.
     *
     * @param coreConf chat bridge configurations
     * @param platformConf platform configurations
     * @param bridge minecraft bridge instance (aka this method caller)
     * @param errorHandler invoke this callback whenever there is a caught error
     */
    public static void init(@NotNull ChatBridgeConfiguration coreConf,
                            @NotNull Map<String, PlatformConfiguration> platformConf,
                            @NotNull IMinecraftBridge bridge,
                            @Nullable ErrorCallback errorHandler) throws Exception {
        Objects.requireNonNull(coreConf);
        Objects.requireNonNull(platformConf);
        Objects.requireNonNull(bridge);

        if (errorHandler != null) {
            onError = errorHandler;
        }

        config = coreConf;
        minecraftBridge = bridge;
        bridge.setReceiveCallback(minecraftReceiveCallback);

        File platformDirectory = new File(config.bridgeDirectory);
        List<URL> listURLs =Arrays.stream(platformDirectory.listFiles(file -> file.getName().endsWith(".jar")))
                .map(File::toURI)
                .map(uri -> {
                    try { return uri.toURL(); } catch (MalformedURLException e) { throw new RuntimeException(e); }
                })
                .toList();

        URL[] urls = listURLs.toArray(new URL[listURLs.size()]);

        classLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());

        listURLs.forEach((url) -> {
            JarFile jar = null;
            try {
                jar = new JarFile(url.getFile());
                Properties prop = new Properties();
                prop.load(jar.getInputStream(jar.getEntry("info")));

                String mainClassName = prop.getProperty("mainClass");

                if (mainClassName.equalsIgnoreCase("manual")) {
                    return;
                }

                Class<IPlatformBridge> mainClass = (Class<IPlatformBridge>) classLoader.loadClass(mainClassName);
                Field instanceField = mainClass.getDeclaredField("INSTANCE");
                IPlatformBridge instance = (IPlatformBridge) instanceField.get(null);

                register(instance, instance.getPlatformName());
            } catch (IOException | NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                if (jar != null) {
                    try {
                        jar.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } );

        platformConf.forEach((pName, pConf) -> {
            IPlatformBridge instance = getPlatform(pName);
            pConf.mainClass = instance.getClass().getName();

            instance.setConfiguration(pConf);
            instance.setReceiveCallback(platformReceiveCallback);

            instance.init();
        });
    }

    /**
     * Register a platform bridge
     * @param instance bridge instance
     * @param name name
     */
    public static void register(IPlatformBridge instance, String name) {
        platforms.put(name, instance);
    }

    /**
     * Get platform bridge class loader
     * @return platform bridge class loader
     */
    public static ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * Get all platforms with name
     * @return all platforms with name
     */
    public static Map<String, IPlatformBridge> getPlatforms() {
        return platforms;
    }

    /**
     * Get platform by name
     * @param name platform name
     * @return platform instance
     */
    public static @Nullable IPlatformBridge getPlatform(String name) {
        return platforms.get(name);
    }

    public static void throwException(Exception e, IPlatformBridge source) {
        onError.onError(e, source);
    }
}
