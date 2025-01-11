package org.example.menu.shop.furniture.building.tableOne;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;
import org.example.menu.shop.furniture.building.UpgradeParameters;
import org.example.stats.PlayerStats;
import org.example.stats.PlayerStatsManager;
import org.example.stats.format.FormatNumber;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TableOneInventory implements Listener {

    private final JavaPlugin plugin;
    private final PlayerStatsManager playerStatsManager;
    private final Map<Integer, UpgradeParameters> upgradeParametersMap;
    private final Map<UUID, Long> cooldowns;
    private final Map<UUID, Integer> blockStarLevels;
    private static final long COOLDOWN_TIME = 1000;
    private static final String BLOCK_METADATA_KEY = "currentTableOneBlock";

    private File dataFile;
    private FileConfiguration dataConfig;

    public TableOneInventory(JavaPlugin plugin, PlayerStatsManager playerStatsManager) {
        this.plugin = plugin;
        this.playerStatsManager = playerStatsManager;
        this.upgradeParametersMap = new HashMap<>();
        this.cooldowns = new ConcurrentHashMap<>();
        this.blockStarLevels = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        initUpgradeParameters();
        loadBlockStarLevels();

        new BukkitRunnable() {
            @Override
            public void run() {
                showParticlesAroundBlocks();
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void initUpgradeParameters() {
        upgradeParametersMap.put(1, new UpgradeParameters(100, 1, 0, 1.0));
        upgradeParametersMap.put(2, new UpgradeParameters(200, 1, 0, 1.1));
        upgradeParametersMap.put(3, new UpgradeParameters(300, 1, 0, 1.2));
        upgradeParametersMap.put(4, new UpgradeParameters(400, 1, 0, 1.3));
        upgradeParametersMap.put(5, new UpgradeParameters(500, 1, 0, 1.5));
    }

    private Inventory createTableInventory(Player player, int currentStars) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Прокачка");

        PlayerStats playerStats = playerStatsManager.getPlayerStats(player);

        ItemStack statisticsItem = new ItemStack(Material.BOOK);
        ItemMeta statisticsMeta = statisticsItem.getItemMeta();
        statisticsMeta.setDisplayName("§6Статистика Стола");
        statisticsMeta.setLore(Arrays.asList(
                String.format("§7Буст статистики: x%.1f", getStatisticBoost(currentStars)),
                getStarsString(currentStars)
        ));
        statisticsItem.setItemMeta(statisticsMeta);
        inventory.setItem(13, statisticsItem);

        int nextStars = currentStars + 1;
        if (nextStars <= 5) {
            UpgradeParameters params = upgradeParametersMap.get(nextStars);
            ItemStack upgradeItem = new ItemStack(Material.ANVIL);
            ItemMeta upgradeMeta = upgradeItem.getItemMeta();
            upgradeMeta.setDisplayName("§6Прокачка Стола");
            upgradeMeta.setLore(Arrays.asList(
                    getStarsString(currentStars) + " -> " + getStarsString(nextStars),
                    String.format("§7Баланс: %s -> %s", FormatNumber.formatNumber(playerStats.getBalance()), FormatNumber.formatNumber(params.getRequiredBalance())),
                    String.format("§7Уровень: %d -> %d", playerStats.getLevel(), params.getRequiredLevel()),
                    String.format("§7Перерождение: %d -> %d", playerStats.getRebirth(), params.getRequiredRebirth())
            ));
            upgradeItem.setItemMeta(upgradeMeta);
            inventory.setItem(22, upgradeItem);
        } else {

            ItemStack maxLevelItem = new ItemStack(Material.BARRIER);
            ItemMeta maxLevelMeta = maxLevelItem.getItemMeta();
            maxLevelMeta.setDisplayName("§6Максимальный уровень");
            maxLevelMeta.setLore(Arrays.asList("§7Этот стол достиг максимального уровня."));
            maxLevelItem.setItemMeta(maxLevelMeta);
            inventory.setItem(22, maxLevelItem);
        }

        return inventory;
    }

    private String getStarsString(int stars) {
        StringBuilder starsString = new StringBuilder();
        for (int i = 0; i < stars; i++) {
            starsString.append("§6★ ");
        }
        for (int i = stars; i < 5; i++) {
            starsString.append("§7★ ");
        }
        return starsString.toString();
    }

    private double getStatisticBoost(int stars) {
        if (stars == 0) {
            return 1.0;
        }
        UpgradeParameters params = upgradeParametersMap.get(stars);
        return params.getStatisticBoost();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock != null && clickedBlock.getType() == Material.OAK_STAIRS) {

                player.setMetadata(BLOCK_METADATA_KEY, new FixedMetadataValue(plugin, clickedBlock));

                int currentStars = blockStarLevels.getOrDefault(player.getUniqueId(), 1);
                player.openInventory(createTableInventory(player, currentStars));
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        PlayerStats playerStats = playerStatsManager.getPlayerStats(player);

        if (event.getView().getTitle().equals("Прокачка")) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.hasItemMeta()) {
                ItemMeta meta = clickedItem.getItemMeta();
                if ("§6Прокачка Стола".equals(meta.getDisplayName())) {
                    if (isOnCooldown(player)) {
                        player.sendMessage("§cПожалуйста, подождите перед следующей покупкой.");
                        return;
                    }

                    List<MetadataValue> metadataValues = player.getMetadata(BLOCK_METADATA_KEY);
                    if (metadataValues.isEmpty()) {
                        player.sendMessage("§cНе удалось найти блок для этого стола.");
                        return;
                    }

                    Block clickedBlock = (Block) metadataValues.get(0).value();
                    int currentStars = blockStarLevels.getOrDefault(player.getUniqueId(), 1);

                    if (currentStars < 5) {
                        if (attemptUpgrade(player, playerStats, currentStars)) {
                            int newStars = currentStars + 1;
                            blockStarLevels.put(player.getUniqueId(), newStars);
                            saveBlockStarLevels();
                            player.openInventory(createTableInventory(player, newStars));
                            setCooldown(player);
                        } else {
                            player.sendMessage("§cНедостаточно средств, уровня или перерождений для прокачки.");
                        }
                    } else if (currentStars == 5) {
                        player.sendMessage("§cЭтот стол достиг максимального уровня.");
                    }
                }
            }
        }
    }

    private boolean attemptUpgrade(Player player, PlayerStats playerStats, int currentStars) {
        int nextStars = currentStars + 1;
        UpgradeParameters params = upgradeParametersMap.get(nextStars);
        if (playerStats.getBalance() >= params.getRequiredBalance() &&
                playerStats.getLevel() >= params.getRequiredLevel() &&
                playerStats.getRebirth() >= params.getRequiredRebirth()) {
            playerStats.setBalance(playerStats.getBalance() - params.getRequiredBalance());
            return true;
        }
        return false;
    }

    private boolean isOnCooldown(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (cooldowns.containsKey(playerUUID)) {
            long lastPurchaseTime = cooldowns.get(playerUUID);
            return (System.currentTimeMillis() - lastPurchaseTime) < COOLDOWN_TIME;
        }
        return false;
    }

    private void setCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    private String getBlockKey(Block block) {
        return block.getWorld().getName() + ":" + block.getX() + "," + block.getY() + "," + block.getZ();
    }

    private void saveBlockStarLevels() {
        if (dataFile == null) {
            dataFile = new File(plugin.getDataFolder(), "blockStarLevels.yml");
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);

        for (Map.Entry<UUID, Integer> entry : blockStarLevels.entrySet()) {
            dataConfig.set(entry.getKey().toString(), entry.getValue());
        }

        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBlockStarLevels() {
        if (dataFile == null) {
            dataFile = new File(plugin.getDataFolder(), "blockStarLevels.yml");
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);

        for (String key : dataConfig.getKeys(false)) {
            blockStarLevels.put(UUID.fromString(key), dataConfig.getInt(key));
        }
    }

    private void showParticlesAroundBlocks() {
        for (UUID key : blockStarLevels.keySet()) {
            String[] parts = key.toString().split(":");
            if (parts.length == 4) {
                String worldName = parts[0];
                int x = Integer.parseInt(parts[1].split(",")[0]);
                int y = Integer.parseInt(parts[1].split(",")[1]);
                int z = Integer.parseInt(parts[1].split(",")[2]);

                Block block = Bukkit.getWorld(worldName).getBlockAt(new Vector(x, y, z).toLocation(Bukkit.getWorld(worldName)));
                if (block.getType() == Material.OAK_STAIRS) {
                    block.getWorld().spawnParticle(Particle.DOLPHIN, block.getLocation().add(0.5, 1, 0.5), 10, 0.5, 0.5, 0.5, 0);
                }
            }
        }
    }
}