package icu.lama.minecraft.chatbridge.core.proxy.minecraft;

import icu.lama.minecraft.chatbridge.core.proxy.IProxy;
import icu.lama.minecraft.chatbridge.core.proxy.Risky;

public interface IProxyWorld extends IProxy {
    /**
     * Get world type. It may be 'overworld', 'nether', 'the_end' or any other types that is added by mods.
     * @return world type
     */
    ProxyWorldType getType();

    /**
     * Get world name
     * @return world name
     */
    String getName();

    /**
     * Saves the world
     */
    @Risky void save();

}
