package net.vakror.jamesconfig.config.config.commands;

import com.google.common.base.Stopwatch;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.Config;
import net.vakror.jamesconfig.config.packet.ArchModPackets;

import java.util.Objects;

public class ReloadAllConfigsServerCommand implements Command {
    public LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("config")
                .then(Commands.literal("reload")
                        .then(Commands.literal("all")
                                .then(Commands.literal("server")
                                        .then(Commands.argument("syncToClients", BoolArgumentType.bool())
                                                .requires(commandSourceStack -> commandSourceStack.hasPermission(Commands.LEVEL_MODERATORS))
                                                .executes(this::execute)))));
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            boolean syncToClients = context.getArgument("syncToClients", Boolean.class);
            for (ResourceLocation location : JamesConfigMod.CONFIGS.keySet()) {
                Config config = JamesConfigMod.CONFIGS.get(location);
                config.readConfig(false);
            }
            if (context.getSource().isPlayer()) {
                stopwatch.stop();
                Objects.requireNonNull(context.getSource().getEntity()).sendSystemMessage(Component.translatable("config.reload.all.remote.success", stopwatch));
            }
            if (syncToClients) {
                ArchModPackets.sendSyncPacket(context.getSource().getLevel().players());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}