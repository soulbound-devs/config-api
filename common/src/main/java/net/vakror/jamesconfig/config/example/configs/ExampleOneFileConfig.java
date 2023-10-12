package net.vakror.jamesconfig.config.example.configs;

import com.google.gson.annotations.Expose;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.Config;
import net.vakror.jamesconfig.config.config.individual.SimpleIndividualFileConfig;
import net.vakror.jamesconfig.config.config.one.SimpleOneFileConfig;

import java.util.ArrayList;
import java.util.List;

public class ExampleOneFileConfig extends SimpleOneFileConfig<StringWithContents> {
    public static final ResourceLocation NAME = new ResourceLocation(JamesConfigMod.MOD_ID, "exampleonefileconfig");
    public ExampleOneFileConfig() {
        super(StringWithContents.CODEC, "example/one_config", NAME, StringWithContents::getName);
    }
    public ExampleOneFileConfig(List<StringWithContents> list) {
        super(StringWithContents.CODEC, "example/one_config", NAME, StringWithContents::getName);
        this.addAll(list);
    }

    public static final ExampleOneFileConfig INSTANCE = new ExampleOneFileConfig();

    @Expose
    //MUST NOT BE STATIC IN A ONE FILE CONFIG
    //you can use a static instance field to access these configs from
    public List<StringWithContents> STRINGS = new ArrayList<>();

    public static final Codec<ExampleOneFileConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            StringWithContents.CODEC.listOf().fieldOf("objects").forGetter(ExampleOneFileConfig::getObjects)
    ).apply(instance, ExampleOneFileConfig::new));
    @Override
    public Codec<? extends Config<StringWithContents>> getCodec() {
        return CODEC;
    }

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
