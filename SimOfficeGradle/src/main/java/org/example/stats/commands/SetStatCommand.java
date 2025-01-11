package org.example.stats.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.example.stats.format.FormatNumber;
import org.example.stats.PlayerStats;
import org.example.stats.PlayerStatsManager;

public class SetStatCommand implements CommandExecutor {
    private final PlayerStatsManager playerStatsManager;

    public SetStatCommand(PlayerStatsManager playerStatsManager) {
        this.playerStatsManager = playerStatsManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("Использование: /setstat <игрок> <баланс|доход|уровень|реборн> <значение>");
            return true;
        }

        String playerName = args[0];
        Player player = sender.getServer().getPlayer(playerName);

        if (player == null) {
            sender.sendMessage("Игрок с таким именем не найден.");
            return true;
        }

        PlayerStats playerStats = playerStatsManager.getPlayerStats(player);

        if (playerStats == null) {
            sender.sendMessage("Не удалось загрузить данные игрока.");
            return true;
        }

        String statType = args[1].toLowerCase();
        String value = args[2];

        try {
            double numericValue = FormatNumber.parseValue(value);
            switch (statType) {
                case "баланс":
                    playerStatsManager.setPlayerBalance(player, numericValue);
                    sender.sendMessage("Баланс установлен на: " + FormatNumber.formatNumber(numericValue));
                    break;
                case "доход":
                    playerStatsManager.setPlayerIncome(player, numericValue);
                    sender.sendMessage("Доход установлен на: " + FormatNumber.formatNumber(numericValue));
                    break;
                case "уровень":
                    int level = (int) numericValue;
                    playerStatsManager.setPlayerLevel(player, level);
                    sender.sendMessage("Уровень установлен на: " + FormatNumber.formatNumber(level));
                    break;
                case "реборн":
                    int rebirth = (int) numericValue;
                    playerStatsManager.setPlayerRebirth(player, rebirth);
                    sender.sendMessage("Реборн установлен на: " + FormatNumber.formatNumber(rebirth));
                    break;
                default:
                    sender.sendMessage("Неизвестный тип статистики: " + statType);
                    break;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage("Неверный формат значения для " + statType + ": " + value);
        }

        return true;
    }
}
