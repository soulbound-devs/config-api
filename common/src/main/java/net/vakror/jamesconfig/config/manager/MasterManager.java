package net.vakror.jamesconfig.config.manager;

import net.vakror.jamesconfig.config.event.ConfigEvents;
import net.vakror.jamesconfig.config.event.RegisterConfigManagersEvent;
import java.util.List;

public class MasterManager {
    public static void register() {
        getAllManagers().forEach(Manager::register);
    }

    public static List<Manager<?>> getAllManagers() {
        ManagerRegister.INSTANCE.register();
        RegisterConfigManagersEvent event = new RegisterConfigManagersEvent();
        ConfigEvents.REGISTER_MANAGER.invoker().post(event);
        return event.getManagers();
    }
}
