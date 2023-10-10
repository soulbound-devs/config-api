package net.vakror.jamesconfig.config.adapter;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundTag;

import java.lang.reflect.Type;

public class CompoundTagAdapter
implements JsonSerializer<CompoundTag>,
JsonDeserializer<CompoundTag> {
    public static CompoundTagAdapter INSTANCE = new CompoundTagAdapter();

    private CompoundTagAdapter() {
    }

    public CompoundTag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return CompoundTag.CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial((error) -> {throw new JsonParseException(error);}).get();
    }

    public JsonElement serialize(CompoundTag src, Type typeOfSrc, JsonSerializationContext context) {
        return CompoundTag.CODEC.encodeStart(JsonOps.INSTANCE, src).resultOrPartial((error) -> {throw new JsonIOException(error);}).get();
    }
}

