package net.vakror.jamesconfig.config.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


@Cancelable
public class GetConfigTypeAdaptersEvent extends Event implements IModBusEvent {
    private final Map<Type, Object> adapters = new HashMap<>();
    private final ResourceLocation configName;

    public GetConfigTypeAdaptersEvent(ResourceLocation configName) {
        this.configName = configName;
    }

    public ResourceLocation getConfigName() {
        return configName;
    }

    public void addAdapter(Type type, Object adapter) {
        adapters.put(type, adapter);
    }

    public void removeAdapter(Type type) {
        adapters.remove(type);
    }

    public Map<Type, Object> getAdapters() {
        return adapters;
    }
}
