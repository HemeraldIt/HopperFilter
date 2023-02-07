package it.ohalee.hopperfilter.data;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.lang.reflect.Type;

public class HopperDeserializer implements JsonDeserializer<HopperData> {
    public HopperData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        JsonObject locObj = obj.getAsJsonObject("location");
        int x = locObj.get("x").getAsInt();
        int y = locObj.get("y").getAsInt();
        int z = locObj.get("z").getAsInt();
        String world = locObj.get("world").getAsString();
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
        return new HopperData(new Location(Bukkit.getWorld(world), x, y, z), items, obj.get("allowedItems").getAsInt(), obj.get("blacklistEnabled").getAsBoolean());
    }
}
