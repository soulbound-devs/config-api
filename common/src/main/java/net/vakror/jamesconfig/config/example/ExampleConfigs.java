package net.vakror.jamesconfig.config.example;

import net.vakror.jamesconfig.config.config.object.default_objects.registry.CompoundRegistryObject;
import net.vakror.jamesconfig.config.example.configs.ExampleRegistryConfigImpl;
import net.vakror.jamesconfig.config.example.configs.ExampleSettingConfig;
import net.vakror.jamesconfig.config.manager.config.SimpleConfigManager;
import net.vakror.jamesconfig.config.manager.object.SimpleConfigObjectManager;

public class ExampleConfigs {
    public static void addExampleConfig() {
        SimpleConfigManager.INSTANCE.register(new ExampleRegistryConfigImpl());
        SimpleConfigManager.INSTANCE.register(new ExampleSettingConfig());

        SimpleConfigObjectManager.INSTANCE.register(new CompoundRegistryObject(""));
    }
}
