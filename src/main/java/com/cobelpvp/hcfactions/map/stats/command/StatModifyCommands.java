package com.cobelpvp.hcfactions.map.stats.command;

import com.cobelpvp.hcfactions.factions.Faction;
import com.cobelpvp.hcfactions.HCFactions;
import com.cobelpvp.hcfactions.map.stats.StatsEntry;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StatModifyCommands {

    @Command(names = "sm setkills", permission = "hcfactions.statsmodify")
    public static void setKills(Player player, @Param(name = "kills") int kills) {
        StatsEntry stats = HCFactions.getInstance().getMapHandler().getStatsHandler().getStats(player);
        stats.setKills(kills);

        HCFactions.getInstance().getKillsMap().setKills(player.getUniqueId(), kills);

        player.sendMessage(ChatColor.GREEN + "You've set your own kills to: " + kills);
    }

    @Command(names = "sm setdeaths", permission = "hcfactions.statsmodify")
    public static void setDeaths(Player player, @Param(name = "deaths") int deaths) {
        StatsEntry stats = HCFactions.getInstance().getMapHandler().getStatsHandler().getStats(player);
        stats.setDeaths(deaths);

        HCFactions.getInstance().getDeathsMap().setDeaths(player.getUniqueId(), deaths);

        player.sendMessage(ChatColor.GREEN + "You've set your own deaths to: " + deaths);
    }

    @Command(names = "sm setfactionkills", permission = "hcfactions.statsmodify")
    public static void setTeamKills(Player player, @Param(name = "kills") int kills) {
        Faction faction = HCFactions.getInstance().getFactionHandler().getTeam(player);

        if (faction != null) {
            faction.setKills(kills);
            player.sendMessage(ChatColor.GREEN + "You've set your faction's kills to: " + kills);
        }
    }

    @Command(names = "sm setfactiondeaths", permission = "hcfactions.statsmodify")
    public static void setTeamDeaths(Player player, @Param(name = "deaths") int deaths) {
        Faction faction = HCFactions.getInstance().getFactionHandler().getTeam(player);

        if (faction != null) {
            faction.setDeaths(deaths);
            player.sendMessage(ChatColor.GREEN + "You've set your faction's deaths to: " + deaths);
        }
    }

}
