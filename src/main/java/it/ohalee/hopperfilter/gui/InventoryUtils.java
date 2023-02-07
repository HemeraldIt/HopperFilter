package it.ohalee.hopperfilter.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryUtils {
    private final ItemStack forwardArrow;
    private final ItemStack backArrow;
    private final ItemStack noPageLeft;
    private final ItemStack itemNotAvailable;
    ItemStack glass;
    Map<String, ItemStack> skulls;
    private ItemStack whitelistButton;
    private ItemStack blacklistButton;

    public InventoryUtils() {
        this.skulls = new HashMap<>();

        this.glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        final ItemMeta meta = this.glass.getItemMeta();
        meta.setDisplayName("§7§oplay.hemerald.net");
        this.glass.setItemMeta(meta);

        this.forwardArrow = new ItemStack(Material.SPECTRAL_ARROW);
        final ItemMeta forwardArrowItemMeta = this.forwardArrow.getItemMeta();
        forwardArrowItemMeta.setDisplayName(ChatColor.YELLOW + "Pagina successiva");
        this.forwardArrow.setItemMeta(forwardArrowItemMeta);

        this.backArrow = new ItemStack(Material.SPECTRAL_ARROW);
        final ItemMeta backArrowItemMeta = this.backArrow.getItemMeta();
        backArrowItemMeta.setDisplayName(ChatColor.YELLOW + "Pagina precedente");
        this.backArrow.setItemMeta(backArrowItemMeta);

        this.noPageLeft = new ItemStack(Material.BARRIER);
        final ItemMeta noPageLeftMeta = this.noPageLeft.getItemMeta();
        noPageLeftMeta.setDisplayName(ChatColor.RED + "Nessuna pagina rimanente!");
        this.noPageLeft.setItemMeta(noPageLeftMeta);

        this.itemNotAvailable = new ItemStack(Material.TNT_MINECART);
        final ItemMeta itemMeta = this.itemNotAvailable.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Slot non disponibile!");
        this.itemNotAvailable.setItemMeta(itemMeta);

        this.setupWhitelistButtons();
    }

    private void setupWhitelistButtons() {
        this.whitelistButton = new ItemStack(Material.QUARTZ);
        final ItemMeta whitelistMeta = this.whitelistButton.getItemMeta();
        whitelistMeta.setDisplayName("§e§lWhitelist");
        final List<String> whitelistLore = new ArrayList<>();
        whitelistLore.add(ChatColor.GRAY + "Solo gli elementi presenti nel");
        whitelistLore.add(ChatColor.GRAY + "filtro possono passare.");
        whitelistMeta.setLore(whitelistLore);
        this.whitelistButton.setItemMeta(whitelistMeta);

        this.blacklistButton = new ItemStack(Material.GILDED_BLACKSTONE);
        final ItemMeta blacklistMeta = this.blacklistButton.getItemMeta();
        final List<String> blacklistLore = new ArrayList<>();
        blacklistLore.add(ChatColor.GRAY + "Tutti gli elementi che non sono");
        blacklistLore.add(ChatColor.GRAY + "presenti nel filtro passeranno.");
        blacklistMeta.setLore(blacklistLore);
        blacklistMeta.setDisplayName("§e§lBlacklist");
        this.blacklistButton.setItemMeta(blacklistMeta);
    }

    public ItemStack getBlackGlass() {
        return this.glass;
    }

    public ItemStack getPlayerHead(final OfflinePlayer player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        final SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(player);
        meta.setDisplayName(ChatColor.GREEN + player.getName());
        head.setItemMeta(meta);
        return head;
    }

    public ItemStack getItemNotAvailable() {
        return this.itemNotAvailable;
    }

    public ItemStack getForwardArrow() {
        return this.forwardArrow;
    }

    public ItemStack getBackwardArrow() {
        return this.backArrow;
    }

    public ItemStack getNoPageLeft() {
        return this.noPageLeft;
    }

    public ItemStack getBlackListButton(final boolean blacklistEnabled) {
        return blacklistEnabled ? this.blacklistButton : this.whitelistButton;
    }
}
