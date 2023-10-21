package net.vakror.jamesconfig.config.manager.object;

import dev.architectury.event.EventResult;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import net.vakror.jamesconfig.config.event.ConfigEvents;
import net.vakror.jamesconfig.config.manager.Manager;
import net.vakror.jamesconfig.config.manager.SimpleManager;

import java.util.List;

public abstract class ConfigObjectManager extends SimpleManager<ConfigObject> {
    @Override
    public void register() {
        new ModEvents(this);
    }

    public static class ModEvents {
        private ModEvents(ConfigObjectManager manager) {
            ConfigEvents.OBJECT_REGISTER_EVENT.register(event -> {
                manager.getAll().forEach(event::register);
                return EventResult.pass();
            });
        }
    }
}
