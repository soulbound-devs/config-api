package net.vakror.jamesconfig.config.example;

import net.vakror.jamesconfig.config.config.object.default_objects.registry.CompoundRegistryObject;
import net.vakror.jamesconfig.config.example.configs.ExampleMultiObjectRegistryConfigImpl;
import net.vakror.jamesconfig.config.example.configs.ExampleSettingConfig;
import net.vakror.jamesconfig.config.example.configs.ExampleSingleObjectRegistryConfigImpl;
import net.vakror.jamesconfig.config.manager.config.SimpleConfigManager;
import net.vakror.jamesconfig.config.manager.object.SimpleConfigObjectManager;

public class ExampleConfigs {
    public static void addExampleConfig() {
        SimpleConfigManager.INSTANCE.register(new ExampleMultiObjectRegistryConfigImpl());
        SimpleConfigManager.INSTANCE.register(new ExampleSettingConfig());
        SimpleConfigManager.INSTANCE.register(new ExampleSingleObjectRegistryConfigImpl());

        SimpleConfigObjectManager.INSTANCE.register(new CompoundRegistryObject(""));
    }
}
