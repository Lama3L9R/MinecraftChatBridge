package icu.lama.minecraft.chatbridge.core.loader;

import icu.lama.minecraft.chatbridge.core.proxy.platform.IPlatformProxy;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClassLoaderEx extends ClassLoader {
    private final HashMap<String, Class<?>> loadedClasses = new HashMap<>();
    private final ArrayList<InstanceBoundCall> initializer = new ArrayList<>();
    private final ArrayList<IPlatformProxy> platforms = new ArrayList<>();
    private final ArrayList<URL> resources = new ArrayList<>();
    private String name;


    public ClassLoaderEx(ClassLoader parent, JarFile jar) throws IOException {
        super(parent);

        var entries = Spliterators.spliteratorUnknownSize(jar.entries().asIterator(), Spliterator.ORDERED);
        var classes = StreamSupport.stream(entries, false)
                .filter(it -> it.getRealName().endsWith(".class"))
                .map(it -> {
                    try {
                        var bis = new BufferedInputStream(jar.getInputStream(it));
                        var buffer = bis.readAllBytes();
                        var parsedName = it.getName().replace("/", ".").replace(".class", "");

                        bis.close();

                        return this.defineClass(parsedName, buffer, 0, buffer.length);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

        StreamSupport.stream(entries, false)
                .filter(it -> !it.getRealName().endsWith(".class"))
                .forEach(it -> {

                });

        classes.forEach(this::resolveClass); // I guess this can speed this up? no clue on this.
        classes.forEach(it -> loadedClasses.put(it.getName(), it));

        classes.forEach(it -> { // Autoload @Platform and @Initializer
            var platformMarker = it.getAnnotation(Plugin.class);
            if (platformMarker != null) {
                this.name = platformMarker.name();
                if (platformMarker.type() == PluginType.PLATFORM_PROXY) {
                    try {
                        Object instance;
                        var instanceField = Arrays.stream(it.getDeclaredFields()).filter(f -> "INSTANCE".equalsIgnoreCase(f.getName())).findFirst();
                        if (instanceField.isPresent()) {
                            if ((instanceField.get().getModifiers() & Modifier.STATIC) != 0 &&
                                    (instanceField.get().getModifiers() & Modifier.PUBLIC) != 0) {
                                instance = instanceField.get().get(null);
                            } else {
                                throw new RuntimeException("Illegal INSTANCE Field! It must be public and static");
                            }
                        } else {
                            var constructor = Arrays.stream(it.getConstructors()).filter(c -> c.getParameterCount() == 0).findFirst();
                            if (constructor.isPresent()) {
                                instance = constructor.get().newInstance();
                            } else {
                                throw  new RuntimeException("No idea how to construct your class! Please consider add a instance field (public static) named INSTANCE.");
                            }
                        }

                        platforms.add((IPlatformProxy) instance);



                        initializer.addAll(Arrays.stream(it.getDeclaredMethods())
                                .filter(m -> m.getAnnotation(Initializer.class) != null)
                                .map(m -> new InstanceBoundCall(m, instance))
                                .collect(Collectors.toList())
                        );
                    } catch (ClassCastException ex) {
                        throw new RuntimeException("Illegal platform marker on " + it.getName() + ". Marker can only mark IPlatformProxy", ex);
                    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }


        });

        jar.close();
    }


    @Override protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (loadedClasses.containsKey(name)) {
            return loadedClasses.get(name);
        } else {
            return Class.forName(name, true, super.getParent());
        }
    }

    public Collection<Class<?>> getLoadedClasses() {
        return loadedClasses.values();
    }

    public ArrayList<InstanceBoundCall> getInitializer() {
        return initializer;
    }

    public ArrayList<IPlatformProxy> getPlatforms() {
        return platforms;
    }

    public Map<String, Class<?>> getNamedLoadedClasses() {
        return this.loadedClasses;
    }
}
