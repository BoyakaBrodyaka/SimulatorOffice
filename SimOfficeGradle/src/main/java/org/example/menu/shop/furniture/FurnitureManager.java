package org.example.menu.shop.furniture;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.menu.shop.furniture.FurnitureInventory;
import org.example.stats.PlayerStatsManager;

public class FurnitureManager {

    private final FurnitureInventory furnitureInventory;
    private final FurnitureItemAdder furnitureItemAdder;

    public FurnitureManager(FurnitureInventory furnitureInventory, PlayerStatsManager playerStatsManager) {
        this.furnitureInventory = furnitureInventory;
        this.furnitureItemAdder = new FurnitureItemAdder(playerStatsManager);
    }

    public void addSampleFurniture(Player player) {
        Inventory furnitureMenu = Bukkit.createInventory(null, 54, furnitureInventory.getFurnitureMenuName());

        furnitureItemAdder.addFurnitureItem(furnitureMenu, 10, Material.OAK_STAIRS, "§6Стул (1)", "Простой деревянный стул", 1, 10, 1, 0, player);

        furnitureItemAdder.addFurnitureItem(furnitureMenu, 11, Material.OAK_PLANKS, "§6Стол (1)", "Простой деревянный стол", 1, 10, 1, 0, player);

        furnitureInventory.openFurnitureInventory(player, furnitureMenu);
    }

    public void openPurchaseConfirmation(Player player, ItemStack item) {
        Inventory purchaseMenu = Bukkit.createInventory(null, 27, "Подтвердить Покупку");

        ItemStack greenWool = new ItemStack(Material.GREEN_WOOL);
        ItemMeta greenMeta = greenWool.getItemMeta();
        greenMeta.setDisplayName("§aКупить");
        greenWool.setItemMeta(greenMeta);

        ItemStack redWool = new ItemStack(Material.RED_WOOL);
        ItemMeta redMeta = redWool.getItemMeta();
        redMeta.setDisplayName("§cОтмена");
        redWool.setItemMeta(redMeta);

        purchaseMenu.setItem(11, greenWool);
        purchaseMenu.setItem(13, item);
        purchaseMenu.setItem(15, redWool);

        player.openInventory(purchaseMenu);
    }
}
