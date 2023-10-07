package net.vakror.jamesconfig.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vakror.jamesconfig.config.event.GetConfigTypeAdaptersEvent;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public abstract class SimpleConfig<P> extends Config<P> {

    private final String subPath;
    private final ResourceLocation name;

    public SimpleConfig(String subPath, ResourceLocation name) {
        this.subPath = subPath;
        this.name = name;
        setGSON();
    }


    @Override
    public String getSubPath() {
        return subPath;
    }

    @Override
    public ResourceLocation getName() {
        return name;
    }

    @Override
    public void add(P object) {
        getObjects().add(object);
    }

    @Override
    public Map<Type, Object> getTypeAdapters() {
        GetConfigTypeAdaptersEvent event = new GetConfigTypeAdaptersEvent(name);
        boolean cancelled = FMLJavaModLoadingContext.get().getModEventBus().post(event);
        return cancelled ? new HashMap<>(): event.getAdapters();
    }
}

