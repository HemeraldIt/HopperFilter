package it.ohalee.hopperfilter.data;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class HopperData {
    public ItemStack[] hopperItems;
    public Location location;
    public int allowedItems;
    public boolean blacklistEnabled;

    public HopperData(final Location hopperLocation, final ItemStack[] items,
                      final Integer allowedItems, final boolean blacklistEnabled) {
        this.hopperItems = items;
        this.location = hopperLocation;
        this.allowedItems = allowedItems;
        this.blacklistEnabled = blacklistEnabled;
    }

}
