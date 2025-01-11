package org.example.start;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CreateOfficeListener implements Listener {

    private final OfficeManager officeManager;

    public CreateOfficeListener(OfficeManager officeManager) {
        this.officeManager = officeManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.PAPER && "Создать офис".equals(item.getItemMeta().getDisplayName())) {
            event.setCancelled(true);
            officeManager.createOffice(player);
        }
    }
}
