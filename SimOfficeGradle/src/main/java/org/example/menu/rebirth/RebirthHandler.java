package org.example.menu.rebirth;

import org.bukkit.entity.Player;
import org.example.stats.format.FormatNumber;
import org.example.stats.PlayerStats;
import org.example.stats.PlayerStatsManager;

import java.util.UUID;

public class RebirthHandler {
    private final double BASE_REBIRTH_COST = 1000000;
    private final int MAX_TOKENS = 3;

    private PlayerStatsManager playerStatsManager;
    private PlayerStats playerStats;
    private double currentRebirthCost;
    private double nextTokenCost;

    public RebirthHandler(PlayerStatsManager playerStatsManager, Player playerName) {
        this.playerStatsManager = playerStatsManager;
        this.playerStats = playerStatsManager.getPlayerStats(playerName);
        this.currentRebirthCost = calculateRebirthCost(playerStats.getRebirth());
        this.nextTokenCost = calculateNextTokenCost(playerStats.getTokens()); }

    public double calculateRebirthCost(int level) {
        return BASE_REBIRTH_COST * level;
    }

    public double calculateNextTokenCost(int tokenCount) {
        return currentRebirthCost * (1 + 0.5 * tokenCount);
    }

    public String getRebirthInfo() {
        int rebirthLevel = playerStats.getRebirth();
        int tokens = playerStats.getTokens();
        int currentLevel = playerStats.getLevel();
        double currentBalance = playerStats.getBalance();
        double requiredBalance = currentRebirthCost;

        return String.format(
                "§bУровень: §a%d/50\n" +
                        "§bТребуемый баланс: §a%s/%s\n" +
                        "§bРебирт уровень: §a%d -> %d\n" +
                        "§bРебирт токены: §a%d -> %d",
                currentLevel, FormatNumber.formatNumber(currentBalance), FormatNumber.formatNumber(requiredBalance), rebirthLevel, rebirthLevel + 1, tokens, Math.min(tokens + 1, MAX_TOKENS));
    }

    public boolean canRebirth(double playerMoney) {
        return playerMoney >= currentRebirthCost;
    }

    public void doRebirth() {
        playerStats.setRebirth(playerStats.getRebirth() + 1);
        playerStats.setTokens(1);
        currentRebirthCost = calculateRebirthCost(playerStats.getRebirth());
        nextTokenCost = calculateNextTokenCost(playerStats.getTokens());
    }
}
