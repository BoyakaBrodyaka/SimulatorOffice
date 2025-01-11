package org.example.start;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final OfficeManager officeManager;
    private final ArenaManager arenaManager;

    public PlayerJoinListener(OfficeManager officeManager, ArenaManager arenaManager) {
        this.officeManager = officeManager;
        this.arenaManager = arenaManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            if (!arenaManager.createArenaForPlayer(player)) {
                player.sendMessage("Не удалось создать арену, попробуйте снова позже.");
                return;
            }
            officeManager.sendStoryline(player);
        } else {
            Location officeLocation = officeManager.getOfficeLocation(player);
            if (officeLocation != null) {
                player.teleport(officeLocation);
                player.sendMessage("Добро пожаловать обратно в ваш офис!");
            } else {
                if (!arenaManager.createArenaForPlayer(player)) {
                    player.sendMessage("Не удалось создать арену, попробуйте снова позже.");
                }
            }
        }
    }
}
