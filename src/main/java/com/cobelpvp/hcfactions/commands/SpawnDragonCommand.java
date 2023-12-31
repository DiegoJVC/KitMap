package com.cobelpvp.hcfactions.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;

public class SpawnDragonCommand {
    @Command(names={ "spawndragon", "spawnenderdragon" }, permission="hcfactions.enderdragon")
    public static void spawnDragon(Player sender) {

        if (sender.getWorld().getEntitiesByClass(EnderDragon.class).size() != 0) {
            sender.sendMessage(ChatColor.RED + "There's already an enderdragon.");
            return;
        }

        if (sender.getWorld().getEnvironment() == World.Environment.THE_END) {
            sender.getWorld().spawnEntity(sender.getLocation(), EntityType.ENDER_DRAGON);
            sender.sendMessage(ChatColor.GREEN + "You have unleashed the beast.");
        } else {
            sender.sendMessage(ChatColor.RED + "You must be in the end to spawn the Enderdragon!");
        }
    }
}

