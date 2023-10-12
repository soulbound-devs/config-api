package net.vakror.jamesconfig.config.event;

import net.vakror.jamesconfig.config.manager.ConfigManager;

import java.util.ArrayList;
import java.util.List;


public class RegisterConfigManagersEvent {
    private final List<ConfigManager> managers = new ArrayList<>();


    public void addManager(ConfigManager type) {
        managers.add(type);
    }

    public void removeManager(ConfigManager type) {
        managers.remove(type);
    }

    public List<ConfigManager> getManagers() {
        return managers;
    }
}
