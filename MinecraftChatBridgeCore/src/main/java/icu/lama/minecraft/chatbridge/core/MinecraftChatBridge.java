package icu.lama.minecraft.chatbridge.core;

import icu.lama.minecraft.chatbridge.core.binding.IBindingDatabase;
import icu.lama.minecraft.chatbridge.core.config.ChatBridgeConfiguration;
import icu.lama.minecraft.chatbridge.core.config.PlatformConfiguration;
import icu.lama.minecraft.chatbridge.core.minecraft.IMinecraftBridge;
import icu.lama.minecraft.chatbridge.core.platform.IPlatformBridge;
import icu.lama.minecraft.chatbridge.core.proxy.IMinecraftServerProxy;
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
import java.util.stream.Collectors;

public class MinecraftChatBridge {
    private static ChatBridgeConfiguration config;

    private static ClassLoader classLoader;

    private static final Map<String, IPlatformBridge> platforms = new HashMap<>();

    private static IMinecraftBridge minecraftBridge;

    private static @Nullable IMinecraftServerProxy serverProxy = null;

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
        init(coreConf, platformConf, bridge, (err, source) -> { });
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
                            @NotNull ErrorCallback errorHandler) throws Exception {
        Objects.requireNonNull(coreConf);
        Objects.requireNonNull(platformConf);
        Objects.requireNonNull(bridge);
        Objects.requireNonNull(errorHandler);

        onError = errorHandler;
        config = coreConf;
        minecraftBridge = bridge;
        bridge.setReceiveCallback(minecraftReceiveCallback);

        File platformDirectory = new File(config.bridgeDirectory);
        if (!platformDirectory.exists()) {
            platformDirectory.mkdir();
        }

        List<URL> listURLs =Arrays.stream(platformDirectory.listFiles(file -> file.getName().endsWith(".jar")))
                .map(File::toURI)
                .map(uri -> {
                    try { return uri.toURL(); } catch (MalformedURLException e) { throw new RuntimeException(e); }
                })
                .collect(Collectors.toList());

        URL[] urls = listURLs.toArray(new URL[0]);

        classLoader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());

        listURLs.forEach((url) -> {
            JarFile jar = null;
            try {
                jar = new JarFile(url.getFile());
                Properties prop = new Properties();
                prop.load(jar.getInputStream(jar.getEntry("info")));

                String mainClassName = prop.getProperty("mainClass");

                if ("manual".equalsIgnoreCase(mainClassName)) {
                    return;
                }

                Class<IPlatformBridge> mainClass = (Class<IPlatformBridge>) classLoader.loadClass(mainClassName);
                Field instanceField = mainClass.getDeclaredField("INSTANCE");
                IPlatformBridge instance = (IPlatformBridge) instanceField.get(null);

                register(instance, instance.getPlatformName());
            } catch (IOException | NoSuchFieldException | ClassNotFoundException | IllegalAccessException | ClassCastException e) {
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
            if (instance == null) {
                return;
            }

            pConf.mainClass = instance.getClass().getName();

            instance.setConfiguration(pConf);
            instance.setReceiveCallback(platformReceiveCallback);

            try {
                instance.init();
            } catch (Throwable exception) {
                throwException(exception, instance);
            }
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

    /**
     * Throw a runtime exception. Should always be called by Platform Bridge
     * @param e exception
     * @param source exception source
     */
    public static void throwException(Throwable e, @NotNull IPlatformBridge source) {
        Objects.requireNonNull(source);

        onError.onError(e, source);
    }

    public static @Nullable IMinecraftServerProxy getServerProxy() {
        return serverProxy;
    }

    public static void setServerProxy(@Nullable IMinecraftServerProxy serverProxy) {
        MinecraftChatBridge.serverProxy = serverProxy;
    }

    public static ChatBridgeConfiguration getConfig() {
        return config;
    }
}
