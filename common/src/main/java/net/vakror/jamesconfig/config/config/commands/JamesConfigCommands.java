package net.vakror.jamesconfig.config.config.commands;

import net.vakror.jamesconfig.config.manager.CommandManager;

public class JamesConfigCommands {
    public static void registerCommands() {
        CommandManager.INSTANCE.register(new AnalyzeConfigPerformanceCommand());
        CommandManager.INSTANCE.register(new ReloadSpecificConfigServerCommand());
        CommandManager.INSTANCE.register(new ReloadSpecificConfigClientCommand());
    }
}
