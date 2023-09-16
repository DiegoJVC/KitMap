package com.cobelpvp.hcfactions.map.kits.command;

import com.cobelpvp.hcfactions.HCFactions;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitMapIsGayCommand {

    @Command(names = "kitmapisdamy", permission = "op")
    public static void kitmapIsGay(Player sender, @Param(name = "kitname") String name) {
        if (HCFactions.getInstance().getMapHandler().getKitManager().getUseAll().contains(name.toLowerCase())) {
            HCFactions.getInstance().getMapHandler().getKitManager().getUseAll().remove(name.toLowerCase());
            sender.sendMessage(ChatColor.RED + "Removed " + name + " from bypass list!");
        } else {
            HCFactions.getInstance().getMapHandler().getKitManager().getUseAll().add(name.toLowerCase());
            sender.sendMessage(ChatColor.GREEN + "Added " + name + " to bypass list!");
        }
    }

}
