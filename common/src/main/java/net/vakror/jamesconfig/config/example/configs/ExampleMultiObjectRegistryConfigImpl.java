package net.vakror.jamesconfig.config.example.configs;

import net.minecraft.resources.ResourceLocation;
import net.vakror.jamesconfig.JamesConfigMod;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.BooleanPrimitiveObject;
import net.vakror.jamesconfig.config.config.object.default_objects.primitive.StringPrimitiveObject;
import net.vakror.jamesconfig.config.config.object.default_objects.registry.CompoundRegistryObject;
import net.vakror.jamesconfig.config.config.registry.multi_object.SimpleMultiObjectRegistryConfigImpl;

public class ExampleMultiObjectRegistryConfigImpl extends SimpleMultiObjectRegistryConfigImpl {
    public static final ResourceLocation NAME = new ResourceLocation(JamesConfigMod.MOD_ID, "exampleconfig");
    public ExampleMultiObjectRegistryConfigImpl() {
        super("example/config", NAME);
    }

    @Override
    protected void resetToDefault() {
        CompoundRegistryObject test = new CompoundRegistryObject("test object");
        CompoundRegistryObject object = new CompoundRegistryObject("this is a registry config");
        object.addObject(new StringPrimitiveObject("will be loaded into the config", "all of these entries"));
        test.addObject(object);
        test.addObject(new BooleanPrimitiveObject("doesThisWork", true));
        add(test);
    }
}
