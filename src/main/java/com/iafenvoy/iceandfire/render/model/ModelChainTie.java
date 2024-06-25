package com.iafenvoy.iceandfire.render.model;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.citadel.client.model.basic.BasicEntityModel;
import com.iafenvoy.citadel.client.model.basic.BasicModelPart;
import com.iafenvoy.iceandfire.entity.EntityChainTie;

public class ModelChainTie extends BasicEntityModel<EntityChainTie> {
    public final BasicModelPart knotRenderer;

    public ModelChainTie() {
        this(0, 0, 32, 32);
    }

    public ModelChainTie(int width, int height, int texWidth, int texHeight) {
        this.knotRenderer = new BasicModelPart(this, width, height);
        this.knotRenderer.addBox(-4.0F, 2.0F, -4.0F, 8, 12, 8, 1.0F);
        this.knotRenderer.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    @Override
    public void setAngles(EntityChainTie entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.knotRenderer.rotateAngleY = headYaw * 0.017453292F;
        this.knotRenderer.rotateAngleX = headPitch * 0.017453292F;
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.knotRenderer);
    }
}
