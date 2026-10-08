package org.lushplugins.regrowthback.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.lushplugins.regrowthback.RegrowthBack;
import org.lushplugins.regrowthback.location.TimestampedLocation;
import org.lushplugins.regrowthback.user.BackUser;

import java.util.EnumSet;

public class PlayerListener implements Listener {
    private static final EnumSet<PlayerTeleportEvent.TeleportCause> IGNORED_CAUSES = EnumSet.of(
        PlayerTeleportEvent.TeleportCause.ENDER_PEARL
    );

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        BackUser user = RegrowthBack.getInstance().getUserCache().getCachedUser(player.getUniqueId());
        if (user == null) {
            return;
        }

        if (IGNORED_CAUSES.contains(event.getCause())) {
            return;
        }

        Location from = event.getFrom();
        try {
            double distance = from.distanceSquared(event.getTo());
            if (distance < 10) {
                return;
            }
        } catch(IllegalArgumentException ignored) {
            // If a distance cannot be compared between the 2 locations then we assume the locations are not nearby
        }

        user.backLocation(new TimestampedLocation(from));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        BackUser user = RegrowthBack.getInstance().getUserCache().getCachedUser(player.getUniqueId());
        if (user == null) {
            return;
        }

        user.deathLocation(new TimestampedLocation(player.getLocation()));
    }
}
