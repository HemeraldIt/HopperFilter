package it.ohalee.hopperfilter.data;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BukkitSerializer {
    public static String itemStackArrayToBase64(final ItemStack[] items) throws IllegalStateException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            try (BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {
                dataOutput.writeInt(items.length);
                for (ItemStack item : items) {
                    dataOutput.writeObject(item);
                }
            }
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static ItemStack[] itemStackArrayFromBase64(final String data) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data))) {
            try (BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
                ItemStack[] items = new ItemStack[dataInput.readInt()];
                for (int i = 0; i < items.length; ++i) {
                    items[i] = (ItemStack) dataInput.readObject();
                }
                return items;
            }
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
