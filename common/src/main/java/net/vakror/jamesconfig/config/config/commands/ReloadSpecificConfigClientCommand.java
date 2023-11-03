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

public class ReloadSpecificConfigClientCommand implements Command {
    public LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("config")
                .then(Commands.literal("reload")
                        .then(Commands.literal("single")
                                .then(Commands.literal("local")
                                        .then((Commands.argument("config", new ResourceLocationArgument()).suggests(JamesConfigMod.buildConfigSuggestions()))
                                                .requires(commandSourceStack -> commandSourceStack.hasPermission(Commands.LEVEL_ALL))
                                                .executes(this::execute)))));
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        try {
            ResourceLocation location = context.getArgument("config", ResourceLocation.class);
            if (!JamesConfigMod.CONFIGS.containsKey(location)) {
                MutableComponent component = Component.literal(location.toString()).withStyle(Style.EMPTY.withColor(ChatFormatting.RED).withUnderlined(true));
                Objects.requireNonNull(context.getSource().getEntity()).sendSystemMessage(Component.translatable("config.invalid_location", component));
                return 0;
            }
            if (context.getSource().isPlayer()) {
                ArchModPackets.sendReloadPacket(context.getSource().getPlayer(), location);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}