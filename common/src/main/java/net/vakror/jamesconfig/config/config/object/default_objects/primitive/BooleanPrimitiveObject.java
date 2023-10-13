package net.vakror.jamesconfig.config.config.object.default_objects.primitive;

import com.mojang.serialization.Codec;
import net.vakror.jamesconfig.config.config.object.ConfigObject;

public class BooleanPrimitiveObject extends PrimitiveObject<Boolean> {

    public BooleanPrimitiveObject(Boolean content, String name) {
        super(content, name);
    }

    public BooleanPrimitiveObject(Boolean content) {
        super(content);
    }

    @Override
    public Codec<Boolean> getCodec() {
        return Codec.BOOL;
    }

    @Override
    public ConfigObject constructWithValue(Boolean value) {
        return new BooleanPrimitiveObject(value);
    }
}
