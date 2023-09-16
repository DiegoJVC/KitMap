package com.cobelpvp.hcfactions.map.kits.command;

import com.cobelpvp.hcfactions.HCFactions;
import com.cobelpvp.hcfactions.map.kits.Kit;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitsEditCommand {

    @Command(names = {"kits edit"}, permission = "hcfactions.kits")
    public static void kit_edit(Player sender, @Param(name = "kit", wildcard = true) Kit kit) {
        kit.update(sender.getInventory());
        HCFactions.getInstance().getMapHandler().getKitManager().save();

        sender.sendMessage(ChatColor.GREEN + "Kit " + ChatColor.BLUE + kit.getName() + ChatColor.GREEN
                + " has been edited and saved.");
    }
}
