package net.vakror.jamesconfig.config.config.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.packet.ArchModPackets;

public class ReloadSpecificConfigClientCommand implements Command {
    public LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("config")
                .then(Commands.literal("reload")
                        .then(Commands.literal("local")
                        .then((Commands.argument("config", new ResourceLocationArgument()).suggests(JamesConfigMod.buildConfigSuggestions()))
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(Commands.LEVEL_ALL))
                        .executes(this::execute))));
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        try {
            ResourceLocation location = context.getArgument("config", ResourceLocation.class);
            if (context.getSource().isPlayer()) {
                ArchModPackets.sendReloadPacket(context.getSource().getPlayer(), location);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}