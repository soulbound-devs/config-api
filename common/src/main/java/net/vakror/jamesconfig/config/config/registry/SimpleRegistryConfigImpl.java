package net.vakror.jamesconfig.config.config.registry;

import com.google.common.base.Stopwatch;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.ConfigObject;

public abstract class SimpleRegistryConfigImpl extends RegistryConfigImpl {

    private final String subPath;
    private final ResourceLocation name;
    private boolean valid = true;


    public SimpleRegistryConfigImpl(String subPath, ResourceLocation name) {
        this.subPath = subPath;
        this.name = name;
    }

    @Override
    public void invalidate() {
        this.valid = false;
    }

    @Override
    public boolean isValueAcceptable(ConfigObject value) {
        return true;
    }

    @Override
    public boolean shouldDiscardConfigOnUnacceptableValue() {
        return false;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean shouldClearBeforeSync() {
        return true;
    }

    @Override
    public void clear() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        JamesConfigMod.LOGGER.info("Clearing config");
        objects.clear();
        JamesConfigMod.LOGGER.info("Finished Clearing config, \033[0;31mTook {}\033[0;0m", stopwatch);
    }

    @Override
    public void discardValue(ConfigObject object) {
        objects.remove(object);
    }

    @Override
    public boolean shouldReadConfig() {
        return true;
    }

    @Override
    public boolean shouldAddObject(ConfigObject object) {
        return true;
    }

    @Override
    protected void resetToDefault() {
    }

    @Override
    public String getSubPath() {
        return subPath;
    }

    @Override
    public ResourceLocation getName() {
        return name;
    }

    @Override
    public void add(ConfigObject object) {
        objects.add(object);
    }

    @Override
    public boolean shouldSync() {
        return true;
    }
}
