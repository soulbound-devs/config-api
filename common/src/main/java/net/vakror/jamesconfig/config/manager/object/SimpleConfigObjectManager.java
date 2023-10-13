package net.vakror.jamesconfig.config.manager.object;

import net.vakror.jamesconfig.config.config.object.ConfigObject;

import java.util.ArrayList;
import java.util.List;

public class SimpleConfigObjectManager extends ConfigObjectManager {

    public static final SimpleConfigObjectManager INSTANCE = new SimpleConfigObjectManager();

    List<ConfigObject> configs = new ArrayList<>();

    @Override
    public void register(ConfigObject config) {
        configs.add(config);
    }

    @Override
    public List<ConfigObject> getAll() {
        return configs;
    }
}
