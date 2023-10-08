package net.vakror.jamesconfig.config.manager;

import net.vakror.jamesconfig.config.config.Config;

public interface WithoutAdapter {
    void addConfig(Config<?> individualFileConfig);
}
