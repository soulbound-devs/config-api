package net.vakror.jamesconfig.config.config.object.default_objects.setting;

import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * a simple implementation of {@link SettingConfigObject}
 */
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

    /**
     * set the name of this object
     * @param name the name to set this objects name to
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * return the required settings as a map
     * for getting values without looping
     * @return a {@link Map}<{@link String Name}, {@link ConfigObject Object}>
     */
    @Override
    public Map<String, ConfigObject> getRequiredSettingsAsMap() {
        return requiredSettings;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * used get the name of this object
     * @return the name of this object
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * all values that are currently present in this config
     */
    private final Map<String, ConfigObject> values = new HashMap<>();

    /**
     * sets a value to the given
     *
     * @param name  the name of the setting to set a value to (in case the config's name is null)
     * @param value the value to set the setting to
     */
    @Override
    public void setValue(String name, ConfigObject value) {
        this.setRequiredSettingsMap();
        if (value.getName() == null || value.getName().isBlank()) {
            value.setName(name);
        }
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

    /**
     * gets the required settings as a string
     * used for logging
     *
     * @param requiredSettings the required settings
     * @return a string representation of all required settings
     */
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

    /**
     *
     * @param name the name of the value to query
     * @return whether {@link #values} contains that object
     */
    @Override
    public boolean hasValue(String name) {
        return values.containsKey(name);
    }

    /**
     * gets the type of this object
     * returns null because setting configs do not need a type
     * however, it is not final because some may use it in a registry config
     * @return null
     */
    @Override
    public ResourceLocation getType() {
        return null;
    }

    /**
     * gets all values of this object
     * @return {@link #values}
     */
    public Map<String, ConfigObject> getValues() {
        return values;
    }
}
