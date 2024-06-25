package com.iafenvoy.iceandfire.render.model;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.citadel.client.model.AdvancedEntityModel;
import com.iafenvoy.citadel.client.model.AdvancedModelBox;
import com.iafenvoy.citadel.client.model.basic.BasicModelPart;
import net.minecraft.client.model.ModelPart;
import net.minecraft.entity.LivingEntity;

public class ModelPixieHouse extends AdvancedEntityModel<LivingEntity> {
    public final AdvancedModelBox stalk;
    public final AdvancedModelBox cap1;
    public final AdvancedModelBox grass;
    public final AdvancedModelBox grass2;
    public final AdvancedModelBox cap2;
    public final AdvancedModelBox stalk2;

    public ModelPixieHouse() {
        this.texWidth = 128;
        this.texHeight = 64;
        this.stalk2 = new AdvancedModelBox(this, 4, 24);
        this.stalk2.setPos(-4.4F, -3.1F, 0.8F);
        this.stalk2.addBox(-1.0F, -5.5F, -1.0F, 2, 6, 2, 0.0F);
        this.setRotateAngle(this.stalk2, 0.091106186954104F, -0.045553093477052F, -1.2292353921796064F);
        this.cap2 = new AdvancedModelBox(this, 0, 44);
        this.cap2.setPos(0.0F, -1.9F, 0.0F);
        this.cap2.addBox(-6.0F, -8.0F, -6.0F, 12, 3, 12, 0.0F);
        this.grass = new AdvancedModelBox(this, 72, 45);
        this.grass.mirror = true;
        this.grass.setPos(-2.8F, -1.4F, 5.9F);
        this.grass.addBox(-2.1F, -6.5F, -6.9F, 2, 8, 7, 0.0F);
        this.setRotateAngle(this.grass, 0.0F, 0.5462880558742251F, 0.0F);
        this.cap1 = new AdvancedModelBox(this, 0, 21);
        this.cap1.setPos(0.0F, -5.0F, 0.0F);
        this.cap1.addBox(-8.0F, -8.0F, -8.0F, 16, 3, 16, 0.0F);
        this.grass2 = new AdvancedModelBox(this, 48, 43);
        this.grass2.mirror = true;
        this.grass2.setPos(4.4F, -1.4F, -6.0F);
        this.grass2.addBox(-0.9F, -6.5F, -6.0F, 3, 8, 8, 0.0F);
        this.setRotateAngle(this.grass2, 0.0F, -2.6406831582674206F, 0.0F);
        this.stalk = new AdvancedModelBox(this, 0, 0);
        this.stalk.setPos(0.0F, 24.0F, 0.0F);
        this.stalk.addBox(-4.5F, -10.0F, -4.5F, 9, 10, 9, 0.0F);
        this.cap1.addChild(this.stalk2);
        this.cap1.addChild(this.cap2);
        this.stalk.addChild(this.grass);
        this.stalk.addChild(this.cap1);
        this.stalk.addChild(this.grass2);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.stalk);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.stalk);
    }

    @Override
    public void setAngles(LivingEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

    }


    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.pitch = x;
        modelRenderer.yaw = y;
        modelRenderer.roll = z;
    }
}
