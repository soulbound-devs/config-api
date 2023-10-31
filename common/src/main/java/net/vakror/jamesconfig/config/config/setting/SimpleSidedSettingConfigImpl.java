package net.vakror.jamesconfig.config.config.setting;

import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public abstract class SimpleSidedSettingConfigImpl extends SimpleSettingConfigImpl {
    private final EnvType side;
    public SimpleSidedSettingConfigImpl(EnvType side, String subPath, ResourceLocation name) {
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
        return Platform.getEnv().equals(side);
    }
}
