package it.ohalee.hopperfilter.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;

public class HopperSerializer implements JsonSerializer<HopperData> {
    public JsonElement serialize(final HopperData src, final Type typeOfSrc, final JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.add("location", new JsonObject());
        obj.addProperty("allowedItems", src.allowedItems);
        JsonObject location = (JsonObject) obj.get("location");
        Location loc = src.location;
        if (loc != null) {
            World world = src.location.getWorld();
            if (world != null) {
                location.addProperty("world", world.getName());
            } else {
                location.addProperty("world", "islands");
                System.out.println("WORLD IS NULL (NOT LOADED?): source: " + src + ", loaded: " + src.location.isWorldLoaded());
            }
            location.addProperty("x", src.location.getBlockX());
            location.addProperty("y", src.location.getBlockY());
            location.addProperty("z", src.location.getBlockZ());
        } else {
            System.out.println("LOCATION IS NULL: " + src);
        }
        obj.addProperty("blacklistEnabled", src.blacklistEnabled);
        if (src.hopperItems != null) {
            obj.addProperty("items", BukkitSerializer.itemStackArrayToBase64(src.hopperItems));
        }
        return obj;
    }
}
