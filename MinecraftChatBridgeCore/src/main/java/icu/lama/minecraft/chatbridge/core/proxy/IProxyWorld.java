package icu.lama.minecraft.chatbridge.core.proxy;

public interface IProxyWorld {
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
