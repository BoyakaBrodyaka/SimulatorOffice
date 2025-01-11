package org.example.menu.shop.equipment;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.example.stats.PlayerStatsManager;

public class EquipmentManager {

    private EquipmentInventory equipmentInventory;
    private EquipmentItemAdder equipmentItemAdder;

    public EquipmentManager(EquipmentInventory equipmentInventory, PlayerStatsManager playerStatsManager) {
        this.equipmentInventory = equipmentInventory;
        this.equipmentItemAdder = new EquipmentItemAdder(playerStatsManager);
    }

    public void addSampleEquipment(Player player) {
        Inventory equipmentMenu = Bukkit.createInventory(null, 54, equipmentInventory.getEquipmentInventoryName());

        equipmentItemAdder.addEquipmentItem(equipmentMenu, 10, Material.DAMAGED_ANVIL, "§6Компьютер (1)", "Старый разобранный б/у компьютер", 1, 10, 1, 0, player);
        equipmentItemAdder.addEquipmentItem(equipmentMenu, 11, Material.CHIPPED_ANVIL, "§6Компьютер (2)", "Старый наполовину разобранный б/у компьютер", 1, 30, 1, 0, player);

        equipmentInventory.openEquipmentInventory(player, equipmentMenu);
    }
}
