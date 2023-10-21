package net.vakror.jamesconfig.config.config.object;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.BooleanPrimitiveObject;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.NumberPrimitiveObject;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.PrimitiveObject;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.StringPrimitiveObject;
import org.jetbrains.annotations.Nullable;

public interface ConfigObject {
    String getName();
    void setName(String name);
    ResourceLocation getType();
    JsonElement serialize();
    ConfigObject deserialize(@Nullable String name, JsonElement element, ConfigObject defaultValue, String configName);

    @Nullable
    static ConfigObject deserializeUnknown(JsonElement element, String configName) {
        if (element instanceof JsonObject object) {
            return deserializeFromObject(null, object, configName);
        } else if (element instanceof JsonPrimitive primitive) {
            return deserializePrimitive(null, primitive);
        } else {
            return null;
        }
    }

    @Nullable
    static ConfigObject deserializeUnknown(String name, JsonElement element, String configName) {
        if (element instanceof JsonObject object) {
            return deserializeFromObject(name, object, configName);
        } else if (element instanceof JsonPrimitive primitive) {
            return deserializePrimitive(name, primitive);
        } else {
            return null;
        }
    }

    static PrimitiveObject<?> deserializePrimitive(String name, JsonPrimitive element) {
        if (element.isNumber()) {
            return new NumberPrimitiveObject(name, element.getAsNumber());
        } else if (element.isString()) {
            return new StringPrimitiveObject(name, element.getAsString());
        } else if (element.isBoolean()) {
            return new BooleanPrimitiveObject(name, element.getAsBoolean());
        }
        return null;
    }

    private static ConfigObject deserializeFromObject(String name, JsonObject jsonObject, String configName) {
        ConfigObject object = JamesConfigMod.KNOWN_OBJECT_TYPES.get(new ResourceLocation(jsonObject.getAsJsonPrimitive("type").getAsString()));
        if (object == null) {
            JamesConfigMod.LOGGER.error("Could not find config object object of type {}", jsonObject.getAsJsonPrimitive("type").getAsString());
            return null;
        }
        return object.deserialize(name, jsonObject, null, configName);
    }
}
