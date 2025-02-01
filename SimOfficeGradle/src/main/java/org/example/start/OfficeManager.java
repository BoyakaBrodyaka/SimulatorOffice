package org.example.start;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ConcurrentHashMap;

public class OfficeManager {

    private final JavaPlugin plugin;
    private final ConcurrentHashMap<Player, Long> cooldowns;
    private final long cooldownTime; // Time in milliseconds
    private final OfficeDataManager officeDataManager;

    public OfficeManager(JavaPlugin plugin, OfficeDataManager officeDataManager) {
        this.plugin = plugin;
        this.cooldowns = new ConcurrentHashMap<>();
        this.cooldownTime = 5000L; // 5 seconds cooldown
        this.officeDataManager = officeDataManager;
    }

    public void sendStoryline(Player player) {
        if (isOnCooldown(player)) {
            player.sendMessage("Подождите немного перед отправкой следующего сообщения.");
            return;
        }

        updateCooldown(player);

        player.sendMessage("Босс: Последний месяц ты плохо работаешь, тебе придется покинуть наш офис!");
        player.sendMessage("Ты: Не вы меня увольняете, я сам ухожу!");
        player.sendMessage("Босс: Досвидания.");

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.sendMessage("Ты: Я сделаю свой офис!");
            giveCreateOfficeItem(player);
        }, 60L); // 3 seconds delay
    }

    private void giveCreateOfficeItem(Player player) {
        if (isOnCooldown(player)) {
            player.sendMessage("Подождите немного перед отправкой следующего сообщения.");
            return;
        }

        updateCooldown(player);

        ItemStack createOfficeItem = new ItemStack(Material.PAPER);
        ItemMeta meta = createOfficeItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("Создать офис");
            createOfficeItem.setItemMeta(meta);
        }
        player.getInventory().setItem(4, createOfficeItem);
    }

    public void createOffice(Player player) {
        player.sendMessage("Берем кредит...");
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.sendMessage("Ищем подходящее место...");
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.sendMessage("Покупаем офис...");
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (!officeDataManager.createOfficeForPlayer(player)) {
                        player.sendMessage("Не удалось создать новый офис, попробуйте позже.");
                        return;
                    }
                    Location officeLocation = officeDataManager.getOfficeLocation(player);
                    player.teleport(getCenterLocation(officeLocation)); // Телепортация внутрь офиса
                    player.getInventory().remove(Material.PAPER); // Удаляем бумажку из инвентаря
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false)); // Эффект ночного зрения
                    player.sendMessage("Добро пожаловать в ваш новый офис!");
                }, 20L); // 1 second delay
            }, 20L); // 1 second delay
        }, 20L); // 1 second delay
    }

    public void teleportToOffice(Player player) {
        if (isOnCooldown(player)) {
            player.sendMessage("Подождите немного перед отправкой следующего сообщения.");
            return;
        }

        updateCooldown(player);

        Location officeLocation = officeDataManager.getOfficeLocation(player);
        if (officeLocation == null) {
            player.sendMessage("Не удалось найти офис, создаем новый.");
            if (!officeDataManager.createOfficeForPlayer(player)) {
                player.sendMessage("Не удалось создать новый офис, попробуйте позже.");
                return;
            }
            officeLocation = officeDataManager.getOfficeLocation(player);
        }

        if (officeLocation == null) {
            player.sendMessage("Ошибка: не удалось найти или создать офис.");
            return;
        }

        player.teleport(getCenterLocation(officeLocation));
        player.sendMessage("Добро пожаловать в ваш офис!");
    }

    private Location getCenterLocation(Location officeLocation) {
        if (officeLocation == null) {
            Bukkit.getLogger().warning("Ошибка: officeLocation равен null");
            return null;
        }
        return officeLocation.clone().add(1, 1, 1); // Убедитесь, что игрок спавнится внутри офиса
    }

    public Location getOfficeLocation(Player player) {
        return officeDataManager.getOfficeLocation(player);
    }

    public void resetOffice(Player player) {
        officeDataManager.removeOffice(player);
        player.getInventory().setItem(4, null);
        giveCreateOfficeItem(player);
        player.sendMessage("Ваш офис был удален. Создайте новый офис с помощью полученного предмета.");
    }

    private boolean isOnCooldown(Player player) {
        return cooldowns.containsKey(player) && (System.currentTimeMillis() - cooldowns.get(player)) < cooldownTime;
    }

    private void updateCooldown(Player player) {
        cooldowns.put(player, System.currentTimeMillis());
    }
}
