package org.lushplugins.regrowthback.config;

import org.bukkit.configuration.ConfigurationSection;
import org.lushplugins.regrowthback.RegrowthBack;

public class ConfigManager {
    private int expiryTime;

    public ConfigManager() {
        RegrowthBack.getInstance().saveDefaultConfig();
    }

    public void reload() {
        RegrowthBack.getInstance().reloadConfig();
        ConfigurationSection config = RegrowthBack.getInstance().getConfig();

        this.expiryTime = config.getInt("expiry-time");
    }

    public int expiryTime() {
        return expiryTime;
    }
}
