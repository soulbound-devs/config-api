package net.vakror.jamesconfig.config.config.individual;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;

import java.util.function.Function;

public abstract class SimpleSidedIndividualFileConfig<P> extends SimpleIndividualFileConfig<P> {
    private final Dist side;
    public SimpleSidedIndividualFileConfig(Dist side, String subPath, ResourceLocation name, Function<P, String> nameGetter) {
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
        if (side == Dist.CLIENT) {
            return Thread.currentThread().getThreadGroup().equals(SidedThreadGroups.CLIENT);
        } else {
            return Thread.currentThread().getThreadGroup().equals(SidedThreadGroups.SERVER);
        }
    }
}
