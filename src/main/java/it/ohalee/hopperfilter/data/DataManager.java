package it.ohalee.hopperfilter.data;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import it.ohalee.hopperfilter.HopperFilter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

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
        Gson gson = new GsonBuilder().registerTypeAdapter(HopperData.class, new HopperSerializer()).setPrettyPrinting().create();
        try (FileWriter fileWriter = new FileWriter(this.file, false)) {
            fileWriter.flush();
            gson.toJson(new ArrayList<>(values), fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HopperData[] getHopperData() {
        try (JsonReader reader = new JsonReader(new FileReader(this.file))) {
            JsonArray arr = new JsonArray();
            JsonElement parsedElement = JsonParser.parseReader(reader);
            if (parsedElement.isJsonArray()) {
                arr = parsedElement.getAsJsonArray();
            }
            Gson gson = new GsonBuilder().registerTypeAdapter(HopperData.class, new HopperDeserializer()).create();
            return gson.fromJson(arr, HopperData[].class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
