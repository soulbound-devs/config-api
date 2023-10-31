package net.vakror.jamesconfig.config.example.configs;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.default_objects.StringWithContents;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.StringPrimitiveObject;
import net.vakror.jamesconfig.config.config.registry.single_object.SimpleSingleObjectRegistryConfigImpl;

public class ExampleSingleObjectRegistryConfigImpl extends SimpleSingleObjectRegistryConfigImpl<StringWithContents> {
    public static final ResourceLocation NAME = new ResourceLocation(JamesConfigMod.MOD_ID, "exampleconfigone");
    public ExampleSingleObjectRegistryConfigImpl() {
        super("example/test", NAME);
    }

    @Override
    public StringWithContents decode(JsonObject object) {
        return (StringWithContents) new StringWithContents("", "").deserialize("", object, null, this.getName().toString());
    }

    @Override
    protected void resetToDefault() {
        StringWithContents root = new StringWithContents("root", "hallo");
        StringWithContents test = new StringWithContents("test", "hello?");
        StringWithContents hallo = new StringWithContents("hallo", "does this work?");
        add(root);
        add(test);
        add(hallo);
    }
}
