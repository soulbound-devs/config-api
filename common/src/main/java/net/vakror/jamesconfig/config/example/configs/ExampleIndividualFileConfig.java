package net.vakror.jamesconfig.config.example.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.Config;
import net.vakror.jamesconfig.config.config.individual.SimpleIndividualFileConfig;

import java.util.ArrayList;
import java.util.List;

public class ExampleIndividualFileConfig extends SimpleIndividualFileConfig<StringWithContents> {
    public static final ResourceLocation NAME = new ResourceLocation(JamesConfigMod.MOD_ID, "exampleconfig");
    public ExampleIndividualFileConfig() {
        super("example/config", NAME, StringWithContents::getName);
    }
    public ExampleIndividualFileConfig(List<StringWithContents> contents) {
        super("example/config", NAME, StringWithContents::getName);
        STRINGS = contents;
    }

    public static List<StringWithContents> STRINGS = new ArrayList<>();

    @Override
    public void addAll(List<StringWithContents> object) {
        STRINGS.addAll(object);
    }

    public static final Codec<ExampleIndividualFileConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            StringWithContents.CODEC.listOf().fieldOf("objects").forGetter(ExampleIndividualFileConfig::getObjects)
    ).apply(instance, ExampleIndividualFileConfig::new));

    @Override
    public Codec<? extends Config<StringWithContents>> getCodec() {
        return CODEC;
    }

    @Override
    public List<StringWithContents> getObjects() {
        return STRINGS;
    }

    @Override
    protected void resetToDefault() {
        STRINGS.add(new StringWithContents("here is an example!", "you can have really cool configs! You can even have custom types, not just these plain boring texts!"));
        STRINGS.add(new StringWithContents("Also Important!", "here, we have individual file configs, but they could also all be in one file!"));
        STRINGS.add(new StringWithContents("important", "if modified manually, they will not be overwritten again unless the config directory is missing or empty!!"));
    }

    @Override
    public Class<StringWithContents> getConfigObjectClass() {
        return StringWithContents.class;
    }
}
