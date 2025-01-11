package org.example.start;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {

    private final OfficeManager officeManager;

    public HomeCommand(OfficeManager officeManager) {
        this.officeManager = officeManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            officeManager.teleportToOffice(player);
            return true;
        }
        sender.sendMessage("Эту команду может использовать только игрок.");
        return false;
    }
}
