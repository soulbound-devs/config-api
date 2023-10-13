package net.vakror.jamesconfig.config.config.object.default_objects.registry;

import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.object.ConfigObject;

public abstract class RegistryConfigObject extends ConfigObject {

    private String name;
    private final ResourceLocation type;

    public RegistryConfigObject(String name, ResourceLocation type) {
        super(name);
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
