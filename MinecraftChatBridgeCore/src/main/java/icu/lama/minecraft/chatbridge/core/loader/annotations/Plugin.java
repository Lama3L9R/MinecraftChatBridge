package icu.lama.minecraft.chatbridge.core.loader.annotations;

import icu.lama.minecraft.chatbridge.core.loader.PluginType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Plugin {
    String name();

    PluginType type();
}
