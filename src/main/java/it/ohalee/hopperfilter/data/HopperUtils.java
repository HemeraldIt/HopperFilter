package it.ohalee.hopperfilter.data;

import it.ohalee.hopperfilter.HopperFilter;
import it.ohalee.hopperfilter.managers.HopperManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HopperUtils {

    private final HopperFilter plugin;
    private final HopperManager hopperManager;

    public HopperUtils(final HopperManager manager, final HopperFilter plugin) {
        this.plugin = plugin;
        this.hopperManager = manager;
    }

    public Integer getAllowedItems(final Inventory inventory, final Player player) {
        final Location hopperLoc = this.hopperManager.getHopperLocations().get(player.getUniqueId());
        if (hopperLoc != null && this.hopperManager.hoppers.containsKey(hopperLoc)) {
            return this.hopperManager.hoppers.get(hopperLoc).allowedItems;
        }
        return this.plugin.getUtils().getAllowedFilterSize(player);
    }

    public HopperData getHopperData(final Player p) {
        if (this.hopperManager.getHopperLocations().containsKey(p.getUniqueId()) && this.hopperManager.hoppers.containsKey(this.hopperManager.getHopperLocations().get(p.getUniqueId()))) {
            return this.hopperManager.hoppers.get(this.hopperManager.getHopperLocations().get(p.getUniqueId()));
        }
        return null;
    }

    public HopperData initHopperData(final Player p, final Location loc) {
        if (this.hopperManager.hoppers.containsKey(loc)) {
            return this.hopperManager.hoppers.get(loc);
        }
        final int allowedItems = this.plugin.getUtils().getAllowedFilterSize(p);
        final HopperData hopperData = new HopperData(loc, null, allowedItems, false);
        this.hopperManager.hoppers.put(loc, hopperData);
        return hopperData;
    }

    public boolean cancelItemTransferEvent(final Location loc, final ItemStack item) {
        final HopperData hopperData = this.hopperManager.hoppers.get(loc);

        if (hopperData == null) {
            return false;
        }

        final ItemStack[] filterItems = hopperData.hopperItems;

        if (filterItems == null) {
            return false;
        }

        boolean cancelEvent = false;
        for (ItemStack filterItem : filterItems) {
            if (filterItem != null) {
                cancelEvent = true;
                if (filterItem.isSimilar(item) || filterItem.getType().equals(item.getType())) {
                    return hopperData.blacklistEnabled;
                }
            }
        }

        if (hopperData.blacklistEnabled) {
            return !cancelEvent;
        }

        return cancelEvent;
    }

    public void dropItems(final HopperData hopperData) {
        final Location loc = hopperData.location;
        final World world = loc.getWorld();
        if (hopperData.hopperItems == null) return;
        for (final ItemStack item : hopperData.hopperItems) {
            if (item != null) {
                world.dropItem(loc, item);
            }
        }
    }
}
