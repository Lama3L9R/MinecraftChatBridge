package icu.lama.minecraft.chatbridge.core.loader.reflection;

import java.lang.reflect.Method;

public class InstanceBoundCall {
    private final Method method;
    private final Object instance;

    public InstanceBoundCall(Method method, Object instance) {
        this.method = method;
        this.instance = instance;
    }

    public Object call() {
        try {
            method.trySetAccessible();
            return method.invoke(instance);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
