package net.vakror.jamesconfig.config.config;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public abstract class Config<P> {
    public abstract void generateConfig();

    @NotNull
    public abstract File getConfigDir();

    @NotNull
    public abstract File getConfigFile(String fileName);

    public abstract String getSubPath();

    public abstract boolean isValueAcceptable(P value);

    public abstract boolean shouldDiscardConfigOnUnacceptableValue();

    public abstract void invalidate();

    public abstract void discardValue(P object);

    public abstract void discardAllValues();

    public abstract ResourceLocation getName();

    @Override
    public String toString() {
        return this.getName().toString();
    }

    public abstract void readConfig(boolean overrideCurrent);

    public abstract void add(P object);

    public abstract String getName(P object);

    public abstract boolean isValid();
    public abstract void addAll(List<P> object);

    public abstract Map<Type, Object> getTypeAdapters();

    public abstract List<P> getObjects();

    protected abstract void resetToDefault();

    public abstract void writeConfig();
}
