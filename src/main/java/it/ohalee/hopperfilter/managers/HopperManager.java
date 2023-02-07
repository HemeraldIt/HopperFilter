package it.ohalee.hopperfilter.managers;

import it.ohalee.hopperfilter.HopperFilter;
import it.ohalee.hopperfilter.data.HopperData;
import it.ohalee.hopperfilter.data.HopperUtils;
import it.ohalee.hopperfilter.gui.HopperGUI;
import it.ohalee.hopperfilter.gui.HopperHolder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HopperManager implements Listener {

    public final HashMap<UUID, Location> openHoppers;
    private final HopperGUI hopperGUI;
    private final HopperUtils hopperUtils;
    private final HopperFilter plugin;
    public Map<Location, HopperData> hoppers;
    Map<UUID, Location> hopperLocations;

    public HopperManager(final HopperFilter plugin) {
        this.hopperLocations = new HashMap<>();
        this.openHoppers = new HashMap<>();
        this.hopperGUI = plugin.getGuiHandler();
        this.hopperUtils = new HopperUtils(this, plugin);
        this.hoppers = plugin.hoppers;
        this.plugin = plugin;
    }

    public Map<UUID, Location> getHopperLocations() {
        return this.hopperLocations;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerClickHopper(final PlayerInteractEvent e) {
        final Block clickedBlock = e.getClickedBlock();
        final Player player = e.getPlayer();

        if (clickedBlock == null) {
            return;
        }

        if (!clickedBlock.getType().equals(Material.HOPPER)) {
            return;
        }

        if (!player.isSneaking()) {
            return;
        }

        if (!e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }

        if (this.openHoppers.containsValue(clickedBlock.getLocation())) {
            e.getPlayer().sendMessage(HopperFilter.PREFIX + "Un giocatore sta già interagendo con questo hopper.");
            return;
        }

        final Location loc = clickedBlock.getLocation();
        this.hopperLocations.put(player.getUniqueId(), loc);
        this.hopperGUI.openHopperGUI(player, this.hopperUtils.initHopperData(player, loc));
        this.openHoppers.put(player.getUniqueId(), loc);
    }

    private boolean allowedOpenHopper(final Location loc) {
        return !this.hoppers.containsKey(loc);
    }

    @EventHandler
    public void onHopperClose(final InventoryCloseEvent e) {
        if (!(e.getInventory().getHolder() instanceof HopperHolder)) {
            return;
        }

        if (this.hopperLocations.containsKey(e.getPlayer().getUniqueId())) {
            final Location invLocation = this.hopperLocations.get(e.getPlayer().getUniqueId());
            final HopperData currentData = this.hoppers.get(invLocation);
            final ItemStack[] items = new ItemStack[currentData.allowedItems];
            for (int i = 0; i < currentData.allowedItems; ++i) {
                if (e.getInventory().getItem(i) != null) {
                    items[i] = e.getInventory().getItem(i);
                }
            }
            currentData.hopperItems = items;
            this.hoppers.put(invLocation, currentData);
            this.openHoppers.remove(e.getPlayer().getUniqueId());

            //      this.plugin.saveHoppers();
        }
    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent e) {

        if (e.isCancelled()) {
            return;
        }

        Inventory destinationInventory = e.getDestination();
        if (destinationInventory.getHolder() != null && destinationInventory.getHolder() instanceof BlockState) {
            Location hopperLocation = ((BlockState) destinationInventory.getHolder()).getLocation();
            if (hoppers.containsKey(hopperLocation)) {
                e.setCancelled(hopperUtils.cancelItemTransferEvent(hopperLocation, e.getItem()));
            }
        }
    }

    @EventHandler
    public void onInventoryInteract(final InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof final Player p)) {
            return;
        }
        if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof HopperHolder && e.getSlot() >= this.hopperUtils.getAllowedItems(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryPickupItem(InventoryPickupItemEvent e) {
        if (!e.getInventory().getType().equals(InventoryType.HOPPER)) {
            return;
        }
        if (e.getInventory().getHolder() instanceof BlockState) {
            e.setCancelled(this.hopperUtils.cancelItemTransferEvent(((BlockState) e.getInventory().getHolder()).getLocation(), e.getItem().getItemStack()));
        }
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent e) {
        if (!e.getBlock().getType().equals(Material.HOPPER)) {
            return;
        }
        if (e.getPlayer().isSneaking()) {
            e.setCancelled(true);
            return;
        }

        final Location loc = e.getBlock().getLocation();

        if (this.openHoppers.containsValue(loc)) {
            if (e.getPlayer().hasPermission("hoppermanager.admin.break")) {
                this.hoppers.remove(loc);

                this.removeOpenHopper(loc);
                e.getPlayer().sendMessage(HopperFilter.PREFIX + "Rimosso dalla lista degli hopper aperti.");

                this.hopperUtils.dropItems(this.hoppers.get(loc));
                return;
            }

            e.getPlayer().sendMessage(HopperFilter.PREFIX + "Non puoi rompere un hopper che è aperto.");
            e.setCancelled(true);
            return;
        }
        if (this.hoppers.containsKey(loc)) {
            this.hopperUtils.dropItems(this.hoppers.get(loc));
            this.hoppers.remove(loc);
            this.removeOpenHopper(loc);
        }
    }

    public HopperData getHopperData(final Player p) {
        return this.hopperUtils.getHopperData(p);
    }

    public void removeOpenHopper(Location location) {
        List<UUID> toRemove = new ArrayList<>();
        for (UUID uuid : this.openHoppers.keySet()) {
            Location loc = this.openHoppers.get(uuid);
            if (loc == null || loc.equals(location)) {
                toRemove.add(uuid);
            }
        }

        for (UUID uuid : toRemove)
            this.openHoppers.remove(uuid);
    }

    public void initHoppers(final HopperData[] hopperData) {
        for (final HopperData data : hopperData) {
            if (data.location != null) {
                this.hoppers.put(data.location, data);
            }
        }
    }
}
