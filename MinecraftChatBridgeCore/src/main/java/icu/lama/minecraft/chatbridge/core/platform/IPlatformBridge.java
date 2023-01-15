package icu.lama.minecraft.chatbridge.core.platform;


import icu.lama.minecraft.chatbridge.core.PlatformReceiveCallback;
import icu.lama.minecraft.chatbridge.core.binding.IBindingDatabase;
import icu.lama.minecraft.chatbridge.core.config.PlatformConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * You should implement this interface to your platform bridge main class
 * Then initialize an instance of your main class and store the instance into a public static field and name it 'INSTANCE'
 * If you don't need auto register then set the main class field value in configuration to 'manual'
 *
 * Example:
 *
 * <code>
 * class ExamplePlatformBridge implements IPlatformBridge {
 *     public static IPlatformBridge INSTANCE = new ExamplePlatformBridge();
 *     // ABOVE IS NECESSARY FOR AUTO REGISTER
 *     // ... do rest of the implements
 * }
 * </code>
 */
public interface IPlatformBridge {
    /**
     * Send a msg to target platform
     * @param name from player name (already parsed to bound name if binding database available otherwise this is player name)
     * @param playerUUID from player uuid
     * @param msg message content
     */
    public void send(String name, UUID playerUUID, String msg);

    /**
     * get this platform registry name
     * @return platform name
     */
    public String getPlatformName();

    /**
     * get is this platform allowed NSFW Contents
     * @return is NSFW Contents allowed in this platform
     */
    public boolean getAllowNSFWContent();

    /**
     * Set the callback that should be called by platform bridge when received a message.
     * Should be called when MinecraftChatBridge loads and never should be called by anything else.
     * @param callback the callback method when platform bridge received a message.
     */
    public void setReceiveCallback(PlatformReceiveCallback callback);

    /**
     * Set this platform configuration
     * @param config config of this platform
     */
    public void setConfiguration(PlatformConfiguration config);

    /**
     * Call when this platform should start create api client, start connections, listen to events etc...
     */
    public void init();

    /**
     * get the binding database of this platform if null when this feature is disabled
     * @return the binding database
     */
    public @Nullable IBindingDatabase getBindingDatabase();
}
