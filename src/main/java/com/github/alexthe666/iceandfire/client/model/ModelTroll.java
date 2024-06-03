package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class ModelTroll extends ModelDragonBase<EntityTroll> {
    private final ModelAnimator animator;
    public AdvancedModelBox body;
    public AdvancedModelBox upperBody;
    public AdvancedModelBox loin;
    public AdvancedModelBox rightleg;
    public AdvancedModelBox leftleg;
    public AdvancedModelBox head;
    public AdvancedModelBox rightarm;
    public AdvancedModelBox leftarm;
    public AdvancedModelBox chest;
    public AdvancedModelBox jaw;
    public AdvancedModelBox mouth;
    public AdvancedModelBox nose;
    public AdvancedModelBox teeth;
    public AdvancedModelBox hornL;
    public AdvancedModelBox hornR;
    public AdvancedModelBox hornL2;
    public AdvancedModelBox hornR2;
    public AdvancedModelBox nose2;
    public AdvancedModelBox rightarm2;
    public AdvancedModelBox log1;
    public AdvancedModelBox log2;
    public AdvancedModelBox handle;
    public AdvancedModelBox column;
    public AdvancedModelBox blade1;
    public AdvancedModelBox blade2;
    public AdvancedModelBox blade2_1;
    public AdvancedModelBox block;
    public AdvancedModelBox blade2_2;
    public AdvancedModelBox bottom;
    public AdvancedModelBox top;
    public AdvancedModelBox leftarm2;
    public AdvancedModelBox rightleg2;
    public AdvancedModelBox leftleg2;

    public ModelTroll() {
        this.texWidth = 256;
        this.texHeight = 128;
        this.log2 = new AdvancedModelBox(this, 10, 70);
        this.log2.setPos(0.0F, 9.0F, -1.0F);
        this.log2.addBox(-2.0F, -6.9F, -3.0F, 7, 24, 6, 0.0F);
        this.setRotateAngle(log2, -0.045553093477052F, 0.0F, 0.0F);
        this.top = new AdvancedModelBox(this, 177, 0);
        this.top.setPos(-1.0F, 0.0F, 0.0F);
        this.top.addBox(-1.5F, 20.0F, -3.5F, 10, 4, 10, 0.0F);
        this.hornL = new AdvancedModelBox(this, 60, 101);
        this.hornL.mirror = true;
        this.hornL.setPos(1.3F, -1.5F, -1.0F);
        this.hornL.addBox(-1.5F, -0.5F, 0.0F, 2, 2, 7, 0.0F);
        this.setRotateAngle(hornL, -0.5918411493512771F, 2.6406831582674206F, -0.17453292519943295F);
        this.body = new AdvancedModelBox(this, 88, 46);
        this.body.setPos(0.0F, -2.4F, 0.0F);
        this.body.addBox(-5.0F, -7.0F, -3.0F, 10, 9, 8, 0.0F);
        this.leftleg2 = new AdvancedModelBox(this, 0, 15);
        this.leftleg2.mirror = true;
        this.leftleg2.setPos(0.0F, 9.6F, -0.2F);
        this.leftleg2.addBox(-3.0F, 1.0F, -2.0F, 6, 15, 6, 0.0F);
        this.setRotateAngle(leftleg2, 0.3490658503988659F, 0.0F, 0.08726646259971647F);
        this.head = new AdvancedModelBox(this, 90, 0);
        this.head.setPos(0.0F, -15.8F, -1.0F);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 10, 8, 0.0F);
        this.setRotateAngle(head, -0.18203784098300857F, 0.0F, 0.0F);
        this.blade1 = new AdvancedModelBox(this, 186, 84);
        this.blade1.setPos(0.0F, 0.0F, -2.0F);
        this.blade1.addBox(-1.0F, 12.0F, 0.0F, 2, 10, 10, 0.0F);
        this.hornR = new AdvancedModelBox(this, 60, 101);
        this.hornR.mirror = true;
        this.hornR.setPos(-1.3F, -1.5F, -1.0F);
        this.hornR.addBox(-0.5F, -0.5F, 0.0F, 2, 2, 7, 0.0F);
        this.setRotateAngle(hornR, -0.7740535232594852F, -2.9595548126067843F, 0.27314402793711257F);
        this.log1 = new AdvancedModelBox(this, 10, 100);
        this.log1.setPos(-0.5F, 8.0F, -0.4F);
        this.log1.addBox(-1.5F, -17.9F, -3.5F, 6, 20, 6, 0.0F);
        this.setRotateAngle(log1, -1.593485607070823F, 0.0F, 0.0F);
        this.bottom = new AdvancedModelBox(this, 177, 0);
        this.bottom.setPos(-1.0F, 0.0F, 0.0F);
        this.bottom.addBox(-1.5F, -21.0F, -3.5F, 10, 4, 10, 0.0F);
        this.nose2 = new AdvancedModelBox(this, 114, 120);
        this.nose2.setPos(0.0F, 5.1F, -3.0F);
        this.nose2.addBox(-1.5F, -1.8F, -1.6F, 3, 4, 2, 0.0F);
        this.setRotateAngle(nose2, -1.2747884856566583F, 0.0F, 0.0F);
        this.blade2_1 = new AdvancedModelBox(this, 189, 69);
        this.blade2_1.setPos(0.0F, 2.0F, -1.0F);
        this.blade2_1.addBox(-1.0F, 2.0F, -1.0F, 2, 2, 6, 0.0F);
        this.hornR2 = new AdvancedModelBox(this, 46, 90);
        this.hornR2.mirror = true;
        this.hornR2.setPos(-0.5F, 1.3F, 6.6F);
        this.hornR2.addBox(-0.01F, -0.8F, -0.0F, 2, 2, 7, 0.0F);
        this.setRotateAngle(hornR2, 1.2747884856566583F, 0.0F, 0.0F);
        this.leftleg = new AdvancedModelBox(this, 0, 45);
        this.leftleg.mirror = true;
        this.leftleg.setPos(4.0F, 1.2F, 1.0F);
        this.leftleg.addBox(-3.0F, 1.0F, -2.0F, 6, 13, 6, 0.0F);
        this.setRotateAngle(leftleg, -0.2617993877991494F, 0.0F, -0.08726646259971647F);
        this.upperBody = new AdvancedModelBox(this, 85, 21);
        this.upperBody.setPos(0.0F, -6.0F, 1.3F);
        this.upperBody.addBox(-6.0F, -13.9F, -4.1F, 12, 14, 9, 0.0F);
        this.setRotateAngle(upperBody, 0.31869712141416456F, 0.0F, 0.0F);
        this.rightleg2 = new AdvancedModelBox(this, 0, 15);
        this.rightleg2.setPos(0.0F, 9.6F, -0.2F);
        this.rightleg2.addBox(-3.0F, 1.0F, -2.0F, 6, 15, 6, 0.0F);
        this.setRotateAngle(rightleg2, 0.3490658503988659F, 0.0F, -0.08726646259971647F);
        this.leftarm = new AdvancedModelBox(this, 64, 0);
        this.leftarm.mirror = true;
        this.leftarm.setPos(5.0F, -11.2F, -0.4F);
        this.leftarm.addBox(0.0F, -2.0F, -2.0F, 6, 13, 7, 0.0F);
        this.setRotateAngle(leftarm, -0.136659280431156F, 0.0F, -0.17453292519943295F);
        this.mouth = new AdvancedModelBox(this, 114, 0);
        this.mouth.setPos(0.0F, 0.3F, -1.0F);
        this.mouth.addBox(-2.5F, -0.6F, -4.6F, 5, 3, 2, 0.0F);
        this.setRotateAngle(mouth, -0.18203784098300857F, 0.0F, 0.0F);
        this.jaw = new AdvancedModelBox(this, 40, 11);
        this.jaw.setPos(0.0F, 2.3F, -2.0F);
        this.jaw.addBox(-2.0F, -0.6F, -4.6F, 4, 2, 6, 0.0F);
        this.setRotateAngle(jaw, -0.091106186954104F, 0.0F, 0.0F);
        this.hornL2 = new AdvancedModelBox(this, 46, 90);
        this.hornL2.mirror = true;
        this.hornL2.setPos(-0.5F, 1.3F, 6.6F);
        this.hornL2.addBox(-1.01F, -0.8F, -0.0F, 2, 2, 7, 0.0F);
        this.setRotateAngle(hornL2, 1.4114477660878142F, 0.0F, 0.0F);
        this.rightleg = new AdvancedModelBox(this, 0, 45);
        this.rightleg.setPos(-4.0F, 1.2F, 1.0F);
        this.rightleg.addBox(-3.0F, 1.0F, -2.0F, 6, 13, 6, 0.0F);
        this.setRotateAngle(rightleg, -0.2617993877991494F, 0.0F, 0.08726646259971647F);
        this.column = new AdvancedModelBox(this, 220, 28);
        this.column.setPos(0.0F, 0.0F, -3.0F);
        this.column.addBox(-1.5F, -20.0F, -2.5F, 8, 42, 8, 0.0F);
        this.setRotateAngle(column, -0.045553093477052F, 0.0F, 0.0F);
        this.leftarm2 = new AdvancedModelBox(this, 60, 24);
        this.leftarm2.mirror = true;
        this.leftarm2.setPos(2.1F, 10.0F, -0.1F);
        this.leftarm2.addBox(-2.0F, -5.0F, -1.7F, 5, 16, 7, 0.0F);
        this.setRotateAngle(leftarm2, -0.31869712141416456F, 0.0F, 0.0F);
        this.rightarm = new AdvancedModelBox(this, 64, 0);
        this.rightarm.setPos(-5.0F, -11.2F, -0.4F);
        this.rightarm.addBox(-6.0F, -2.0F, -2.0F, 6, 13, 7, 0.0F);
        this.setRotateAngle(rightarm, -0.045553093477052F, 0.27314402793711257F, 0.0F);
        this.handle = new AdvancedModelBox(this, 232, 80);
        this.handle.setPos(0.3F, 0.0F, 1.0F);
        this.handle.addBox(-1.5F, -20.0F, -1.5F, 3, 42, 3, 0.0F);
        this.setRotateAngle(handle, -0.045553093477052F, 0.0F, 0.0F);
        this.block = new AdvancedModelBox(this, 182, 19);
        this.block.setPos(0.0F, 0.0F, 0.0F);
        this.block.addBox(-2.0F, 11.0F, -8.0F, 4, 10, 15, 0.0F);
        this.blade2_2 = new AdvancedModelBox(this, 161, 99);
        this.blade2_2.setPos(0.0F, 17.0F, 9.0F);
        this.blade2_2.addBox(-1.0F, -5.0F, -6.0F, 2, 5, 5, 0.0F);
        this.setRotateAngle(blade2_2, 3.141592653589793F, 0.0F, 0.0F);
        this.loin = new AdvancedModelBox(this, 50, 56);
        this.loin.setPos(0.0F, 0.0F, 0.0F);
        this.loin.addBox(-5.5F, 0.0F, -3.5F, 11, 13, 9, 0.0F);
        this.nose = new AdvancedModelBox(this, 114, 100);
        this.nose.setPos(0.0F, -4.5F, -0.2F);
        this.nose.addBox(-1.0F, -0.6F, -4.6F, 2, 5, 2, 0.0F);
        this.setRotateAngle(nose, -0.5462880558742251F, 0.0F, 0.0F);
        this.teeth = new AdvancedModelBox(this, 39, 0);
        this.teeth.setPos(0.0F, 2.0F, -1.0F);
        this.teeth.addBox(-2.1F, -3.6F, -3.6F, 4, 2, 6, 0.0F);
        this.chest = new AdvancedModelBox(this, 93, 29);
        this.chest.setPos(0.0F, -8.9F, 0.5F);
        this.chest.addBox(-5.0F, -2.0F, -4.8F, 10, 6, 2, 0.0F);
        this.setRotateAngle(chest, -0.22759093446006054F, 0.0F, 0.0F);
        this.blade2 = new AdvancedModelBox(this, 186, 66);
        this.blade2.setPos(0.0F, 7.0F, -1.0F);
        this.blade2.addBox(-1.0F, 1.0F, -1.0F, 2, 2, 9, 0.0F);
        this.rightarm2 = new AdvancedModelBox(this, 60, 24);
        this.rightarm2.setPos(-2.1F, 10.0F, 0.1F);
        this.rightarm2.addBox(-3.0F, -5.0F, -1.7F, 5, 16, 7, 0.0F);
        this.setRotateAngle(rightarm2, -2.1399481958702475F, 0.22759093446006054F, -0.136659280431156F);
        this.log1.addChild(this.log2);
        this.column.addChild(this.top);
        this.jaw.addChild(this.hornL);
        this.leftleg.addChild(this.leftleg2);
        this.upperBody.addChild(this.head);
        this.handle.addChild(this.blade1);
        this.jaw.addChild(this.hornR);
        this.rightarm2.addChild(this.log1);
        this.column.addChild(this.bottom);
        this.nose.addChild(this.nose2);
        this.handle.addChild(this.blade2_1);
        this.hornR.addChild(this.hornR2);
        this.body.addChild(this.leftleg);
        this.body.addChild(this.upperBody);
        this.rightleg.addChild(this.rightleg2);
        this.upperBody.addChild(this.leftarm);
        this.head.addChild(this.mouth);
        this.head.addChild(this.jaw);
        this.hornL.addChild(this.hornL2);
        this.body.addChild(this.rightleg);
        this.log1.addChild(this.column);
        this.leftarm.addChild(this.leftarm2);
        this.upperBody.addChild(this.rightarm);
        this.log1.addChild(this.handle);
        this.handle.addChild(this.block);
        this.blade1.addChild(this.blade2_2);
        this.body.addChild(this.loin);
        this.head.addChild(this.nose);
        this.jaw.addChild(this.teeth);
        this.upperBody.addChild(this.chest);
        this.handle.addChild(this.blade2);
        this.rightarm.addChild(this.rightarm2);
        animator = ModelAnimator.create();
        this.updateDefaultPose();
    }


    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(body, upperBody, loin, rightleg, leftleg, head, rightarm,
            leftarm, chest, jaw, mouth, nose, teeth, hornL, hornR, hornL2, hornR2, nose2,
            rightarm2, log1, log2, handle, column, blade1, blade2, blade2_1, block, blade2_2,
            bottom, top, leftarm2, rightleg2, leftleg2);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.log1.showModel = true;
        this.resetToDefaultPose();
        animator.update(entity);
        if (animator.setAnimation(EntityTroll.ANIMATION_SPEAK)) {
            animator.startKeyframe(5);
            this.rotate(animator, jaw, 25, 0, 0);
            animator.endKeyframe();
            animator.startKeyframe(5);
            this.rotate(animator, jaw, 0, 0, 0);
            animator.endKeyframe();
        }
        if (animator.setAnimation(EntityTroll.ANIMATION_ROAR)) {
            animator.startKeyframe(5);
            this.rotateMinus(animator, leftleg, -31, -26, -5);
            this.rotateMinus(animator, rightleg, -31, 26, 5);
            this.rotate(animator, upperBody, 18, 0, 5);
            this.rotateMinus(animator, leftleg2, 41, 0, 5);
            this.rotateMinus(animator, rightleg2, 41, 0, -5);
            this.rotateMinus(animator, leftarm, -26, -44, -2);
            this.rotateMinus(animator, leftarm2, -60, 0, 0);
            this.rotateMinus(animator, rightarm, -39, 57, 0);
            this.rotateMinus(animator, rightarm2, -73, 13, -7);
            this.rotate(animator, head, -57, 0, 0);
            this.rotate(animator, jaw, 60, 0, 0);
            animator.move(body, 0, 2, 0);
            animator.endKeyframe();
            animator.startKeyframe(5);
            this.rotateMinus(animator, leftleg, -31, -26, -5);
            this.rotateMinus(animator, rightleg, -31, 26, 5);
            this.rotate(animator, upperBody, 18, 0, 5);
            this.rotateMinus(animator, leftleg2, 41, 0, 5);
            this.rotateMinus(animator, rightleg2, 41, 0, -5);
            this.rotateMinus(animator, leftarm, -26, -44, -2);
            this.rotateMinus(animator, leftarm2, -60, 0, 0);
            this.rotateMinus(animator, rightarm, -39, 57, 0);
            this.rotateMinus(animator, rightarm2, -73, 13, -7);
            this.rotate(animator, head, -57, -28, 0);
            this.rotate(animator, jaw, 60, 0, 0);
            animator.move(body, 0, 2, 0);
            animator.endKeyframe();
            animator.startKeyframe(5);
            this.rotateMinus(animator, leftleg, -31, -26, -5);
            this.rotateMinus(animator, rightleg, -31, 26, 5);
            this.rotate(animator, upperBody, 18, 0, 5);
            this.rotateMinus(animator, leftleg2, 41, 0, 5);
            this.rotateMinus(animator, rightleg2, 41, 0, -5);
            this.rotateMinus(animator, leftarm, -26, -44, -2);
            this.rotateMinus(animator, leftarm2, -60, 0, 0);
            this.rotateMinus(animator, rightarm, -39, 57, 0);
            this.rotateMinus(animator, rightarm2, -73, 13, -7);
            this.rotate(animator, head, -57, 28, 0);
            this.rotate(animator, jaw, 60, 0, 0);
            animator.move(body, 0, 2, 0);
            animator.endKeyframe();
            animator.resetKeyframe(10);
        }
        if (animator.setAnimation(EntityTroll.ANIMATION_STRIKE_HORIZONTAL)) {
            animator.startKeyframe(10);
            this.rotate(animator, body, 0, 31, 0);
            this.rotate(animator, upperBody, 18, 39, 0);
            this.rotate(animator, leftarm, 18, 0, -10);
            this.rotate(animator, rightarm, 41, 57, 65);
            this.rotate(animator, rightarm2, 50, 0, 0);
            this.rotate(animator, rightleg, -15, 57, 5);
            this.rotate(animator, leftleg, -13, -44, -5);
            animator.move(body, 0, 3, 0);
            animator.endKeyframe();
            animator.startKeyframe(5);
            this.rotate(animator, body, 0, 31, 0);
            this.rotate(animator, upperBody, 18, -39, 0);
            this.rotate(animator, leftarm, 18, 0, -10);
            this.rotate(animator, rightarm, -60, -40, -35);
            this.rotate(animator, rightarm2, 80, -45, 40);
            this.rotate(animator, rightleg, -15, 57, 5);
            this.rotate(animator, leftleg, -13, -44, -5);
            this.rotate(animator, log1, 15, 0, 0);
            animator.move(body, 0, 3, 0);
            animator.endKeyframe();
            animator.resetKeyframe(5);
        }
        if (animator.setAnimation(EntityTroll.ANIMATION_STRIKE_VERTICAL)) {
            animator.startKeyframe(7);
            this.rotate(animator, upperBody, -30, 0, 0);
            this.rotate(animator, rightleg, -15, 57, 5);
            this.rotate(animator, leftleg, -13, -44, -5);
            this.rotate(animator, leftarm, -203, 35, -15);
            this.rotate(animator, rightarm, -212, -40, 25);
            this.rotate(animator, leftarm2, 18, 0, 0);
            this.rotate(animator, rightarm2, 122, -13, 7);
            this.rotate(animator, log1, 0, -40, 0);
            animator.move(body, 0, 3, 0);
            animator.move(log1, 5, 0, 0);
            animator.endKeyframe();
            animator.startKeyframe(5);
            this.rotate(animator, body, 5, 0, 0);
            this.rotate(animator, upperBody, 35, 0, 0);
            this.rotate(animator, rightleg, -15, 57, 5);
            this.rotate(animator, leftleg, -13, -44, -5);
            this.rotate(animator, leftarm, -103, 20, -15);
            this.rotate(animator, rightarm, -112, -20, 25);
            this.rotate(animator, leftarm2, 18, 0, 0);
            this.rotate(animator, rightarm2, 122, -13, 7);
            this.rotate(animator, log1, 90, 25, 20);
            animator.move(body, 0, 3, 0);
            animator.move(log1, 2, 0, 0);
            animator.endKeyframe();
            animator.setStaticKeyframe(3);
            animator.resetKeyframe(5);
            animator.endKeyframe();
        }
    }

    @Override
    public void setAngles(EntityTroll entity, float limbSwing, float limbSwingAmount, float ageInTicks, float f3, float f4) {
        this.resetToDefaultPose();
        this.log1.showModel = true;

        animate(entity, limbSwing, limbSwingAmount, ageInTicks, f3, f4, 1);

        this.progressRotation(head, entity.stoneProgress, (float) Math.toRadians(-31), 0.0F, 0.0F);
        this.progressRotation(jaw, entity.stoneProgress, (float) Math.toRadians(54), 0.0F, 0.0F);
        this.progressRotation(leftarm, entity.stoneProgress, (float) Math.toRadians(10), (float) Math.toRadians(-73), (float) Math.toRadians(-60));
        this.progressRotation(leftarm2, entity.stoneProgress, (float) Math.toRadians(-80), 0.0F, 0.0F);
        this.progressRotation(rightarm, entity.stoneProgress, (float) Math.toRadians(-101), (float) Math.toRadians(70), 0);
        this.progressRotation(rightarm2, entity.stoneProgress, (float) Math.toRadians(-40), 0.0F, 0.0F);

        float speed_walk = 0.2F;
        float speed_idle = 0.05F;
        float degree_walk = 0.75F;
        float degree_idle = 0.5F;
        this.walk(this.rightleg, speed_walk, degree_walk * -0.75F, true, 0, 0F, limbSwing, limbSwingAmount);
        this.walk(this.leftleg, speed_walk, degree_walk * -0.75F, false, 0, 0F, limbSwing, limbSwingAmount);
        this.walk(this.rightleg2, speed_walk, degree_walk * -0.5F, true, 1, -0.3F, limbSwing, limbSwingAmount);
        this.walk(this.leftleg2, speed_walk, degree_walk * -0.5F, false, 1, 0.3F, limbSwing, limbSwingAmount);
        this.walk(this.leftarm, speed_walk, degree_walk * -0.75F, true, 0, 0F, limbSwing, limbSwingAmount);
        this.walk(this.leftarm2, speed_walk, degree_walk * -0.5F, true, 1, 0.3F, limbSwing, limbSwingAmount);
        this.swing(this.body, speed_walk, degree_walk * -0.5F, false, 0, 0F, limbSwing, limbSwingAmount);
        this.swing(this.upperBody, speed_walk, degree_walk * -0.25F, true, 0, 0F, limbSwing, limbSwingAmount);
        this.walk(this.rightarm, speed_walk, degree_walk * -0.25F, false, 0, 0F, limbSwing, limbSwingAmount);
        this.walk(this.rightarm2, speed_walk, degree_walk * -0.125F, false, 1, -0.3F, limbSwing, limbSwingAmount);
        this.walk(this.body, speed_idle, degree_idle * -0.1F, true, 0F, -0.1F, ageInTicks, 1);
        this.walk(this.rightleg, speed_idle, degree_idle * 0.1F, true, 0F, 0.1F, ageInTicks, 1);
        this.walk(this.leftleg, speed_idle, degree_idle * 0.1F, true, 0F, 0.1F, ageInTicks, 1);

        //this.flap(this.leftarm, speed_idle, degree_idle * -0.1F, true, 0, 0F, f2, 1);
        //this.flap(this.rightarm, speed_idle, degree_idle * -0.1F, false, 0, 0F, f2, 1);
        //this.flap(this.leftarm2, speed_idle, degree_idle * -0.1F, true, 0, -0.1F, f2, 1);
        //this.flap(this.rightarm2, speed_idle, degree_idle * -0.1F, false, 0, -0.1F, f2, 1);
        this.walk(this.jaw, speed_idle, degree_idle * -0.15F, true, 0F, -0.1F, ageInTicks, 1);
        this.walk(this.mouth, speed_idle, degree_idle * -0.15F, false, 0F, -0.1F, ageInTicks, 1);
        this.faceTarget(f3, f4, 1, this.head);

    }

    public void animateStatue(EntityTroll troll) {
        this.progressRotation(head, 20, (float) Math.toRadians(-31), 0.0F, 0.0F);
        this.progressRotation(jaw, 20, (float) Math.toRadians(54), 0.0F, 0.0F);
        this.progressRotation(leftarm, 20, (float) Math.toRadians(10), (float) Math.toRadians(-73), (float) Math.toRadians(-60));
        this.progressRotation(leftarm2, 20, (float) Math.toRadians(-80), 0.0F, 0.0F);
        this.progressRotation(rightarm, 20, (float) Math.toRadians(-101), (float) Math.toRadians(70), 0);
        this.progressRotation(rightarm2, 20, (float) Math.toRadians(-40), 0.0F, 0.0F);
    }

    @Override
    public void renderStatue(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, Entity living) {
        animateStatue((EntityTroll) living);
        this.log1.showModel = false;
        this.render(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
