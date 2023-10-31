package net.vakror.jamesconfig.config.config.registry.single_object;

import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.vakror.jamesconfig.config.config.object.ConfigObject;

public abstract class SimpleSidedSingleObjectRegistryConfigImpl<P extends ConfigObject> extends SimpleSingleObjectRegistryConfigImpl<P> {
    private final EnvType side;
    public SimpleSidedSingleObjectRegistryConfigImpl(EnvType side, String subPath, ResourceLocation name) {
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
    public boolean shouldLoad() {
        if (side == null) {
            return true;
        }
        if (side == EnvType.CLIENT) {
            return Platform.getEnv().equals(EnvType.CLIENT);
        } else {
            return Platform.getEnv().equals(EnvType.SERVER);
        }
    }
}
