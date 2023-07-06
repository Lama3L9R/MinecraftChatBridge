package icu.lama.minecraft.chatbridge.core.proxy.minecraft;

public enum ServerType {
    /**
     * Vanilla minecraft server
     */
    MINECRAFT_SERVER,
    /**
     * Vanilla minecraft client
     */
    MINECRAFT_CLIENT,
    /**
     * Modded minecraft server
     * Forge, Fabric, Liteloader etc.
     * /
    MINECRAFT_MODDED_SERVER,
    /**
     * Modded minecraft client
     * Forge, Fabric, Liteloader etc.
     * /
    MINECRAFT_MODDED_CLIENT,
    /**
     * Plugin-only minecraft server
     * Which have Bukkit API and CraftBukkit abstract layer
     * Spigot, Paper etc.
     */
    MINECRAFT_PLUGIN,
    /**
     * Support both bukkit plugin and mods
     * KCauldron, Uranium etc.
     */
    MINECRAFT_MOD_PLUS,
    /**
     * Minecraft proxy server
     * BungeeCord, Waterfall etc.
     */
    PROXY
}
