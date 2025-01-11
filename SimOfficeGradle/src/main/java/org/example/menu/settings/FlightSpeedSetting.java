package org.example.menu.settings;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FlightSpeedSetting {

    private SettingsMenu settingsMenu;

    public FlightSpeedSetting(SettingsMenu settingsMenu) {
        this.settingsMenu = settingsMenu;
    }

    public static ItemStack getFlightSpeedItem(Player player) {
        float speed = player.getFlySpeed() * 5;
        ItemStack flightSpeed = new ItemStack(Material.PAPER);
        ItemMeta meta = flightSpeed.getItemMeta();
        meta.setDisplayName("§7Скорость полета: " + (int) speed);
        flightSpeed.setItemMeta(meta);
        return flightSpeed;
    }

    public void changeFlightSpeed(Player player, int delta) {
        if (!player.getAllowFlight()) {
            player.sendMessage("§cОшибка: Полет выключен.");
            return;
        }
        float speed = player.getFlySpeed() * 5 + delta;
        if (speed < 1) speed = 1;
        if (speed > 5) speed = 5;
        setFlightSpeed(player, (int) speed);
    }

    public void setFlightSpeed(Player player, int speed) {
        float flySpeed = speed / 5.0f;
        player.setFlySpeed(flySpeed);
        settingsMenu.openSettingsMenu(player);
    }
}