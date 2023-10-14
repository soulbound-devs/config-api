package net.vakror.jamesconfig.config.config.object.default_objects.primitive;

import net.vakror.jamesconfig.config.config.object.ConfigObject;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Representation of a {@link Number} ({@link Float}, {@link Double}, {@link Integer}, {@link BigDecimal}, {@link Long}, {@link Short}, {@link BigInteger}, or {@link Byte}) primitive as a {@link ConfigObject}
 */
public class NumberPrimitiveObject extends PrimitiveObject<Number> {

    /**
     * @param content the value of the primitive
     * @param name the name of the primitive
     */
    public NumberPrimitiveObject(Number content, String name) {
        super(content, name);
    }

    /**
     * a constructor which sets the name to null
     * @param content the value of the primitive
     */
    public NumberPrimitiveObject(Number content) {
        super(content);
    }
}
