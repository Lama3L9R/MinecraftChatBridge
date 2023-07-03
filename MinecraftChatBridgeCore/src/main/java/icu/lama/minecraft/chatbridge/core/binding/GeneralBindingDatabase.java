package icu.lama.minecraft.chatbridge.core.binding;

import icu.lama.minecraft.chatbridge.core.MinecraftChatBridge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GeneralBindingDatabase implements IBindingDatabase {
    private final Map<String, UUID> bindingMap;
    private final Map<UUID, String> reverseMap = new HashMap<>();


    public GeneralBindingDatabase() {
        var dataFolder = new File(MinecraftChatBridge.getConfig().dataStore);
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        var data = new File(dataFolder, "bindings.bin");

        if (data.exists()) {
            Map<String, UUID> map = null;
            try {
                var ois = new ObjectInputStream(new FileInputStream(data));
                map = (Map<String, UUID>) ois.readObject();
                map.forEach((name, uuid) -> reverseMap.put(uuid, name));

                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to load bindings.bin! Using a empty one.");

                map = new HashMap<>();
            } finally {
                bindingMap = map;
            }
        } else {
            bindingMap = new HashMap<>();
        }

    }

    @Override
    public @Nullable UUID getBinding(String name) {
        return bindingMap.get(name);
    }

    @Override
    public @Nullable String getName(UUID uuid) {
        return reverseMap.get(uuid);
    }

    @Override
    public void update(@NotNull String name, @NotNull UUID playerUUID) {
        bindingMap.put(name, playerUUID);
        reverseMap.put(playerUUID, name);
    }

    @Override
    public void save() throws IOException {
        var dataFolder = new File(MinecraftChatBridge.getConfig().dataStore);
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        var data = new File(dataFolder, "bindings.bin");
        if (data.exists()) {
            data.delete();
        }

        data.createNewFile();
        var oos = new ObjectOutputStream(new FileOutputStream(data));
        oos.writeObject(bindingMap);
        oos.flush();
        oos.close();
    }
}
