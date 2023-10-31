package net.vakror.jamesconfig.config.config.registry.multi_object;

import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

/**
 * setting the side to server will ONLY load it when it is on dedicated server, not internal server
 */
public abstract class SimpleSidedMultiObjectRegistryConfigImpl extends SimpleMultiObjectRegistryConfigImpl {
    private final EnvType side;
    public SimpleSidedMultiObjectRegistryConfigImpl(EnvType side, String subPath, ResourceLocation name) {
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
        return Objects.equals(Platform.getEnv(), side);
    }
}
