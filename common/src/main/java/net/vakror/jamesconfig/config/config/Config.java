package net.vakror.jamesconfig.config.config;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public abstract class Config {
    public abstract void generateConfig();

    @NotNull
    public abstract File getConfigDir();

    public abstract String getSubPath();

    public abstract ResourceLocation getName();

    @Override
    public String toString() {
        return this.getName().toString();
    }

    public abstract void readConfig(boolean overrideCurrent);

    public abstract boolean shouldReadConfig();

    public abstract void writeConfig();

    public abstract List<JsonObject> serialize();

    /**
     *
     * @return whether to clear this config before it is synced. Recommended for all non-clientside configs
     */
    public abstract boolean shouldClearBeforeSync();

    /**
     * will not do anything if config is clientside
     * @return whether to sync
     */
    public abstract boolean shouldSync();

    public abstract void add(ConfigObject object);

    public abstract List<ConfigObject> getAll();

    public abstract List<ConfigObject> parse(JsonObject jsonObject);

    public abstract void clear();
}
