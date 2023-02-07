package it.ohalee.hopperfilter.data;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class HopperData {
    public ItemStack[] hopperItems;
    public Location location;
    public int allowedItems;
    public boolean blacklistEnabled;

    public HopperData(Location hopperLocation, ItemStack[] items, Integer allowedItems, boolean blacklistEnabled) {
        this.hopperItems = items;
        this.location = hopperLocation;
        this.allowedItems = allowedItems;
        this.blacklistEnabled = blacklistEnabled;
    }

}
