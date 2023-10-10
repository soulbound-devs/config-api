package net.vakror.jamesconfig.config.config.one;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.vakror.jamesconfig.config.event.GetConfigTypeAdaptersEvent;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class SimpleOneFileConfig<P> extends OneFileConfig<P> {

    private final String subPath;
    private final ResourceLocation name;
    private final Function<P, String> nameGetter;
    private boolean valid = true;

    public SimpleOneFileConfig(Codec<P> codec, String subPath, ResourceLocation name, Function<P, String> nameGetter) {
        super(codec);
        this.subPath = subPath;
        this.name = name;
        this.nameGetter = nameGetter;
        setGSON();
    }

    @Override
    public void invalidate() {
        this.valid = false;
    }

    @Override
    public boolean isValueAcceptable(P value) {
        return true;
    }

    @Override
    public boolean shouldDiscardConfigOnUnacceptableValue() {
        return false;
    }

    @Override
    public boolean isValid() {
        return valid;
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
    public String getName(P object) {
        return nameGetter.apply(object);
    }

    @Override
    public boolean shouldClearBeforeSync() {
        return true;
    }

    @Override
    public boolean shouldReadConfig() {
        return true;
    }

    @Override
    public boolean shouldAddObject(P object) {
        return true;
    }

    @Override
    public void onAddObject(P object) {
    }

    @Override
    public void discardAllValues() {
        getObjects().clear();
    }

    @Override
    public void discardValue(P object) {
        getObjects().remove(object);
    }

    @Override
    protected void resetToDefault() {
    }

    @Override
    public boolean shouldSync() {
        return true;
    }

    @Override
    public void add(P object) {
        getObjects().add(object);
    }

    @Override
    public void addAll(List<P> object) {
        getObjects().addAll(object);
    }

    @Override
    public Map<Type, Object> getTypeAdapters() {
        GetConfigTypeAdaptersEvent event = new GetConfigTypeAdaptersEvent(name);
        boolean cancelled = MinecraftForge.EVENT_BUS.post(event);
        return cancelled ? new HashMap<>() : event.getAdapters();
    }
}
