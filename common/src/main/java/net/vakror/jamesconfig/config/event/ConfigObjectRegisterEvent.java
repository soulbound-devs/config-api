package net.vakror.jamesconfig.config.event;

import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.object.ConfigObject;

import java.util.HashMap;
import java.util.Map;

public class ConfigObjectRegisterEvent {
    private final Map<ResourceLocation, ConfigObject> configs = new HashMap<>();
    public void register(ConfigObject object) {
        configs.put(object.getType(), object);
    }

    public Map<ResourceLocation, ConfigObject> getKnownTypes() {
        return configs;
    }
}
