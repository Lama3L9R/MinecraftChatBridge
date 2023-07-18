package icu.lama.minecraft.chatbridge.core.loader;

import java.io.File;
import java.util.Map;


public class JarPlugin {
    private final Map<String, Class<?>> classes;
    private final String identifier;
    private final File file;

    public JarPlugin(Map<String, Class<?>> classes, String identifier, File file) {
        this.classes = classes;
        this.identifier = identifier;
        this.file = file;
    }

    public Map<String, Class<?>> getClasses() {
        return classes;
    }

    public String getIdentifier() {
        return identifier;
    }

    public File getFile() {
        return file;
    }
}
