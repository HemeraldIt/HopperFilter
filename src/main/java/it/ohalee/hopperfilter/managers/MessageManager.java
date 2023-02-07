package it.ohalee.hopperfilter.managers;

import it.ohalee.hopperfilter.HopperFilter;
import org.bukkit.entity.Player;

public class MessageManager {

    public static void CannotOpenHopper(final Player player) {
        player.sendMessage(HopperFilter.PREFIX + "Non hai il permesso di modificare questo hopper");
    }

    public static void cannotBreakHopper(final Player player) {
        player.sendMessage(HopperFilter.PREFIX + "Non hai il permesso di distruggere questo hopper");
    }

    public static void onlyHopperOwnerCanDoThis(final Player player) {
        player.sendMessage(HopperFilter.PREFIX + "Solo il proprietario dell'hopper può eseguire questa azione");
    }

}
