package net.vakror.jamesconfig.config.config.object.default_objects.primitive.number;

import com.mojang.serialization.Codec;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import net.vakror.jamesconfig.config.config.object.PrimitiveObject;

public class IntegerPrimitiveObject extends PrimitiveObject<Integer> {

    public IntegerPrimitiveObject(Integer content, String name) {
        super(content, name);
    }

    public IntegerPrimitiveObject(Integer content) {
        super(content);
    }

    @Override
    public Codec<Integer> getCodec() {
        return Codec.INT;
    }

    @Override
    public ConfigObject constructWithValue(Integer value) {
        return new IntegerPrimitiveObject(value);
    }
}
