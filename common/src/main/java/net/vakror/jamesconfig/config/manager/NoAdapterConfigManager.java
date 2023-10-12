package net.vakror.jamesconfig.config.manager;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.vakror.jamesconfig.config.adapter.SimpleCodecAdapter;
import net.vakror.jamesconfig.config.config.Config;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoAdapterConfigManager extends ConfigManager implements WithoutAdapter{

    public static final NoAdapterConfigManager INSTANCE = new NoAdapterConfigManager();

    List<Config<?>> configs = new ArrayList<>();

    @Override
    public void addConfig(Config<?> individualFileConfig) {
        configs.add(individualFileConfig);
    }

    @Override
    public Map<Config<?>, List<Pair<Type, Object>>> getAllConfigs() {
        Map<Config<?>, List<Pair<Type, Object>>> configMap = new HashMap<>();
        for (Config<?> config : configs) {
            configMap.put(config, List.of());
        }
        return configMap;
    }
}
