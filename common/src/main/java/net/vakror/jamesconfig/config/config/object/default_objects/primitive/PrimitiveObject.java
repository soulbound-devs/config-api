package net.vakror.jamesconfig.config.config.object.default_objects.primitive;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.config.config.object.ConfigObject;

public abstract class PrimitiveObject<P> extends ConfigObject {
    protected final P content;
    protected String name;

    public PrimitiveObject(P content, String name) {
        super(name);
        this.content = content;
    }

    public PrimitiveObject(P content) {
        super(null);
        this.content = content;
    }

    @Override
    public ConfigObject deserialize(String name, JsonElement element, ConfigObject defaultValue) {
        this.name = name;
        ConfigObject object = ConfigObject.deserializePrimitive(name, (JsonPrimitive) element);
        if (object == null || !object.getClass().equals(this.getClass())) {
            return defaultValue;
        }
        return object;
    }

    @Override
    public JsonElement serialize() {
        JsonElement element = null;
        if (getValue() instanceof Number number) {
             element = new JsonPrimitive(number);
        }
        if (getValue() instanceof Boolean bool) {
            element = new JsonPrimitive(bool);
        }
        if (getValue() instanceof String string) {
            element = new JsonPrimitive(string);
        }
        return element;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name == null ? content.toString(): name;
    }

    @Override
    public ResourceLocation getType() {
        return null;
    }

    public P getValue() {
        return content;
    }
}
