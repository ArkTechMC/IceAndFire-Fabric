package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexSoldier;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class ModelMyrmexSoldier extends ModelMyrmexBase {
    public AdvancedModelBox Body2;
    public AdvancedModelBox Body3;
    public AdvancedModelBox Body1;
    public AdvancedModelBox legTopR2;
    public AdvancedModelBox legTopR2_1;
    public AdvancedModelBox Body4;
    public AdvancedModelBox legTopR3;
    public AdvancedModelBox legTopR3_1;
    public AdvancedModelBox Body5;
    public AdvancedModelBox Tail1;
    public AdvancedModelBox Tail2;
    public AdvancedModelBox Stinger;
    public AdvancedModelBox legMidR3;
    public AdvancedModelBox legBottomR3;
    public AdvancedModelBox legMidR3_1;
    public AdvancedModelBox legBottomR3_1;
    public AdvancedModelBox Neck1;
    public AdvancedModelBox legTopR1;
    public AdvancedModelBox legTopR1_1;
    public AdvancedModelBox HeadBase;
    public AdvancedModelBox EyeR;
    public AdvancedModelBox MandibleL;
    public AdvancedModelBox MandibleR;
    public AdvancedModelBox EyeL;
    public AdvancedModelBox legMidR1;
    public AdvancedModelBox legBottomR1;
    public AdvancedModelBox legMidR1_1;
    public AdvancedModelBox legBottomR1_1;
    public AdvancedModelBox legMidR2;
    public AdvancedModelBox legBottomR2;
    public AdvancedModelBox legMidR2_1;
    public AdvancedModelBox legBottomR2_1;
    private final ModelAnimator animator;

    public ModelMyrmexSoldier() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.legMidR3 = new AdvancedModelBox(this, 11, 50);
        this.legMidR3.setPos(0.0F, 6.4F, 0.1F);
        this.legMidR3.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(legMidR3, 0.0F, 0.0F, 1.1383037381507017F);
        this.legBottomR1_1 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR1_1.mirror = true;
        this.legBottomR1_1.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR1_1.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(legBottomR1_1, 0.0F, 0.0F, 1.3203415791337103F);
        this.legTopR1 = new AdvancedModelBox(this, 0, 54);
        this.legTopR1.mirror = true;
        this.legTopR1.setPos(-3.3F, 1.0F, -1.4F);
        this.legTopR1.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(legTopR1, -0.5009094953223726F, -0.22759093446006054F, 0.6981317007977318F);
        this.legBottomR3 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR3.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR3.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(legBottomR3, 0.0F, 0.0F, -1.3203415791337103F);
        this.legTopR2 = new AdvancedModelBox(this, 0, 54);
        this.legTopR2.setPos(3.3F, 1.0F, 1.6F);
        this.legTopR2.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(legTopR2, 0.0F, 0.0F, -0.6981317007977318F);
        this.Body3 = new AdvancedModelBox(this, 36, 73);
        this.Body3.setPos(0.0F, 0.2F, 4.1F);
        this.Body3.addBox(-4.5F, -3.4F, -1.4F, 9, 9, 9, 0.0F);
        this.Body4 = new AdvancedModelBox(this, 58, 35);
        this.Body4.setPos(0.0F, -0.4F, 7.3F);
        this.Body4.addBox(-3.0F, -2.7F, -1.5F, 6, 7, 4, 0.0F);
        this.setRotateAngle(Body4, 0.40980330836826856F, 0.0F, 0.0F);
        this.Stinger = new AdvancedModelBox(this, 60, 0);
        this.Stinger.setPos(0.0F, 0.6F, 6.0F);
        this.Stinger.addBox(-1.0F, -2.7F, -1.7F, 2, 10, 2, 0.0F);
        this.setRotateAngle(Stinger, 0.6373942428283291F, 0.0F, 0.0F);
        this.legBottomR2 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR2.mirror = true;
        this.legBottomR2.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR2.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(legBottomR2, 0.0F, 0.0F, 1.3203415791337103F);
        this.EyeL = new AdvancedModelBox(this, 40, 0);
        this.EyeL.setPos(4.0F, -0.3F, -3.5F);
        this.EyeL.addBox(-1.5F, -1.0F, -3.0F, 3, 2, 6, 0.0F);
        this.setRotateAngle(EyeL, 0.22689280275926282F, -0.08726646259971647F, 1.5707963267948966F);
        this.legMidR3_1 = new AdvancedModelBox(this, 11, 50);
        this.legMidR3_1.mirror = true;
        this.legMidR3_1.setPos(0.0F, 6.4F, 0.1F);
        this.legMidR3_1.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(legMidR3_1, 0.0F, 0.0F, -1.1383037381507017F);
        this.legBottomR1 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR1.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR1.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(legBottomR1, 0.0F, 0.0F, -1.3203415791337103F);
        this.legTopR3 = new AdvancedModelBox(this, 0, 54);
        this.legTopR3.mirror = true;
        this.legTopR3.setPos(-3.3F, 1.0F, 1.6F);
        this.legTopR3.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(legTopR3, 0.5009094953223726F, 0.22759093446006054F, 0.7740535232594852F);
        this.legTopR1_1 = new AdvancedModelBox(this, 0, 54);
        this.legTopR1_1.setPos(3.3F, 1.0F, -1.4F);
        this.legTopR1_1.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(legTopR1_1, -0.5009094953223726F, 0.22759093446006054F, -0.6981317007977318F);
        this.legTopR3_1 = new AdvancedModelBox(this, 0, 54);
        this.legTopR3_1.setPos(3.3F, 1.0F, 1.6F);
        this.legTopR3_1.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(legTopR3_1, 0.5009094953223726F, -0.22759093446006054F, -0.7740535232594852F);
        this.Tail2 = new AdvancedModelBox(this, 60, 17);
        this.Tail2.setPos(0.0F, 0.6F, 12.0F);
        this.Tail2.addBox(-4.0F, -2.7F, -0.1F, 8, 8, 6, 0.0F);
        this.setRotateAngle(Tail2, -0.40980330836826856F, 0.0F, 0.0F);
        this.Tail1 = new AdvancedModelBox(this, 80, 51);
        this.Tail1.setPos(0.0F, -0.4F, 1.2F);
        this.Tail1.addBox(-5.5F, -3.2F, -0.1F, 11, 11, 13, 0.0F);
        this.setRotateAngle(Tail1, -0.4553564018453205F, 0.0F, 0.0F);
        this.legMidR2 = new AdvancedModelBox(this, 11, 50);
        this.legMidR2.mirror = true;
        this.legMidR2.setPos(0.0F, 6.4F, 0.1F);
        this.legMidR2.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(legMidR2, 0.0F, 0.0F, -1.1383037381507017F);
        this.legTopR2_1 = new AdvancedModelBox(this, 0, 54);
        this.legTopR2_1.mirror = true;
        this.legTopR2_1.setPos(-3.3F, 1.0F, 1.6F);
        this.legTopR2_1.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(legTopR2_1, 0.0F, 0.0F, 0.6981317007977318F);
        this.Body2 = new AdvancedModelBox(this, 70, 53);
        this.Body2.setPos(0.0F, 10.0F, -6.0F);
        this.Body2.addBox(-3.0F, -2.7F, -0.1F, 6, 7, 4, 0.0F);
        this.setRotateAngle(Body2, -0.045553093477052F, 0.0F, 0.0F);
        this.Body1 = new AdvancedModelBox(this, 34, 47);
        this.Body1.setPos(0.0F, -0.7F, -1.0F);
        this.Body1.addBox(-3.5F, -2.1F, -6.3F, 7, 8, 9, 0.0F);
        this.setRotateAngle(Body1, 0.045553093477052F, 0.0F, 0.0F);
        this.legBottomR2_1 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR2_1.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR2_1.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(legBottomR2_1, 0.0F, 0.0F, -1.3203415791337103F);
        this.MandibleL = new AdvancedModelBox(this, 0, 25);
        this.MandibleL.setPos(4.4F, 3.7F, -6.7F);
        this.MandibleL.addBox(-2.0F, -2.51F, -10.1F, 4, 2, 11, 0.0F);
        this.setRotateAngle(MandibleL, 0.17453292519943295F, 0.18203784098300857F, 0.0F);
        this.legMidR1_1 = new AdvancedModelBox(this, 11, 50);
        this.legMidR1_1.mirror = true;
        this.legMidR1_1.setPos(0.0F, 6.4F, 0.1F);
        this.legMidR1_1.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(legMidR1_1, 0.0F, 0.0F, -1.1383037381507017F);
        this.Body5 = new AdvancedModelBox(this, 82, 35);
        this.Body5.setPos(0.0F, -0.4F, 4.2F);
        this.Body5.addBox(-3.5F, -2.5F, -2.1F, 7, 8, 6, 0.0F);
        this.setRotateAngle(Body5, -0.045553093477052F, 0.0F, 0.0F);
        this.legMidR2_1 = new AdvancedModelBox(this, 11, 50);
        this.legMidR2_1.setPos(0.0F, 6.4F, 0.1F);
        this.legMidR2_1.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(legMidR2_1, 0.0F, 0.0F, 1.1383037381507017F);
        this.legBottomR3_1 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR3_1.mirror = true;
        this.legBottomR3_1.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR3_1.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(legBottomR3_1, 0.0F, 0.0F, 1.3203415791337103F);
        this.MandibleR = new AdvancedModelBox(this, 0, 25);
        this.MandibleR.mirror = true;
        this.MandibleR.setPos(-4.4F, 3.7F, -6.7F);
        this.MandibleR.addBox(-2.0F, -2.51F, -10.1F, 4, 2, 11, 0.0F);
        this.setRotateAngle(MandibleR, 0.17453292519943295F, -0.18203784098300857F, 0.0F);
        this.EyeR = new AdvancedModelBox(this, 40, 0);
        this.EyeR.mirror = true;
        this.EyeR.setPos(-4.0F, -0.3F, -3.5F);
        this.EyeR.addBox(-1.5F, -1.0F, -3.0F, 3, 2, 6, 0.0F);
        this.setRotateAngle(EyeR, 0.22689280275926282F, 0.08726646259971647F, -1.5707963267948966F);
        this.Neck1 = new AdvancedModelBox(this, 32, 22);
        this.Neck1.setPos(0.0F, 0.0F, -6.0F);
        this.Neck1.addBox(-2.5F, -2.0F, -3.5F, 5, 5, 4, 0.0F);
        this.setRotateAngle(Neck1, -0.27314402793711257F, 0.0F, 0.0F);
        this.HeadBase = new AdvancedModelBox(this, 0, 0);
        this.HeadBase.setPos(0.0F, -0.1F, -2.4F);
        this.HeadBase.addBox(-4.5F, -2.51F, -11.1F, 9, 6, 11, 0.0F);
        this.setRotateAngle(HeadBase, 0.6373942428283291F, 0.0F, 0.0F);
        this.legMidR1 = new AdvancedModelBox(this, 11, 50);
        this.legMidR1.setPos(0.0F, 6.4F, 0.1F);
        this.legMidR1.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(legMidR1, 0.0F, 0.0F, 1.1383037381507017F);
        this.legTopR3.addChild(this.legMidR3);
        this.legMidR1_1.addChild(this.legBottomR1_1);
        this.Body1.addChild(this.legTopR1);
        this.legMidR3.addChild(this.legBottomR3);
        this.Body2.addChild(this.legTopR2);
        this.Body2.addChild(this.Body3);
        this.Body3.addChild(this.Body4);
        this.Tail2.addChild(this.Stinger);
        this.legMidR2.addChild(this.legBottomR2);
        this.HeadBase.addChild(this.EyeL);
        this.legTopR3_1.addChild(this.legMidR3_1);
        this.legMidR1.addChild(this.legBottomR1);
        this.Body3.addChild(this.legTopR3);
        this.Body1.addChild(this.legTopR1_1);
        this.Body3.addChild(this.legTopR3_1);
        this.Tail1.addChild(this.Tail2);
        this.Body5.addChild(this.Tail1);
        this.legTopR2.addChild(this.legMidR2);
        this.Body2.addChild(this.legTopR2_1);
        this.Body2.addChild(this.Body1);
        this.legMidR2_1.addChild(this.legBottomR2_1);
        this.HeadBase.addChild(this.MandibleL);
        this.legTopR1_1.addChild(this.legMidR1_1);
        this.Body4.addChild(this.Body5);
        this.legTopR2_1.addChild(this.legMidR2_1);
        this.legMidR3_1.addChild(this.legBottomR3_1);
        this.HeadBase.addChild(this.MandibleR);
        this.HeadBase.addChild(this.EyeR);
        this.Body1.addChild(this.Neck1);
        this.Neck1.addChild(this.HeadBase);
        this.legTopR1.addChild(this.legMidR1);
        animator = ModelAnimator.create();
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(Body2);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(Body2, Body3, Body1, legTopR2, legTopR2_1, Body4, legTopR3, legTopR3_1, Body5,
            Tail1, Tail2, Stinger, legMidR3, legBottomR3, legMidR3_1, legBottomR3_1, Neck1, legTopR1, legTopR1_1,
            HeadBase, EyeR, MandibleL, MandibleR, EyeL, legMidR1, legBottomR1, legMidR1_1, legBottomR1_1,
            legMidR2, legBottomR2, legMidR2_1, legBottomR2_1);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        animator.update(entity);
        if (animator.setAnimation(EntityMyrmexSoldier.ANIMATION_BITE)) {
            animator.startKeyframe(5);
            ModelUtils.rotate(animator, Neck1, -50, 0, 0);
            ModelUtils.rotate(animator, HeadBase, 50, 0, 0);
            ModelUtils.rotate(animator, MandibleR, 0, 35, 0);
            ModelUtils.rotate(animator, MandibleL, 0, -35, 0);
            animator.endKeyframe();
            animator.startKeyframe(5);
            ModelUtils.rotate(animator, Neck1, 30, 0, 0);
            ModelUtils.rotate(animator, HeadBase, -30, 0, 0);
            ModelUtils.rotate(animator, MandibleR, 0, -50, 0);
            ModelUtils.rotate(animator, MandibleL, 0, 50, 0);
            animator.endKeyframe();
            animator.resetKeyframe(5);
        }
        if (animator.setAnimation(EntityMyrmexSoldier.ANIMATION_STING)) {
            animator.startKeyframe(5);
            animator.move(Body2, 0, -4, 0);
            ModelUtils.rotate(animator, Body3, -35, 0, 0);
            ModelUtils.rotate(animator, Body4, -49, 0, 0);
            ModelUtils.rotate(animator, Body5, -5, 0, 0);
            ModelUtils.rotate(animator, Tail1, -57, 0, 0);
            ModelUtils.rotate(animator, Tail2, -40, 0, 0);
            ModelUtils.rotate(animator, Stinger, 90, 0, 0);
            ModelUtils.rotateFrom(animator, legTopR3, 44, -7, 44);
            ModelUtils.rotateFrom(animator, legTopR3_1, 44, 7, -44);
            ModelUtils.rotateFrom(animator, legMidR3, 0, 0, 50);
            ModelUtils.rotateFrom(animator, legMidR2, 0, 0, -45);
            ModelUtils.rotateFrom(animator, legMidR1, 0, 0, 45);
            ModelUtils.rotateFrom(animator, legMidR3_1, 0, 0, -50);
            ModelUtils.rotateFrom(animator, legMidR2_1, 0, 0, 45);
            ModelUtils.rotateFrom(animator, legMidR1_1, 0, 0, -45);
            animator.endKeyframe();
            animator.resetKeyframe(10);
        }

    }

    @Override
    public void setAngles(Entity entity, float f, float f1, float f2, float f3, float f4) {
        animate((IAnimatedEntity) entity, f, f1, f2, f3, f4, 1);
        AdvancedModelBox[] GASTER = new AdvancedModelBox[]{Body4, Body5, Tail1, Tail2, Stinger};
        AdvancedModelBox[] NECK = new AdvancedModelBox[]{Neck1, HeadBase};
        AdvancedModelBox[] LEGR1 = new AdvancedModelBox[]{legTopR1, legMidR1, legBottomR1};
        AdvancedModelBox[] LEGR2 = new AdvancedModelBox[]{legTopR2, legMidR2, legBottomR2};
        AdvancedModelBox[] LEGR3 = new AdvancedModelBox[]{legTopR3, legMidR3, legBottomR3};
        AdvancedModelBox[] LEGL1 = new AdvancedModelBox[]{legTopR1_1, legMidR1_1, legBottomR1_1};
        AdvancedModelBox[] LEGL2 = new AdvancedModelBox[]{legTopR2_1, legMidR2_1, legBottomR2_1};
        AdvancedModelBox[] LEGL3 = new AdvancedModelBox[]{legTopR3_1, legMidR3_1, legBottomR3_1};
        float speed_walk = 0.9F;
        float speed_idle = 0.05F;
        float degree_walk = 0.3F;
        float degree_idle = 0.25F;
        if (entity.getPassengerList().isEmpty()) {
            this.faceTarget(f3, f4, 2, NECK);
        }
        this.chainWave(GASTER, speed_idle, degree_idle * 0.25F, 0, f2, 1);
        this.chainWave(NECK, speed_idle, degree_idle * -0.15F, 2, f2, 1);
        this.swing(MandibleR, speed_idle * 2F, degree_idle * -0.75F, false, 1, 0.2F, f2, 1);
        this.swing(MandibleL, speed_idle * 2F, degree_idle * -0.75F, true, 1, 0.2F, f2, 1);
        this.animateLeg(LEGR1, speed_walk, degree_walk, false, 0, 1, f, f1);
        this.animateLeg(LEGR3, speed_walk, degree_walk, false, 0, 1, f, f1);
        this.animateLeg(LEGR2, speed_walk, degree_walk, true, 0, 1, f, f1);

        this.animateLeg(LEGL1, speed_walk, degree_walk, false, 1, -1, f, f1);
        this.animateLeg(LEGL3, speed_walk, degree_walk, false, 1, -1, f, f1);
        this.animateLeg(LEGL2, speed_walk, degree_walk, true, 1, -1, f, f1);
    }

    private void animateLeg(AdvancedModelBox[] models, float speed, float degree, boolean reverse, float offset, float weight, float f, float f1) {
        this.flap(models[0], speed, degree * 0.4F, reverse, offset, weight * 0.2F, f, f1);
        this.flap(models[1], speed, degree * 2, reverse, offset, weight * -0.4F, f, f1);
        this.flap(models[1], speed, -degree * 1.2F, reverse, offset, weight * 0.5F, f, f1);
        this.walk(models[0], speed, degree, reverse, offset, 0F, f, f1);

    }

    @Override
    public BasicModelPart[] getHeadParts() {
        return new BasicModelPart[]{Neck1, HeadBase};
    }

    @Override
    public void renderStatue(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, Entity living) {
        this.render(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
