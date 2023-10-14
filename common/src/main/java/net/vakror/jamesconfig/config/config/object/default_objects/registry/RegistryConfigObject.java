package net.vakror.jamesconfig.config.config.object.default_objects.registry;

import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.object.ConfigObject;

import java.util.Objects;

/**
 * A simple class representing a {@link ConfigObject} meant to be used in a registry config, though it can be used in a setting config.
 * If used in a setting config, it will NOT have required values and will instead be directly deserialized, no matter the values contained in it.
 */
public abstract class RegistryConfigObject implements ConfigObject {

    /**
     * The name of this registry config object
     */
    private String name;

    /**
     * a {@link ResourceLocation} representing the type of this object
     */
    private final ResourceLocation type;

    /**
     * a simple constructor which sets the name and the type of this object
     * @param name the name to set this object's name to
     * @param type the type of this object
     */
    public RegistryConfigObject(String name, ResourceLocation type) {
        setName(name);
        this.type = type;
    }

    /**
     * query the name of this object, used for serialization, logging, and ensuring correctness in a setting config
     * @return the name of this object
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Query the type of this object, used mainly in registry configs, where the deserializer does not know what to deserialize to
     * @return a resourceLocation that represents this object's type
     */
    @Override
    public ResourceLocation getType() {
        return type;
    }

    /**
     * sets the name of this object, used if the object's name is wrong and needs to be corrected, such as if the object does not use its name parameter in {@link #deserialize}
     * @param name the name to set this primitive's name to
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }
}
