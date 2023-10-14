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
     * @param name the name of the setting to set a value to (in case the config's name is null
     * @param value the value to set the setting to
     */
    public abstract void setValue(String name, ConfigObject value);

    /**
     * Queries a {@link Map}<{@link String}, {@link ConfigObject}>, where {@link String} is the name of the setting, and {@link ConfigObject} is the value of the setting
     * @return all of the values contained in this object
     */
    public abstract Map<String, ConfigObject> getValues();

    /**
     * Query the settings that can be present in this config object
     * @return
     */
    public abstract List<ConfigObject> getRequiredSettings();

    /**
     * Query the settings that can be present in this config object
     * @return
     */
    public abstract Map<String, ConfigObject> getRequiredSettingsAsMap();

    public abstract SettingConfigObject newDefinition(String name);

    default void setRequiredSettingsMap() {
        if (getRequiredSettingsAsMap().isEmpty()) {
            for (ConfigObject requiredSetting : getRequiredSettings()) {
                getRequiredSettingsAsMap().put(requiredSetting.getName(), requiredSetting);
            }
        }
    }

    public abstract boolean hasValue(String name);

    @Override
    default JsonObject serialize() {
        JsonObject object = new JsonObject();
        for (String key : getValues().keySet()) {
            object.add(key, getValues().get(key).serialize());
        }
        return object;
    }

    @Override
    default ConfigObject deserialize(@Nullable String name, JsonElement element, ConfigObject defaultValue) {
        return null;
    }

    default ConfigObject deserializeSettingValues(String key, JsonObject jsonObject, String configName) {
        setRequiredSettingsMap();
        SettingConfigObject definition = newDefinition(key);
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String jsonKey = entry.getKey();
            if (!getRequiredSettingsAsMap().containsKey(jsonKey)) {
                JamesConfigMod.LOGGER.error("Key {} present in object {} of config {}, even though it was not requested", jsonKey, key, configName);
            } else {
                ConfigObject object = getRequiredSettingsAsMap().get(jsonKey);
                if (object instanceof SettingConfigObject settingConfigObject) {
                    if (!entry.getValue().isJsonObject()) {
                        JamesConfigMod.LOGGER.error("Config setting definition {} of object {} in config {} is not json object", jsonKey, key, configName);
                    }
                    ConfigObject object1 = settingConfigObject.deserializeSettingValues(jsonKey, (JsonObject) entry.getValue(), configName);
                    object1.setName(jsonKey);
                    definition.setValue(jsonKey, object1);
                } else if (object != null){
                    ConfigObject object1 = object.deserialize(jsonKey, entry.getValue(), getRequiredSettingsAsMap().get(jsonKey));
                    object1.setName(jsonKey);
                    definition.setValue(jsonKey, object1);
                }
            }
        }
        definition.setName(key);
        return definition;
    }
}
