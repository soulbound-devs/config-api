package net.vakror.jamesconfig.config.config.object.default_objects.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * A config object with a special deserialize method which checks for required settings
 */
public interface SettingConfigObject extends ConfigObject {
    /**
     * Sets the value of a setting
     * @param name  the name of the setting to set a value to (in case the config's name is null)
     * @param value the value to set the setting to
     */
    void setValue(String name, ConfigObject value);

    /**
     * Queries a {@link Map}<{@link String}, {@link ConfigObject}>, where {@link String} is the name of the setting, and {@link ConfigObject} is the value of the setting
     *
     * @return all of the values contained in this object
     */
    Map<String, ConfigObject> getValues();

    /**
     * Query the settings that can be present in this config object
     *
     * @return the settings that are required to be present in this object
     */
    List<ConfigObject> getRequiredSettings();

    /**
     * Query the settings that can be present in this config object
     * MUST return a mutable map reference to a field!
     *
     * @return required settings as a {@link Map}<{@link String Name}, {@link ConfigObject Object}>
     */
    Map<String, ConfigObject> getRequiredSettingsAsMap();

    /**
     * creates a new instance of this object
     *
     * @param name the name of the object to create
     * @return the object that has been created
     */
    SettingConfigObject newDefinition(String name);

    /**
     * sets the required settings map to the required settings
     */
    default void setRequiredSettingsMap() {
        if (getRequiredSettingsAsMap().isEmpty()) {
            for (ConfigObject requiredSetting : getRequiredSettings()) {
                getRequiredSettingsAsMap().put(requiredSetting.getName(), requiredSetting);
            }
        }
    }

    /**
     * check if this object contains a value of name
     *
     * @param name the name of the value to query
     * @return whether the object contains this value
     */
    boolean hasValue(String name);

    /**
     * serializes this object into a {@link JsonObject}
     *
     * @return the serialized form of this object
     */
    @Override
    default JsonObject serialize() {
        JsonObject object = new JsonObject();
        for (String key : getValues().keySet()) {
            object.add(key, getValues().get(key).serialize());
        }
        return object;
    }

    /**
     * deserializes this object from a {@link JsonObject}
     *
     * @param name the name of this object
     * @param element the object to deserialize from
     * @param defaultValue the default value of this object
     * @param configName the name of the config containing this object
     * @return a {@link ConfigObject} representing the json object
     */
    @Override
    default ConfigObject deserialize(@Nullable String name, JsonElement element, ConfigObject defaultValue, String configName) {
        if (!element.isJsonObject()) {
            JamesConfigMod.LOGGER.error("Config setting definition {} in config {} is not json object", name, getName());
        } else {
            try {
                JsonObject jsonObject = (JsonObject) element;
                setRequiredSettingsMap();
                SettingConfigObject definition = newDefinition(name);
                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    String key = entry.getKey();
                    if (!getRequiredSettingsAsMap().containsKey(key)) {
                        JamesConfigMod.LOGGER.error("Key {} present in object {} of config {}, even though it was not requested", key, name, configName);
                    } else {
                        ConfigObject object = getRequiredSettingsAsMap().get(key);
                        if (object instanceof SettingConfigObject settingConfigObject) {
                            if (!entry.getValue().isJsonObject()) {
                                JamesConfigMod.LOGGER.error("Config setting definition {} of object {} in config {} is not json object", key, name, configName);
                            }
                            ConfigObject object1 = settingConfigObject.deserialize(key, entry.getValue(), getRequiredSettingsAsMap().getOrDefault(key, null), configName);
                            object1.setName(key);
                            definition.setValue(key, object1);
                        } else if (object != null) {
                            ConfigObject object1 = object.deserialize(key, entry.getValue(), getRequiredSettingsAsMap().get(key), configName);
                            object1.setName(key);
                            definition.setValue(key, object1);
                        }
                    }
                }
                definition.setName(name);
                return definition;
            } catch (Exception e) {
                JamesConfigMod.LOGGER.error("Error parsing setting object {}, setting to default", name);
                e.printStackTrace();
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
