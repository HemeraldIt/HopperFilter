package it.ohalee.hopperfilter;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Utils {
    HopperFilter plugin;

    public Utils(final HopperFilter plugin) {
        this.plugin = plugin;
    }

    public int getInventorySize(final int configSize) {
        return 27;
    }

    public int getAllowedFilterSize(final Player player) {
        return 9;
    }

    public boolean inventoryContainsItem(final Inventory inventory, final ItemStack currentItem, final HumanEntity whoClicked) {
        if (whoClicked instanceof Player) {
            final Player player = (Player) whoClicked;
            for (int allowedItems = this.getAllowedFilterSize(player), i = 0; i < allowedItems; ++i) {
                if (inventory.getItem(i) != null) {
                    if (inventory.getItem(i).isSimilar(currentItem)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
