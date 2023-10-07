package net.vakror.jamesconfig.config.example.configs;

import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.SimpleConfig;

import java.util.ArrayList;
import java.util.List;

public class ExampleConfig extends SimpleConfig<StringWithContents> {
    public static final ResourceLocation NAME = new ResourceLocation(JamesConfigMod.MOD_ID, "exampleconfig");
    public ExampleConfig() {
        super("example/config", NAME);
    }

    public static List<StringWithContents> STRINGS = new ArrayList<>();

    @Override
    public List<StringWithContents> getObjects() {
        return STRINGS;
    }

    @Override
    public String getFileName(StringWithContents object) {
        return object.name;
    }

    @Override
    protected void resetToDefault() {
        STRINGS.add(new StringWithContents("here is an example!", "you can have really cool configs! You can even have custom types, not just these plain boring texts!"));
        STRINGS.add(new StringWithContents("important", "if modified manually, they will not be overwritten again unless the config directory is missing or empty!!"));
    }

    @Override
    public Class<StringWithContents> getConfigClass() {
        return StringWithContents.class;
    }
}
