package com.cobelpvp.hcfactions.factions.commands.faction.subclaim;

import com.cobelpvp.hcfactions.HCFactions;
import com.cobelpvp.hcfactions.factions.Faction;
import com.cobelpvp.hcfactions.factions.claims.LandBoard;
import com.cobelpvp.hcfactions.factions.claims.Subclaim;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import com.cobelpvp.atheneum.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TeamSubclaimCommand implements Listener {

    public static final ItemStack SELECTION_WAND = new ItemStack(Material.WOOD_SPADE);
    @Getter
    private static Map<String, Selection> selections = new HashMap<>();

    static {
        ItemMeta meta = SELECTION_WAND.getItemMeta();

        meta.setDisplayName("§a§oSubclaim Wand");
        meta.setLore(Arrays.asList(

                "",
                "§eRight/Left Click§6 Block",
                "§b- §fSelect subclaim's corners"

        ));

        SELECTION_WAND.setItemMeta(meta);
    }

    @Command(names = {"f subclaim", "faction subclaim", "fac subclaim", "f sub", "faction sub", "fac sub"}, permission = "")
    public static void teamSubclaim(Player sender) {
        sender.sendMessage(ChatColor.RED + "/t subclaim start - starts the subclaiming process");
        sender.sendMessage(ChatColor.RED + "/t subclaim map - toggles a visual subclaim map");
        sender.sendMessage(ChatColor.RED + "/t subclaim create <subclaim> - creates a subclaim");
        sender.sendMessage(ChatColor.RED + "/t subclaim addplayer <subclaim> <player> - adds a player to a subclaim");
        sender.sendMessage(ChatColor.RED + "/t subclaim removeplayer <subclaim> <player> - removes a player from a subclaim");
        sender.sendMessage(ChatColor.RED + "/t subclaim list - views all subclaims");
        sender.sendMessage(ChatColor.RED + "/t subclaim info <subclaim> - views info about a subclaim");
        sender.sendMessage(ChatColor.RED + "/t subclaim unclaim <subclaim> <player> - unclaims a subclaim");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Faction faction = HCFactions.getInstance().getFactionHandler().getTeam(event.getPlayer());

        if (event.getItem() != null && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) && event.getItem().getType() == SELECTION_WAND.getType()) {
            if (event.getItem().hasItemMeta() && event.getItem().getItemMeta().getDisplayName() != null && event.getItem().getItemMeta().getDisplayName().contains("Subclaim")) {
                event.setCancelled(true);

                if (faction != null) {
                    Subclaim subclaim = faction.getSubclaim(event.getClickedBlock().getLocation());

                    if (subclaim != null) {
                        event.getPlayer().sendMessage(ChatColor.RED + "(" + event.getClickedBlock().getX() + ", " + event.getClickedBlock().getY() + ", " + event.getClickedBlock().getZ() + ") is a part of " + subclaim.getName() + "!");
                        return;
                    }

                    if (LandBoard.getInstance().getTeam(event.getClickedBlock().getLocation()) != faction) {
                        event.getPlayer().sendMessage(ChatColor.RED + "This block is not a part of your faction's territory!");
                        return;
                    }
                }

                Selection selection = new Selection(null, null);

                if (selections.containsKey(event.getPlayer().getName())) {
                    selection = selections.get(event.getPlayer().getName());
                }

                int set;

                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    set = 2;
                    selection.setLoc1(event.getClickedBlock().getLocation());
                } else {
                    set = 1;
                    selection.setLoc2(event.getClickedBlock().getLocation());
                }

                event.getPlayer().sendMessage(ChatColor.YELLOW + "Set subclaim's location " + ChatColor.LIGHT_PURPLE + set + ChatColor.YELLOW + " to " + ChatColor.GREEN + "(" + ChatColor.WHITE + event.getClickedBlock().getX() + ", " + event.getClickedBlock().getY() + ", " + event.getClickedBlock().getZ() + ChatColor.GREEN + ")" + ChatColor.YELLOW + ".");
                selections.put(event.getPlayer().getName(), selection);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().equals(TeamSubclaimCommand.SELECTION_WAND)) {
            event.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.getPlayer().getInventory().remove(TeamSubclaimCommand.SELECTION_WAND);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        event.getPlayer().getInventory().remove(TeamSubclaimCommand.SELECTION_WAND);
    }

    @Data
    @AllArgsConstructor
    public static class Selection {

        private Location loc1;
        private Location loc2;

        public boolean isComplete() {
            return (loc1 != null && loc2 != null);
        }

    }

}
