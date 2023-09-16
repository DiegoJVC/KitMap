package com.cobelpvp.hcfactions.map.kits.command;

import com.cobelpvp.hcfactions.map.kits.Kit;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitsLoadCommand {

    @Command(names = {"kits load"}, permission = "hcfactions.kits")
    public static void kits_load(Player sender, @Param(name = "kit", wildcard = true) Kit kit) {
        kit.apply(sender);

        sender.sendMessage(ChatColor.GREEN + "Giving you the kit " + ChatColor.BLUE + kit.getName() + ChatColor.GREEN + ".");
    }

}
