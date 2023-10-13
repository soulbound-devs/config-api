package net.vakror.jamesconfig.config.config.object.default_objects.primitive;

import com.mojang.serialization.Codec;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import net.vakror.jamesconfig.config.config.object.PrimitiveObject;

public class StringPrimitiveObject extends PrimitiveObject<String> {

    public StringPrimitiveObject(String content, String name) {
        super(content, name);
    }

    public StringPrimitiveObject(String content) {
        super(content);
    }

    @Override
    public Codec<String> getCodec() {
        return Codec.STRING;
    }

    @Override
    public ConfigObject constructWithValue(String value) {
        return new StringPrimitiveObject(value);
    }
}
