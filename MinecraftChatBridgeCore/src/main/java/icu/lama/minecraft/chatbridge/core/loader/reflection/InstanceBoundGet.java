package icu.lama.minecraft.chatbridge.core.loader.reflection;

import java.lang.reflect.Field;

public class InstanceBoundGet {
    private final Field field;
    private final Object instance;

    public InstanceBoundGet(Field field, Object instance) {
        this.field = field;
        this.instance = instance;
    }

    public Object get() {
        try {
            field.trySetAccessible();
            return field.get(instance);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public void set(Object obj) {
        try {
            field.trySetAccessible();
            field.set(instance, obj);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
