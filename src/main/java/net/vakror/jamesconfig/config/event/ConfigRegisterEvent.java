package net.vakror.jamesconfig.config.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.vakror.jamesconfig.config.Config;

import java.util.HashMap;
import java.util.Map;

public class ConfigRegisterEvent extends Event implements IModBusEvent {
    private final Map<ResourceLocation, Config<?>> configs = new HashMap<>();
    public void register(Config<?> config) {
        configs.put(config.getName(), config);
    }

    public Map<ResourceLocation, Config<?>> getConfigs() {
        return configs;
    }
}
