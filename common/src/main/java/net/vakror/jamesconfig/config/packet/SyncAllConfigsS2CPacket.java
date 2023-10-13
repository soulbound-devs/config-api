package net.vakror.jamesconfig.config.packet;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.Config;
import net.vakror.jamesconfig.config.config.object.ConfigObject;

import java.util.*;

public class SyncAllConfigsS2CPacket {
    private final Multimap<ResourceLocation, ConfigObject> configs;

    public SyncAllConfigsS2CPacket(Multimap<ResourceLocation, ConfigObject> configs) {
        this.configs = configs;
    }

    public SyncAllConfigsS2CPacket(FriendlyByteBuf buf) {
        this.configs = Multimaps.newMultimap(new HashMap<>(), ArrayList::new);
        byte[] data = buf.readByteArray();
        JsonArray object = (JsonArray) JsonParser.parseString(new String(data));
        for (JsonElement element : object) {
            JsonObject object1 = (JsonObject) element;
            Config config = JamesConfigMod.CONFIGS.get(new ResourceLocation(object1.get("configName").getAsString()));
            if (config != null) {
                List<ConfigObject> parsed = config.parse((JsonObject) object1.get("object"));
                if (parsed != null) {
                    for (ConfigObject configObject : parsed) {
                        configs.put(config.getName(), configObject);
                    }
                }
            }
        }
    }

    public void encode(FriendlyByteBuf buf) {
        JsonArray object = new JsonArray();
        configs.keySet().forEach(((location) -> serializeObject(location, object)));
        buf.writeByteArray(object.toString().getBytes());
    }

    public void serializeObject(ResourceLocation location, JsonArray array) {
        Config config = JamesConfigMod.CONFIGS.get(location);
        if (config != null) {
            if (config.shouldSync()) {
                for (JsonObject jsonObject : JamesConfigMod.CONFIGS.get(location).serialize()) {
                    JsonObject object = new JsonObject();
                    object.add("object", jsonObject);
                    object.addProperty("configName", location.toString());
                    array.add(object);
                }
            } else {
                JamesConfigMod.LOGGER.error("Attempted to sync unsyncable config \"{}\"", location.toString());
            }
        } else {
            JamesConfigMod.LOGGER.error("Attempted to sync config object with invalid config location \"{}\"", location.toString());
        }
    }

    public void handle() {
        assert Minecraft.getInstance().player != null;

        JamesConfigMod.CONFIGS.forEach(((resourceLocation, config) -> {
            if (config.shouldSync()) {
                if (config.shouldClearBeforeSync()) {
                    config.clear();
                }
                if (configs.containsKey(resourceLocation)) {
                    for (ConfigObject object : configs.get(resourceLocation)) {
                        config.add(object);
                    }
                    configs.removeAll(resourceLocation);
                }
            }
        }));
    }
}
