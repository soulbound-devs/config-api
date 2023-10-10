package net.vakror.jamesconfig.config.example;

import net.vakror.jamesconfig.config.adapter.SimpleCodecAdapter;
import net.vakror.jamesconfig.config.example.configs.ExampleIndividualFileConfig;
import net.vakror.jamesconfig.config.example.configs.ExampleOneFileConfig;
import net.vakror.jamesconfig.config.example.configs.StringWithContents;
import net.vakror.jamesconfig.config.manager.SimpleConfigManager;

public class ExampleConfigs {
    public static void addExampleConfig() {
        SimpleConfigManager.INSTANCE.addConfig(new ExampleIndividualFileConfig(), StringWithContents.class, new SimpleCodecAdapter<>(StringWithContents.CODEC));
        SimpleConfigManager.INSTANCE.addConfig(ExampleOneFileConfig.INSTANCE, StringWithContents.class, new SimpleCodecAdapter<>(StringWithContents.CODEC));
    }
}
