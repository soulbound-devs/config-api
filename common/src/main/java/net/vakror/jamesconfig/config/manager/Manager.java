package net.vakror.jamesconfig.config.manager;

import java.util.List;

public abstract class Manager<P> {
    public abstract void register();

    public abstract void register(P object);

    public abstract List<P> getAll();
}
