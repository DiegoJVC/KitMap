package com.cobelpvp.hcfactions.map.kits.command;

import com.cobelpvp.hcfactions.server.SpawnTagHandler;
import com.google.common.collect.Maps;
import com.cobelpvp.hcfactions.HCFactions;
import com.cobelpvp.hcfactions.map.kits.Kit;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.scoreboard.ScoreFunction;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class KitCommand {

    private static Map<String, Map<UUID, Long>> cooldownsMap = Maps.newHashMap();

    @Command(names = {"kitmapkit", "kitmapkit"}, permission = "")
    public static void kits_command(Player sender, @Param(name = "<kit>", defaultValue = "some-unused-value", wildcard = true) String kitName) {
        kitName = kitName.toLowerCase();
        if (kitName.equalsIgnoreCase("some-unused-value")) {
            StringBuilder kitsBuilder = new StringBuilder();
            for (Kit kit : HCFactions.getInstance().getMapHandler().getKitManager().getKits()) {
                if (!canUse(sender, kit)) continue;

                if (kitsBuilder.length() != 0) {
                    kitsBuilder.append(',');
                    kitsBuilder.append(' ');
                }

                kitsBuilder.append(kit.getName());
            }

            if (kitsBuilder.length() == 0) {
                sender.sendMessage(ChatColor.RED + "You have no usable donor kits!");
                return;
            }

            sender.sendMessage(ChatColor.RED + "Your usable donor kits: " + kitsBuilder.toString());
            return;
        }

        Kit targetKit = HCFactions.getInstance().getMapHandler().getKitManager().get(kitName);

        if (targetKit == null) {
            sender.sendMessage(ChatColor.RED + "Unable to locate kit '" + kitName + "'.");
            return;
        }

        if (SpawnTagHandler.isTagged(sender)) {
            sender.sendMessage(ChatColor.RED + "You're ineligible to use donator kits whilst you have an active Spawn Tag.");
            return;
        }

        if (sender.hasMetadata("modmode")) {
            sender.sendMessage(ChatColor.RED + "You can't do this whilst in mod mode.");
            return;
        }

        if (!canUse(sender, targetKit)) {
            sender.sendMessage(ChatColor.RED + "You're ineligible to use the donator kit '" + kitName + "'.");
            return;
        }

        Map<UUID, Long> cooldownMap = cooldownsMap.get(kitName);
        if (cooldownMap == null) {
            cooldownsMap.put(kitName, cooldownMap = Maps.newHashMap());
        }

        if (0 <= cooldownMap.getOrDefault(sender.getUniqueId(), -1L) - System.currentTimeMillis()) {
            long difference = cooldownMap.get(sender.getUniqueId()) - System.currentTimeMillis();
            sender.sendMessage(ChatColor.RED + "You're ineligible to use this kit for " + ScoreFunction.TIME_SIMPLE.apply(difference / 1000F));
            return;
        } else {
            String lowerCaseName = kitName.toLowerCase();
            cooldownMap.put(sender.getUniqueId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis((lowerCaseName.contains("Cobel") && (lowerCaseName.endsWith("+") || lowerCaseName.endsWith("plus"))) ? 15 : 30));
        }

        targetKit.apply(sender);
    }

    public static boolean canUse(Player player, Kit kit) {
        String kitName = kit.getName();

        // Don't show any of these kits as donator kits
        if (kitName.equals("Debuff") || kitName.equals("PvP") || kitName.equals("Archer") || kitName.equals("Bard") || kitName.equals("Miner") || kitName.equals("Builder")) {
            return false;
        }

        // Don't show any of these kitmap specific kits as donator kits either
        if (HCFactions.getInstance().getMapHandler().isKitMap() || HCFactions.getInstance().getServerHandler().isCobelKitMap()) {
            if (kitName.equals("Vip")) {
                return false;
            }
        }

        return player.hasPermission("hcfactions.staff") || player.hasPermission("hcfactions.youtuber") || player.hasPermission("hcfactions.famous");

    }


}
