package net.vakror.jamesconfig.config.manager;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.vakror.jamesconfig.config.config.commands.Command;

public class CommandManager extends SimpleManager<Command> {

    public static final CommandManager INSTANCE = new CommandManager();

    @Override
    public void register() {
        new ModEvents(this);
    }

    public static class ModEvents {
        private ModEvents(CommandManager manager) {
            CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> manager.getAll().forEach((command) -> dispatcher.register(command.register())));
            CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
            });
        }
    }
}
