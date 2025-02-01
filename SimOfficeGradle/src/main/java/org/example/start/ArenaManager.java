package org.example.start;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ArenaManager {

    private final JavaPlugin plugin;
    private final OfficeDataManager officeDataManager;
    private final Random random;

    public ArenaManager(JavaPlugin plugin, OfficeDataManager officeDataManager) {
        this.plugin = plugin;
        this.officeDataManager = officeDataManager;
        this.random = new Random();
    }

    public boolean createArenaForPlayer(Player player) {
        World world = Bukkit.getWorld("PrisonServer");
        if (world == null) {
            player.sendMessage("Мир не найден!");
            return false;
        }

        Location arenaLocation;
        int attempts = 0;
        do {

            int x = random.nextInt(10000) - 5000;
            int z = random.nextInt(10000) - 5000;
            int y = world.getHighestBlockYAt(x, z) + 15;

            arenaLocation = new Location(world, x, y, z);

            attempts++;
            if (attempts > 100) {
                player.sendMessage("Не удалось найти подходящее место для арены.");
                return false;
            }
        } while (!isLocationSuitable(arenaLocation));

        buildArena(arenaLocation);
        officeDataManager.setOfficeLocation(player, arenaLocation);
        player.sendMessage("Ваша арена была создана в координатах: " + arenaLocation.getBlockX() + ", " + arenaLocation.getBlockY() + ", " + arenaLocation.getBlockZ());
        return true;
    }

    private boolean isLocationSuitable(Location location) {
        for (Map.Entry<String, Location> entry : officeDataManager.getAllOfficeLocations().entrySet()) {
            if (entry.getValue().distance(location) < 20) {
                return false;
            }
        }
        return true;
    }


    private void buildArena(Location location) {
        World world = location.getWorld();
        if (world == null) return;

        int size = 20;

        for (int x = -size; x <= size; x++) {
            for (int z = -size; z <= size; z++) {
                world.getBlockAt(location.clone().add(x, 0, z)).setType(Material.STONE);
                world.getBlockAt(location.clone().add(x, 1, z)).setType(Material.AIR);
                world.getBlockAt(location.clone().add(x, 2, z)).setType(Material.AIR);
            }
        }
    }
}
