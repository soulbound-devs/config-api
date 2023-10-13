package net.vakror.jamesconfig.config.manager.config;

import net.vakror.jamesconfig.config.config.Config;

import java.util.ArrayList;
import java.util.List;

public class SimpleConfigManager extends ConfigManager {

    public static final SimpleConfigManager INSTANCE = new SimpleConfigManager();

    List<Config> configs = new ArrayList<>();

    @Override
    public void register(Config config) {
        configs.add(config);
    }

    @Override
    public List<Config> getAll() {
        return configs;
    }
}
