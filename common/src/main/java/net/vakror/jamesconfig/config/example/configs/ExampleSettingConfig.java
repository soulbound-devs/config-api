package net.vakror.jamesconfig.config.example.configs;

import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import net.vakror.jamesconfig.config.config.object.default_objects.setting.SettingConfigObject;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.BooleanPrimitiveObject;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.StringPrimitiveObject;
import net.vakror.jamesconfig.config.config.object.default_objects.setting.SimpleSettingConfigObject;
import net.vakror.jamesconfig.config.config.setting.SimpleSettingConfigImpl;
import java.util.HashMap;
import java.util.Map;

public class ExampleSettingConfig extends SimpleSettingConfigImpl {
    public ExampleSettingConfig() {
        super("example/config/setting", new ResourceLocation(JamesConfigMod.MOD_ID, "setting"), "settingTest");
    }

    @Override
    public Map<String, ConfigObject> getRequiredSettings() {
        Map<String, ConfigObject> map = new HashMap<>();
        map.put("this is a setting config", new TestSettingObject());
        map.put("doesThisWork", new BooleanPrimitiveObject(false));
        return map;
    }

    public static class TestSettingObject extends SimpleSettingConfigObject {

        @Override
        public Map<String, ConfigObject> getRequiredSettings() {
            return Map.of("all the files in this directory will not be loaded", new StringPrimitiveObject(""));
        }

        @Override
        public SettingConfigObject newDefinition(String name)  {
            return new TestSettingObject();
        }
    }

    @Override
    public Map<String, ConfigObject> getDefaultValues() {
        Map<String, ConfigObject> map = new HashMap<>();
        TestSettingObject object = new TestSettingObject();
        object.setValue("all the files in this directory will not be loaded", new StringPrimitiveObject("into the config, only this one will with exactly these options"));
        map.put("this is a setting config", object);
        map.put("doesThisWork", new BooleanPrimitiveObject(true));
        return map;
    }
}
