package org.example.menu.shop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.menu.shop.equipment.EquipmentInventory;
import org.example.menu.shop.equipment.EquipmentManager;
import org.example.menu.shop.furniture.FurnitureInventory;
import org.example.menu.shop.furniture.FurnitureManager;
import org.example.menu.shop.office.OfficeInventory;
import org.example.stats.PlayerStatsManager;

public class ShopMenu {

    private final String SHOP_MENU_NAME = "§eМагазин";
    private FurnitureInventory furnitureInventory;
    private EquipmentInventory equipmentInventory;
    private OfficeInventory officeInventory;
    private FurnitureManager furnitureManager;
    private EquipmentManager equipmentManager;

    public ShopMenu(PlayerStatsManager playerStatsManager) {
        this.furnitureInventory = new FurnitureInventory(this, furnitureManager);
        this.equipmentInventory = new EquipmentInventory(this);
        this.officeInventory = new OfficeInventory(this);
        this.furnitureManager = new FurnitureManager(furnitureInventory, playerStatsManager);
        this.equipmentManager = new EquipmentManager(equipmentInventory, playerStatsManager);
    }

    public void openShopMenu(Player player) {
        Inventory shopMenu = Bukkit.createInventory(null, 27, SHOP_MENU_NAME);

        ItemStack furniture = new ItemStack(Material.GOLD_INGOT);
        ItemMeta furnitureMeta = furniture.getItemMeta();
        furnitureMeta.setDisplayName("§6Мебель");
        furniture.setItemMeta(furnitureMeta);

        ItemStack equipment = new ItemStack(Material.DIAMOND);
        ItemMeta equipmentMeta = equipment.getItemMeta();
        equipmentMeta.setDisplayName("§bОборудование");
        equipment.setItemMeta(equipmentMeta);

        ItemStack office = new ItemStack(Material.NETHER_STAR);
        ItemMeta officeMeta = office.getItemMeta();
        officeMeta.setDisplayName("§cОфис");
        office.setItemMeta(officeMeta);

        ItemStack grayPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta grayPaneMeta = grayPane.getItemMeta();
        grayPaneMeta.setDisplayName(" ");
        grayPane.setItemMeta(grayPaneMeta);

        for (int i = 0; i < shopMenu.getSize(); i++) {
            if (shopMenu.getItem(i) == null) {
                shopMenu.setItem(i, grayPane);
            }
        }

        shopMenu.setItem(11, furniture);
        shopMenu.setItem(13, equipment);
        shopMenu.setItem(15, office);

        player.openInventory(shopMenu);
    }

    public void handleShopMenuClick(Player player, ItemStack item) {
        if(item == null || item.getType() == Material.AIR) {
            return;
        }

        switch (item.getType()) {
            case GOLD_INGOT:
                furnitureManager.addSampleFurniture(player);
                break;
            case DIAMOND:
                equipmentManager.addSampleEquipment(player);
                break;
            case NETHER_STAR:
                officeInventory.openOfficeInventory(player);
                break;
            case BARRIER:
                player.closeInventory();
                break;
            case ARROW:
                break;
            default:
                break;

        }
    }

    public String getShopMenuName() {
        return SHOP_MENU_NAME;
    }
}
