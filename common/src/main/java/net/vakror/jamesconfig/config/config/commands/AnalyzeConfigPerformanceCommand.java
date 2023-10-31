package net.vakror.jamesconfig.config.config.commands;

import com.google.common.base.Stopwatch;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.architectury.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.vakror.jamesconfig.JamesConfigMod;

public class AnalyzeConfigPerformanceCommand implements Command {

    public LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("config")
                .then(Commands.literal("analyze")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(0))
                        .executes(this::execute));
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            JamesConfigMod.analyzePerformance();
            if (context.getSource().getEntity() instanceof Player player) {
                stopwatch.stop();

                MutableComponent component2 = Component.literal("Config Performance").withStyle(ChatFormatting.UNDERLINE).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, Platform.getGameFolder().resolve("config performance").toFile().getAbsolutePath())));
                player.sendSystemMessage(Component.translatable("config.analyze.success", stopwatch.toString(), component2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}