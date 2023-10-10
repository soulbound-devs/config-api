package net.vakror.jamesconfig.config.config.individual;

import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.event.ConfigEvents;
import net.vakror.jamesconfig.config.event.GetConfigTypeAdaptersEvent;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class SimpleIndividualFileConfig<P> extends IndividualFileConfig<P> {

    private final String subPath;
    private final ResourceLocation name;
    private final Function<P, String> nameGetter;
    private boolean valid = true;


    public SimpleIndividualFileConfig(String subPath, ResourceLocation name, Function<P, String> nameGetter) {
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
    public boolean shouldClearBeforeSync() {
        return true;
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
    protected void resetToDefault() {
    }

    @Override
    public String getName(P object) {
        return nameGetter.apply(object);
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
    public void addAll(List<P> object) {
        getObjects().addAll(object);
    }

    @Override
    public boolean shouldSync() {
        return true;
    }

    @Override
    public Map<Type, Object> getTypeAdapters() {
        GetConfigTypeAdaptersEvent event = new GetConfigTypeAdaptersEvent(name);
        boolean cancelled = ConfigEvents.REGISTER_CONFIG_TYPE_ADAPTERS.invoker().post(event).interruptsFurtherEvaluation();
        return cancelled ? new HashMap<>(): event.getAdapters();
    }
}

