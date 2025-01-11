package org.example.menu.shop.furniture;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.stats.PlayerStats;
import org.example.stats.PlayerStatsManager;

import java.util.Arrays;

public class FurnitureItemAdder {
    private final PlayerStatsManager playerStatsManager;

    public FurnitureItemAdder(PlayerStatsManager playerStatsManager) {
        this.playerStatsManager = playerStatsManager;
    }

    public void addFurnitureItem(Inventory inventory, int slot, Material material, String name, String description, int stars, int balanceRequirement, int levelRequirement, int rebirthsRequirement, Player player) {
        PlayerStats playerStats = playerStatsManager.getPlayerStats(player);

        if (!meetsLevelAndRebirthRequirements(playerStats, levelRequirement, rebirthsRequirement)) {
            ItemStack barrier = new ItemStack(Material.BARRIER);
            ItemMeta barrierMeta = barrier.getItemMeta();
            assert barrierMeta != null;
            barrierMeta.setDisplayName("§cНедостаточные требования");
            barrier.setItemMeta(barrierMeta);
            inventory.setItem(slot, barrier);
            return;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;

        meta.setDisplayName(name);

        StringBuilder starsString = new StringBuilder("§e");
        for (int i = 0; i < stars; i++) {
            starsString.append("★");
        }
        for (int i = stars; i < 5; i++) {
            starsString.append("§7★");
        }

        meta.setLore(Arrays.asList(
                starsString.toString(),
                "§7Требования:",
                "§7Баланс: §e" + balanceRequirement,
                "§7Уровень: §e" + levelRequirement,
                "§7Перерождения: §e" + rebirthsRequirement,
                "§7Описание: §e" + description
        ));

        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }

    private boolean meetsLevelAndRebirthRequirements(PlayerStats playerStats, int levelRequirement, int rebirthsRequirement) {
        int playerLevel = playerStats.getLevel();
        int playerRebirths = playerStats.getRebirth();

        return playerLevel >= levelRequirement && playerRebirths >= rebirthsRequirement;
    }

    public PlayerStatsManager getPlayerStatsManager() {
        return playerStatsManager;
    }
}
