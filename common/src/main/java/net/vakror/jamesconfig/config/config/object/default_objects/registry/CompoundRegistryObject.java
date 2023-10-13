package net.vakror.jamesconfig.config.config.object.default_objects.registry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.ConfigObject;

import java.util.ArrayList;
import java.util.List;

public class CompoundRegistryObject extends RegistryConfigObject {
    public List<ConfigObject> objects = new ArrayList<>();

    public CompoundRegistryObject(String name, List<ConfigObject> objects) {
        this(name);
        this.objects = objects;
    }

    public CompoundRegistryObject(String name) {
        super(name, new ResourceLocation(JamesConfigMod.MOD_ID, "compound"));
    }
    
    public void addObject(ConfigObject object) {
        objects.add(object);
    }

    @Override
    public JsonElement serialize() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", getType().toString());
        for (ConfigObject object : objects) {
            jsonObject.add(object.getName(), object.serialize());
        }
        return jsonObject;
    }

    @Override
    public ConfigObject deserialize(String name, JsonElement element, ConfigObject defaultValue) {
        JsonObject object = (JsonObject) element;
        CompoundRegistryObject compoundObject = new CompoundRegistryObject(name);

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
