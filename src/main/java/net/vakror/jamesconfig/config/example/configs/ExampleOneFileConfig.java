package net.vakror.jamesconfig.config.example.configs;

import com.google.gson.annotations.Expose;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.individual.SimpleIndividualFileConfig;
import net.vakror.jamesconfig.config.config.one.SimpleOneFileConfig;

import java.util.ArrayList;
import java.util.List;

public class ExampleOneFileConfig extends SimpleOneFileConfig<StringWithContents> {
    public static final ResourceLocation NAME = new ResourceLocation(JamesConfigMod.MOD_ID, "exampleonefileconfig");
    public ExampleOneFileConfig() {
        super(StringWithContents.CODEC, "example/one_config", NAME);
    }

    @Expose
    //MUST NOT BE STATIC
    //you can use a static instance field to access these configs from
    public List<StringWithContents> STRINGS = new ArrayList<>();

    @Override
    public List<StringWithContents> getObjects() {
        return STRINGS;
    }

    @Override
    public String getFileName() {
        return "example 2";
    }

    @Override
    protected void resetToDefault() {
        STRINGS.add(new StringWithContents("here is an example!", "these are all in ONE file, not multiple as you can see in the other config!"));
        STRINGS.add(new StringWithContents("important", "this can become messy with a lot of config objects though"));
    }
}
