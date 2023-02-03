package icu.lama.minecraft.chatbridge.core.proxy;

public enum MinecraftServerType {
    /**
     * Refers all kind of server which is based on BukkitAPI
     * Ex. CraftBukkit, Spigot, Paper
     */
    BUKKIT_BASED,
    /**
     * Refers all kind of server which is based on ForgeAPI
     * Ex. Forge Modded Server, Arclight
     */
    FORGE_BASED,
    /**
     * Refers all kind of server which is based on FabricAPI and FabricLoader
     * Ex. Fabric Modded Server
     */
    FABRIC_BASED,
    /**
     * Refers all kind of server which is based on BungeeCord Proxy
     * Ex. BungeeCord, Waterfall
     */
    BC_PROXY,
    /**
     * Refers all kind of Proxy Server but does not support any well-known APIs
     * Ex. Velocity
     */
    OTHER_PROXY,
    /**
     * Vanilla server but ... Why? and How????
     * Developer of MinecraftChatBridge (Qumolama.d): I guess this will never be used...
     * Ex. Vanilla Server
     */
    VANILLA,
    /**
     * Refers to Other Kind Server but does not support any well-known APIs
     * Ex. I Don't Know Please Tell Me If You Know
     */
    OTHER
}
