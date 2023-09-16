package com.cobelpvp.hcfactions.map.kits.command;

import com.cobelpvp.hcfactions.HCFactions;
import com.cobelpvp.hcfactions.map.kits.Kit;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitsDeleteCommand {

    @Command(names = {"kits delete"}, permission = "hcfactions.kits")
    public static void kits_delete(Player sender, @Param(name = "kit", wildcard = true) Kit kit) {
        HCFactions.getInstance().getMapHandler().getKitManager().delete(kit);

        sender.sendMessage(
                ChatColor.RED + "Kit " + ChatColor.BLUE + kit.getName() + ChatColor.RED + " has been deleted.");
    }
}
