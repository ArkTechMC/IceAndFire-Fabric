package com.iafenvoy.iceandfire.client.model;

import com.iafenvoy.citadel.animation.Animation;
import com.iafenvoy.citadel.client.model.ModelAnimator;
import com.iafenvoy.iceandfire.client.model.util.HideableModelRenderer;
import com.iafenvoy.iceandfire.entity.EntityDreadKnight;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

public class ModelDreadKnight extends ModelDreadBase<EntityDreadKnight> {
    public final HideableModelRenderer chestplate;
    public final HideableModelRenderer cloak;
    public final HideableModelRenderer crown;
    public final HideableModelRenderer sleeveRight;
    public final HideableModelRenderer robeLowerRight;
    public final HideableModelRenderer sleeveLeft;
    public final HideableModelRenderer robeLowerLeft;

    public ModelDreadKnight(float modelScale) {
        this.texWidth = 128;
        this.texHeight = 64;
        this.leftArmPose = BipedEntityModel.ArmPose.EMPTY;
        this.rightArmPose = BipedEntityModel.ArmPose.EMPTY;
        this.sleeveRight = new HideableModelRenderer(this, 35, 33);
        this.sleeveRight.setPos(0.0F, -0.1F, 0.0F);
        this.sleeveRight.addBox(-4.0F, -2.1F, -2.5F, 5, 6, 5, modelScale);
        this.chestplate = new HideableModelRenderer(this, 1, 32);
        this.chestplate.setPos(0.0F, 0.1F, 0.0F);
        this.chestplate.addBox(-4.5F, 0.0F, -2.5F, 9, 11, 5, modelScale);
        this.crown = new HideableModelRenderer(this, 58, -1);
        this.crown.setPos(0.0F, 0.0F, 0.0F);
        this.crown.addBox(-4.5F, -10.2F, -4.5F, 9, 11, 9, modelScale);
        this.body = new HideableModelRenderer(this, 16, 16);
        this.body.setPos(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelScale);
        this.legRight = new HideableModelRenderer(this, 0, 16);
        this.legRight.setPos(-1.9F, 12.0F, 0.1F);
        this.legRight.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelScale);
        this.armLeft = new HideableModelRenderer(this, 40, 16);
        this.armLeft.mirror = true;
        this.armLeft.setPos(5.0F, 2.0F, -0.0F);
        this.armLeft.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelScale);
        this.setRotateAngle(this.armLeft, -0.0F, 0.10000736613927509F, -0.10000736613927509F);
        this.legLeft = new HideableModelRenderer(this, 0, 16);
        this.legLeft.mirror = true;
        this.legLeft.setPos(1.9F, 12.0F, 0.1F);
        this.legLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelScale);
        this.head = new HideableModelRenderer(this, 0, 0);
        this.head.setPos(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelScale);
        this.sleeveLeft = new HideableModelRenderer(this, 35, 33);
        this.sleeveLeft.mirror = true;
        this.sleeveLeft.setPos(0.0F, -0.1F, 0.0F);
        this.sleeveLeft.addBox(-1.0F, -2.1F, -2.5F, 5, 6, 5, modelScale);
        this.robeLowerRight = new HideableModelRenderer(this, 58, 33);
        this.robeLowerRight.mirror = true;
        this.robeLowerRight.setPos(0.0F, -0.2F, 0.0F);
        this.robeLowerRight.addBox(-2.1F, 0.0F, -2.5F, 4, 7, 5, modelScale);
        this.cloak = new HideableModelRenderer(this, 81, 37);
        this.cloak.setPos(0.0F, 0.1F, 0.0F);
        this.cloak.addBox(-4.5F, 0.0F, 2.3F, 9, 21, 1, modelScale);
        this.setRotateAngle(this.cloak, 0.045553093477052F, 0.0F, 0.0F);
        this.armRight = new HideableModelRenderer(this, 40, 16);
        this.armRight.setPos(-5.0F, 2.0F, 0.0F);
        this.armRight.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelScale);
        this.setRotateAngle(this.armRight, -0.0F, -0.10000736613927509F, 0.10000736613927509F);
        this.robeLowerLeft = new HideableModelRenderer(this, 58, 33);
        this.robeLowerLeft.setPos(0.0F, -0.2F, 0.0F);
        this.robeLowerLeft.addBox(-1.9F, 0.0F, -2.5F, 4, 7, 5, modelScale);
        this.armRight.addChild(this.sleeveRight);
        this.body.addChild(this.chestplate);
        this.head.addChild(this.crown);
        this.body.addChild(this.legRight);
        this.body.addChild(this.armLeft);
        this.body.addChild(this.legLeft);
        this.body.addChild(this.head);
        this.armLeft.addChild(this.sleeveLeft);
        this.legRight.addChild(this.robeLowerRight);
        this.body.addChild(this.cloak);
        this.body.addChild(this.armRight);
        this.legLeft.addChild(this.robeLowerLeft);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public void animateModel(EntityDreadKnight entity, float limbAngle, float limbDistance, float tickDelta) {
        this.rightArmPose = BipedEntityModel.ArmPose.EMPTY;
        this.leftArmPose = BipedEntityModel.ArmPose.EMPTY;
        ItemStack itemstack = entity.getStackInHand(Hand.MAIN_HAND);

        if (itemstack.getItem() == Items.BOW && entity.handSwinging)
            if (entity.getMainArm() == Arm.RIGHT)
                this.rightArmPose = BipedEntityModel.ArmPose.BOW_AND_ARROW;
            else
                this.leftArmPose = BipedEntityModel.ArmPose.BOW_AND_ARROW;

        super.animateModel(entity, limbAngle, limbDistance, tickDelta);
    }

    @Override
    public void setRotationAnglesSpawn(EntityDreadKnight entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void animate(EntityDreadKnight entity, float f, float f1, float f2, float f3, float f4, float f5) {
    }

    @Override
    public Animation getSpawnAnimation() {
        return EntityDreadKnight.ANIMATION_SPAWN;
    }

    @Override
    public void copyStateTo(EntityModel<EntityDreadKnight> model) {
        super.copyStateTo(model);
        if (model instanceof BipedEntityModel bipedEntityModel) {
            bipedEntityModel.leftArmPose = this.leftArmPose;
            bipedEntityModel.rightArmPose = this.rightArmPose;
            bipedEntityModel.sneaking = this.isSneak;
        }
    }
}