package net.vakror.jamesconfig.config.packet;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
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
            configs.put(new ResourceLocation(object1.get("configName").getAsString()), ConfigObject.deserializeUnknown(object1.get("object")));
        }
    }

    public void encode(FriendlyByteBuf buf) {
        JsonArray object = new JsonArray();
        configs.forEach(((location, configObject) -> serializeObject(location, configObject, object)));
        buf.writeByteArray(object.toString().getBytes());
    }

    public void serializeObject(ResourceLocation location, ConfigObject configObject, JsonArray array) {
        JsonElement serialized = configObject.serialize();
        JsonObject object = new JsonObject();
        object.add("object", serialized);
        object.addProperty("configName", location.toString());
        array.add(serialized);
    }

    public boolean handle() {
        //TODO:figure out what this is supposed Todo, ig the player request sync? i dont get it

        assert Minecraft.getInstance().player != null;

        JamesConfigMod.CONFIGS.forEach(((resourceLocation, config) -> {
            if (config.shouldSync()) {
                if (config.shouldClearBeforeSync()) {
                    config.discardAllValues();
                }
                if (configs.containsKey(resourceLocation)) {
                    for (ConfigObject object : configs.get(config.getName())) {
                        config.add(object);
                        configs.remove(resourceLocation, object);
                    }
                }
            }
        }));
        return true;
    }
}
