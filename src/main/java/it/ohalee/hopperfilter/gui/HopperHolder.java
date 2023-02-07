package it.ohalee.hopperfilter.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class HopperHolder implements InventoryHolder {

    private final Player player;

    public HopperHolder(Player player) {
        this.player = player;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return player.getInventory();
    }
}
