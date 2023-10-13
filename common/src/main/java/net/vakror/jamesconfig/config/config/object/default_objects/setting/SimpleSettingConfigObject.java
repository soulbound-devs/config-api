package net.vakror.jamesconfig.config.config.object.default_objects.setting;

import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.ConfigObject;

import java.util.HashMap;
import java.util.Map;

public abstract class SimpleSettingConfigObject extends SettingConfigObject {
    private final Map<String, ConfigObject> values = new HashMap<>();


    /**
     * names are not needed, as they are declared in the setting config
     */
    public SimpleSettingConfigObject() {
        super(null);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {
    }

    @Override
    public void setValue(String name, ConfigObject value) {
        if (!getRequiredSettings().containsKey(name)) {
            JamesConfigMod.LOGGER.error("Attempted to add setting \"{}\" which is not found in required values, not adding value", name);
        } else {
            values.put(name, value);
        }
    }

    @Override
    public ResourceLocation getType() {
        return null;
    }

    public Map<String, ConfigObject> getValues() {
        return values;
    }
}
