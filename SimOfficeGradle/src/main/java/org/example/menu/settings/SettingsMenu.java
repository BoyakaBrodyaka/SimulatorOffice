package org.example.menu.settings;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class SettingsMenu implements Listener {
    private final String SETTINGS_MENU_NAME = "§7Настройки";
    private FlightSetting flightSetting;
    private FlightSpeedSetting flightSpeedSetting;

    public void setFlightSetting(FlightSetting flightSetting) { this.flightSetting = flightSetting; }
    public void setFlightSpeedSetting(FlightSpeedSetting flightSpeedSetting) { this.flightSpeedSetting = flightSpeedSetting; }

    public void openSettingsMenu(Player player) {
        Inventory settingsMenu = Bukkit.createInventory(null, 27, SETTINGS_MENU_NAME);

        ItemStack flightToggle = FlightSetting.getFlightToggleItem(player);
        ItemStack flightSpeed = FlightSpeedSetting.getFlightSpeedItem(player);

        settingsMenu.setItem(11, flightToggle);
        settingsMenu.setItem(15, flightSpeed);

        player.openInventory(settingsMenu);
        Bukkit.getLogger().info("Settings menu opened for player: " + player.getName());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals(SETTINGS_MENU_NAME)) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            if (item == null || item.getType() == Material.AIR) {
                return;
            }

            switch (item.getType()) {
                case GREEN_WOOL:
                case RED_WOOL:
                    flightSetting.toggleFlight(player);
                    break;
                case PAPER:
                    if (event.isLeftClick()) { flightSpeedSetting.changeFlightSpeed(player, 1);
                    } else if (event.isRightClick()) {
                        flightSpeedSetting.changeFlightSpeed(player, -1);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        try {
            int speed = Integer.parseInt(message);
            if (speed < 1 || speed > 5) {
                player.sendMessage("§cОшибка: скорость должна быть от 1 до 5.");
            } else {
                flightSpeedSetting.setFlightSpeed(player, speed);
                player.sendMessage("§aСкорость полета установлена на " + speed + ".");
            }
        } catch (NumberFormatException e) {
            player.sendMessage("§cОшибка: введите число от 1 до 5.");
        }
        event.setCancelled(true);
    }
}
