package net.vakror.jamesconfig.config.manager;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleManager<P> extends Manager<P> {
    List<P> values = new ArrayList<>();

    @Override
    public void register(P object) {
        values.add(object);
    }

    @Override
    public List<P> getAll() {
        return values;
    }
}
