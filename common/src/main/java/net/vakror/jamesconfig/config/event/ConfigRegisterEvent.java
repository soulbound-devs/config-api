package net.vakror.jamesconfig.config.event;

import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.Config;

import java.util.HashMap;
import java.util.Map;

public class ConfigRegisterEvent {
    private final Map<ResourceLocation, Config<?>> configs = new HashMap<>();
    public void register(Config<?> individualFileConfig) {
        configs.put(individualFileConfig.getName(), individualFileConfig);
    }

    public Map<ResourceLocation, Config<?>> getConfigs() {
        return configs;
    }
}
