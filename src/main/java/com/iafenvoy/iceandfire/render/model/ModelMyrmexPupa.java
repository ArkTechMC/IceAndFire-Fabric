package com.iafenvoy.iceandfire.render.model;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.iceandfire.entity.EntityMyrmexBase;
import com.iafenvoy.uranus.animation.IAnimatedEntity;
import com.iafenvoy.uranus.client.model.AdvancedModelBox;
import com.iafenvoy.uranus.client.model.ModelAnimator;
import com.iafenvoy.uranus.client.model.basic.BasicModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class ModelMyrmexPupa extends ModelDragonBase {
    public final AdvancedModelBox Body2;
    public final AdvancedModelBox Body3;
    public final AdvancedModelBox Body1;
    public final AdvancedModelBox legTopR2;
    public final AdvancedModelBox legTopR2_1;
    public final AdvancedModelBox Body4;
    public final AdvancedModelBox legTopR3;
    public final AdvancedModelBox legTopR3_1;
    public final AdvancedModelBox Body5;
    public final AdvancedModelBox Tail1;
    public final AdvancedModelBox Tail2;
    public final AdvancedModelBox Stinger;
    public final AdvancedModelBox legMidR3;
    public final AdvancedModelBox legBottomR3;
    public final AdvancedModelBox legMidR3_1;
    public final AdvancedModelBox legBottomR3_1;
    public final AdvancedModelBox Neck1;
    public final AdvancedModelBox legTopR1;
    public final AdvancedModelBox legTopR1_1;
    public final AdvancedModelBox HeadBase;
    public final AdvancedModelBox EyeR;
    public final AdvancedModelBox MandibleL;
    public final AdvancedModelBox MandibleR;
    public final AdvancedModelBox EyeL;
    public final AdvancedModelBox legMidR1;
    public final AdvancedModelBox legBottomR1;
    public final AdvancedModelBox legMidR1_1;
    public final AdvancedModelBox legBottomR1_1;
    public final AdvancedModelBox legMidR2;
    public final AdvancedModelBox legBottomR2;
    public final AdvancedModelBox legMidR2_1;
    public final AdvancedModelBox legBottomR2_1;
    private final ModelAnimator animator;

    public ModelMyrmexPupa() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.Neck1 = new AdvancedModelBox(this, 32, 22);
        this.Neck1.setPos(0.0F, 2.0F, -5.0F);
        this.Neck1.addBox(-2.5F, -2.0F, -3.5F, 5, 5, 4, 0.0F);
        this.setRotateAngle(this.Neck1, -1.6845917940249266F, 0.0F, 0.0F);
        this.MandibleR = new AdvancedModelBox(this, 0, 25);
        this.MandibleR.mirror = true;
        this.MandibleR.setPos(-3.4F, 3.7F, -7.7F);
        this.MandibleR.addBox(-2.0F, -2.51F, -5.1F, 4, 2, 6, 0.0F);
        this.setRotateAngle(this.MandibleR, 0.136659280431156F, -0.7285004297824331F, 0.0F);
        this.legBottomR2 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR2.mirror = true;
        this.legBottomR2.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR2.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(this.legBottomR2, 0.0F, 0.0F, 2.6179938779914944F);
        this.legMidR2 = new AdvancedModelBox(this, 11, 50);
        this.legMidR2.mirror = true;
        this.legMidR2.setPos(0.0F, 6.4F, 0.1F);
        this.legMidR2.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(this.legMidR2, 0.0F, 0.0F, -2.621484536495483F);
        this.legTopR1_1 = new AdvancedModelBox(this, 0, 54);
        this.legTopR1_1.setPos(3.3F, 1.0F, -1.4F);
        this.legTopR1_1.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(this.legTopR1_1, 0.0F, 0.8726646259971648F, -0.2617993877991494F);
        this.legMidR1_1 = new AdvancedModelBox(this, 11, 50);
        this.legMidR1_1.mirror = true;
        this.legMidR1_1.setPos(0.0F, 6.4F, 0.1F);
        this.legMidR1_1.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(this.legMidR1_1, 0.0F, 0.0F, -2.6179938779914944F);
        this.Body3 = new AdvancedModelBox(this, 36, 73);
        this.Body3.setPos(0.0F, 0.2F, 4.1F);
        this.Body3.addBox(-4.5F, -3.4F, -1.4F, 9, 9, 9, 0.0F);
        this.Tail1 = new AdvancedModelBox(this, 80, 51);
        this.Tail1.setPos(0.0F, -0.4F, 1.2F);
        this.Tail1.addBox(-5.5F, -3.2F, -0.1F, 11, 11, 13, 0.0F);
        this.setRotateAngle(this.Tail1, 0.045553093477052F, 0.0F, 0.0F);
        this.Stinger = new AdvancedModelBox(this, 60, 0);
        this.Stinger.setPos(0.0F, 0.6F, 6.0F);
        this.Stinger.addBox(-1.0F, -2.7F, -1.7F, 2, 8, 2, 0.0F);
        this.setRotateAngle(this.Stinger, 0.6373942428283291F, 0.0F, 0.0F);
        this.legMidR1 = new AdvancedModelBox(this, 11, 50);
        this.legMidR1.setPos(0.0F, 6.4F, 0.1F);
        this.legMidR1.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(this.legMidR1, 0.0F, 0.0F, 2.6179938779914944F);
        this.legMidR3_1 = new AdvancedModelBox(this, 11, 50);
        this.legMidR3_1.mirror = true;
        this.legMidR3_1.setPos(0.0F, 6.4F, 0.1F);
        this.legMidR3_1.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(this.legMidR3_1, 0.0F, 0.0F, -2.6179938779914944F);
        this.Body5 = new AdvancedModelBox(this, 82, 35);
        this.Body5.setPos(0.0F, -0.4F, 4.2F);
        this.Body5.addBox(-3.5F, -2.5F, -2.1F, 7, 8, 6, 0.0F);
        this.setRotateAngle(this.Body5, -0.045553093477052F, 0.0F, 0.0F);
        this.legMidR3 = new AdvancedModelBox(this, 11, 50);
        this.legMidR3.setPos(0.0F, 6.4F, 0.1F);
        this.legMidR3.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(this.legMidR3, 0.0F, 0.0F, 2.6179938779914944F);
        this.EyeL = new AdvancedModelBox(this, 40, 0);
        this.EyeL.setPos(4.0F, -0.3F, -3.5F);
        this.EyeL.addBox(-1.5F, -1.0F, -3.0F, 3, 2, 6, 0.0F);
        this.setRotateAngle(this.EyeL, 0.22689280275926282F, -0.08726646259971647F, 1.5707963267948966F);
        this.legTopR3_1 = new AdvancedModelBox(this, 0, 54);
        this.legTopR3_1.setPos(3.3F, 1.0F, 1.6F);
        this.legTopR3_1.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(this.legTopR3_1, 0.0F, -0.8726646259971648F, -0.2617993877991494F);
        this.EyeR = new AdvancedModelBox(this, 40, 0);
        this.EyeR.mirror = true;
        this.EyeR.setPos(-4.0F, -0.3F, -3.5F);
        this.EyeR.addBox(-1.5F, -1.0F, -3.0F, 3, 2, 6, 0.0F);
        this.setRotateAngle(this.EyeR, 0.22689280275926282F, 0.08726646259971647F, -1.5707963267948966F);
        this.legBottomR3_1 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR3_1.mirror = true;
        this.legBottomR3_1.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR3_1.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(this.legBottomR3_1, 0.0F, 0.0F, 2.6179938779914944F);
        this.legBottomR1_1 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR1_1.mirror = true;
        this.legBottomR1_1.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR1_1.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(this.legBottomR1_1, 0.0F, 0.0F, 2.6179938779914944F);
        this.Tail2 = new AdvancedModelBox(this, 60, 17);
        this.Tail2.setPos(0.0F, 0.6F, 12.0F);
        this.Tail2.addBox(-4.0F, -2.7F, -0.1F, 8, 8, 6, 0.0F);
        this.setRotateAngle(this.Tail2, -0.40980330836826856F, 0.0F, 0.0F);
        this.legTopR3 = new AdvancedModelBox(this, 0, 54);
        this.legTopR3.mirror = true;
        this.legTopR3.setPos(-3.3F, 1.0F, 1.6F);
        this.legTopR3.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(this.legTopR3, 0.0F, 0.8726646259971648F, 0.2617993877991494F);
        this.legMidR2_1 = new AdvancedModelBox(this, 11, 50);
        this.legMidR2_1.setPos(0.0F, 6.4F, 0.1F);
        this.legMidR2_1.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(this.legMidR2_1, 0.0F, 0.0F, 2.6179938779914944F);
        this.legTopR1 = new AdvancedModelBox(this, 0, 54);
        this.legTopR1.mirror = true;
        this.legTopR1.setPos(-3.3F, 1.0F, -1.4F);
        this.legTopR1.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(this.legTopR1, 0.0F, -0.8726646259971648F, 0.2617993877991494F);
        this.legTopR2 = new AdvancedModelBox(this, 0, 54);
        this.legTopR2.setPos(3.3F, 1.0F, 1.6F);
        this.legTopR2.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(this.legTopR2, 0.0F, 0.0F, -0.2617993877991494F);
        this.HeadBase = new AdvancedModelBox(this, 0, 0);
        this.HeadBase.setPos(0.0F, 1.9F, -4.4F);
        this.HeadBase.addBox(-4.0F, -2.51F, -10.1F, 8, 6, 10, 0.0F);
        this.setRotateAngle(this.HeadBase, 2.777342438698576F, 0.0F, 0.0F);
        this.Body1 = new AdvancedModelBox(this, 34, 47);
        this.Body1.setPos(0.0F, -0.7F, -1.0F);
        this.Body1.addBox(-3.5F, -2.1F, -6.3F, 7, 8, 9, 0.0F);
        this.setRotateAngle(this.Body1, 0.045553093477052F, 0.0F, 0.0F);
        this.Body4 = new AdvancedModelBox(this, 58, 35);
        this.Body4.setPos(0.0F, -0.4F, 7.3F);
        this.Body4.addBox(-3.0F, -2.7F, -1.5F, 6, 7, 4, 0.0F);
        this.setRotateAngle(this.Body4, -0.091106186954104F, 0.0F, 0.0F);
        this.MandibleL = new AdvancedModelBox(this, 0, 25);
        this.MandibleL.setPos(3.4F, 3.7F, -7.7F);
        this.MandibleL.addBox(-2.0F, -2.51F, -5.1F, 4, 2, 6, 0.0F);
        this.setRotateAngle(this.MandibleL, 0.17453292519943295F, 0.7740535232594852F, 0.0F);
        this.legBottomR3 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR3.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR3.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(this.legBottomR3, 0.0F, 0.0F, -2.6179938779914944F);
        this.legBottomR1 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR1.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR1.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(this.legBottomR1, 0.0F, 0.0F, -2.6179938779914944F);
        this.legTopR2_1 = new AdvancedModelBox(this, 0, 54);
        this.legTopR2_1.mirror = true;
        this.legTopR2_1.setPos(-3.3F, 1.0F, 1.6F);
        this.legTopR2_1.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(this.legTopR2_1, 0.0F, 0.0F, 0.2617993877991494F);
        this.Body2 = new AdvancedModelBox(this, 70, 53);
        this.Body2.setPos(0.0F, 16.0F, -6.0F);
        this.Body2.addBox(-3.0F, -2.7F, -0.1F, 6, 7, 4, 0.0F);
        this.setRotateAngle(this.Body2, -0.045553093477052F, 0.0F, 0.0F);
        this.legBottomR2_1 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR2_1.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR2_1.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(this.legBottomR2_1, 0.0F, 0.0F, -2.6179938779914944F);
        this.Body1.addChild(this.Neck1);
        this.HeadBase.addChild(this.MandibleR);
        this.legMidR2.addChild(this.legBottomR2);
        this.legTopR2.addChild(this.legMidR2);
        this.Body1.addChild(this.legTopR1_1);
        this.legTopR1_1.addChild(this.legMidR1_1);
        this.Body2.addChild(this.Body3);
        this.Body5.addChild(this.Tail1);
        this.Tail2.addChild(this.Stinger);
        this.legTopR1.addChild(this.legMidR1);
        this.legTopR3_1.addChild(this.legMidR3_1);
        this.Body4.addChild(this.Body5);
        this.legTopR3.addChild(this.legMidR3);
        this.HeadBase.addChild(this.EyeL);
        this.Body3.addChild(this.legTopR3_1);
        this.HeadBase.addChild(this.EyeR);
        this.legMidR3_1.addChild(this.legBottomR3_1);
        this.legMidR1_1.addChild(this.legBottomR1_1);
        this.Tail1.addChild(this.Tail2);
        this.Body3.addChild(this.legTopR3);
        this.legTopR2_1.addChild(this.legMidR2_1);
        this.Body1.addChild(this.legTopR1);
        this.Body2.addChild(this.legTopR2);
        this.Neck1.addChild(this.HeadBase);
        this.Body2.addChild(this.Body1);
        this.Body3.addChild(this.Body4);
        this.HeadBase.addChild(this.MandibleL);
        this.legMidR3.addChild(this.legBottomR3);
        this.legMidR1.addChild(this.legBottomR1);
        this.Body2.addChild(this.legTopR2_1);
        this.legMidR2_1.addChild(this.legBottomR2_1);
        this.animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.Body2);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.Body2, this.Body3, this.Body1, this.legTopR2, this.legTopR2_1, this.Body4, this.legTopR3, this.legTopR3_1, this.Body5,
                this.Tail1, this.Tail2, this.Stinger, this.legMidR3, this.legBottomR3, this.legMidR3_1, this.legBottomR3_1, this.Neck1, this.legTopR1, this.legTopR1_1,
                this.HeadBase, this.EyeR, this.MandibleL, this.MandibleR, this.EyeL, this.legMidR1, this.legBottomR1, this.legMidR1_1, this.legBottomR1_1, this.legMidR2, this.legBottomR2, this.legMidR2_1, this.legBottomR2_1);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        if (this.animator.setAnimation(EntityMyrmexBase.ANIMATION_PUPA_WIGGLE)) {
            this.animator.startKeyframe(5);
            ModelUtils.rotate(this.animator, this.Body1, 0, -15, 0);
            ModelUtils.rotate(this.animator, this.Body2, 0, -15, 0);
            ModelUtils.rotate(this.animator, this.Body3, 0, -15, 0);
            ModelUtils.rotate(this.animator, this.Body4, 0, 15, 0);
            ModelUtils.rotate(this.animator, this.Body5, 0, 15, 0);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            ModelUtils.rotate(this.animator, this.Body1, 0, 15, 0);
            ModelUtils.rotate(this.animator, this.Body2, 0, 15, 0);
            ModelUtils.rotate(this.animator, this.Body3, 0, 15, 0);
            ModelUtils.rotate(this.animator, this.Body4, 0, -15, 0);
            ModelUtils.rotate(this.animator, this.Body5, 0, -15, 0);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            ModelUtils.rotate(this.animator, this.Body1, 0, -15, 0);
            ModelUtils.rotate(this.animator, this.Body2, 0, -15, 0);
            ModelUtils.rotate(this.animator, this.Body3, 0, -15, 0);
            ModelUtils.rotate(this.animator, this.Body4, 0, 15, 0);
            ModelUtils.rotate(this.animator, this.Body5, 0, 15, 0);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
    }

    @Override
    public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.animate((IAnimatedEntity) entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch, 1);
        this.resetToDefaultPose();
        float speed_idle = 0.025F;
        float degree_idle = 0.25F;
        AdvancedModelBox[] PARTS = new AdvancedModelBox[]{this.Body1, this.Body2, this.Body3, this.Body4, this.Body5};
        this.bob(this.Body2, speed_idle, degree_idle * 2.5F, true, animationProgress, 1);
        this.chainSwing(PARTS, speed_idle, degree_idle * 0.15F, 1, animationProgress, 1);
        this.chainFlap(PARTS, speed_idle, degree_idle * 0.15F, 1, animationProgress, 1);
    }

    @Override
    public void renderStatue(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, Entity living) {
        this.render(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
