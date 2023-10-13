package net.vakror.jamesconfig.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(JsonPrimitive.class)
public abstract class JsonPrimitiveAccessor extends JsonElement {
    @Accessor("value")
    public Object getValue() {
        throw new AssertionError();
    }
}
