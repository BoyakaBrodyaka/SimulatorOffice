package org.example.menu.rebirth;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UpgradesMenu {
    private final String UPGRADES_MENU_NAME = "§6Улучшения";

    public void openUpgradesMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 27, UPGRADES_MENU_NAME);

        player.openInventory(menu);
    }

    public String getUpgradesMenuName() {
        return UPGRADES_MENU_NAME;
    }
}
