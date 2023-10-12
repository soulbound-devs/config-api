package net.vakror.jamesconfig.config.config.one;

import com.mojang.serialization.Codec;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public abstract class SimpleSidedOneFileConfig<P> extends SimpleOneFileConfig<P> {
    private final EnvType side;

    public SimpleSidedOneFileConfig(EnvType side, Codec<P> codec, String subPath, ResourceLocation name, Function<P, String> nameGetter) {
        super(codec, subPath, name, nameGetter);
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
