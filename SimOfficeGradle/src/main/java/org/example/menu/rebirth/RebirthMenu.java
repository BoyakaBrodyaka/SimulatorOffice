package org.example.menu.rebirth;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.menu.rebirth.RebirthHandler;
import org.example.stats.PlayerStats;

import java.util.Arrays;

public class RebirthMenu {
    private final String REBIRTH_MENU_NAME = "§cПерерождение";
    private RebirthHandler rebirthHandler;
    private PlayerStats playerStats;

    public RebirthMenu(RebirthHandler rebirthHandler, PlayerStats playerStats) {
        this.rebirthHandler = rebirthHandler;
        this.playerStats = playerStats;
    }

    public void openRebirthMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 27, REBIRTH_MENU_NAME);

        ItemStack upgrades = new ItemStack(Material.BOOK);
        ItemMeta upgradesMeta = upgrades.getItemMeta();
        upgradesMeta.setDisplayName("§6Улучшения");
        upgrades.setItemMeta(upgradesMeta);

        ItemStack rebirth = new ItemStack(Material.REDSTONE);
        ItemMeta rebirthMeta = rebirth.getItemMeta();
        rebirthMeta.setDisplayName("§cСделать перерождение");
        rebirthMeta.setLore(Arrays.asList(rebirthHandler.getRebirthInfo().split("\n")));
        rebirth.setItemMeta(rebirthMeta);

        menu.setItem(11, upgrades);
        menu.setItem(15, rebirth);

        player.openInventory(menu);
    }

    public String getRebirthMenuName() {
        return REBIRTH_MENU_NAME;
    }
}
