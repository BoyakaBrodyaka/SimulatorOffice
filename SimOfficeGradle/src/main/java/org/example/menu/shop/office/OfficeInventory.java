package org.example.menu.shop.office;

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

public class OfficeInventory implements Listener {

    private final String OFFICE_MENU_NAME = "§cОфис";
    private ShopMenu shopMenu;

    public OfficeInventory(ShopMenu shopMenu) {
        this.shopMenu = shopMenu;
    }

    public void openOfficeInventory(Player player) {
        Inventory officeMenu = Bukkit.createInventory(null, 54, OFFICE_MENU_NAME);

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
            officeMenu.setItem(i, grayPane);
        }
        for (int i = 45; i < 54; i++) {
            officeMenu.setItem(i, grayPane);
        }
        for (int i = 9; i < 45; i += 9) {
            officeMenu.setItem(i, grayPane);
            officeMenu.setItem(i + 8, grayPane);
        }

        officeMenu.setItem(48, previous);
        officeMenu.setItem(49, exit);
        officeMenu.setItem(50, next);

        player.openInventory(officeMenu);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getView().getTitle().equals(OFFICE_MENU_NAME) && event.getCurrentItem() != null) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();
            handleOfficeMenuClick(player, item, shopMenu);
        }
    }

    public void handleOfficeMenuClick(Player player, ItemStack item, ShopMenu shopMenu) {
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

    public String getOfficeMenuName() {
        return OFFICE_MENU_NAME;
    }
}
