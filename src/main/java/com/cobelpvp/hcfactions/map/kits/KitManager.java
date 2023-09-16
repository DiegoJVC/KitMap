package com.cobelpvp.hcfactions.map.kits;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import com.cobelpvp.hcfactions.HCFactions;
import com.cobelpvp.atheneum.Atheneum;
import com.cobelpvp.atheneum.command.TeamsCommandHandler;
import com.cobelpvp.atheneum.util.UUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Only gets instantiated if the server is kitmap
 */
public class KitManager {

    @Getter
    private List<Kit> kits = Lists.newArrayList();
    private Map<UUID, Map<String, Kit>> userKits = Maps.newHashMap();
    @Getter
    private List<String> useAll = new ArrayList<>();

    public KitManager() {
        // load all kits from local redis
        Atheneum.getInstance().runRedisCommand((redis) -> {
            for (String key : redis.keys("kit.*")) {
                Kit kit = Atheneum.PLAIN_GSON.fromJson(redis.get(key), Kit.class);

                kits.add(kit);
            }
            return null;
        });

        // sort kits by name, alphabetically
        kits.sort((first, second) -> first.getName().compareToIgnoreCase(second.getName()));
        HCFactions.getInstance().getLogger().info("- Kit Manager - Loaded " + kits.size() + " kits.");

        // We have to do this later to 'steal' priority
        Bukkit.getScheduler().runTaskLater(HCFactions.getInstance(), () -> {
            TeamsCommandHandler.registerPackage(HCFactions.getInstance(), "com.cobelpvp.hcfactions.map.kits.command");
            TeamsCommandHandler.registerParameterType(Kit.class, new Kit.Type());
        }, 5L);
        Bukkit.getPluginManager().registerEvents(new KitListener(), HCFactions.getInstance());
    }

    public Kit get(UUID player, String name) {
        Kit kit = get(name);

        if (kit == null) {
            return null;
        }

        if (userKits.containsKey(player)) {
            Map<String, Kit> subMap = userKits.get(player);

            if (subMap.containsKey(kit.getName())) {
                return subMap.get(kit.getName());
            }
        }

        return kit;
    }

    public Kit get(String name) {
        for (Kit kit : kits) {
            if (kit.getName().equalsIgnoreCase(name)) {
                return kit;
            }
        }

        return null;
    }

    public Kit getOrCreate(String name) {
        for (Kit kit : kits) {
            if (kit.getName().equalsIgnoreCase(name)) {
                return kit;
            }
        }

        Kit kit = new Kit(name);
        kits.add(kit);

        return kit;
    }

    public void delete(Kit kit) {
        kits.remove(kit);
    }

    public void save() {
        Atheneum.getInstance().runRedisCommand((redis) -> {
            for (Kit kit : kits) {
                redis.set("kit." + kit.getName(), Atheneum.PLAIN_GSON.toJson(kit));
            }
            return null;
        });
    }

    public void loadKits(UUID player) {
        Bukkit.getScheduler().runTaskAsynchronously(HCFactions.getInstance(), () -> {
            Atheneum.getInstance().runRedisCommand((redis) -> {
                for (String key : redis.keys("playerKits:" + player.toString() + ".*")) {
                    Kit kit = Atheneum.PLAIN_GSON.fromJson(redis.get(key), Kit.class);

                    if (!userKits.containsKey(player)) {
                        userKits.put(player, Maps.newHashMap());
                    }

                    userKits.get(player).put(kit.getName(), kit);
                }
                return null;
            });

            Bukkit.getLogger().info("Loaded " + userKits.getOrDefault(player, Maps.newHashMap()).size() + " kits for " + UUIDUtils.name(player));
        });
    }

    public void saveKit(UUID player, String kitName, ItemStack[] newContents) {
        Kit kit = get(kitName);

        if (kit == null) {
            Bukkit.getLogger().info("cant find kit");
            return;
        }

        kit = kit.clone();
        kit.setInventoryContents(newContents);

        if (!userKits.containsKey(player)) {
            userKits.put(player, Maps.newHashMap());
        }

        userKits.get(player).put(kitName, kit);

        final Kit finalKit = kit;

        Bukkit.getScheduler().runTaskAsynchronously(HCFactions.getInstance(), () -> {
            Atheneum.getInstance().runRedisCommand((redis) -> {
                redis.set("playerKits:" + player.toString() + "." + kitName, Atheneum.PLAIN_GSON.toJson(finalKit));

                return null;
            });
        });
    }

    public void logout(UUID player) {
        userKits.remove(player);
    }

    public boolean canUseKit(Player player, String kitName) {
        // You can always use these kits
        if (kitName.equals("Miner") || kitName.equals("Builder") || kitName.equals("Debuff") || kitName.equals("PvP") || kitName.equals("Archer") || kitName.equals("Bard")) {
            return true;
        }

        if (kitName.equals("Vip")) {
            return true;
        }

        if (useAll.contains(kitName.toLowerCase())) {
            return true;
        }

        return player.hasPermission("hcfactions.staff") || player.hasPermission("hcfactions.youtuber") || player.hasPermission("hcfactions.famous");
    }

}
