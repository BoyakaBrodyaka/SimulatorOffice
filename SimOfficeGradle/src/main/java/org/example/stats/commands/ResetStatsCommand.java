package org.example.stats.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.stats.PlayerStats;
import org.example.stats.PlayerStatsManager;

import java.io.File;
import java.util.UUID;

public class ResetStatsCommand implements CommandExecutor {
    private final File dataFolder;
    private PlayerStatsManager playerStatsManager;

    public ResetStatsCommand(File dataFolder, PlayerStatsManager playerStatsManager) {
        this.dataFolder = dataFolder;
        this.playerStatsManager = playerStatsManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду может использовать только игрок.");
            return true;
        }

        Player player = (Player) sender;
        PlayerStats playerStats = new PlayerStats(player, dataFolder, playerStatsManager);

        playerStats.reset();

        player.sendMessage("Ваша статистика была успешно сброшена.");
        return true;
    }
}
