package net.vakror.jamesconfig.config.manager;

import dev.architectury.event.EventResult;
import net.vakror.jamesconfig.config.config.Config;
import net.vakror.jamesconfig.config.event.ConfigEvents;

import java.util.List;

public abstract class Manager<P> {

    public abstract void register();

    public abstract void register(P object);

    public abstract List<P> getAll();
}
