package org.example.stats.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;
import org.example.stats.format.FormatNumber;
import org.example.stats.IncomeManager;
import org.example.stats.PlayerStats;
import org.example.stats.PlayerStatsManager;

import java.util.UUID;
import java.util.logging.Logger;

public class ScoreboardManager {
    private Scoreboard scoreboard;
    private Objective objective;
    private String realmName;
    private Logger logger;
    private FormatNumber formatNumber;
    private PlayerStatsManager playerStatsManager;

    public ScoreboardManager(PlayerStatsManager playerStatsManager, String realmName) {
        this.playerStatsManager = playerStatsManager;
        this.realmName = realmName;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("test", "dummy", ChatColor.GOLD + "----=ИНФОРМАЦИЯ=----");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.logger = Bukkit.getLogger();
        this.formatNumber = new FormatNumber();

        new IncomeManager(playerStatsManager).startIncomeTask((JavaPlugin) Bukkit.getPluginManager().getPlugin("SimOffice"));
    }

    public void updateScoreboard(Player player) {
        World playerWorld = player.getWorld();
        if (!playerWorld.getName().equals(realmName)) {
            return;
        }

        UUID uuid = player.getUniqueId();
        PlayerStats playerStats = playerStatsManager.getPlayerStats(player);

        if (playerStats != null) {

            scoreboard.getEntries().forEach(entry -> scoreboard.resetScores(entry));

            Score empty1 = objective.getScore("");
            Score empty2 = objective.getScore(" ");
            Score empty3 = objective.getScore("  ");
            Score empty4 = objective.getScore("   ");
            Score empty5 = objective.getScore("    ");
            Score empty6 = objective.getScore("     ");
            empty1.setScore(10);

            Score status = objective.getScore(ChatColor.GREEN + "Статус: " + ChatColor.WHITE + playerStats.getStatus());
            status.setScore(9);

            empty2.setScore(8);
            Score balance = objective.getScore(ChatColor.GREEN + "Баланс: " + ChatColor.WHITE + FormatNumber.formatNumber(playerStats.getBalance()));
            balance.setScore(7);

            empty3.setScore(6);
            Score income = objective.getScore(ChatColor.GREEN + "Доход: " + ChatColor.WHITE + FormatNumber.formatNumber(playerStats.getIncome()));
            income.setScore(5);

            empty4.setScore(4);
            Score level = objective.getScore(ChatColor.GREEN + "Уровень: " + ChatColor.WHITE + playerStats.getLevel());
            level.setScore(3);

            empty5.setScore(2);
            Score rebirth = objective.getScore(ChatColor.GREEN + "Перерождение: " + ChatColor.WHITE + playerStats.getRebirth());
            rebirth.setScore(1);

            empty6.setScore(0);

            player.setScoreboard(scoreboard);
        } else {
            logger.severe("PlayerStats is null for player: " + player.getName() + " with UUID: " + uuid);
        }
    }
}

