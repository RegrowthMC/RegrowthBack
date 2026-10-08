package org.lushplugins.regrowthback;

import org.bukkit.plugin.java.JavaPlugin;

public final class RegrowthBack extends JavaPlugin {
    private static RegrowthBack plugin;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        // Enable implementation
    }

    @Override
    public void onDisable() {
        // Disable implementation
    }

    public static RegrowthBack getInstance() {
        return plugin;
    }
}
