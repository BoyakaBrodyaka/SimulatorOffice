package org.example.menu.settings;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.menu.settings.SettingsMenu;

public class FlightSetting {
    private SettingsMenu settingsMenu;

    public FlightSetting(SettingsMenu settingsMenu) {
        this.settingsMenu = settingsMenu;
    }

    public static ItemStack getFlightToggleItem(Player player) {
        boolean isFlying = player.getAllowFlight();
        ItemStack flightToggle = new ItemStack(isFlying ? Material.GREEN_WOOL : Material.RED_WOOL);
        ItemMeta meta = flightToggle.getItemMeta();
        meta.setDisplayName(isFlying ? "§aПолет: Вкл" : "§cПолет: Выкл");
        flightToggle.setItemMeta(meta);
        return flightToggle;
    }

    public void toggleFlight(Player player) {
        boolean isFlying = player.getAllowFlight();
        player.setAllowFlight(!isFlying);

        settingsMenu.openSettingsMenu(player);
    }
}