package org.example.menu.shop.furniture;

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

public class FurnitureInventory implements Listener {

    private final String FURNITURE_MENU_NAME = "§6Мебель";
    private ShopMenu shopMenu;
    private FurnitureManager furnitureManager;

    public FurnitureInventory(ShopMenu shopMenu, FurnitureManager furnitureManager) {
        this.shopMenu = shopMenu;
        this.furnitureManager = furnitureManager;
    }

    public void openFurnitureInventory(Player player, Inventory furnitureMenu) {

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
            furnitureMenu.setItem(i, grayPane);
        }
        for (int i = 45; i < 54; i++) {
            furnitureMenu.setItem(i, grayPane);
        }
        for (int i = 9; i < 45; i += 9) {
            furnitureMenu.setItem(i, grayPane);
            furnitureMenu.setItem(i + 8, grayPane);
        }

        furnitureMenu.setItem(48, previous);
        furnitureMenu.setItem(49, exit);
        furnitureMenu.setItem(50, next);

        player.openInventory(furnitureMenu);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        Inventory clickedInventory = event.getClickedInventory();

        if(event.getView().getTitle().equals(FURNITURE_MENU_NAME) && event.getCurrentItem() != null) {
            event.setCancelled(true);
            handleFurnitureMenuClick(player, item, shopMenu);
        } else if (event.getView().getTitle().equals("Подтвердить Покупку") && event.getCurrentItem() != null) {
            event.setCancelled(true);
            handlePurchaseConfirmationClick(player, item, event);
        }
    }

    public void handleFurnitureMenuClick(Player player, ItemStack item, ShopMenu shopMenu) {
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
                player.closeInventory();
                furnitureManager.openPurchaseConfirmation(player, item);
                break;

        }
    }

    public void handlePurchaseConfirmationClick(Player player, ItemStack item, InventoryClickEvent event) {
        switch (item.getType()) {
            case GREEN_WOOL:
                ItemStack purchasedItem = event.getView().getItem(13);
                if (purchasedItem != null && purchasedItem.hasItemMeta()) {
                    ItemMeta purchasedItemMeta = purchasedItem.getItemMeta();
                    assert purchasedItemMeta != null;
                    String displayName = purchasedItemMeta.getDisplayName();

                    purchasedItem.setItemMeta(purchasedItemMeta);
                    player.getInventory().addItem(purchasedItem);
                    player.closeInventory();
                    player.sendMessage("§aВы успешно купили " + displayName);
                } else {
                    player.sendMessage("§cОшибка: Предмет отсутствует или не имеет метаданных.");
                }
                break;
            case RED_WOOL:
                player.closeInventory();
                player.sendMessage("§cПокупка отменена");
                break;
            default:
                break;
        }
    }

    public String getFurnitureMenuName() {
        return FURNITURE_MENU_NAME;
    }
}
