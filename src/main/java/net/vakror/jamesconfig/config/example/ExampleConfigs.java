package net.vakror.jamesconfig.config.example;

import net.vakror.jamesconfig.config.example.configs.ExampleConfig;
import net.vakror.jamesconfig.config.example.configs.StringWithContents;
import net.vakror.jamesconfig.config.manager.SimpleConfigManager;

public class ExampleConfigs {
    public static void addExampleConfig() {
        SimpleConfigManager.INSTANCE.addConfig(new ExampleConfig(), StringWithContents.class, StringWithContents.CODEC);
    }
}
