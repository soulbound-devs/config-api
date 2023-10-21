package net.vakror.jamesconfig.config.config.registry.single_object;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.PrimitiveObject;

public abstract class SimpleSingleObjectRegistryConfigImpl<P extends ConfigObject> extends SingleObjectRegistryConfigImpl<P> {

    private final String subPath;
    private final ResourceLocation name;
    private boolean valid = true;


    public SimpleSingleObjectRegistryConfigImpl(String subPath, ResourceLocation name) {
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
        JamesConfigMod.LOGGER.info("Clearing config");
        objects.clear();
        JamesConfigMod.LOGGER.info("Finished Clearing config");
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
        try {
            if (object != null) {
                objects.add((P) object);
            }
        } catch (Exception e) {
            JamesConfigMod.LOGGER.error("Config object {} of type {} in config {} is not of correct type", object.getName(), object.getType().toString(), this.getName().toString());
        }
    }

    @Override
    public boolean shouldSync() {
        return true;
    }
}

