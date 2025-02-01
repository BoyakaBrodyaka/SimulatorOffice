package org.example;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.design.ChatMessageListener;
import org.example.menu.MenuItemHandler;
import org.example.menu.MenuListener;
import org.example.menu.rebirth.UpgradesMenu;
import org.example.menu.settings.FlightSetting;
import org.example.menu.settings.FlightSpeedSetting;
import org.example.menu.settings.SettingsMenu;
import org.example.menu.shop.ShopMenu;
import org.example.menu.shop.equipment.EquipmentInventory;
import org.example.menu.shop.furniture.FurnitureInventory;
import org.example.menu.shop.furniture.FurnitureManager;
import org.example.menu.shop.furniture.building.chairOne.ChairOne;
import org.example.menu.shop.furniture.building.chairOne.ChairOneInventory;
import org.example.menu.shop.furniture.building.tableOne.TableOne;
import org.example.menu.shop.office.OfficeInventory;
import org.example.start.*;
import org.example.stats.IncomeManager;
import org.example.stats.PlayerStats;
import org.example.stats.PlayerStatsManager;
import org.example.stats.commands.ResetStatsCommand;
import org.example.stats.commands.SetStatCommand;
import org.example.stats.commands.SetStatusCommand;
import org.example.stats.scoreboard.ScoreboardManager;

import java.util.UUID;
import java.util.logging.Level;

public class MainClass extends JavaPlugin implements Listener {
    private PlayerStats playerStats;
    private ScoreboardManager scoreboardManager;
    private static final String REALM_NAME = "PrisonServer";
    private IncomeManager incomeManager;
    private PlayerStatsManager playerStatsManager;
    private ArenaManager arenaManager;
    private OfficeManager officeManager;
    private OfficeDataManager officeDataManager;
    private SettingsMenu settingsMenu;
    private ShopMenu shopMenu;
    private FurnitureInventory furnitureInventory;
    private UpgradesMenu upgradesMenu;

    @Override
    public void onEnable() {
        // Подключение к MongoDB

            playerStatsManager = new PlayerStatsManager(this);
            incomeManager = new IncomeManager(playerStatsManager);
            officeDataManager = new OfficeDataManager(this);
            officeManager = new OfficeManager(this, officeDataManager);
            arenaManager = new ArenaManager(this, officeDataManager);
            shopMenu = new ShopMenu(playerStatsManager);
            furnitureInventory = new FurnitureInventory(shopMenu, new FurnitureManager(furnitureInventory, playerStatsManager));
            upgradesMenu = new UpgradesMenu();

            settingsMenu = new SettingsMenu();
            FlightSetting flightSetting = new FlightSetting(settingsMenu);
            FlightSpeedSetting flightSpeedSetting = new FlightSpeedSetting(settingsMenu);
            settingsMenu.setFlightSetting(flightSetting);
            settingsMenu.setFlightSpeedSetting(flightSpeedSetting);

            incomeManager.startIncomeTask(this);

            scoreboardManager = new ScoreboardManager(playerStatsManager, REALM_NAME);
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player player : getServer().getOnlinePlayers()) {
                        UUID uuid = player.getUniqueId();
                        scoreboardManager.updateScoreboard(player);
                        PlayerStats playerStats = playerStatsManager.getPlayerStats(player);
                        if (playerStats != null) {
                            playerStats.updateTabName();
                        }
                    }
                }
            }.runTaskTimer(this, 0L, 20L);

            getServer().getPluginCommand("setstat").setExecutor(new SetStatCommand(playerStatsManager));
            getServer().getPluginCommand("setstatus").setExecutor(new SetStatusCommand(playerStatsManager));
            getServer().getPluginCommand("resetstats").setExecutor(new ResetStatsCommand(getDataFolder(), playerStatsManager));
            getServer().getPluginCommand("home").setExecutor(new HomeCommand(officeManager));
            getServer().getPluginCommand("resetoffice").setExecutor(new ResetOfficeCommand(officeManager));

            getServer().getPluginManager().registerEvents(settingsMenu, this);
            getServer().getPluginManager().registerEvents(new TableOne(this), this);
            getServer().getPluginManager().registerEvents(new ChairOne(this), this);
            getServer().getPluginManager().registerEvents(new ChairOneInventory(this, playerStatsManager), this);
            Bukkit.getPluginManager().registerEvents(new MenuListener(settingsMenu, officeManager, playerStatsManager, this), this);
            Bukkit.getPluginManager().registerEvents(new MenuItemHandler(), this);
            getServer().getPluginManager().registerEvents(playerStatsManager, this);
            getServer().getPluginManager().registerEvents(new PlayerJoinListener(officeManager, arenaManager, playerStatsManager), this);
            getServer().getPluginManager().registerEvents(new CreateOfficeListener(officeManager), this);
            getServer().getPluginManager().registerEvents(new OfficeInventory(shopMenu), this);
            getServer().getPluginManager().registerEvents(new FurnitureInventory(shopMenu, new FurnitureManager(furnitureInventory, playerStatsManager)), this);
            getServer().getPluginManager().registerEvents(new EquipmentInventory(shopMenu), this);
            getServer().getPluginManager().registerEvents(new ChatMessageListener(playerStatsManager), this);
            getServer().getPluginManager().registerEvents(new HungerSpeedListener(), this);

            for (Player player : Bukkit.getOnlinePlayers()) {
                playerStatsManager.loadPlayerData(player);
            }
        }

    @Override
    public void onDisable() {
        if (playerStatsManager != null) {
            playerStatsManager.saveAllPlayerData();
        }
        if (playerStats != null) {
            playerStats.save();
        }
    }

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public OfficeManager getOfficeManager() {
        return officeManager;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public OfficeDataManager getOfficeDataManager() {
        return officeDataManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MenuItemHandler.giveMenuItem(player);
    }
}