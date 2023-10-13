package net.vakror.jamesconfig.config.config.object.default_objects.primitive.number;

import com.mojang.serialization.Codec;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import net.vakror.jamesconfig.config.config.object.PrimitiveObject;

public class ShortPrimitiveObject extends PrimitiveObject<Short> {

    public ShortPrimitiveObject(Short content, String name) {
        super(content, name);
    }

    public ShortPrimitiveObject(Short content) {
        super(content);
    }

    @Override
    public Codec<Short> getCodec() {
        return Codec.SHORT;
    }

    @Override
    public ConfigObject constructWithValue(Short value) {
        return new ShortPrimitiveObject(value);
    }
}
