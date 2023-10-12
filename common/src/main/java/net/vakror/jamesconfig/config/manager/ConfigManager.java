package net.vakror.jamesconfig.config.manager;

import com.mojang.datafixers.util.Pair;
import dev.architectury.event.EventResult;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.Config;
import net.vakror.jamesconfig.config.event.ConfigEvents;
import net.vakror.jamesconfig.config.event.ConfigRegisterEvent;
import net.vakror.jamesconfig.config.event.GetConfigTypeAdaptersEvent;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public abstract class ConfigManager {

    public void register() {
        new ModEvents(this);
    }

    public abstract Map<Config<?>, List<Pair<Type, Object>>> getAllConfigs();

    public static class ModEvents {
        private final ConfigManager manager;
        private ModEvents(ConfigManager manager) {
            this.manager = manager;
            ConfigEvents.CONFIG_REGISTER_EVENT.register(event -> {
                manager.getAllConfigs().keySet().forEach((config) -> event.register(config));
                return EventResult.pass();
            });
            ConfigEvents.REGISTER_CONFIG_TYPE_ADAPTERS.register(event -> {
                manager.getAllConfigs().forEach((config, adapterArray) -> {
                    if (event.getConfigName().equals(config.getName())) {
                        for (Pair<Type, Object> adapter : adapterArray) {
                            event.addAdapter(adapter.getFirst(), adapter.getSecond());
                        }
                    }
                });
                return EventResult.pass();
            });
        }
    }
}
