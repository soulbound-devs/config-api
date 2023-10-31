package net.vakror.jamesconfig.config.config.registry.multi_object;

import dev.architectury.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.ConfigObject;

public abstract class SimpleMultiObjectRegistryConfigImpl extends MultiObjectRegistryConfigImpl {

    private final String subPath;
    private final ResourceLocation name;
    private boolean valid = true;
    private final String[] requiredMods;


    public SimpleMultiObjectRegistryConfigImpl(String subPath, ResourceLocation name) {
        this.subPath = subPath;
        this.name = name;
        requiredMods = new String[0];
    }
    
    public SimpleMultiObjectRegistryConfigImpl(String subPath, ResourceLocation name, String... requiredMods) {
        this.subPath = subPath;
        this.name = name;
        this.requiredMods = requiredMods;
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
    public final boolean shouldReadConfig() {
        for (String requiredMod : requiredMods) {
            if (!Platform.isModLoaded(requiredMod)) {
                JamesConfigMod.LOGGER.error("Mod \"" + requiredMod + "\" is not present but required by config \"" + this + "\", skipping read");
                return false;
            }
        }
        return shouldLoad();
    }

    public boolean shouldLoad() {
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

}

