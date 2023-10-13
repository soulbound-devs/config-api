package net.vakror.jamesconfig.config.config;

import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public abstract class Config {
    public abstract void generateConfig();

    @NotNull
    public abstract File getConfigDir();

    @NotNull
    public abstract File getConfigFile(String fileName);

    public abstract String getSubPath();

    public abstract boolean isValueAcceptable(ConfigObject value);

    public abstract boolean shouldDiscardConfigOnUnacceptableValue();

    public abstract void invalidate();

    public abstract void discardValue(ConfigObject object);

    public abstract void discardAllValues();

    public abstract ResourceLocation getName();

    @Override
    public String toString() {
        return this.getName().toString();
    }

    public abstract void readConfig(boolean overrideCurrent);

    public abstract void add(ConfigObject object);

    public abstract boolean isValid();

    public abstract boolean shouldReadConfig();

    public abstract boolean shouldAddObject(ConfigObject object);

    public abstract void onAddObject(ConfigObject object);

    public abstract Multimap<ResourceLocation, ConfigObject> getObjects();

    protected abstract void resetToDefault();

    public abstract void writeConfig();

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
}
