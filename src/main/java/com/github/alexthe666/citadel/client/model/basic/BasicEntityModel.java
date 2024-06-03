package com.github.alexthe666.citadel.client.model.basic;

import java.util.function.Function;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public abstract class BasicEntityModel<T extends Entity> extends EntityModel<T> {
    public int textureWidth = 64;
    public int textureHeight = 32;

    protected BasicEntityModel() {
        this(RenderLayer::getEntityCutoutNoCull);
    }

    protected BasicEntityModel(Function<Identifier, RenderLayer> p_102613_) {
        super(p_102613_);
    }

    @Override
    public void render(MatrixStack p_103013_, VertexConsumer p_103014_, int p_103015_, int p_103016_, float p_103017_, float p_103018_, float p_103019_, float p_103020_) {
        this.parts().forEach((p_103030_) -> {
            p_103030_.render(p_103013_, p_103014_, p_103015_, p_103016_, p_103017_, p_103018_, p_103019_, p_103020_);
        });
    }

    public abstract Iterable<BasicModelPart> parts();

    @Override
    public abstract void setAngles(T p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_, float p_102623_);

    @Override
    public void animateModel(T p_102614_, float p_102615_, float p_102616_, float p_102617_) {
    }
}