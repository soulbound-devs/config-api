package net.vakror.jamesconfig.config.manager;

import dev.architectury.event.EventResult;
import net.vakror.jamesconfig.config.event.ConfigEvents;

public class ManagerRegister extends SimpleManager<Manager<?>> {
    public static ManagerRegister INSTANCE = new ManagerRegister();

    @Override
    public void register() {
        new ModEvents(this);
    }

    public static class ModEvents {
        private ModEvents(ManagerRegister manager) {
            ConfigEvents.REGISTER_MANAGER.register(event -> {
                manager.getAll().forEach(event::register);
                return EventResult.pass();
            });
        }
    }
}
