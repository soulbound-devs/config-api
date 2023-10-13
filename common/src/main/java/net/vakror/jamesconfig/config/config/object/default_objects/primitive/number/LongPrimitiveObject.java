package net.vakror.jamesconfig.config.config.object.default_objects.primitive.number;

import com.mojang.serialization.Codec;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import net.vakror.jamesconfig.config.config.object.PrimitiveObject;

public class LongPrimitiveObject extends PrimitiveObject<Long> {

    public LongPrimitiveObject(Long content, String name) {
        super(content, name);
    }

    public LongPrimitiveObject(Long content) {
        super(content);
    }

    @Override
    public Codec<Long> getCodec() {
        return Codec.LONG;
    }

    @Override
    public ConfigObject constructWithValue(Long value) {
        return new LongPrimitiveObject(value);
    }
}
