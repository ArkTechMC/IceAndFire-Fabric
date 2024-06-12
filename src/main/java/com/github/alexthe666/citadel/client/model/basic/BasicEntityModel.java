package com.github.alexthe666.citadel.client.model.basic;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public abstract class BasicEntityModel<T extends Entity> extends EntityModel<T> {
    public final int textureWidth = 64;
    public final int textureHeight = 32;

    protected BasicEntityModel() {
        this(RenderLayer::getEntityCutoutNoCull);
    }

    protected BasicEntityModel(Function<Identifier, RenderLayer> p_102613_) {
        super(p_102613_);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.parts().forEach(part -> part.render(matrices, vertices, light, overlay, red, green, blue, alpha));
    }

    public abstract Iterable<BasicModelPart> parts();

    @Override
    public abstract void setAngles(T p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_);

    @Override
    public void animateModel(T p_102614_, float p_102615_, float p_102616_, float p_102617_) {
    }
}