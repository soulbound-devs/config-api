package net.vakror.jamesconfig.config.config.object.default_objects.registry;

import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.object.ConfigObject;

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

    public RegistryConfigObject(String name, ResourceLocation type) {
        setName(name);
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ResourceLocation getType() {
        return type;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
