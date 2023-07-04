package icu.lama.minecraft.chatbridge.fabric;

import icu.lama.minecraft.chatbridge.core.config.ChatBridgeConfiguration;
import icu.lama.minecraft.chatbridge.core.config.PlatformConfiguration;

import java.util.List;
import java.util.Map;

public class ModConfig {
    public ChatBridgeConfiguration core;
    public Map<String, PlatformConfiguration> platformConf;
    public List<String> forceBinding;
    public Formats formats;

    public static class Formats {
        public List<String> messageFormat;
        public List<String> noBinding;
        public List<String> bindSuccess;
        public List<String> bindHint;
        public String bindHintButtonStyle;

        public Formats() {
            this.messageFormat = List.of("[%s] <%s> %s");
            this.noBinding = List.of(
                    "You are not allowed to register / play in this server unless you bind your Minecraft account with your %s account.",
                    "Please use /bind command to make the connection!"
            );
            this.bindSuccess = List.of("You have successfully bind your Minecraft account with %s account! You can now proceed to register");
            this.bindHint = List.of("Click <ButtonPlaceholder> and then send the copied data to chat platform.", "Or you can type it manually:§n§c /bind %s §r");
            this.bindHintButtonStyle = "§b§n§a[ HERE ]§r";
        }

    }
}
