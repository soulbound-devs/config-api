package net.vakror.jamesconfig.config.config.object.default_objects.registry;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.vakror.jamesconfig.config.config.object.ConfigObject;

public abstract class SimpleCodecConfigObject<P> extends ConfigObject {
    public SimpleCodecConfigObject(String name) {
        super(name);
    }

    public abstract Codec<P> getCodec();

    public abstract P getValue();

    public abstract ConfigObject constructWithValue(P value);

    @Override
    public JsonElement serialize() {
        Either<JsonElement, DataResult.PartialResult<JsonElement>> optional = getCodec().encodeStart(JsonOps.INSTANCE, getValue()).get();
        if (optional.left().isPresent()) {
            return optional.left().get();
        } else {
            return null;
        }
    }

    @Override
    public ConfigObject deserialize(String name, JsonElement element) {
        Either<Pair<P, JsonElement>, DataResult.PartialResult<Pair<P, JsonElement>>> optional = getCodec().decode(JsonOps.INSTANCE, element).get();


        if (optional.left().isPresent()) {
            ConfigObject object = constructWithValue(optional.left().get().getFirst());
            object.setName(name);
            return object;
        } else {
            return null;
        }
    }
}
