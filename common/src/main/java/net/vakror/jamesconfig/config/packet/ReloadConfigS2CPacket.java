package net.vakror.jamesconfig.config.packet;

import com.google.common.base.Stopwatch;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.Config;

import java.util.Objects;

public class ReloadConfigS2CPacket {
    private final ResourceLocation location;

    public ReloadConfigS2CPacket(ResourceLocation location) {
        this.location = Objects.requireNonNullElseGet(location, () -> new ResourceLocation(""));
    }

    public ReloadConfigS2CPacket(FriendlyByteBuf buf) {
        this.location = buf.readResourceLocation();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(location);
    }

    public void handle() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        if (JamesConfigMod.CONFIGS.containsKey(location)) {
            Config config = JamesConfigMod.CONFIGS.get(location);
            config.readConfig(false);
            stopwatch.stop();
            Objects.requireNonNull(Minecraft.getInstance().player).sendSystemMessage(Component.translatable("config.reload.local.success", config.getName(), stopwatch));
        } else {
            MutableComponent component = Component.literal(location.toString()).withStyle(Style.EMPTY.withColor(ChatFormatting.RED).withUnderlined(true));
            Objects.requireNonNull(Minecraft.getInstance().player).sendSystemMessage(Component.translatable("config.invalid_location", component));
        }
    }
}
