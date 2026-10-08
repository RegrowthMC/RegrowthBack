package org.lushplugins.regrowthback.user;

import org.bukkit.plugin.java.JavaPlugin;
import org.jooq.Record;
import org.jooq.Result;
import org.lushplugins.regrowthback.RegrowthBack;
import org.lushplugins.regrowthback.storage.UsersTable;
import org.lushplugins.regrowthback.storage.binding.BinaryUUIDBinding;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserCache extends org.lushplugins.lushlib.utils.cache.UserCache<BackUser> {

    public UserCache(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    protected CompletableFuture<BackUser> load(UUID uuid) {
        return RegrowthBack.getInstance().getStorageHandler().query((context) -> {
            Result<Record> result = context
                .select()
                .from(UsersTable.TABLE)
                .where(UsersTable.UUID.eq(BinaryUUIDBinding.to(uuid)))
                .fetch();
            if (result.isEmpty()) {
                return new BackUser(uuid);
            }

            return BackUser.read(result.getFirst());
        });
    }
}
