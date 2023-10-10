package net.vakror.jamesconfig.config.config.individual;

import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public abstract class SimpleSidedIndividualFileConfig<P> extends SimpleIndividualFileConfig<P> {
    private final EnvType side;
    public SimpleSidedIndividualFileConfig(EnvType side, String subPath, ResourceLocation name, Function<P, String> nameGetter) {
        super(subPath, name, nameGetter);
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
