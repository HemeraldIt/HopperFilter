package it.ohalee.hopperfilter.gui;

import it.ohalee.hopperfilter.HopperFilter;
import it.ohalee.hopperfilter.data.HopperData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HopperGUI implements Listener {
    private final HopperFilter plugin;

    public HopperGUI(final HopperFilter plugin) {
        this.plugin = plugin;
    }

    public void openHopperGUI(Player player, HopperData hopperData) {
        Inventory inventory = Bukkit.createInventory(new HopperHolder(player), 27, Component.text("§8Filtro Hopper"));
        if (hopperData.hopperItems != null) inventory.setContents(hopperData.hopperItems);
        this.initHotbar(inventory, hopperData.allowedItems, hopperData);
        player.openInventory(inventory);
    }

    private void initHotbar(final Inventory inventory, final int allowedSlots, final HopperData hopperData) {

        for (int i = 0; i < inventory.getSize(); ++i) {
            if (i >= allowedSlots) {
                if (i < inventory.getSize() - 18) {
                    inventory.setItem(i, this.plugin.getInventoryUtils().getItemNotAvailable());
                } else {
                    inventory.setItem(i, this.plugin.getInventoryUtils().getBlackGlass());
                }
            }
        }

        ItemStack blackWhiteList = this.plugin.getInventoryUtils().getBlackListButton(hopperData.blacklistEnabled);
        inventory.setItem(23, blackWhiteList);

        ItemStack close = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = close.getItemMeta();
        itemMeta.setDisplayName("§7Chiudi");
        close.setItemMeta(itemMeta);

        inventory.setItem(22, close);

    }

    @EventHandler
    public void onInventoryInteract(final InventoryClickEvent e) {
        final ItemStack clickedItem = e.getCurrentItem();

        if (e.getView().getTitle().equalsIgnoreCase("§8Filtro Hopper")) {

            final Player player = (Player) e.getWhoClicked();
            final HopperData data = this.plugin.getHopperManager().getHopperData(player);

            if (clickedItem == null) {
                return;
            }

            final int slot = 23;

            String name = clickedItem.getItemMeta().getDisplayName();

            if (e.getSlot() == slot && (name.equalsIgnoreCase("§e§lBlacklist") ||
                    name.equalsIgnoreCase("§e§lWhitelist"))) {
                e.setCancelled(true);
                data.blacklistEnabled = !data.blacklistEnabled;
                e.getInventory().setItem(slot, this.plugin.getInventoryUtils().getBlackListButton(data.blacklistEnabled));
            } else if (e.getSlot() == slot - 1 && name.equalsIgnoreCase("§7Chiudi")) {
                e.setCancelled(true);
                player.closeInventory();
            }

        }

    }

}
