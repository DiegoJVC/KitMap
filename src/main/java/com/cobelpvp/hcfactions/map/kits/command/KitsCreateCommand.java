package com.cobelpvp.hcfactions.map.kits.command;

import com.cobelpvp.hcfactions.map.kits.Kit;
import com.cobelpvp.hcfactions.HCFactions;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitsCreateCommand {

    @Command(names = {"kits create"}, permission = "hcfactions.kits")
    public static void kits_create(Player sender, @Param(name = "name", wildcard = true) String name) {
        if (HCFactions.getInstance().getMapHandler().getKitManager().get(name) != null) {
            sender.sendMessage(ChatColor.RED + "That kit already exists.");
            return;
        }

        Kit kit = HCFactions.getInstance().getMapHandler().getKitManager().getOrCreate(name);

        kit.update(sender.getInventory());
        HCFactions.getInstance().getMapHandler().getKitManager().save();

        sender.sendMessage(ChatColor.GREEN + "The " + ChatColor.BLUE + kit.getName() + ChatColor.GREEN
                + " kit has been created from your inventory.");
    }

}
