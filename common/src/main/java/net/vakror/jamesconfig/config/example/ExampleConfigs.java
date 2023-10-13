package net.vakror.jamesconfig.config.example;

import net.vakror.jamesconfig.config.example.configs.ExampleRegistryConfigImpl;
import net.vakror.jamesconfig.config.config.object.default_objects.StringWithContents;
import net.vakror.jamesconfig.config.manager.config.SimpleConfigManager;
import net.vakror.jamesconfig.config.manager.object.SimpleConfigObjectManager;

public class ExampleConfigs {
    public static void addExampleConfig() {
        SimpleConfigManager.INSTANCE.register(new ExampleRegistryConfigImpl());
        SimpleConfigObjectManager.INSTANCE.register(new StringWithContents("", ""));
    }
}
