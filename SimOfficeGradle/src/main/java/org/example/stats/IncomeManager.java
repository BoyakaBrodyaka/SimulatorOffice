package org.example.stats;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class IncomeManager {

    private PlayerStatsManager playerStatsManager;

    public IncomeManager(PlayerStatsManager playerStatsManager) {
        this.playerStatsManager = playerStatsManager;
    }

    public void startIncomeTask(JavaPlugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    PlayerStats playerStats = playerStatsManager.getPlayerStats(player);
                    if (playerStats != null) {
                        double modifiedIncome = playerStats.getModifiedIncome();
                        playerStats.setBalance(playerStats.getBalance() + modifiedIncome);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}
