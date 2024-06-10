package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class ModelDeathWorm extends ModelDragonBase<EntityDeathWorm> {
    private final ModelAnimator animator;
    public AdvancedModelBox Body;
    public AdvancedModelBox Head;
    public AdvancedModelBox Spine1;
    public AdvancedModelBox Body2;
    public AdvancedModelBox JawExtender;
    public AdvancedModelBox HeadInner;
    public AdvancedModelBox ToothB;
    public AdvancedModelBox ToothT;
    public AdvancedModelBox ToothL;
    public AdvancedModelBox ToothL_1;
    public AdvancedModelBox Spine2;
    public AdvancedModelBox Body3;
    public AdvancedModelBox Spine3;
    public AdvancedModelBox Body4;
    public AdvancedModelBox Spine4;
    public AdvancedModelBox Body5;
    public AdvancedModelBox Spine5;
    public AdvancedModelBox Body6;
    public AdvancedModelBox Spine6;
    public AdvancedModelBox Body7;
    public AdvancedModelBox Spine7;
    public AdvancedModelBox Body8;
    public AdvancedModelBox Spine8;
    public AdvancedModelBox Body9;
    public AdvancedModelBox Spine9;
    public AdvancedModelBox Tail1;
    public AdvancedModelBox TailSpine1;
    public AdvancedModelBox Tail2;
    public AdvancedModelBox TailSpine2;
    public AdvancedModelBox Tail3;
    public AdvancedModelBox TailSpine3;
    public AdvancedModelBox Tail4;
    public AdvancedModelBox TailSpine4;
    public AdvancedModelBox TailSpine5;
    public AdvancedModelBox JawExtender2;
    public AdvancedModelBox TopJaw;
    public AdvancedModelBox BottomJaw;
    public AdvancedModelBox JawHook;

    public ModelDeathWorm() {
        this.animator = ModelAnimator.create();
        this.texWidth = 128;
        this.texHeight = 64;
        this.Body = new AdvancedModelBox(this, 38, 45);
        this.Body.setPos(0.0F, 18.0F, -0.2F);
        this.Body.addBox(-4.5F, -4.5F, -0.1F, 9, 9, 10, 0.0F);
        this.Head = new AdvancedModelBox(this, 0, 29);
        this.Head.setPos(0.0F, 0.0F, 1.5F);
        this.Head.addBox(-5.0F, -5.0F, -8.0F, 10, 10, 8, 0.0F);
        this.TopJaw = new AdvancedModelBox(this, 19, 7);
        this.TopJaw.setPos(0.0F, -0.2F, -11.4F);
        this.TopJaw.addBox(-2.0F, -1.5F, -6.4F, 4, 2, 6, 0.0F);
        this.setRotateAngle(this.TopJaw, 0.091106186954104F, 0.0F, 0.0F);
        this.Body9 = new AdvancedModelBox(this, 38, 45);
        this.Body9.setPos(0.0F, 0.0F, 4.2F);
        this.Body9.addBox(-4.5F, -4.5F, -0.1F, 9, 9, 10, 0.0F);
        this.Spine6 = new AdvancedModelBox(this, 34, 32);
        this.Spine6.setPos(0.0F, -3.5F, 2.0F);
        this.Spine6.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(this.Spine6, 1.2747884856566583F, -0.0F, 0.0F);
        this.TailSpine3 = new AdvancedModelBox(this, 34, 28);
        this.TailSpine3.setPos(0.0F, -3.0F, 5.0F);
        this.TailSpine3.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(this.TailSpine3, 1.2747884856566583F, -0.0F, 0.0F);
        this.Body7 = new AdvancedModelBox(this, 38, 45);
        this.Body7.setPos(0.0F, 0.0F, 4.2F);
        this.Body7.addBox(-4.5F, -4.5F, -0.1F, 9, 9, 10, 0.0F);
        this.TailSpine1 = new AdvancedModelBox(this, 34, 32);
        this.TailSpine1.setPos(0.0F, -3.5F, 4.0F);
        this.TailSpine1.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(this.TailSpine1, 1.2747884856566583F, -0.0F, 0.0F);
        this.JawHook = new AdvancedModelBox(this, 0, 7);
        this.JawHook.setPos(0.0F, -0.3F, -6.0F);
        this.JawHook.addBox(-0.5F, -0.7F, -2.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(this.JawHook, 1.730144887501979F, 0.0F, 0.0F);
        this.ToothL = new AdvancedModelBox(this, 52, 34);
        this.ToothL.setPos(4.5F, 0.0F, -7.5F);
        this.ToothL.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(this.ToothL, -3.141592653589793F, 0.3490658503988659F, 0.0F);
        this.Spine4 = new AdvancedModelBox(this, 34, 32);
        this.Spine4.setPos(0.0F, -3.5F, 2.0F);
        this.Spine4.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(this.Spine4, 1.2747884856566583F, -0.0F, 0.0F);
        this.Tail2 = new AdvancedModelBox(this, 66, 32);
        this.Tail2.setPos(0.0F, 0.0F, 7.2F);
        this.Tail2.addBox(-3.5F, -3.5F, -0.1F, 7, 7, 8, 0.0F);
        this.Body2 = new AdvancedModelBox(this, 78, 51);
        this.Body2.setPos(0.0F, 0.0F, 9.2F);
        this.Body2.addBox(-4.0F, -4.0F, -0.1F, 8, 8, 5, 0.0F);
        this.Spine3 = new AdvancedModelBox(this, 34, 28);
        this.Spine3.setPos(0.0F, -4.5F, 4.5F);
        this.Spine3.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(this.Spine3, 1.2747884856566583F, -0.0F, 0.0F);
        this.Spine9 = new AdvancedModelBox(this, 34, 28);
        this.Spine9.setPos(0.0F, -4.5F, 4.5F);
        this.Spine9.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(this.Spine9, 1.2747884856566583F, -0.0F, 0.0F);
        this.Body4 = new AdvancedModelBox(this, 78, 51);
        this.Body4.setPos(0.0F, 0.0F, 9.2F);
        this.Body4.addBox(-4.0F, -4.0F, -0.1F, 8, 8, 5, 0.0F);
        this.ToothB = new AdvancedModelBox(this, 52, 34);
        this.ToothB.setPos(0.0F, 4.5F, -7.5F);
        this.ToothB.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(this.ToothB, 2.7930504019665254F, -0.0F, 0.0F);
        this.Spine5 = new AdvancedModelBox(this, 34, 28);
        this.Spine5.setPos(0.0F, -4.5F, 4.5F);
        this.Spine5.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(this.Spine5, 1.2747884856566583F, -0.0F, 0.0F);
        this.JawExtender2 = new AdvancedModelBox(this, 0, 7);
        this.JawExtender2.setPos(0.0F, 0.0F, 0.0F);
        this.JawExtender2.addBox(-1.5F, -1.5F, -13.0F, 3, 3, 13, 0.0F);
        this.Tail3 = new AdvancedModelBox(this, 94, 15);
        this.Tail3.setPos(0.0F, 0.0F, 7.2F);
        this.Tail3.addBox(-3.0F, -3.0F, -0.1F, 6, 6, 10, 0.0F);
        this.Body5 = new AdvancedModelBox(this, 38, 45);
        this.Body5.setPos(0.0F, 0.0F, 4.2F);
        this.Body5.addBox(-4.5F, -4.5F, -0.1F, 9, 9, 10, 0.0F);
        this.JawExtender = new AdvancedModelBox(this, 0, 7);
        this.JawExtender.setPos(0.0F, 0.0F, 10.0F);
        this.JawExtender.addBox(-1.5F, -1.5F, -13.0F, 3, 3, 13, 0.0F);
        this.Body3 = new AdvancedModelBox(this, 38, 45);
        this.Body3.setPos(0.0F, 0.0F, 4.2F);
        this.Body3.addBox(-4.5F, -4.5F, -0.1F, 9, 9, 10, 0.0F);
        this.Spine2 = new AdvancedModelBox(this, 34, 32);
        this.Spine2.setPos(0.0F, -3.5F, 2.0F);
        this.Spine2.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(this.Spine2, 1.2747884856566583F, -0.0F, 0.0F);
        this.Body6 = new AdvancedModelBox(this, 78, 51);
        this.Body6.setPos(0.0F, 0.0F, 9.2F);
        this.Body6.addBox(-4.0F, -4.0F, -0.1F, 8, 8, 5, 0.0F);
        this.Spine8 = new AdvancedModelBox(this, 34, 32);
        this.Spine8.setPos(0.0F, -3.5F, 2.0F);
        this.Spine8.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(this.Spine8, 1.2747884856566583F, -0.0F, 0.0F);
        this.Tail1 = new AdvancedModelBox(this, 96, 33);
        this.Tail1.setPos(0.0F, 0.0F, 9.8F);
        this.Tail1.addBox(-4.0F, -4.0F, -0.1F, 8, 8, 8, 0.0F);
        this.TailSpine4 = new AdvancedModelBox(this, 34, 32);
        this.TailSpine4.setPos(0.0F, -2.1F, 2.7F);
        this.TailSpine4.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(this.TailSpine4, 1.2747884856566583F, -0.0F, 0.0F);
        this.BottomJaw = new AdvancedModelBox(this, 40, 7);
        this.BottomJaw.setPos(0.0F, 0.8F, -12.3F);
        this.BottomJaw.addBox(-2.0F, 0.2F, -4.9F, 4, 1, 5, 0.0F);
        this.setRotateAngle(this.BottomJaw, -0.045553093477052F, 0.0F, 0.0F);
        this.Spine1 = new AdvancedModelBox(this, 34, 28);
        this.Spine1.setPos(0.0F, -4.5F, 4.5F);
        this.Spine1.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(this.Spine1, 1.2747884856566583F, -0.0F, 0.0F);
        this.ToothT = new AdvancedModelBox(this, 52, 34);
        this.ToothT.setPos(0.0F, -4.5F, -7.5F);
        this.ToothT.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(this.ToothT, -2.7930504019665254F, -0.0F, 0.0F);
        this.Spine7 = new AdvancedModelBox(this, 34, 28);
        this.Spine7.setPos(0.0F, -4.5F, 4.5F);
        this.Spine7.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(this.Spine7, 1.2747884856566583F, -0.0F, 0.0F);
        this.Body8 = new AdvancedModelBox(this, 78, 51);
        this.Body8.setPos(0.0F, 0.0F, 9.2F);
        this.Body8.addBox(-4.0F, -4.0F, -0.1F, 8, 8, 5, 0.0F);
        this.HeadInner = new AdvancedModelBox(this, 0, 48);
        this.HeadInner.setPos(0.0F, 0.0F, -0.3F);
        this.HeadInner.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, 0.0F);
        this.ToothL_1 = new AdvancedModelBox(this, 52, 34);
        this.ToothL_1.setPos(-4.5F, 0.0F, -7.5F);
        this.ToothL_1.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(this.ToothL_1, -3.141592653589793F, -0.3490658503988659F, 0.0F);
        this.Tail4 = new AdvancedModelBox(this, 62, 15);
        this.Tail4.setPos(0.0F, 0.0F, 9.2F);
        this.Tail4.addBox(-2.5F, -2.5F, -0.1F, 5, 5, 10, 0.0F);
        this.TailSpine5 = new AdvancedModelBox(this, 34, 28);
        this.TailSpine5.setPos(0.0F, -2.1F, 8.0F);
        this.TailSpine5.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 2, 0.0F);
        this.setRotateAngle(this.TailSpine5, 1.2747884856566583F, -0.0F, 0.0F);
        this.TailSpine2 = new AdvancedModelBox(this, 34, 32);
        this.TailSpine2.setPos(0.0F, -3.0F, 4.0F);
        this.TailSpine2.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(this.TailSpine2, 1.2747884856566583F, -0.0F, 0.0F);
        this.Body.addChild(this.Head);
        this.JawExtender2.addChild(this.TopJaw);
        this.Body8.addChild(this.Body9);
        this.Body6.addChild(this.Spine6);
        this.Tail3.addChild(this.TailSpine3);
        this.Body6.addChild(this.Body7);
        this.Tail1.addChild(this.TailSpine1);
        this.TopJaw.addChild(this.JawHook);
        this.Head.addChild(this.ToothL);
        this.Body4.addChild(this.Spine4);
        this.Tail1.addChild(this.Tail2);
        this.Body.addChild(this.Body2);
        this.Body3.addChild(this.Spine3);
        this.Body9.addChild(this.Spine9);
        this.Body3.addChild(this.Body4);
        this.Head.addChild(this.ToothB);
        this.Body5.addChild(this.Spine5);
        this.JawExtender.addChild(this.JawExtender2);
        this.Tail2.addChild(this.Tail3);
        this.Body4.addChild(this.Body5);
        this.Body.addChild(this.JawExtender);
        this.Body2.addChild(this.Body3);
        this.Body2.addChild(this.Spine2);
        this.Body5.addChild(this.Body6);
        this.Body8.addChild(this.Spine8);
        this.Body9.addChild(this.Tail1);
        this.Tail4.addChild(this.TailSpine4);
        this.JawExtender2.addChild(this.BottomJaw);
        this.Body.addChild(this.Spine1);
        this.Head.addChild(this.ToothT);
        this.Body7.addChild(this.Spine7);
        this.Body7.addChild(this.Body8);
        this.Head.addChild(this.HeadInner);
        this.Head.addChild(this.ToothL_1);
        this.Tail3.addChild(this.Tail4);
        this.Tail4.addChild(this.TailSpine5);
        this.Tail2.addChild(this.TailSpine2);
        this.updateDefaultPose();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        if (this.animator.setAnimation(EntityDeathWorm.ANIMATION_BITE)) {
            this.animator.startKeyframe(3);
            this.rotate(this.animator, this.TopJaw, -20, 0, 0);
            this.rotate(this.animator, this.BottomJaw, 20, 0, 0);
            this.animator.move(this.JawExtender, 0, 0, -8);
            this.animator.move(this.JawExtender2, 0, 0, -8);
            this.animator.endKeyframe();
            this.animator.startKeyframe(3);
            this.rotate(this.animator, this.TopJaw, -40, 0, 0);
            this.rotate(this.animator, this.BottomJaw, 40, 0, 0);
            this.animator.move(this.JawExtender, 0, 0, -10);
            this.animator.move(this.JawExtender2, 0, 0, -10);
            this.animator.endKeyframe();
            this.animator.startKeyframe(2);
            this.rotate(this.animator, this.TopJaw, 5, 0, 0);
            this.rotate(this.animator, this.BottomJaw, -5, 0, 0);
            this.animator.move(this.JawExtender, 0, 0, -7);
            this.animator.move(this.JawExtender2, 0, 0, -7);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(2);
        }
    }

    @Override
    public void setAngles(EntityDeathWorm entity, float f, float f1, float f2, float f3, float f4) {
        float speed_idle = 0.1F;
        float degree_idle = 0.5F;
        float speed_walk = 0.2F;
        float degree_walk = 0.15F;
        EntityDeathWorm worm = entity;
        this.animate(entity, f, f1, f2, f3, f4);
        AdvancedModelBox[] WORM = {this.Body, this.Body2, this.Body3, this.Body4, this.Body5, this.Body6, this.Body7, this.Body8, this.Body9, this.Tail1, this.Tail2, this.Tail3, this.Tail4};
        this.walk(this.ToothT, speed_idle, degree_idle * 0.15F, true, 0.1F, 0F, f2, 1);
        this.walk(this.ToothB, speed_idle, degree_idle * 0.15F, false, 0.1F, 0F, f2, 1);
        this.swing(this.ToothL, speed_idle, degree_idle * 0.15F, true, 0.1F, 0F, f2, 1);
        this.swing(this.ToothL_1, speed_idle, degree_idle * 0.15F, false, 0.1F, 0F, f2, 1);
        this.walk(this.TopJaw, speed_idle * 0.5F, degree_idle * 0.15F, true, -0.1F, 0F, f2, 1);
        this.walk(this.BottomJaw, speed_idle * 0.5F, degree_idle * 0.15F, false, -0.1F, 0F, f2, 1);
        this.chainSwing(WORM, speed_walk, degree_walk * 0.1F, -3, f2, 1);
        this.chainSwing(WORM, speed_walk, degree_walk, -3, f, f1);
        this.chainFlap(WORM, speed_walk, degree_walk * 0.75F, -3, f, f1);
        float jumpProgress = worm.prevJumpProgress + (worm.jumpProgress - worm.prevJumpProgress) * (f2 - worm.age);
        this.progressRotation(this.Head, jumpProgress, (float) Math.toRadians(25), 0.0F, 0.0F);
        this.progressRotation(this.Body, jumpProgress, (float) Math.toRadians(65), 0.0F, 0.0F);
        this.progressRotation(this.Body2, jumpProgress, (float) Math.toRadians(-21), 0.0F, 0.0F);
        this.progressRotation(this.Body3, jumpProgress, (float) Math.toRadians(-21), 0.0F, 0.0F);
        this.progressRotation(this.Body4, jumpProgress, (float) Math.toRadians(-21), 0.0F, 0.0F);
        this.progressRotation(this.Body5, jumpProgress, (float) Math.toRadians(-21), 0.0F, 0.0F);
        this.progressRotation(this.Body6, jumpProgress, (float) Math.toRadians(-21), 0.0F, 0.0F);
        this.progressRotation(this.Body8, jumpProgress, (float) Math.toRadians(-21), 0.0F, 0.0F);
        this.progressRotation(this.Body9, jumpProgress, (float) Math.toRadians(-21), 0.0F, 0.0F);
        this.progressRotation(this.Tail1, jumpProgress, (float) Math.toRadians(-21), 0.0F, 0.0F);
        this.progressRotation(this.Tail2, jumpProgress, (float) Math.toRadians(-21), 0.0F, 0.0F);
        this.progressRotation(this.Tail3, jumpProgress, (float) Math.toRadians(-21), 0.0F, 0.0F);
        this.progressRotation(this.Tail4, jumpProgress, (float) Math.toRadians(-21), 0.0F, 0.0F);
        if (worm.tail_buffer != null)
            worm.tail_buffer.applyChainSwingBuffer(WORM);

        if (worm.getWormJumping() > 0) {
            this.Body.rotateAngleX += f4 * ((float) Math.PI / 180F);
        }
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.Body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.Body, this.Head, this.Spine1, this.Body2, this.JawExtender, this.HeadInner, this.ToothB, this.ToothT, this.ToothL, this.ToothL_1, this.Spine2, this.Body3, this.Spine3, this.Body4, this.Spine4, this.Body5, this.Spine5, this.Body6, this.Spine6, this.Body7, this.Spine7, this.Body8, this.Spine8, this.Body9, this.Spine9, this.Tail1, this.TailSpine1, this.Tail2, this.TailSpine2, this.Tail3, this.TailSpine3, this.Tail4, this.TailSpine4, this.TailSpine5, this.JawExtender2, this.TopJaw, this.BottomJaw, this.JawHook);
    }

    @Override
    public void renderStatue(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, Entity living) {
        this.render(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
