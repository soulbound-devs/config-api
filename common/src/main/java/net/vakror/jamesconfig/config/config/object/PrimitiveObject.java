package net.vakror.jamesconfig.config.config.object;

import net.minecraft.resources.ResourceLocation;

public abstract class PrimitiveObject<P> extends SimpleCodecConfigObject<P> {
    protected final P content;
    protected String name;

    public PrimitiveObject(P content, String name) {
        super(name);
        this.content = content;
    }

    public PrimitiveObject(P content) {
        super(null);
        this.content = content;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name == null ? content.toString(): name;
    }

    @Override
    public ResourceLocation getType() {
        return null;
    }

    @Override
    public P getValue() {
        return content;
    }
}
