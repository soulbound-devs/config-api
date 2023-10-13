package net.vakror.jamesconfig.config.config.registry;

import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public abstract class SimpleSidedRegistryConfigImpl extends SimpleRegistryConfigImpl {
    private final EnvType side;
    public SimpleSidedRegistryConfigImpl(EnvType side, String subPath, ResourceLocation name) {
        super(subPath, name);
        this.side = side;
    }

    @Override
    public boolean shouldClearBeforeSync() {
        return false;
    }

    @Override
    public boolean shouldSync() {
        return false;
    }

    @Override
    public boolean shouldReadConfig() {
        if (side == null) {
            return true;
        }
        if (side == EnvType.CLIENT) {
            //TODO:make sure this evn check is correct?
            return Platform.getEnv().equals(EnvType.CLIENT);
        } else {
            return Platform.getEnv().equals(EnvType.SERVER);
        }
    }
}
