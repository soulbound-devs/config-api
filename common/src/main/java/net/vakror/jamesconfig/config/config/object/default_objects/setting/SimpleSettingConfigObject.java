package net.vakror.jamesconfig.config.config.object.default_objects.setting;

import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class SimpleSettingConfigObject implements SettingConfigObject {

    /**
     * The name of this object
     */
    String name;

    /**
     * A map of this object's required settings
     * do not update manually
     */
    public Map<String, ConfigObject> requiredSettings = new HashMap<>(getRequiredSettings().size());

    /**
     * A simple constructor that initializes the name of this object
     * @param name the name to set this object's name to
     */
    public SimpleSettingConfigObject(String name) {
        setName(name);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Map<String, ConfigObject> getRequiredSettingsAsMap() {
        return requiredSettings;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }

    private final Map<String, ConfigObject> values = new HashMap<>();

    @Override
    public void setValue(String name, ConfigObject value) {
        this.setRequiredSettingsMap();
        if (!requiredSettings.containsKey(name)) {
            JamesConfigMod.LOGGER.error("Attempted to add setting \"{}\" to object \"{}\" which is not found in required values, not adding value. Required values are: {}", name, getName(), getRequiredSettingsAsString(requiredSettings));
        } else {
            values.put(name, value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleSettingConfigObject that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @NotNull
    public static String getRequiredSettingsAsString(Map<String, ConfigObject> requiredSettings) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (String string : requiredSettings.keySet()) {
            builder.append("\"").append(string).append("\"");
            builder.append(", ");
        }
        builder.append("]");
        return builder.toString();
    }

    @Override
    public boolean hasValue(String name) {
        return values.containsKey(name);
    }

    @Override
    public ResourceLocation getType() {
        return null;
    }

    public Map<String, ConfigObject> getValues() {
        return values;
    }
}
