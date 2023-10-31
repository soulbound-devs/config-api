package net.vakror.jamesconfig.config.config.object.default_objects;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import net.vakror.jamesconfig.config.config.object.default_objects.registry.RegistryConfigObject;
import org.jetbrains.annotations.Nullable;

public class ExampleObject extends RegistryConfigObject {
    /**
     * a simple constructor which sets the name and the type of this object
     *
     * @param name the name to set this object's name to
     * @param type the type of this object
     */
    public ExampleObject(String name, ResourceLocation type) {
        super(name, type);
    }

    @Override
    public JsonElement serialize() {
        return null;
    }

    @Override
    public ConfigObject deserialize(@Nullable String name, JsonElement element, ConfigObject defaultValue, String configName) {
        return null;
    }
}
