package net.vakror.jamesconfig.config.example.configs;

import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.default_objects.CompoundObject;
import net.vakror.jamesconfig.config.config.object.default_objects.StringWithContents;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.BooleanPrimitiveObject;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.StringPrimitiveObject;
import net.vakror.jamesconfig.config.config.registry.SimpleRegistryConfigImpl;

public class ExampleRegistryConfigImpl extends SimpleRegistryConfigImpl {
    public static final ResourceLocation NAME = new ResourceLocation(JamesConfigMod.MOD_ID, "exampleconfig");
    public ExampleRegistryConfigImpl() {
        super("example/config", NAME);
    }

    @Override
    protected void resetToDefault() {
        add(new StringWithContents("here is an example!", "you can have really cool configs! You can even have custom types, not just these plain boring texts!"));
        add(new StringWithContents("Also Important!", "here, we have individual file configs, but they could also all be in one file!"));
        add(new StringWithContents("important", "if modified manually, they will not be overwritten again unless the config directory is missing or empty!!"));
        add(new StringPrimitiveObject("there can be multiple types of objects in a config"));

        CompoundObject test = new CompoundObject("test object");
        CompoundObject object = new CompoundObject("does nesting work");
        object.addObject(new StringPrimitiveObject("huh", "test"));
        test.addObject(object);
        test.addObject(new BooleanPrimitiveObject(true, "doesThisWork"));

        add(test);
    }
}
