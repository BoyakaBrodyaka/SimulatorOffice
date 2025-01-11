package org.example.start;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetOfficeCommand implements CommandExecutor {

    private final OfficeManager officeManager;

    public ResetOfficeCommand(OfficeManager officeManager) {
        this.officeManager = officeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            officeManager.resetOffice(player);
            return true;
        }
        sender.sendMessage("Эту команду может использовать только игрок.");
        return false;
    }
}
