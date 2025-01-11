package org.example.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.example.menu.settings.SettingsMenu;
import org.example.menu.shop.ShopMenu;
import org.example.menu.rebirth.RebirthMenu;
import org.example.menu.rebirth.UpgradesMenu;
import org.example.menu.rebirth.RebirthListener;
import org.example.menu.rebirth.RebirthHandler;
import org.example.start.OfficeManager;
import org.example.stats.PlayerStats;
import org.example.stats.PlayerStatsManager;
import org.bukkit.plugin.Plugin;

public class MenuListener implements Listener {

    private final String MENU_NAME = "§6Меню";
    private SettingsMenu settingsMenu;
    private OfficeManager officeManager;
    private ShopMenu shopMenu;
    private PlayerStatsManager playerStatsManager;
    private Plugin plugin;

    public MenuListener(SettingsMenu settingsMenu, OfficeManager officeManager, PlayerStatsManager playerStatsManager, Plugin plugin) {
        this.settingsMenu = settingsMenu;
        this.officeManager = officeManager;
        this.shopMenu = new ShopMenu(playerStatsManager);
        this.playerStatsManager = playerStatsManager;
        this.plugin = plugin;

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() == Material.CLOCK && MENU_NAME.equals(item.getItemMeta().getDisplayName())) {
                openMenu(player);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getType() != InventoryType.CHEST) {
            return;
        }
        if (event.getView().getTitle().equals(MENU_NAME)) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            if (item == null || item.getType() == Material.AIR) {
                return;
            }

            PlayerStats playerStats = playerStatsManager.getPlayerStats(player);
            if (playerStats == null) {
                player.sendMessage("§cОшибка: Данные игрока не найдены!");
                return;
            }

            RebirthHandler rebirthHandler = new RebirthHandler(playerStatsManager, player);
            RebirthMenu rebirthMenu = new RebirthMenu(rebirthHandler, playerStats);
            UpgradesMenu upgradesMenu = new UpgradesMenu();
            RebirthListener rebirthListener = new RebirthListener(rebirthMenu, upgradesMenu, rebirthHandler, playerStats);
            plugin.getServer().getPluginManager().registerEvents(rebirthListener, plugin);

            switch (item.getType()) {
                case REDSTONE:
                    player.closeInventory();
                    rebirthMenu.openRebirthMenu(player);
                    break;
                case EMERALD:
                    player.sendMessage("§aУровень");
                    break;
                case GOLD_INGOT:
                    shopMenu.openShopMenu(player);
                    break;
                case DIAMOND:
                    player.sendMessage("§bДостижения");
                    break;
                case NETHER_STAR:
                    settingsMenu.openSettingsMenu(player);
                    break;
                case POLISHED_ANDESITE:
                    officeManager.teleportToOffice(player);
                    break;
                default:
                    break;
            }
        } else if (event.getView().getTitle().equals(shopMenu.getShopMenuName())) {
            event.setCancelled(true);
            shopMenu.handleShopMenuClick(player, event.getCurrentItem());
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (item.getType() == Material.CLOCK && MENU_NAME.equals(item.getItemMeta().getDisplayName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        org.example.menu.MenuItemHandler.giveMenuItem(player);
    }

    private void openMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 45, MENU_NAME);

        ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassPaneMeta = glassPane.getItemMeta();
        glassPaneMeta.setDisplayName(" ");
        glassPane.setItemMeta(glassPaneMeta);

        for (int i = 0; i < menu.getSize(); i++) {
            menu.setItem(i, glassPane);
        }

        ItemStack rebirth = new ItemStack(Material.REDSTONE);
        ItemMeta rebirthMeta = rebirth.getItemMeta();
        rebirthMeta.setDisplayName("§cПерерождение");
        rebirth.setItemMeta(rebirthMeta);

        ItemStack level = new ItemStack(Material.EMERALD);
        ItemMeta levelMeta = level.getItemMeta();
        levelMeta.setDisplayName("§aУровень");
        level.setItemMeta(levelMeta);

        ItemStack shop = new ItemStack(Material.GOLD_INGOT);
        ItemMeta shopMeta = shop.getItemMeta();
        shopMeta.setDisplayName("§eМагазин");
        shop.setItemMeta(shopMeta);

        ItemStack achievements = new ItemStack(Material.DIAMOND);
        ItemMeta achievementsMeta = achievements.getItemMeta();
        achievementsMeta.setDisplayName("§bДостижения");
        achievements.setItemMeta(achievementsMeta);

        ItemStack settings = new ItemStack(Material.NETHER_STAR);
        ItemMeta settingsMeta = settings.getItemMeta();
        settingsMeta.setDisplayName("§7Настройки");
        settings.setItemMeta(settingsMeta);

        ItemStack location = new ItemStack(Material.DIAMOND_BLOCK);
        ItemMeta locationMeta = location.getItemMeta();
        locationMeta.setDisplayName("§aДонат");
        location.setItemMeta(locationMeta);

        ItemStack myOffice = new ItemStack(Material.POLISHED_ANDESITE);
        ItemMeta myOfficeMeta = myOffice.getItemMeta();
        myOfficeMeta.setDisplayName("§cОтправиться в офис");
        myOffice.setItemMeta(myOfficeMeta);

        menu.setItem(4, settings);
        menu.setItem(19, rebirth);
        menu.setItem(20, level);
        menu.setItem(22, myOffice);
        menu.setItem(24, shop);
        menu.setItem(25, achievements);
        menu.setItem(40, location);

        player.openInventory(menu);
    }
}
