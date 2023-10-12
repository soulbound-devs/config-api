package net.vakror.jamesconfig.config.manager;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.vakror.jamesconfig.config.config.Config;

import java.lang.reflect.Type;

public interface WithAdapter {
    void addConfig(Config<?> individualFileConfig, Pair<Type, Object>... adapters);
    void addConfig(Config<?> individualFileConfig, Type type, Object adaptersArray);
    void addCodecConfig(Config<?> individualFileConfig, Pair<Type, Codec<?>>... codecArray);
    void addCodecConfig(Config<?> individualFileConfig, Type type, Codec<?> adaptersArray);
}