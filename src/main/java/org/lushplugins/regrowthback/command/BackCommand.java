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
@Command("back")
public class BackCommand {

    @Command("back")
    @CommandPermission("regrowthback.back")
    public void back(BukkitCommandActor actor) {
        Player player = actor.requirePlayer();
        BackUser user = RegrowthBack.getInstance().getUserCache().getCachedUser(player.getUniqueId());
        if (user == null) {
            actor.sender().sendMessage(Component.text()
                .content("Something went wrong.. Please try again later")
                .color(TextColor.fromHexString("#ff6969"))
                .build());
            return;
        }

        TimestampedLocation timestampedLocation = user.backLocation();
        if (timestampedLocation == null) {
            actor.sender().sendMessage(Component.text()
                .content("No location to teleport back to")
                .color(TextColor.fromHexString("#ff6969"))
                .build());
            return;
        }

        Location location = timestampedLocation.toLocation();
        if (location != null) {
            player.teleportAsync(location);

            actor.sender().sendActionBar(Component.text()
                .content("Teleported back")
                .color(TextColor.fromHexString("#b7faa2"))
                .build());
        }
    }
}
