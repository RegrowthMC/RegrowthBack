package org.lushplugins.regrowthback.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.lushplugins.regrowthback.RegrowthBack;
import org.lushplugins.regrowthback.location.TimestampedLocation;
import org.lushplugins.regrowthback.user.BackUser;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@SuppressWarnings("unused")
@Command("deathback")
public class DeathBackCommand {

    @Command("deathback")
    @CommandPermission("regrowthback.deathback")
    public void deathBack(BukkitCommandActor actor) {
        Player player = actor.requirePlayer();
        BackUser user = RegrowthBack.getInstance().getUserCache().getCachedUser(player.getUniqueId());
        if (user == null) {
            actor.sender().sendMessage(Component.text()
                .content("Something went wrong.. Please try again later")
                .color(TextColor.fromHexString("#ff6969"))
                .build());
            return;
        }

        TimestampedLocation timestampedLocation = user.deathLocation();
        if (timestampedLocation == null) {
            actor.sender().sendMessage(Component.text()
                .content("No death location to teleport to")
                .color(TextColor.fromHexString("#ff6969"))
                .build());
            return;
        }

        Location location = timestampedLocation.toLocation();
        if (location != null) {
            player.teleportAsync(location);
            user.deathLocation(null);

            actor.sender().sendActionBar(Component.text()
                .content("Teleported to your latest death location")
                .color(TextColor.fromHexString("#b7faa2"))
                .build());
        }
    }
}
