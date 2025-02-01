package org.example.start;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.example.stats.PlayerStats;
import org.example.stats.PlayerStatsManager;

public class PlayerJoinListener implements Listener {

    private final OfficeManager officeManager;
    private final ArenaManager arenaManager;
    private final PlayerStatsManager playerStatsManager;

    public PlayerJoinListener(OfficeManager officeManager, ArenaManager arenaManager, PlayerStatsManager playerStatsManager) {
        this.officeManager = officeManager;
        this.arenaManager = arenaManager;
        this.playerStatsManager = playerStatsManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerStats playerStats = playerStatsManager.getPlayerStats(player);
        if (playerStats != null) {
            playerStats.updateTabName();
        }
        if (!player.hasPlayedBefore()) {
            if (!arenaManager.createArenaForPlayer(player)) {
                player.sendMessage("Не удалось создать арену, попробуйте снова позже.");
                return;
            }
            officeManager.sendStoryline(player);
        } else {
            Location officeLocation = officeManager.getOfficeLocation(player);
            if (officeLocation != null) {
                player.teleport(officeLocation);
                player.sendMessage("Добро пожаловать обратно в ваш офис!");
            } else {
                if (!arenaManager.createArenaForPlayer(player)) {
                    player.sendMessage("Не удалось создать арену, попробуйте снова позже.");
                }
            }
        }
    }
}
