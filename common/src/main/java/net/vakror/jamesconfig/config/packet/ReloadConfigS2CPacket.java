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

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ReloadConfigS2CPacket {
    private final Collection<ResourceLocation> locations;

    public ReloadConfigS2CPacket(ResourceLocation locations) {
        this.locations = List.of(Objects.requireNonNullElseGet(locations, () -> new ResourceLocation("")));
    }

    public ReloadConfigS2CPacket(Collection<ResourceLocation> locations) {
        this.locations = locations;
    }

    public ReloadConfigS2CPacket(FriendlyByteBuf buf) {
        this.locations = buf.readList(FriendlyByteBuf::readResourceLocation);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeCollection(locations, FriendlyByteBuf::writeResourceLocation);
    }

    public void handle() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (ResourceLocation location : locations) {
            if (JamesConfigMod.CONFIGS.containsKey(location)) {
                Config config = JamesConfigMod.CONFIGS.get(location);
                config.readConfig(false);
            } else {
                MutableComponent component = Component.literal(location.toString()).withStyle(Style.EMPTY.withColor(ChatFormatting.RED).withUnderlined(true));
                Objects.requireNonNull(Minecraft.getInstance().player).sendSystemMessage(Component.translatable("config.not_present_on_client", component));
            }
        }
        stopwatch.stop();
        Objects.requireNonNull(Minecraft.getInstance().player).sendSystemMessage(Component.translatable("config.reload.all.local.success", stopwatch));
    }
}
