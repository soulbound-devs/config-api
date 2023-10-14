package net.vakror.jamesconfig.config.config.object.default_objects.primitive;

import net.vakror.jamesconfig.config.config.object.ConfigObject;

/**
 * Representation of a {@link String} primitive as a {@link ConfigObject}
 */
public class StringPrimitiveObject extends PrimitiveObject<String> {

    /**
     * @param content the value of the primitive
     * @param name the name of the primitive
     */
    public StringPrimitiveObject(String content, String name) {
        super(content, name);
    }

    /**
     * a constructor which sets the name to null
     * @param content the value of the primitive
     */
    public StringPrimitiveObject(String content) {
        super(content);
    }
}
