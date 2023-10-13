package net.vakror.jamesconfig.config.config.object.default_objects;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.ConfigObject;

public class StringWithContents extends ConfigObject {

    public static final Codec<StringWithContents> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("type").forGetter(StringWithContents::getType),
            Codec.STRING.fieldOf("name").forGetter(StringWithContents::getName),
            Codec.STRING.fieldOf("content").forGetter(StringWithContents::getContent)

    ).apply(instance, StringWithContents::new));

    String name;
    private final String content;

    public StringWithContents(ResourceLocation type, String name, String content) {
        this(name, content);
    }

    public StringWithContents(String name, String content) {
        super(name);
        this.content = content;
    }

    @Override
    public ResourceLocation getType() {
        return new ResourceLocation(JamesConfigMod.MOD_ID, "string_with_contents");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public JsonElement serialize() {
        return CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow(false, (s) -> {throw new IllegalStateException(s);});
    }

    @Override
    public ConfigObject deserialize(String name, JsonElement element, ConfigObject defaultValue) {
        return CODEC.decode(JsonOps.INSTANCE, element).getOrThrow(false, (s) -> {throw new IllegalStateException(s);}).getFirst();
    }

    public String getContent() {
        return content;
    }
}
