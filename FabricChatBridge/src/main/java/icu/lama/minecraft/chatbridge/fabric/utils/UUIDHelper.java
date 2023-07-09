package icu.lama.minecraft.chatbridge.fabric.utils;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Uuids;

import java.util.HashSet;
import java.util.UUID;

public class UUIDHelper {
    private static HashSet<String> mojangNameCache = null;

    static {
        try {
            var cacheField = Class.forName("xyz.nikitacartes.easyauth.EasyAuth").getDeclaredField("mojangAccountNamesCache");
            cacheField.trySetAccessible();
            mojangNameCache = (HashSet<String>) cacheField.get(null); // get the mojangAccountNamesCache reference
        } catch (ClassNotFoundException | IllegalAccessException ignored) {
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    public static UUID getFakeUUID(ServerPlayerEntity entity) {
        if (entity.server.isOnlineMode()) {
            if (mojangNameCache != null && mojangNameCache.contains(entity.getGameProfile().getName())) {
                return entity.getUuid();
            } else {
                return Uuids.getOfflinePlayerUuid(entity.getGameProfile().getName());
            }
        } else {
            return Uuids.getOfflinePlayerUuid(entity.getGameProfile().getName());
        }
    }

}
