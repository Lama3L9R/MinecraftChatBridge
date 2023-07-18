package icu.lama.minecraft.chatbridge.fabric;

import icu.lama.minecraft.chatbridge.core.proxy.minecraft.IProxyWorld;
import icu.lama.minecraft.chatbridge.core.proxy.minecraft.ProxyWorldType;
import net.minecraft.server.world.ServerWorld;

public class MinecraftServerWorldProxy implements IProxyWorld {
    private final ServerWorld world;

    public MinecraftServerWorldProxy(ServerWorld world) {
        this.world = world;
    }

    @Override public Object unwrap() {
        return this.world;
    }

    @Override public ProxyWorldType getType() {
        // Since vanilla minecraft is using 1 world multiple dimensions design,
        // which is different to bukkit 1 world 1 dimension design style
        // Technically there is no way to know the dimension in forge / fabric servers

        return ProxyWorldType.VANILLA_STYLE;
    }

    @Override public String getName() {
        return world.worldProperties.getLevelName();
    }

    @Override public void save() {
        world.save(null, true, false);
    }
}
