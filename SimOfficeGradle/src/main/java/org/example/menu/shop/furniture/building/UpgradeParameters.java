package org.example.menu.shop.furniture.building;

public class UpgradeParameters {
    private final int requiredBalance;
    private final int requiredLevel;
    private final int requiredRebirth;
    private final double statisticBoost;

    public UpgradeParameters(int requiredBalance, int requiredLevel, int requiredRebirth, double statisticBoost) {
        this.requiredBalance = requiredBalance;
        this.requiredLevel = requiredLevel;
        this.requiredRebirth = requiredRebirth;
        this.statisticBoost = statisticBoost;
    }

    public int getRequiredBalance() {
        return requiredBalance;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public double getStatisticBoost() {
        return statisticBoost;
    }

    public int getRequiredRebirth() {
        return requiredRebirth;
    }
}
