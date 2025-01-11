package org.example.stats;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.stats.PlayerStats;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PlayerStatsManager implements Listener {

    private JavaPlugin plugin;
    private Map<Player, PlayerStats> playerDataMap = new HashMap<>();

    public PlayerStatsManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        loadPlayerData(player);
        PlayerStats playerStats = playerDataMap.get(player);
        if (playerStats != null) {
            player.sendMessage("Добро пожаловать, ваши данные загружены.");
        } else {
            player.sendMessage("Не удалось загрузить ваши данные, создаем новые...");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        savePlayerData(player);
    }

    public void saveAllPlayerData() {
        for (Player player : playerDataMap.keySet()) {
            savePlayerData(player);
        }
    }

    public void loadAllPlayerData() {
        File folder = plugin.getDataFolder();
        if (!folder.exists()) {
            folder.mkdir();
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                Player player = plugin.getServer().getPlayer(file.getName().replace(".yml", ""));
                loadPlayerData(player);
            }
        }
    }

    public void loadPlayerData(Player player) {
        File file = new File(plugin.getDataFolder(), player.getName() + ".yml");
        if (!file.exists()) {
            plugin.getLogger().info("Создан новый файл для игрока: " + player.getName());
            PlayerStats playerStats = new PlayerStats(player, plugin.getDataFolder(), this);
            playerDataMap.put(player, playerStats);
            return;
        }

        PlayerStats playerStats = new PlayerStats(player, plugin.getDataFolder(), this);
        playerStats.load();
        playerDataMap.put(player, playerStats);
        plugin.getLogger().info("Данные загружены для игрока: " + player.getName());
    }

    public void savePlayerData(Player player) {
        PlayerStats playerStats = playerDataMap.get(player);
        if (playerStats == null) {
            plugin.getLogger().warning("PlayerStats is null for player: " + player.getName());
            return;
        }

        playerStats.save();
        plugin.getLogger().info("Данные сохранены для игрока: " + player.getName());
    }

    public PlayerStats getPlayerStats(Player player) {
        return playerDataMap.get(player);
    }

    public PlayerStats getPlayerStatsByName(String playerName) {
        for (PlayerStats stats : playerDataMap.values()) {
            if (stats.getPlayerName().equalsIgnoreCase(playerName)) {
                return stats;
            }
        }
        return null;
    }

    public void setPlayerRebirth(Player player, int rebirth) {
        PlayerStats playerStats = getPlayerStats(player);
        if (playerStats != null) {
            playerStats.setRebirth(rebirth);
        }
    }

    public void setPlayerLevel(Player player, int level) {
        PlayerStats playerStats = getPlayerStats(player);
        if (playerStats != null) {
            playerStats.setLevel(level);
        }
    }

    public void setPlayerIncome(Player player, double income) {
        PlayerStats playerStats = getPlayerStats(player);
        if (playerStats != null) {
            playerStats.setIncome(income);
        }
    }

    public void setPlayerBalance(Player player, double balance) {
        PlayerStats playerStats = getPlayerStats(player);
        if (playerStats != null) {
            playerStats.setBalance(balance);
        }
    }

    public void setPlayerTokens(Player player, int tokens) {
        PlayerStats playerStats = getPlayerStats(player);
        if (playerStats != null) {
            playerStats.setTokens(tokens);
        }
    }
}
