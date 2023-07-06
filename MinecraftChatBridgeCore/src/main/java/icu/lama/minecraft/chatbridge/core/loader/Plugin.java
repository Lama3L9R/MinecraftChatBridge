package icu.lama.minecraft.chatbridge.core.loader;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Plugin {
    String name();

    PluginType type();
}
