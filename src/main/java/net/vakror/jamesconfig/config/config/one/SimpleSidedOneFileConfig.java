package net.vakror.jamesconfig.config.config.one;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;

import java.util.function.Function;

public abstract class SimpleSidedOneFileConfig<P> extends SimpleOneFileConfig<P> {
    private final Dist side;
    public SimpleSidedOneFileConfig(Dist side, Codec<P> codec, String subPath, ResourceLocation name, Function<P, String> nameGetter) {
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
        if (side == Dist.CLIENT) {
            return Thread.currentThread().getThreadGroup().equals(SidedThreadGroups.CLIENT);
        } else {
            return Thread.currentThread().getThreadGroup().equals(SidedThreadGroups.SERVER);
        }
    }
}
