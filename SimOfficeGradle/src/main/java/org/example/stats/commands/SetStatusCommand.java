package org.example.stats.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.stats.PlayerStats;
import org.example.stats.PlayerStatsManager;

import java.util.UUID;

public class SetStatusCommand implements CommandExecutor {

    private PlayerStatsManager playerStatsManager;

    public SetStatusCommand(PlayerStatsManager playerDataManager) {
        this.playerStatsManager = playerDataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду могут использовать только игроки.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Использование: /setstatus <player> <отличный|хороший|средний|плохой|ужасный>");
            return true;
        }

        Player targetPlayer = sender.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Игрок не найден.");
            return true;
        }

        PlayerStats playerStats = playerStatsManager.getPlayerStats(targetPlayer);
        if (playerStats == null) {
            sender.sendMessage(ChatColor.RED + "Нет данных для игрока: " + targetPlayer.getName());
            return true;
        }

        String status = args[1].toLowerCase();
        switch (status) {
            case "отличный":
            case "хороший":
            case "средний":
            case "плохой":
            case "ужасный":
                playerStats.setStatus(status.substring(0, 1).toUpperCase() + status.substring(1));
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Неизвестный статус: " + status);
                return true;
        }

        sender.sendMessage(ChatColor.GREEN + "Статус игрока " + targetPlayer.getName() + " успешно обновлен.");
        return true;
    }
}
