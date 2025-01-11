package org.example.menu.rebirth;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.example.menu.rebirth.RebirthHandler;
import org.example.stats.PlayerStats;

public class RebirthListener implements Listener {

    private RebirthMenu rebirthMenu;
    private UpgradesMenu upgradesMenu;
    private RebirthHandler rebirthHandler;
    private PlayerStats playerStats;

    public RebirthListener(RebirthMenu rebirthMenu, UpgradesMenu upgradesMenu, RebirthHandler rebirthHandler, PlayerStats playerStats) {
        this.rebirthMenu = rebirthMenu;
        this.upgradesMenu = upgradesMenu;
        this.rebirthHandler = rebirthHandler;
        this.playerStats = playerStats;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        if (title.equals(rebirthMenu.getRebirthMenuName())) {
            event.setCancelled(true);
            switch (item.getType()) {
                case BOOK:
                    upgradesMenu.openUpgradesMenu(player);
                    break;
                case REDSTONE:
                    if (rebirthHandler.canRebirth(playerStats.getBalance())) {
                        rebirthHandler.doRebirth();
                        player.sendMessage("§cВы выбрали сделать перерождение!");
                    } else {
                        player.sendMessage("§cНедостаточно денег для перерождения!");
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
