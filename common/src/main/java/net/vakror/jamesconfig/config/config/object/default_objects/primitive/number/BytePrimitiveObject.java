package net.vakror.jamesconfig.config.config.object.default_objects.primitive.number;

import com.mojang.serialization.Codec;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.PrimitiveObject;

public class BytePrimitiveObject extends PrimitiveObject<Byte> {

    public BytePrimitiveObject(Byte content, String name) {
        super(content, name);
    }

    public BytePrimitiveObject(Byte content) {
        super(content);
    }

    @Override
    public Codec<Byte> getCodec() {
        return Codec.BYTE;
    }

    @Override
    public ConfigObject constructWithValue(Byte value) {
        return new BytePrimitiveObject(value);
    }
}
