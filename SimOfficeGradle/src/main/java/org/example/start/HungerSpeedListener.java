package org.example.start;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.entity.Player;

public class HungerSpeedListener implements Listener {

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getFoodLevel() < 1) {
                event.setCancelled(true); // Отменяем изменение уровня голода, чтобы игрок не умирал
                player.setFoodLevel(1); // Устанавливаем минимальный уровень голода
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        player.setSprinting(true); // Разрешаем бег и прыжки при любом уровне сытости
    }

    @EventHandler
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event) {
        Player player = event.getPlayer();
        if (player.isSprinting()) {
            player.setSprinting(true); // Разрешаем спринт при любом уровне сытости
        }
    }
}
