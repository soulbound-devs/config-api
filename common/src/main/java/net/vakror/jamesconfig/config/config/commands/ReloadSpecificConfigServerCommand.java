package net.vakror.jamesconfig.config.config.commands;

import com.google.common.base.Stopwatch;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.Config;
import net.vakror.jamesconfig.config.packet.ArchModPackets;

import java.util.Objects;

public class ReloadSpecificConfigServerCommand implements Command {
    public LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("config")
                .then(Commands.literal("reload")
                        .then(Commands.literal("single")
                                .then(Commands.literal("server")
                                        .then((Commands.argument("config", new ResourceLocationArgument()).suggests(JamesConfigMod.buildConfigSuggestions()))
                                                .then(Commands.argument("syncToClients", BoolArgumentType.bool())
                                                        .requires(commandSourceStack -> commandSourceStack.hasPermission(Commands.LEVEL_MODERATORS))
                                                        .executes(this::execute))))));
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            ResourceLocation location = context.getArgument("config", ResourceLocation.class);
            boolean syncToClients = context.getArgument("syncToClients", Boolean.class);
            if (JamesConfigMod.CONFIGS.containsKey(location)) {
                Config config = JamesConfigMod.CONFIGS.get(location);
                config.readConfig(false);
                if (context.getSource().isPlayer()) {
                    stopwatch.stop();
                    Objects.requireNonNull(context.getSource().getEntity()).sendSystemMessage(Component.translatable("config.reload.remote.success", config.getName(), stopwatch));
                }
            } else {
                MutableComponent component = Component.literal(location.toString()).withStyle(Style.EMPTY.withColor(ChatFormatting.RED).withUnderlined(true));
                Objects.requireNonNull(context.getSource().getEntity()).sendSystemMessage(Component.translatable("config.invalid_location", component));
                return 0;
            }
            if (syncToClients) {
                ArchModPackets.sendSyncPacket(context.getSource().getLevel().players(), location);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}