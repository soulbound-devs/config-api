package net.vakror.jamesconfig.config.config.object.default_objects.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SettingConfigObject extends ConfigObject {
    String name;
    public Map<String, ConfigObject> requiredSettings = new HashMap<>(getRequiredSettings().size());
    public SettingConfigObject(String name) {
        super(name);
    }

    public abstract void setValue(String name, ConfigObject value);

    public abstract Map<String, ConfigObject> getValues();

    public abstract List<ConfigObject> getRequiredSettings();

    public abstract SettingConfigObject newDefinition(String name);

    public final void setRequiredSettingsMap() {
        if (requiredSettings.isEmpty()) {
            for (ConfigObject requiredSetting : getRequiredSettings()) {
                requiredSettings.put(requiredSetting.getName(), requiredSetting);
            }
        }
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public JsonObject serialize() {
        JsonObject object = new JsonObject();
        for (String key : getValues().keySet()) {
            object.add(key, getValues().get(key).serialize());
        }
        return object;
    }

    @Override
    public ConfigObject deserialize(@Nullable String name, JsonElement element, ConfigObject defaultValue) {
        return null;
    }

    public ConfigObject deserializeSettingValues(String key, JsonObject jsonObject, String configName) {
        setRequiredSettingsMap();
        SettingConfigObject definition = newDefinition(key);
        for (String jsonKey : jsonObject.keySet()) {
            if (!requiredSettings.containsKey(jsonKey)) {
                JamesConfigMod.LOGGER.error("Key {} present in object {} of config {}, even though it was not requested", jsonKey, key, configName);
            } else {
                ConfigObject object = requiredSettings.get(jsonKey);
                if (object instanceof SettingConfigObject settingConfigObject) {
                    if (!jsonObject.get(jsonKey).isJsonObject()) {
                        JamesConfigMod.LOGGER.error("Config setting definition {} of object {} in config {} is not json object", jsonKey, key, configName);
                    }
                    ConfigObject object1 = settingConfigObject.deserializeSettingValues(jsonKey, (JsonObject) jsonObject.get(jsonKey), configName);
                    object1.setName(jsonKey);
                    definition.setValue(jsonKey, object1);
                } else if (object != null){
                    ConfigObject object1 = object.deserialize(jsonKey, jsonObject.get(jsonKey), requiredSettings.get(jsonKey));
                    object1.setName(jsonKey);
                    definition.setValue(jsonKey, object1);
                }
            }
        }
        definition.setName(key);
        return definition;
    }
}
