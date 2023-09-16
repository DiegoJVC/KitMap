package com.cobelpvp.hcfactions.map.stats.command;

import com.cobelpvp.hcfactions.HCFactions;
import com.cobelpvp.hcfactions.factions.Faction;
import com.cobelpvp.hcfactions.map.stats.StatsEntry;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.UUIDUtils;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import java.util.UUID;

public class StatsCommand {

    @Command(names = {"stats"}, permission = "")
    public static void stats(CommandSender sender, @Param(name = "player", defaultValue = "self") UUID uuid) {
        StatsEntry stats = HCFactions.getInstance().getMapHandler().getStatsHandler().getStats(uuid);

        if (stats == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }

        sender.sendMessage(ChatColor.RED.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
        sender.sendMessage(ChatColor.GREEN + UUIDUtils.name(uuid));
        sender.sendMessage(ChatColor.RED.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));

        sender.sendMessage(ChatColor.GREEN + "Kills: " + ChatColor.RED + stats.getKills());
        sender.sendMessage(ChatColor.GREEN + "Deaths: " + ChatColor.RED + stats.getDeaths());
        sender.sendMessage(ChatColor.GREEN + "Killstreak: " + ChatColor.RED + stats.getKillstreak());
        sender.sendMessage(ChatColor.GREEN + "Highest Killstreak: " + ChatColor.RED + stats.getHighestKillstreak());
        sender.sendMessage(ChatColor.GREEN + "KD: " + ChatColor.RED + (stats.getDeaths() == 0 ? "Infinity" : Faction.DTR_FORMAT.format((double) stats.getKills() / (double) stats.getDeaths())));

        sender.sendMessage(ChatColor.RED.toString() + ChatColor.STRIKETHROUGH + StringUtils.repeat('-', 53));
    }

    @Command(names = {"clearallstats"}, permission = "op")
    public static void clearallstats(Player sender) {
        ConversationFactory factory = new ConversationFactory(HCFactions.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

            public String getPromptText(ConversationContext context) {
                return "§aAre you sure you want to clear all stats? Type §byes§a to confirm or §cno§a to quit.";
            }

            @Override
            public Prompt acceptInput(ConversationContext cc, String s) {
                if (s.equalsIgnoreCase("yes")) {
                    HCFactions.getInstance().getMapHandler().getStatsHandler().clearLeaderboards();
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "All Stats have been cleared.");
                    return Prompt.END_OF_CONVERSATION;
                }

                if (s.equalsIgnoreCase("no")) {
                    cc.getForWhom().sendRawMessage(ChatColor.RED + "Stats clear command Aborted.");
                    return Prompt.END_OF_CONVERSATION;
                }

                cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Unrecognized response. Type §byes§a to confirm or §cno§a to quit.");
                return Prompt.END_OF_CONVERSATION;
            }

        }).withLocalEcho(false).withEscapeSequence("/no").withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");
        Conversation con = factory.buildConversation(sender);
        sender.beginConversation(con);
    }

}
