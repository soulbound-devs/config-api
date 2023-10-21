package net.vakror.jamesconfig.config.event;

import net.vakror.jamesconfig.config.manager.Manager;

import java.util.ArrayList;
import java.util.List;


public class RegisterConfigManagersEvent {
    private final List<Manager<?>> managers = new ArrayList<>();

    public void addManager(Manager<?> type) {
        managers.add(type);
    }

    public void removeManager(Manager<?> type) {
        managers.remove(type);
    }

    public List<Manager<?>> getManagers() {
        return managers;
    }
}
