package com.cobelpvp.hcfactions.deathmessage.objects;

import com.cobelpvp.hcfactions.HCFactions;
import lombok.Getter;
import com.cobelpvp.atheneum.util.UUIDUtils;
import org.bukkit.ChatColor;

public abstract class Damage {

    @Getter private String damaged;
    @Getter private double damage;
    @Getter private long time;

    public Damage(String damaged, double damage) {
        this.damaged = damaged;
        this.damage = damage;
        this.time = System.currentTimeMillis();
    }

    public abstract String getDeathMessage();

    public String wrapName(String player) {
        int kills = HCFactions.getInstance().getMapHandler().isKitMap() || HCFactions.getInstance().getServerHandler().isCobelKitMap() ? HCFactions.getInstance().getMapHandler().getStatsHandler().getStats(player).getKills() : HCFactions.getInstance().getKillsMap().getKills(UUIDUtils.uuid(player));

        return (ChatColor.RED + player + ChatColor.GOLD + "[" + kills + "]" + ChatColor.YELLOW);
    }

    public long getTimeDifference() {
        return System.currentTimeMillis() - time;
    }

}