package it.ohalee.hopperfilter.data;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.lang.reflect.Type;

public class HopperDeserializer implements JsonDeserializer<HopperData> {
    public HopperData deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject obj = json.getAsJsonObject();
        final JsonObject locObj = obj.getAsJsonObject("location");
        final int x = locObj.get("x").getAsInt();
        final int y = locObj.get("y").getAsInt();
        final int z = locObj.get("z").getAsInt();
        final String world = locObj.get("world").getAsString();
        ItemStack[] items = null;
        try {

            if (obj.get("items") == null) {
                Bukkit.getLogger().warning("HOPPER NULL: " + x + " | " + y + " | " + z + " | " + world);
                items = new ItemStack[0];
            } else {
                items = BukkitSerializer.itemStackArrayFromBase64(obj.get("items").getAsString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Integer allowedItems = obj.get("allowedItems").getAsInt();
        final boolean blacklistEnabled = obj.get("blacklistEnabled").getAsBoolean();
        return new HopperData(new Location(Bukkit.getWorld(world), x, y, z),
                items, allowedItems,
                blacklistEnabled);
    }
}
