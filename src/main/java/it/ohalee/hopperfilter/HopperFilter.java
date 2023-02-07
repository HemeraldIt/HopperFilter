package it.ohalee.hopperfilter;

import it.ohalee.hopperfilter.data.DataManager;
import it.ohalee.hopperfilter.data.HopperData;
import it.ohalee.hopperfilter.gui.HopperGUI;
import it.ohalee.hopperfilter.gui.InventoryUtils;
import it.ohalee.hopperfilter.managers.HopperManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class HopperFilter extends JavaPlugin {

    public final static String PREFIX = "§aHoppers §8» §7";
    public DataManager dataManager;
    public Map<Location, HopperData> hoppers = new HashMap<>();
    private HopperGUI guiHandler;
    private HopperManager hopperManager;
    private Utils utils;
    private InventoryUtils inventoryUtils;

    @Override
    public void onEnable() {
        this.utils = new Utils(this);
        this.inventoryUtils = new InventoryUtils();
        this.guiHandler = new HopperGUI(this);
        this.dataManager = new DataManager(this);
        this.hopperManager = new HopperManager(this);
        this.hopperManager.initHoppers(this.dataManager.getHopperData());

        Bukkit.getPluginManager().registerEvents(this.hopperManager, this);
        Bukkit.getPluginManager().registerEvents(this.guiHandler, this);

        getCommand("hopperremove").setExecutor(this);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, this::saveHoppers, 5 * 20, 5 * 60 * 20);
    }

    @Override
    public void onDisable() {
        saveHoppers();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("hopperfilter.remove")) {
                Block block = player.getTargetBlock(null, 3);
                getHopperManager().openHoppers.remove(block.getLocation());
            }
            return true;
        }
        return false;
    }

    public HopperGUI getGuiHandler() {
        return this.guiHandler;
    }

    public HopperManager getHopperManager() {
        return this.hopperManager;
    }

    public Utils getUtils() {
        return this.utils;
    }

    public InventoryUtils getInventoryUtils() {
        return this.inventoryUtils;
    }

    public void saveHoppers() {
        this.dataManager.saveHoppers(this.hoppers.values());
    }

}
