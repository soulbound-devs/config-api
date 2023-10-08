package net.vakror.jamesconfig.config.manager;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.vakror.jamesconfig.config.config.Config;
import net.vakror.jamesconfig.config.adapter.SimpleCodecAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleConfigManager extends ConfigManager {

    public static final SimpleConfigManager INSTANCE = new SimpleConfigManager();

    Map<Config<?>, List<Pair<Type, Object>>> configs = new HashMap<>();

    @Override
    public void addConfig(Config<?> individualFileConfig, Pair<Type, Object>... adapters) {
        configs.put(individualFileConfig, List.of(adapters));
    }

    @Override
    public void addConfig(Config<?> individualFileConfig, Type type, Object adaptersArray) {
        configs.put(individualFileConfig, List.of(new Pair<>(type, adaptersArray)));
    }

    @Override
    public void addCodecConfig(Config<?> individualFileConfig, Pair<Type, Codec<?>>... codecArray) {
        List<Pair<Type, Object>> adapters = new ArrayList<>();
        for (Pair<Type, Codec<?>> pair : codecArray) {
            adapters.add(new Pair<>(pair.getFirst(), new SimpleCodecAdapter<>(pair.getSecond())));
        }
        configs.put(individualFileConfig, adapters);
    }

    @Override
    public void addCodecConfig(Config<?> individualFileConfig, Type type, Codec<?> codec) {
        configs.put(individualFileConfig, List.of(new Pair<>(type, new SimpleCodecAdapter<>(codec))));
    }

    @Override
    public Map<Config<?>, List<Pair<Type, Object>>> getAllConfigs() {
        return configs;
    }
}
