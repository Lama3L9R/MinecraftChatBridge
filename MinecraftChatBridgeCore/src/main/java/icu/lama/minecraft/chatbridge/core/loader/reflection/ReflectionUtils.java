package icu.lama.minecraft.chatbridge.core.loader.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class ReflectionUtils {
    private final static Method getDeclaredField0;

    static {
        try {
            getDeclaredField0 = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
            getDeclaredField0.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<Field> getDeclaredFieldNative(Class<?> clazz, String field) throws InvocationTargetException, IllegalAccessException {
        return Arrays
                .stream((Field[]) getDeclaredField0.invoke(clazz, false))
                .filter(it -> it.getName().equals(field))
                .findAny();
    }
}
