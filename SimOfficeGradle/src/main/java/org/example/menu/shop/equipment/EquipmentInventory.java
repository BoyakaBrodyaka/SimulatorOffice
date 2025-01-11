package org.example.menu.shop.equipment;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.menu.shop.ShopMenu;

public class EquipmentInventory implements Listener {

    private final String EQUIPMENT_MENU_NAME = "§bОборудование";
    private ShopMenu shopMenu;

    public EquipmentInventory(ShopMenu shopMenu) {
        this.shopMenu = shopMenu;
    }

    public void openEquipmentInventory(Player player, Inventory equipmentMenu) {

        ItemStack exit = new ItemStack(Material.BARRIER);
        ItemMeta exitMeta = exit.getItemMeta();
        exitMeta.setDisplayName("§cВыход");
        exit.setItemMeta(exitMeta);

        ItemStack previous = new ItemStack(Material.ARROW);
        ItemMeta previousMeta = previous.getItemMeta();
        previousMeta.setDisplayName("§aПредыдущая");
        previous.setItemMeta(previousMeta);

        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName("§aСледующая");
        next.setItemMeta(nextMeta);

        ItemStack grayPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta grayPaneMeta = grayPane.getItemMeta();
        grayPaneMeta.setDisplayName(" ");
        grayPane.setItemMeta(grayPaneMeta);

        for (int i = 0; i < 9; i++) {
            equipmentMenu.setItem(i, grayPane);
        }
        for (int i = 45; i < 54; i++) {
            equipmentMenu.setItem(i, grayPane);
        }
        for (int i = 9; i < 45; i += 9) {
            equipmentMenu.setItem(i, grayPane);
            equipmentMenu.setItem(i + 8, grayPane);
        }

        equipmentMenu.setItem(48, previous);
        equipmentMenu.setItem(49, exit);
        equipmentMenu.setItem(50, next);

        player.openInventory(equipmentMenu);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getView().getTitle().equals(EQUIPMENT_MENU_NAME) && event.getCurrentItem() != null) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();
            handleEquipmentMenuClick(player, item, shopMenu);
        }
    }

    public void handleEquipmentMenuClick(Player player, ItemStack item, ShopMenu shopMenu) {
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        switch (item.getType()) {
            case BARRIER:
                shopMenu.openShopMenu(player);
                break;
            case ARROW:
                break;
            default:
                break;

        }
    }

    public String getEquipmentInventoryName() {
        return EQUIPMENT_MENU_NAME;
    }
}
