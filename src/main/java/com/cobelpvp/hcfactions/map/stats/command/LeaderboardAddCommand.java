package com.cobelpvp.hcfactions.map.stats.command;

import com.cobelpvp.hcfactions.HCFactions;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

public class LeaderboardAddCommand {

    @Command(names = {"leaderboard add"}, permission = "hcfactions.leaderboards")
    public static void leaderboardAdd(Player sender, @Param(name = "objective") String objectiveName, @Param(name = "place") int place) {
        Block block = sender.getTargetBlock(null, 10);
        StatsTopCommand.StatsObjective objective;

        try {
            objective = StatsTopCommand.StatsObjective.valueOf(objectiveName);
        } catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "Invalid objective!");
            return;
        }

        if (block == null || !(block.getState() instanceof Skull || block.getState() instanceof Sign)) {
            sender.sendMessage(ChatColor.RED + "You must be looking at a head or a sign.");
            return;
        }

        if (block.getState() instanceof Skull) {
            Skull skull = (Skull) block.getState();

            if (skull.getSkullType() != SkullType.PLAYER) {
                sender.sendMessage(ChatColor.RED + "That's not a player skull.");
                return;
            }

            HCFactions.getInstance().getMapHandler().getStatsHandler().getLeaderboardHeads().put(skull.getLocation(), place);
            HCFactions.getInstance().getMapHandler().getStatsHandler().getObjectives().put(skull.getLocation(), objective);
            HCFactions.getInstance().getMapHandler().getStatsHandler().updatePhysicalLeaderboards();
            sender.sendMessage(ChatColor.GREEN + "This skull will now display the number " + ChatColor.WHITE + place + ChatColor.GREEN + " player's head.");
        } else {
            Sign sign = (Sign) block.getState();

            HCFactions.getInstance().getMapHandler().getStatsHandler().getLeaderboardSigns().put(sign.getLocation(), place);
            HCFactions.getInstance().getMapHandler().getStatsHandler().getObjectives().put(sign.getLocation(), objective);
            HCFactions.getInstance().getMapHandler().getStatsHandler().updatePhysicalLeaderboards();
            sender.sendMessage(ChatColor.GREEN + "This sign will now display the number " + ChatColor.WHITE + place + ChatColor.GREEN + " player's stats.");
        }

        HCFactions.getInstance().getMapHandler().getStatsHandler().getObjectives().put(block.getLocation(), objective);
    }

    @Command(names = {"clearleaderboards"}, permission = "op")
    public static void clearallstats(Player sender) {
        ConversationFactory factory = new ConversationFactory(HCFactions.getInstance()).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {

            public String getPromptText(ConversationContext context) {
                return "§aAre you sure you want to clear leaderboards? Type §byes§a to confirm or §cno§a to quit.";
            }

            @Override
            public Prompt acceptInput(ConversationContext cc, String s) {
                if (s.equalsIgnoreCase("yes")) {
                    HCFactions.getInstance().getMapHandler().getStatsHandler().clearLeaderboards();
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Leaderboards cleared");
                    return Prompt.END_OF_CONVERSATION;
                }

                if (s.equalsIgnoreCase("no")) {
                    cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Cancelled.");
                    return Prompt.END_OF_CONVERSATION;
                }

                cc.getForWhom().sendRawMessage(ChatColor.GREEN + "Unrecognized response. Type §b/yes§a to confirm or §c/no§a to quit.");
                return Prompt.END_OF_CONVERSATION;
            }

        }).withLocalEcho(false).withEscapeSequence("/no").withTimeout(10).thatExcludesNonPlayersWithMessage("Go away evil console!");

        Conversation con = factory.buildConversation(sender);
        sender.beginConversation(con);
    }

}
