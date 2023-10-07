package net.vakror.jamesconfig.config.adapter;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

import java.lang.reflect.Type;

public class SimpleCodecAdapter<T>
        implements JsonSerializer<T>,
        JsonDeserializer<T> {

    private final Codec<T> codec;
    public SimpleCodecAdapter(Codec<T> codec) {
        this.codec = codec;
    }

    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return codec.parse(JsonOps.INSTANCE, json).resultOrPartial((error) -> {throw new JsonParseException(error);}).get();
    }

    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        return codec.encodeStart(JsonOps.INSTANCE, src).resultOrPartial((error) -> {throw new JsonIOException(error);}).get();
    }
}

