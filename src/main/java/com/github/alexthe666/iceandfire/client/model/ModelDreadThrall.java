package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.HideableModelRenderer;
import com.github.alexthe666.iceandfire.entity.EntityDreadThrall;
import net.minecraft.client.render.entity.model.BipedEntityModel;

public class ModelDreadThrall extends ModelDreadBase<EntityDreadThrall> {

    public ModelDreadThrall(float modelScale, boolean bodyArmorModel) {
        this.texHeight = 32;
        this.texWidth = 64;
        this.leftArmPose = BipedEntityModel.ArmPose.EMPTY;
        this.rightArmPose = BipedEntityModel.ArmPose.EMPTY;
        this.body = new HideableModelRenderer(this, 16, 16);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelScale);
        this.body.setPos(0.0F, 0.0F, 0.0F);
        this.armRight = new HideableModelRenderer(this, 40, 16);
        this.armRight.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelScale);
        this.armRight.setPos(-5.0F, 2.0F, 0.0F);
        this.armLeft = new HideableModelRenderer(this, 40, 16);
        this.armLeft.mirror = true;
        this.armLeft.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelScale);
        this.armLeft.setPos(5.0F, 2.0F, 0.0F);
        this.legRight = new HideableModelRenderer(this, 0, 16);
        this.legRight.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelScale);
        this.legRight.setPos(-2.0F, 12.0F, 0.0F);
        this.legLeft = new HideableModelRenderer(this, 0, 16);
        this.legLeft.mirror = true;
        this.legLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelScale);
        this.legLeft.setPos(2.0F, 12.0F, 0.0F);
        this.head = new HideableModelRenderer(this, 0, 0);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelScale - 0.5F);
        this.head.setPos(0.0F, 0.0F, 0.0F);
        this.headware = new HideableModelRenderer(this, 32, 0);
        this.headware.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelScale);
        this.headware.setPos(0.0F, 0.0F, 0.0F);
        if (bodyArmorModel) {
            this.head = new HideableModelRenderer(this, 0, 0);
            this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelScale);
            this.head.setPos(0.0F, 0.0F + 0.0F, 0.0F);
            this.headware = new HideableModelRenderer(this, 32, 0);
            this.headware.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelScale + 0.5F);
            this.headware.setPos(0.0F, 0.0F + 0.0F, 0.0F);
            this.body = new HideableModelRenderer(this, 16, 16);
            this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelScale);
            this.body.setPos(0.0F, 0.0F + 0.0F, 0.0F);
            this.armRight = new HideableModelRenderer(this, 40, 16);
            this.armRight.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelScale);
            this.armRight.setPos(-5.0F, 2.0F + 0.0F, 0.0F);
            this.armLeft = new HideableModelRenderer(this, 40, 16);
            this.armLeft.mirror = true;
            this.armLeft.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelScale);
            this.armLeft.setPos(5.0F, 2.0F + 0.0F, 0.0F);
            this.legRight = new HideableModelRenderer(this, 0, 16);
            this.legRight.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelScale);
            this.legRight.setPos(-1.9F, 12.0F + 0.0F, 0.0F);
            this.legLeft = new HideableModelRenderer(this, 0, 16);
            this.legLeft.mirror = true;
            this.legLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelScale);
            this.legLeft.setPos(1.9F, 12.0F + 0.0F, 0.0F);
        }
        this.body.addChild(this.head);
        this.head.addChild(this.headware);
        this.body.addChild(this.armRight);
        this.body.addChild(this.armLeft);
        this.body.addChild(this.legRight);
        this.body.addChild(this.legLeft);
        this.animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public void animateModel(EntityDreadThrall entity, float limbAngle, float limbDistance, float tickDelta) {
        this.rightArmPose = BipedEntityModel.ArmPose.EMPTY;
        this.leftArmPose = BipedEntityModel.ArmPose.EMPTY;
        super.animateModel(entity, limbAngle, limbDistance, tickDelta);
    }

    @Override
    public void setAngles(EntityDreadThrall entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        super.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        this.flap(this.body, 0.5F, 0.15F, false, 1, 0F, limbAngle, limbDistance);
    }

    @Override
    public Animation getSpawnAnimation() {
        return EntityDreadThrall.ANIMATION_SPAWN;
    }

}