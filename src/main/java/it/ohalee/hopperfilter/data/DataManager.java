package it.ohalee.hopperfilter.data;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import it.ohalee.hopperfilter.HopperFilter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataManager {

    private final HopperFilter plugin;
    private final File file;

    public DataManager(HopperFilter plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder().getPath() + File.separator + "hoppers.json");
        if (!this.file.exists()) {
            plugin.saveResource(this.file.getName(), false);
        }
    }

    public void saveHoppers(final Collection<HopperData> values) {

      //  for (Player player : Bukkit.getOnlinePlayers()) {
      //      player.closeInventory();
      //  }

        final Gson gson = new GsonBuilder().registerTypeAdapter(HopperData.class, new HopperSerializer()).setPrettyPrinting().create();
        final List<HopperData> data = new ArrayList<>(values);
        data.toArray();
        try {
            try (FileWriter fileWriter = new FileWriter(this.file, false)) {
                fileWriter.flush();
                gson.toJson(data, fileWriter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public HopperData[] getHopperData() {
        try {
            Gson gson;
            JsonArray arr = new JsonArray();
            final JsonReader reader = new JsonReader(new FileReader(this.file));
            final JsonParser parser = new JsonParser();
            final JsonElement parsedElement = parser.parse(reader);
            if (parsedElement.isJsonArray()) {
                arr = parsedElement.getAsJsonArray();
            }
            gson = new GsonBuilder().registerTypeAdapter(HopperData.class, new HopperDeserializer()).create();
            reader.close();
            return gson.fromJson(arr, HopperData[].class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
