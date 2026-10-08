package org.lushplugins.regrowthback;

import org.bukkit.Bukkit;
import org.lushplugins.lushlib.utils.plugin.SpigotPlugin;
import org.lushplugins.regrowthback.command.BackCommand;
import org.lushplugins.regrowthback.command.DeathBackCommand;
import org.lushplugins.regrowthback.config.ConfigManager;
import org.lushplugins.regrowthback.listener.PlayerListener;
import org.lushplugins.regrowthback.location.TimestampedLocation;
import org.lushplugins.regrowthback.storage.UsersTable;
import org.lushplugins.regrowthback.storage.binding.StringTimestampedLocationBinding;
import org.lushplugins.regrowthback.user.BackUser;
import org.lushplugins.regrowthback.user.UserCache;
import org.lushplugins.storagehandler.StorageHandler;
import revxrsal.commands.bukkit.BukkitLamp;

import java.time.Duration;

public final class RegrowthBack extends SpigotPlugin {
    private static RegrowthBack plugin;

    private ConfigManager configManager;
    private UserCache userCache;
    private StorageHandler storageHandler;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager();
        this.configManager.reload();

        this.userCache = new UserCache(this);
        this.storageHandler = StorageHandler.builder(this).build();
        this.storageHandler.execute(UsersTable::createTableIfNotExists);

        registerListener(new PlayerListener());

        BukkitLamp.builder(this)
            .build()
            .register(
                new BackCommand(),
                new DeathBackCommand()
            );

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            RegrowthBack.getInstance().getUserCache().getCachedUsers().forEach(BackUser::validateLocations);

            RegrowthBack.getInstance().getStorageHandler().execute(context -> context
                .select()
                .where(UsersTable.BACK_LOCATION.isNotNull())
                .or(UsersTable.DEATH_LOCATION.isNotNull())
                .fetch()
                .forEach(record -> {
                    TimestampedLocation backLocation = StringTimestampedLocationBinding.from(record.get(UsersTable.BACK_LOCATION));
                    if (backLocation != null && backLocation.hasExpired()) {
                        record.set(UsersTable.BACK_LOCATION, null);
                    }

                    TimestampedLocation deathLocation = StringTimestampedLocationBinding.from(record.get(UsersTable.DEATH_LOCATION));
                    if (deathLocation != null && deathLocation.hasExpired()) {
                        record.set(UsersTable.BACK_LOCATION, null);
                    }
                })
            );
        }, 200, Duration.ofHours(3).toSeconds() * 20);
    }

    @Override
    public void onDisable() {
        if (this.storageHandler != null) {
            this.storageHandler.shutdown();
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public UserCache getUserCache() {
        return userCache;
    }

    public StorageHandler getStorageHandler() {
        return storageHandler;
    }

    public static RegrowthBack getInstance() {
        return plugin;
    }
}
