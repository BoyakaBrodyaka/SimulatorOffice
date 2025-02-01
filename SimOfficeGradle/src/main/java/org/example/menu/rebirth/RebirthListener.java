package org.example.menu.rebirth;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.example.stats.PlayerStats;

import java.util.HashMap;
import java.util.UUID;

public class RebirthListener implements Listener {

    private final RebirthMenu rebirthMenu;
    private final UpgradesMenu upgradesMenu;
    private final RebirthHandler rebirthHandler;
    private final PlayerStats playerStats;
    private final HashMap<UUID, Long> cooldowns;
    private final HashMap<UUID, Long> lastMessageTime;
    private final long cooldownTime; // Cooldown time in milliseconds
    private final long messageCooldownTime; // Cooldown time for messages in milliseconds

    public RebirthListener(RebirthMenu rebirthMenu, UpgradesMenu upgradesMenu, RebirthHandler rebirthHandler, PlayerStats playerStats) {
        this.rebirthMenu = rebirthMenu;
        this.upgradesMenu = upgradesMenu;
        this.rebirthHandler = rebirthHandler;
        this.playerStats = playerStats;
        this.cooldowns = new HashMap<>();
        this.lastMessageTime = new HashMap<>();
        this.cooldownTime = 1000;
        this.messageCooldownTime = 5000; // 5 seconds message cooldown
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
                    if (isOnCooldown(player)) {
                        player.sendMessage("§cПодождите немного перед повторным перерождением!");
                        return;
                    }
                    if (rebirthHandler.canRebirth(playerStats.getBalance(), playerStats.getLevel())) {
                        rebirthHandler.doRebirth(player);
                        player.sendMessage("§cВы выбрали сделать перерождение!");
                        updateCooldown(player);
                        rebirthMenu.openRebirthMenu(player); // Обновление меню без закрытия
                    } else {
                        sendMessageWithCooldown(player, "§cНедостаточно условий для перерождения!");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private boolean isOnCooldown(Player player) {
        if (!cooldowns.containsKey(player.getUniqueId())) {
            return false;
        }
        long lastUsed = cooldowns.get(player.getUniqueId());
        return (System.currentTimeMillis() - lastUsed) < cooldownTime;
    }

    private void updateCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    private void sendMessageWithCooldown(Player player, String message) {
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        if (!lastMessageTime.containsKey(playerUUID) || (currentTime - lastMessageTime.get(playerUUID)) >= messageCooldownTime) {
            player.sendMessage(message);
            lastMessageTime.put(playerUUID, currentTime);
        }
    }
}
