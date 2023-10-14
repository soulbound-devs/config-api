package net.vakror.jamesconfig.config.config.object.default_objects.primitive;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.object.ConfigObject;

import java.util.Objects;

/**
 * Representation of a primitive (a {@link Number}, {@link Boolean}, or {@link String} as a {@link ConfigObject}
 * Primitives are deserialized using {@link ConfigObject#deserializePrimitive}
 * This class contains a simple implementation of names, as well as the value, where {@link P} is the type of a primitive ({@link Number}, {@link Boolean}, or {@link String})
 */
public abstract class PrimitiveObject<P> implements ConfigObject {

    /**
     * The value that this primitive holds
     */
    protected final P content;

    /**
     * The name of this primitive.
     * Used for deserialization and serialization, especially in setting configs
     */
    protected String name;

    /**
     * @param content The value ({@link P}) of the primitive
     * @param name the name of the primitive
     */
    public PrimitiveObject(P content, String name) {
        setName(name);
        this.content = content;
    }

    /**
     * A constructor that defaults the name to null
     * @param content the content ({@link P}) of the primitive
     */
    public PrimitiveObject(P content) {
        this.content = content;
    }

    /**
     * Uses {@link ConfigObject#deserializePrimitive} to deserialize this primitive
     * @param name the name of the primitive object
     * @param element the {@link JsonPrimitive} that contains the primitive
     * @param defaultValue the default value – used if the element is invalid or the wrong type to reset the value of the primitive to default
     * @return a primitive object representing the deserialized value
     */
    @Override
    public ConfigObject deserialize(String name, JsonElement element, ConfigObject defaultValue) {
        this.name = name;
        PrimitiveObject<?> object = ConfigObject.deserializePrimitive(name, (JsonPrimitive) element);
        if (object == null || !object.getClass().equals(this.getClass())) {
            return defaultValue;
        }
        return object;
    }

    /**
     * converts this primitive into a {@link JsonPrimitive}, which can then be used to write to a file
     * @return a {@link JsonPrimitive} representing this primitive's value
     */
    @Override
    public JsonPrimitive serialize() {
        JsonPrimitive element = null;
        if (getValue() instanceof Number number) {
             element = new JsonPrimitive(number);
        }
        if (getValue() instanceof Boolean bool) {
            element = new JsonPrimitive(bool);
        }
        if (getValue() instanceof String string) {
            element = new JsonPrimitive(string);
        }
        return element;
    }

    /**
     * sets the name of this object, used if the object's name is wrong and needs to be corrected, such as if the object does not use its name parameter in {@link #deserialize}
     * @param name the name to set this primitive's name to
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * query the name of this primitive, used for serialization, logging, and ensuring correctness in a setting config
     * @return the name of this primitive
     */
    @Override
    public String getName() {
        return name == null ? content.toString(): name;
    }

    /**
     * Query the type of this primitive, used mainly in registry configs, where the deserializer does not know what to deserialize to
     * primitives do not need to specify a type, instead, their type is inferred, which is why this returns null
     * @return a resourceLocation that represents this primitive's type
     */
    @Override
    public ResourceLocation getType() {
        return null;
    }

    /**
     * Query the value of this primitive, used for serialization
     * @return the value of this primitive
     */
    public P getValue() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrimitiveObject<?> that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
