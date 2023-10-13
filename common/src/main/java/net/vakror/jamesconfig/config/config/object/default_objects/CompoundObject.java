package net.vakror.jamesconfig.config.config.object.default_objects;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.ConfigObject;

import java.util.ArrayList;
import java.util.List;

public class CompoundObject extends ConfigObject {
    public List<ConfigObject> objects = new ArrayList<>();
    public String name;

    public CompoundObject(String name, List<ConfigObject> objects) {
        this(name);
        this.objects = objects;
    }

    public CompoundObject(String name) {
        super(name);
    }

    public void addObject(ConfigObject object) {
        objects.add(object);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ResourceLocation getType() {
        return new ResourceLocation(JamesConfigMod.MOD_ID, "compound");
    }

    @Override
    public JsonElement serialize() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        for (ConfigObject object : objects) {
            jsonObject.add(object.getName(), object.serialize());
        }
        return jsonObject;
    }

    @Override
    public ConfigObject deserialize(String name, JsonElement element) {
        JsonObject object = new JsonObject();
        CompoundObject compoundObject = new CompoundObject(object.get("name").getAsString());

        for (String key : object.keySet()) {
            ConfigObject configObject = ConfigObject.deserializeUnknown(key, object.get(key));
            if (configObject != null) {
                compoundObject.addObject(configObject);
            }
        }
        compoundObject.setName(name);
        return compoundObject;
    }
}
