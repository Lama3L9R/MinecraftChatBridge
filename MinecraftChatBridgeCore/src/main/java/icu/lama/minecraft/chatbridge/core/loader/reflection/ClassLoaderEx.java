package icu.lama.minecraft.chatbridge.core.loader.reflection;

import icu.lama.minecraft.chatbridge.core.loader.BridgePlugin;
import icu.lama.minecraft.chatbridge.core.loader.PluginType;
import icu.lama.minecraft.chatbridge.core.loader.annotations.ConfigInject;
import icu.lama.minecraft.chatbridge.core.loader.annotations.Initializer;
import icu.lama.minecraft.chatbridge.core.loader.annotations.Plugin;
import icu.lama.minecraft.chatbridge.core.proxy.platform.IPlatformProxy;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClassLoaderEx extends ClassLoader {
    private final HashMap<String, Class<?>> loadedClasses = new HashMap<>();
    private final ArrayList<InstanceBoundCall> initializers = new ArrayList<>();
    private final ArrayList<IPlatformProxy> platforms = new ArrayList<>();
    private final HashMap<String, URL> resources = new HashMap<>();
    private final TreeMap<String, URL> constructionPending = new TreeMap<>();
    private final ArrayList<String> loadedClassNames = new ArrayList<>();
    private final File jarFile;
    private String name;
    private BridgePlugin plugin;


    public ClassLoaderEx(ClassLoader parent, File jarFile) throws IOException {
        super(parent);

        this.jarFile = jarFile;

        var jar = new JarFile(jarFile);
        var entries = Spliterators.spliteratorUnknownSize(jar.entries().asIterator(), Spliterator.ORDERED);


        StreamSupport.stream(entries, false)
                .forEach(it -> {
                    var fullPath = "jar:file:///" + jarFile.getAbsolutePath() + "!/" + it.getName();
                    try {
                        if (it.getName().contains("module-info")) {
                            return;
                        }

                        if (it.getName().endsWith(".class")) {
                            var parsedName = it.getName().replace("/", ".").replace(".class", "");
                            try {
                                Class.forName(parsedName);
                            } catch (ClassNotFoundException ex) {
                                constructionPending.put(parsedName, new URL(fullPath));
                            }
                        } else {
                            resources.put(it.getName(), new URL(fullPath));
                        }
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                });

        constructClasses();
        loadedClassNames.forEach(it -> {
            try {
                loadedClasses.put(it, Class.forName(it, false, this));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        jar.close();
    }

    public void initPlugin() {
        List<InstanceBoundGet> pendingConfigInjects = new ArrayList<>();

        for (var it : loadedClasses.values().stream().filter(it -> it.getName().startsWith("icu.lama")).collect(Collectors.toList())) { // Autoload @Platform, @Initializer and @ConfigInject
            Plugin platformMarker = (Plugin) it.getAnnotation(Plugin.class);
            Object instance = null;


            try { // Load / Create instance
                var instanceField = Arrays.stream(it.getDeclaredFields()).filter(f -> "INSTANCE".equalsIgnoreCase(f.getName())).findFirst();
                if (instanceField.isPresent()) {
                    if ((instanceField.get().getModifiers() & Modifier.STATIC) != 0 &&
                        (instanceField.get().getModifiers() & Modifier.PUBLIC) != 0) {
                        instance = instanceField.get().get(null);
                    } else {
                        throw new RuntimeException("Illegal INSTANCE Field! It must be public and static");
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            if (platformMarker != null) {
                name = platformMarker.name();

                if (instance == null) {
                    try {
                        var constructor = Arrays.stream(it.getConstructors()).filter(c -> c.getParameterCount() == 0).findFirst();
                        if (constructor.isPresent()) {
                            instance = constructor.get().newInstance();
                        } else {
                            throw new RuntimeException("No idea on how to construct your class! Please consider add a instance field (public static) named INSTANCE.");
                        }
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException("No idea on how to constru your class! Please consider add a instance field (public static) named INSTANCE.", e);
                    }
                }


                if (platformMarker.type() == PluginType.PLATFORM_PROXY) {
                    platforms.add((IPlatformProxy) instance);
                }
            }

            final Object instanceCpy = instance;
            initializers.addAll(Arrays.stream(it.getDeclaredMethods())
                    .filter(m -> m.getAnnotation(Initializer.class) != null)
                    .map(m -> {
                        if ((m.getModifiers() & Modifier.STATIC) != 0) {
                            return new InstanceBoundCall(null, instanceCpy);
                        } else {
                            return new InstanceBoundCall(m, instanceCpy);
                        }
                    })
                    .collect(Collectors.toList())
            );

            pendingConfigInjects.addAll(Arrays.stream(it.getDeclaredFields())
                    .filter(m -> m.getAnnotation(ConfigInject.class) != null)
                    .map(m -> {
                        if ((m.getModifiers() & Modifier.STATIC) != 0) {
                            return new InstanceBoundGet(null, instanceCpy);
                        } else {
                            return new InstanceBoundGet(m, instanceCpy);
                        }
                    })
                    .collect(Collectors.toList())
            );
        }

        plugin = new BridgePlugin(this, jarFile);

        pendingConfigInjects.stream().forEach(it -> { // use stream api to do it async-ly
            it.set(plugin.getPluginConfig());
        });
    }

    private void constructClasses() {
        while (constructionPending.size() != 0) {
            var task = constructionPending.pollFirstEntry();

            if (!loadedClasses.containsKey(task.getKey())) {
                try {
                    var pending = (JarURLConnection) task.getValue().openConnection();
                    var bis = new BufferedInputStream(pending.getJarFile().getInputStream(pending.getJarEntry()));
                    var buffer = bis.readAllBytes();

                    this.defineClass(task.getKey(), buffer, 0, buffer.length);

                    loadedClassNames.add(task.getKey());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    private void constructClass(String name) {
        try {
            if (loadedClasses.containsKey(name)) {
                return;
            }

            var pending = (JarURLConnection) constructionPending.get(name).openConnection();
            var bis = new BufferedInputStream(pending.getJarFile().getInputStream(pending.getJarEntry()));
            var buffer = bis.readAllBytes();

            this.defineClass(name, buffer, 0, buffer.length);

            loadedClassNames.add(name);
            this.constructionPending.remove(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (loadedClassNames.contains(name)) {
            return Class.forName(name, false, this);
        } else if (constructionPending.containsKey(name)) {
            constructClass(name);
            return findClass(name);
        } else {
            return null;
            //return Class.forName(name, true, super.getParent());
        }
    }

    public Collection<Class<?>> getLoadedClasses() {
        return loadedClasses.values();
    }

    public ArrayList<InstanceBoundCall> getInitializers() {
        return initializers;
    }

    public ArrayList<IPlatformProxy> getPlatforms() {
        return platforms;
    }

    public Map<String, Class<?>> getNamedLoadedClasses() {
        return this.loadedClasses;
    }

    public BridgePlugin getPlugin() {
        return this.plugin;
    }

    @Override protected URL findResource(String name) {
        return this.resources.get(name);
    }

    @Nullable @Override public URL getResource(String name) {
        return this.resources.get(name);
    }

    @Override public Enumeration<URL> findResources(String name) throws IOException {
        return Collections.enumeration(this.resources.entrySet()
                .stream()
                .filter(it -> it.getKey().contains(name))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList()));
    }
}
