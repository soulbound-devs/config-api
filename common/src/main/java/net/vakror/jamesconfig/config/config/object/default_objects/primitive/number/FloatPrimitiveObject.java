package net.vakror.jamesconfig.config.config.object.default_objects.primitive.number;

import com.mojang.serialization.Codec;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import net.vakror.jamesconfig.config.config.object.PrimitiveObject;

public class FloatPrimitiveObject extends PrimitiveObject<Float> {

    public FloatPrimitiveObject(Float content, String name) {
        super(content, name);
    }

    public FloatPrimitiveObject(Float content) {
        super(content);
    }

    @Override
    public Codec<Float> getCodec() {
        return Codec.FLOAT;
    }

    @Override
    public ConfigObject constructWithValue(Float value) {
        return new FloatPrimitiveObject(value);
    }
}
