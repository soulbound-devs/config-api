package net.vakror.jamesconfig.config.manager;

import net.vakror.jamesconfig.config.event.ConfigEvents;
import net.vakror.jamesconfig.config.event.RegisterConfigManagersEvent;

import java.util.List;

public class MasterConfigManager {
    public static void register() {
        getAllManagers().forEach(ConfigManager::register);
    }

    public static List<ConfigManager> getAllManagers() {
        RegisterConfigManagersEvent event = new RegisterConfigManagersEvent();
        ConfigEvents.REGISTER_CONFIG_MANAGERS.invoker().post(event);
        return event.getManagers();
    }
}
