package net.vakror.jamesconfig.config.config.object;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LazilyParsedNumber;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.BooleanPrimitiveObject;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.NumberPrimitiveObject;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.StringPrimitiveObject;
import org.jetbrains.annotations.Nullable;

public abstract class ConfigObject {
    public ConfigObject(String name) {
        setName(name);
    }
    public abstract String getName();
    public abstract void setName(String name);
    public abstract ResourceLocation getType();
    public abstract JsonElement serialize();
    public abstract ConfigObject deserialize(@Nullable String name, JsonElement element, ConfigObject defaultValue);

    @Nullable
    public static ConfigObject deserializeUnknown(JsonElement element) {
        if (element instanceof JsonObject object) {
            return deserializeFromObject(null, object);
        } else if (element instanceof JsonPrimitive primitive) {
            return deserializePrimitive(null, primitive);
        } else {
            return null;
        }
    }

    @Nullable
    public static ConfigObject deserializeUnknown(String name, JsonElement element) {
        if (element instanceof JsonObject object) {
            return deserializeFromObject(name, object);
        } else if (element instanceof JsonPrimitive primitive) {
            return deserializePrimitive(name, primitive);
        } else {
            return null;
        }
    }

    public static ConfigObject deserializePrimitive(String name, JsonPrimitive element) {
        if (element.isNumber()) {
            Number number = element.getAsNumber();
            return new NumberPrimitiveObject(number, name);
        } else if (element.isString()) {
            return new StringPrimitiveObject(element.getAsString(), name);
        } else if (element.isBoolean()) {
            return new BooleanPrimitiveObject(element.getAsBoolean(), name);
        }
        return null;
    }

    private static ConfigObject deserializeFromObject(String name, JsonObject jsonObject) {
        ConfigObject object = JamesConfigMod.KNOWN_OBJECT_TYPES.get(new ResourceLocation(jsonObject.getAsJsonPrimitive("type").getAsString()));
        if (object == null) {
            JamesConfigMod.LOGGER.error("Could not find config object object of type {}", jsonObject.getAsJsonPrimitive("type").getAsString());
            return null;
        }
        return object.deserialize(name, jsonObject, null);
    }
}
