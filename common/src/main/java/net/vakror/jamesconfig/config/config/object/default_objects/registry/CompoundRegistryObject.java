package net.vakror.jamesconfig.config.config.object.default_objects.registry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import net.vakror.jamesconfig.config.config.object.default_objects.setting.SimpleSettingConfigObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A compound object to be used in registry configs
 * Unlike {@link SimpleSettingConfigObject}, this class is not meant to be extended
 */
public class CompoundRegistryObject extends RegistryConfigObject {
    /**
     * a list of sub-objects to serialize and deserialize
     * do not modify directly, instead, use {@link #addObject}
     */
    public List<ConfigObject> objects = new ArrayList<>();

    /**
     * A simple constructor which initializes {@link #objects}
     * @param name the name of this registry object
     * @param objects the initial value of {@link #objects}
     */
    public CompoundRegistryObject(String name, List<ConfigObject> objects) {
        this(name);
        this.objects = objects;
    }

    /**
     * @param name the name of this registry object
     */
    public CompoundRegistryObject(String name) {
        super(name, new ResourceLocation(JamesConfigMod.MOD_ID, "compound"));
    }

    /**
     * a simple method which adds objects to the compound
     * @param object the object to add
     */
    public void addObject(ConfigObject object) {
        objects.add(object);
    }

    /**
     * A mirror method to {@link #deserialize} to serialize this compound and all children, including other compounds into a {@link JsonElement}
     * @return the serialized version of this compound
     */
    @Override
    public JsonElement serialize() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", getType().toString());
        for (ConfigObject object : objects) {
            jsonObject.add(object.getName(), object.serialize());
        }
        return jsonObject;
    }

    /**
     * A mirror method to {@link #serialize)} which deserializes this compound and all children, including other compounds, from a {@link JsonElement}
     * @param name the name of the compound
     * @param element the {@link JsonElement} to deserialize from
     * @param defaultValue the default value – used if the element is invalid or the wrong type to reset the value of the primitive to default, null if called from a registry config
     * @param configName the name of the config containing this value
     * @return the deserialized form of the compound
     */
    @Override
    public ConfigObject deserialize(String name, JsonElement element, ConfigObject defaultValue, String configName) {
        JsonObject object = (JsonObject) element;
        CompoundRegistryObject compoundObject = new CompoundRegistryObject(name);

        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            String key = entry.getKey();
            ConfigObject configObject = ConfigObject.deserializeUnknown(key, object.get(key), this.getName());
            if (configObject != null) {
                compoundObject.addObject(configObject);
            }
        }
        compoundObject.setName(name);
        return compoundObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompoundRegistryObject that)) return false;
        return Objects.equals(objects, that.objects) && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
