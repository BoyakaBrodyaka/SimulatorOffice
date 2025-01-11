package org.example.start;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

public class OfficeDataManager {

    private final JavaPlugin plugin;
    private final Map<UUID, Location> officeLocations;
    private final Random random;

    public OfficeDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.officeLocations = new HashMap<>();
        this.random = new Random();
    }

    public Location getOfficeLocation(Player player) {
        return officeLocations.get(player.getUniqueId());
    }

    public void setOfficeLocation(Player player, Location location) {
        officeLocations.put(player.getUniqueId(), location);
    }

    public Map<UUID, Location> getAllOfficeLocations() {
        return officeLocations;
    }

    public boolean createOfficeForPlayer(Player player) {
        org.bukkit.World bukkitWorld = Bukkit.getWorld("PrisonServer");
        if (bukkitWorld == null) {
            player.sendMessage("Мир PrisonServer не найден!");
            return false;
        }

        int x, y, z;
        Location officeLocation = null;
        int attempts = 0;

        do {
            x = random.nextInt(10000) - 5000;
            z = random.nextInt(10000) - 5000;
            y = getHighestBlockYAt(bukkitWorld, x, z) + 15;

            officeLocation = new Location(bukkitWorld, x, y, z);

            attempts++;
            if (attempts > 100) {
                player.sendMessage("Не удалось найти подходящее место для офиса.");
                return false;
            }
        } while (!isLocationSuitable(officeLocation));

        try {
            World world = BukkitAdapter.adapt(bukkitWorld);
            pasteStructure(BlockVector3.at(x, y, z), world);
            // Обновляем координаты спавна игрока внутри офиса
            Location spawnLocation = new Location(bukkitWorld, x + 2, y + 1, z + 2);
            setOfficeLocation(player, spawnLocation);
            player.teleport(spawnLocation); // Телепортируем игрока внутрь офиса
            player.sendMessage("Ваш офис был создан в координатах: " + x + ", " + y + ", " + z);
        } catch (IOException e) {
            player.sendMessage("Произошла ошибка при создании офиса.");
            plugin.getLogger().log(Level.SEVERE, "Error creating office", e);
            return false;
        }

        return true;
    }

    private void pasteStructure(BlockVector3 location, World world) throws IOException {
        File schematicFile = new File(plugin.getDataFolder(), "officeOne.schem");
        if (!schematicFile.exists()) {
            throw new IOException("Schematic file not found: " + schematicFile.getAbsolutePath());
        }

        com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat format = com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats.findByFile(schematicFile);
        if (format == null) {
            throw new IOException("Unsupported schematic format for file: " + schematicFile.getAbsolutePath());
        }

        try (ClipboardReader reader = format.getReader(new FileInputStream(schematicFile))) {
            Clipboard clipboard = reader.read();
            ClipboardHolder holder = new ClipboardHolder(clipboard);

            try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
                Operation operation = holder.createPaste(editSession)
                        .to(location)
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            } catch (WorldEditException e) {
                throw new RuntimeException("Error completing WorldEdit operation", e);
            }
        }
    }

    public void removeOffice(Player player) {
        Location officeLocation = officeLocations.remove(player.getUniqueId());
        if (officeLocation != null) {
            World world = BukkitAdapter.adapt(officeLocation.getWorld());
            BlockVector3 pt1 = BlockVector3.at(officeLocation.getBlockX(), officeLocation.getBlockY(), officeLocation.getBlockZ());

            BlockVector3 pt2 = pt1.add(20, 7, 20);

            try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
                CuboidRegion region = new CuboidRegion(pt1, pt2);
                editSession.setBlocks(region, BlockTypes.AIR.getDefaultState());
                Operations.complete(editSession.commit());
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error removing office", e);
            }
        }
    }

    private boolean isLocationSuitable(Location location) {
        for (Location existingLocation : officeLocations.values()) {
            if (existingLocation.distance(location) < 20) {
                return false;
            }
        }
        return true;
    }

    private int getHighestBlockYAt(org.bukkit.World world, int x, int z) {
        for (int y = world.getMaxHeight(); y > 0; y--) {
            if (!world.getBlockAt(x, y, z).getType().isAir()) {
                return y;
            }
        }
        return world.getMinHeight();
    }
}
