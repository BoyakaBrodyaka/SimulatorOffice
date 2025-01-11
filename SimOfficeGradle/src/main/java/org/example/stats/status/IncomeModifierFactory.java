package org.example.stats.status;

public class IncomeModifierFactory {
    public static IncomeModifier getIncomeModifier(String status) {
        Statuses statuses = new Statuses();
        switch (status.toLowerCase()) {
            case "отличный":
                return statuses.new GoodStatus();
            case "хороший":
                return statuses.new NeutralStatus();
            case "средний":
                return statuses.new AverageStatus();
            case "плохой":
                return statuses.new BadStatus();
            case "ужасный":
                return statuses.new TerribleStatus();
            default:
                return statuses.new NeutralStatus();
        }
    }
}

