package net.vakror.jamesconfig.config.manager.config;

import dev.architectury.event.EventResult;
import net.vakror.jamesconfig.config.config.Config;
import net.vakror.jamesconfig.config.event.ConfigEvents;
import net.vakror.jamesconfig.config.manager.Manager;

import java.util.List;

public abstract class ConfigManager extends Manager<Config> {

    @Override
    public void register() {
        new ModEvents(this);
    }

    @Override
    public abstract void register(Config individualFileConfig);

    @Override
    public abstract List<Config> getAll();

    public static class ModEvents {
        private ModEvents(ConfigManager manager) {
            ConfigEvents.CONFIG_REGISTER_EVENT.register(event -> {
                manager.getAll().forEach(event::register);
                return EventResult.pass();
            });
        }
    }
}
