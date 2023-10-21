package net.vakror.jamesconfig.config.event;

import java.util.ArrayList;
import java.util.List;

public class RegisterEvent<P> {
    List<P> objects = new ArrayList<>();

    public void register(P object) {
        objects.add(object);
    }
    public List<P> getAll() {
        return objects;
    }
}
