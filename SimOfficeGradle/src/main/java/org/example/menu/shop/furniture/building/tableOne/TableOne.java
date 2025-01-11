package org.example.menu.shop.furniture.building.tableOne;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TableOne implements Listener {
    private Block lastTargetBlock = null;
    private HashMap<UUID, BukkitRunnable> highlightTasks = new HashMap<>();
    private HashMap<UUID, Long> cooldowns = new HashMap<>();
    private HashSet<UUID> buildingPlayers = new HashSet<>();
    private static final int COOLDOWN_TIME = 2000;

    private ProtocolManager protocolManager;

    public TableOne(JavaPlugin plugin) {
        protocolManager = ProtocolLibrary.getProtocolManager();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void setBlockMetadata(Block block) {
        block.setMetadata("table", new FixedMetadataValue(JavaPlugin.getProvidingPlugin(getClass()), "§6Стол (1)"));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        Block targetBlock = player.getTargetBlockExact(5);
        if (targetBlock == null || targetBlock.getType() == Material.AIR || targetBlock.equals(lastTargetBlock)) {
            return;
        }
        lastTargetBlock = targetBlock;

        if (highlightTasks.containsKey(playerUUID)) {
            highlightTasks.get(playerUUID).cancel();
            highlightTasks.remove(playerUUID);
        }

        if (player.getInventory().getItemInMainHand().getType() == Material.OAK_PLANKS &&
                "§6Стол (1)".equals(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName())) {

            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    generateParticleOutline(targetBlock, player);
                }
            };
            task.runTaskTimer(JavaPlugin.getProvidingPlugin(getClass()), 0L, 20L);
            highlightTasks.put(playerUUID, task);
        } else if (isBuiltTable(targetBlock)) {

            Block blockBelow = targetBlock.getRelative(org.bukkit.block.BlockFace.DOWN);
            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    generateParticleOutline(blockBelow, player);
                }
            };
            task.runTaskTimer(JavaPlugin.getProvidingPlugin(getClass()), 0L, 20L);
            highlightTasks.put(playerUUID, task);
        } else {
            if (highlightTasks.containsKey(playerUUID)) {
                highlightTasks.get(playerUUID).cancel();
                highlightTasks.remove(playerUUID);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        if (cooldowns.containsKey(playerUUID) && currentTime - cooldowns.get(playerUUID) < COOLDOWN_TIME) {
            return;
        }

        if (buildingPlayers.contains(playerUUID)) {
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() == Material.OAK_PLANKS &&
                "§6Стол (1)".equals(itemInHand.getItemMeta().getDisplayName()) &&
                event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            Block targetBlock = event.getClickedBlock();
            if (targetBlock == null || targetBlock.getType() == Material.AIR) {
                player.sendMessage(ChatColor.RED + "Нет целевого блока для постройки.");
                return;
            }

            org.bukkit.block.BlockFace playerFacing = player.getFacing();
            boolean blockExistsNearby = false;

            for (int step = 0; step < 3; step++) {
                Block blockToPlace = targetBlock.getRelative(step * playerFacing.getModX(), 1, step * playerFacing.getModZ());
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        Block adjacentBlock = blockToPlace.getRelative(x, 0, z);
                        if ((x != 0 || z != 0) && adjacentBlock.getType() != Material.AIR) {
                            blockExistsNearby = true;
                            break;
                        }
                    }
                    if (blockExistsNearby) break;
                }
                if (blockExistsNearby) break;
            }

            if (blockExistsNearby) {
                player.sendMessage(ChatColor.RED + "Постройка невозможна: блок уже существует рядом.");
                return;
            }

            buildingPlayers.add(playerUUID);
            player.getInventory().setItemInMainHand(null);

            if (highlightTasks.containsKey(playerUUID)) {
                highlightTasks.get(playerUUID).cancel();
                highlightTasks.remove(playerUUID);
            }

            new BukkitRunnable() {
                int step = 0;

                @Override
                public void run() {
                    if (step >= 3) {
                        buildingPlayers.remove(playerUUID);
                        cancel();
                        return;
                    }

                    BlockData blockData;
                    Block block = targetBlock.getRelative(step * playerFacing.getModX(), 1, step * playerFacing.getModZ());

                    if (step == 0) {
                        blockData = Bukkit.createBlockData(Material.OAK_STAIRS);
                        Stairs stairsData = (Stairs) blockData;
                        stairsData.setFacing(playerFacing.getOppositeFace());
                        stairsData.setHalf(Stairs.Half.TOP);
                        block.setBlockData(stairsData);

                        setBlockMetadata(block);
                    } else if (step == 1) {
                        blockData = Bukkit.createBlockData(Material.OAK_SLAB);
                        Slab slabData = (Slab) blockData;
                        slabData.setType(Slab.Type.TOP);
                        block.setBlockData(slabData);
                    } else if (step == 2) {
                        blockData = Bukkit.createBlockData(Material.OAK_STAIRS);
                        Stairs stairsData = (Stairs) blockData;
                        stairsData.setFacing(playerFacing);
                        stairsData.setHalf(Stairs.Half.TOP);
                        block.setBlockData(stairsData);

                        setBlockMetadata(block);
                    }

                    step++;
                }
            }.runTaskTimer(JavaPlugin.getProvidingPlugin(getClass()), 0L, 20L);
        }
        buildingPlayers.remove(playerUUID);
    }

    private boolean isBuiltTable(Block block) {
        if (block.getType() == Material.OAK_STAIRS || block.getType() == Material.OAK_SLAB) {
            if (block.hasMetadata("table")) {
                List<MetadataValue> metadataValues = block.getMetadata("table");
                for (MetadataValue metadata : metadataValues) {
                    if ("§6Стол (1)".equals(metadata.asString())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (highlightTasks.containsKey(playerUUID)) {
            highlightTasks.get(playerUUID).cancel();
            highlightTasks.remove(playerUUID);
        }
    }

    private void generateParticleOutline(Block block, Player player) {
        World world = block.getWorld();
        double x = block.getX();
        double y = block.getY() + 1;
        double z = block.getZ();
        Location corner1 = new Location(world, x, y, z);
        Location corner2;

        org.bukkit.block.BlockFace playerFacing = player.getFacing();
        int length = 3;
        int width = 1;

        switch (playerFacing) {
            case NORTH:
                corner1 = new Location(world, x, y, z + 1);
                corner2 = new Location(world, x + width, y + 1, z - length + 1);
                break;
            case SOUTH:
                corner2 = new Location(world, x + width, y + 1, z + length);
                break;
            case WEST:
                corner1 = new Location(world, x + 1, y, z);
                corner2 = new Location(world, x - length + 1, y + 1, z + width);
                break;
            case EAST:
                corner2 = new Location(world, x + length, y + 1, z + width);
                break;
            default:
                corner2 = new Location(world, x + length, y + 1, z + width);
                break;
        }

        List<Location> outlineLocations = getRectangleOutline(corner1, corner2, 0.1);

        for (Location location : outlineLocations) {
            world.spawnParticle(Particle.DOLPHIN, location.getX(), location.getY(), location.getZ(), 1);
        }
    }

    private List<Location> getRectangleOutline(Location corner1, Location corner2, double particleDistance) {
        List<Location> result = new ArrayList<>();
        World world = corner1.getWorld();
        double minX = Math.min(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        for (double x = minX; x <= maxX; x += particleDistance) {
            result.add(new Location(world, x, maxY, minZ));
            result.add(new Location(world, x, maxY, maxZ));
        }
        for (double z = minZ; z <= maxZ; z += particleDistance) {
            result.add(new Location(world, minX, maxY, z));
            result.add(new Location(world, maxX, maxY, z));
        }

        for (double x = minX; x <= maxX; x += particleDistance) {
            result.add(new Location(world, x, minY, minZ));
            result.add(new Location(world, x, minY, maxZ));
        }
        for (double z = minZ; z <= maxZ; z += particleDistance) {
            result.add(new Location(world, minX, minY, z));
            result.add(new Location(world, maxX, minY, z));
        }

        for (double y = minY; y <= maxY; y += particleDistance) {
            result.add(new Location(world, minX, y, minZ));
            result.add(new Location(world, minX, y, maxZ));
            result.add(new Location(world, maxX, y, minZ));
            result.add(new Location(world, maxX, y, maxZ));
        }

        return result;
    }
}
