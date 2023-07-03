package icu.lama.minecraft.chatbridge.core.binding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.UUID;

public interface IBindingDatabase {

    /**
     * Get the relation between the input name and minecraft uuid
     * @param name unique name from chat
     * @return minecraft uuid
     */
    @Nullable UUID getBinding(String name);

    /**
     * Get the relation between the input minecraft uuid and name
     * @param uuid minecraft uuid
     * @return unique name from chat
     */
    @Nullable String getName(UUID uuid);

    /**
     * Update(Create) the relation between the input name and minecraft uuid
     * @param name unique name from chat
     * @param playerUUID minecraft uuid
     */
    void update(@NotNull String name, @NotNull UUID playerUUID);

    /**
     * Save the database to disk or whatever place.
     * May cause lag if this is called in main thread.
     */
    void save() throws IOException;
}
