package net.vakror.jamesconfig.config.example.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class StringWithContents {

    public static final Codec<StringWithContents> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(StringWithContents::getName),
            Codec.STRING.fieldOf("content").forGetter(StringWithContents::getContent)

    ).apply(instance, StringWithContents::new));

    String name;
    String content;

    public StringWithContents(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }
}
