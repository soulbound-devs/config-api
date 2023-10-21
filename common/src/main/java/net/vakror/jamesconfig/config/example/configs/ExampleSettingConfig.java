package net.vakror.jamesconfig.config.example.configs;

import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.ConfigObject;
import net.vakror.jamesconfig.config.config.object.default_objects.setting.SettingConfigObject;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.BooleanPrimitiveObject;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.StringPrimitiveObject;
import net.vakror.jamesconfig.config.config.object.default_objects.setting.SimpleSettingConfigObject;
import net.vakror.jamesconfig.config.config.setting.SimpleSettingConfigImpl;

import java.util.List;

public class ExampleSettingConfig extends SimpleSettingConfigImpl {
    public ExampleSettingConfig() {
        super("example/config/setting", new ResourceLocation(JamesConfigMod.MOD_ID, "setting"));
    }

    @Override
    public List<ConfigObject> getRequiredSettings() {
        return List.of(
                new TestSettingObject("this is a setting config").setValue("all the files in this directory will not be loaded",
                        new StringPrimitiveObject("into the config, only this one will with exactly these options")), new BooleanPrimitiveObject("doesThisWork", true));
    }

    public static class TestSettingObject extends SimpleSettingConfigObject {
        public TestSettingObject(String name) {
            super(name);
        }

        @Override
        public List<ConfigObject> getRequiredSettings() {
            return List.of(new StringPrimitiveObject("all the files in this directory will not be loaded", ""));
        }

        @Override
        public SettingConfigObject newDefinition(String name)  {
            return new TestSettingObject(name);
        }
    }
}
