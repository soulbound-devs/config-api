package net.vakror.jamesconfig.config.config.object.default_objects.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class SettingConfigObject extends ConfigObject {
    public SettingConfigObject(String name) {
        super(name);
    }

    public abstract void setValue(String name, ConfigObject value);

    public abstract Map<String, ConfigObject> getValues();

    public abstract Map<String, ConfigObject> getRequiredSettings();

    public abstract SettingConfigObject newDefinition(String name);

    @Override
    public JsonObject serialize() {
        JsonObject object = new JsonObject();
        for (String key : getValues().keySet()) {
            object.add(key, getValues().get(key).serialize());
        }
        return object;
    }

    @Override
    public ConfigObject deserialize(@Nullable String name, JsonElement element) {
        return null;
    }

    public ConfigObject deserializeSettingValues(String key, JsonObject jsonObject, String configName) {
        SettingConfigObject definition = newDefinition(key);
        for (String jsonKey : jsonObject.keySet()) {
            if (!getRequiredSettings().containsKey(jsonKey)) {
                JamesConfigMod.LOGGER.error("Key {} present in object {} of config {}, even though it was not requested", jsonKey, key, configName);
            } else {
                ConfigObject object = getRequiredSettings().get(jsonKey);
                if (object instanceof SettingConfigObject settingConfigObject) {
                    if (!jsonObject.get(jsonKey).isJsonObject()) {
                        JamesConfigMod.LOGGER.error("Config setting definition {} of object {} in config {} is not json object", jsonKey, key, configName);
                    }
                    definition.setValue(jsonKey, settingConfigObject.deserializeSettingValues(jsonKey, (JsonObject) jsonObject.get(jsonKey), configName));
                } else if (object != null){
                    definition.setValue(jsonKey, object.deserialize(jsonKey, jsonObject.get(jsonKey)));
                }
            }
        }
        return definition;
    }
}
