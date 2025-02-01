package org.example.design;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.example.stats.PlayerStats;
import org.example.stats.PlayerStatsManager;

public class ChatMessageListener implements Listener {
    private PlayerStatsManager playerStatsManager;

    public ChatMessageListener(PlayerStatsManager playerStatsManager) {
        this.playerStatsManager = playerStatsManager;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerStats playerStats = playerStatsManager.getPlayerStats(player);
        if (playerStats != null) {
            String formattedName = TextFormatter.formatLevelAndNickname(playerStats.getLevel(), playerStats.getPlayerName());
            event.setFormat(formattedName + ": §f%2$s");
        }
    }
}
