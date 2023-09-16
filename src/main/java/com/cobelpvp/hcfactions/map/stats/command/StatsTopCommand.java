package com.cobelpvp.hcfactions.map.stats.command;

import lombok.Getter;
import com.cobelpvp.hcfactions.HCFactions;
import com.cobelpvp.hcfactions.factions.HCFactionsConstants;
import com.cobelpvp.hcfactions.map.stats.StatsEntry;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.UUIDUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class StatsTopCommand {

    @Command(names = {"statstop", "leaderboards"}, permission = "")
    public static void statstop(CommandSender sender, @Param(name = "objective", defaultValue = "kills") StatsObjective objective) {
        sender.sendMessage(HCFactionsConstants.getCenter(ChatColor.AQUA + "Leaderboards for Kills"));
        int index = 0;
        for (Map.Entry<StatsEntry, String> entry : HCFactions.getInstance().getMapHandler().getStatsHandler().getLeaderboards(objective, 10).entrySet()) {
            index++;
            sender.sendMessage((index == 1 ? ChatColor.GOLD + "1 " : ChatColor.YELLOW.toString() + index + " ") + ChatColor.YELLOW.toString() + UUIDUtils.name(entry.getKey().getOwner()) + ": " + ChatColor.GRAY + entry.getValue());
        }
    }

    @Getter
    public enum StatsObjective {

        KILLS("Kills", "k"),
        DEATHS("Deaths", "d"),
        KD("KD", "kdr"),
        HIGHEST_KILLSTREAK("Highest Killstreak", "killstreak", "highestkillstreak", "ks", "highestks", "hks");

        private String name;
        private String[] aliases;

        StatsObjective(String name, String... aliases) {
            this.name = name;
            this.aliases = aliases;
        }

    }

}
