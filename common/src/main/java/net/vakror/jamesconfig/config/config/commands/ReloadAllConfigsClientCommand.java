package net.vakror.jamesconfig.config.config.commands;

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
import net.vakror.jamesconfig.config.packet.ArchModPackets;

import java.util.Objects;

public class ReloadAllConfigsClientCommand implements Command {
    public LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("config")
                .then(Commands.literal("reload")
                        .then(Commands.literal("all")
                        .then(Commands.literal("local")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(Commands.LEVEL_ALL))
                        .executes(this::execute))));
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        try {
            if (context.getSource().isPlayer()) {
                ArchModPackets.sendReloadPacket(context.getSource().getPlayer());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}