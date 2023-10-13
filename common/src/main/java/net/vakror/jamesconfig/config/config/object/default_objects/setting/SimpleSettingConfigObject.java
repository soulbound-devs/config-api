package net.vakror.jamesconfig.config.config.object.default_objects.setting;

import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class SimpleSettingConfigObject extends SettingConfigObject {
    private final Map<String, ConfigObject> values = new HashMap<>();

    public SimpleSettingConfigObject(String name) {
        super(name);
    }

    @Override
    public void setValue(String name, ConfigObject value) {
        this.setRequiredSettingsMap();
        if (!requiredSettings.containsKey(name)) {
            JamesConfigMod.LOGGER.error("Attempted to add setting \"{}\" to object \"{}\" which is not found in required values, not adding value. Required values are: {}", name, getName(), getRequiredSettingsAsString(requiredSettings));
        } else {
            values.put(name, value);
        }
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
    public ResourceLocation getType() {
        return null;
    }

    public Map<String, ConfigObject> getValues() {
        return values;
    }
}
