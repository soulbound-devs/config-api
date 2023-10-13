package net.vakror.jamesconfig.config.config.object.default_objects.primitive.number;

import com.mojang.serialization.Codec;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.PrimitiveObject;

public class DoublePrimitiveObject extends PrimitiveObject<Double> {

    public DoublePrimitiveObject(Double content, String name) {
        super(content, name);
    }

    public DoublePrimitiveObject(Double content) {
        super(content);
    }

    @Override
    public Codec<Double> getCodec() {
        return Codec.DOUBLE;
    }

    @Override
    public ConfigObject constructWithValue(Double value) {
        return new DoublePrimitiveObject(value);
    }
}
