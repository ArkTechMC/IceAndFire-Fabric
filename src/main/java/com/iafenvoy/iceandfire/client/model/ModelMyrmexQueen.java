package com.iafenvoy.iceandfire.client.model;

import com.iafenvoy.citadel.animation.IAnimatedEntity;
import com.iafenvoy.citadel.client.model.AdvancedModelBox;
import com.iafenvoy.citadel.client.model.ModelAnimator;
import com.iafenvoy.citadel.client.model.basic.BasicModelPart;
import com.iafenvoy.iceandfire.entity.EntityMyrmexQueen;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelMyrmexQueen extends ModelMyrmexBase {
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
    public final AdvancedModelBox Tail3;
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
    public final AdvancedModelBox crestbase;
    public final AdvancedModelBox crest1;
    public final AdvancedModelBox crest2;
    public final AdvancedModelBox legMidR1;
    public final AdvancedModelBox legBottomR1;
    public final AdvancedModelBox legMidR1_1;
    public final AdvancedModelBox legBottomR1_1;
    public final AdvancedModelBox legMidR2;
    public final AdvancedModelBox legBottomR2;
    public final AdvancedModelBox legMidR2_1;
    public final AdvancedModelBox legBottomR2_1;
    private final ModelAnimator animator;

    public ModelMyrmexQueen() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.Tail3 = new AdvancedModelBox(this, 55, 12);
        this.Tail3.setPos(0.0F, 0.3F, 11.0F);
        this.Tail3.addBox(-4.0F, -2.7F, -0.1F, 8, 8, 11, 0.0F);
        this.setRotateAngle(this.Tail3, -0.22759093446006054F, 0.0F, 0.0F);
        this.crest1 = new AdvancedModelBox(this, 0, 88);
        this.crest1.mirror = true;
        this.crest1.setPos(-1.2F, -0.1F, -1.0F);
        this.crest1.addBox(-1.5F, -2.51F, -0.1F, 3, 1, 6, 0.0F);
        this.setRotateAngle(this.crest1, 0.40980330836826856F, -0.17453292519943295F, -0.2792526803190927F);
        this.legBottomR3_1 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR3_1.mirror = true;
        this.legBottomR3_1.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR3_1.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(this.legBottomR3_1, 0.0F, 0.0F, 1.3203415791337103F);
        this.Stinger = new AdvancedModelBox(this, 60, 0);
        this.Stinger.setPos(0.0F, 1.6F, 11.0F);
        this.Stinger.addBox(-1.0F, -2.7F, -1.7F, 2, 8, 2, 0.0F);
        this.setRotateAngle(this.Stinger, 0.6373942428283291F, 0.0F, 0.0F);
        this.Body1 = new AdvancedModelBox(this, 34, 47);
        this.Body1.setPos(0.0F, -0.7F, -1.0F);
        this.Body1.addBox(-3.5F, -2.1F, -6.3F, 7, 8, 9, 0.0F);
        this.setRotateAngle(this.Body1, 0.045553093477052F, 0.0F, 0.0F);
        this.Body2 = new AdvancedModelBox(this, 70, 53);
        this.Body2.setPos(0.0F, 10.0F, -6.0F);
        this.Body2.addBox(-3.0F, -2.7F, -0.1F, 6, 7, 4, 0.0F);
        this.setRotateAngle(this.Body2, -0.045553093477052F, 0.0F, 0.0F);
        this.legTopR2_1 = new AdvancedModelBox(this, 0, 54);
        this.legTopR2_1.mirror = true;
        this.legTopR2_1.setPos(-3.3F, 1.0F, 1.6F);
        this.legTopR2_1.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(this.legTopR2_1, 0.0F, 0.0F, 0.6981317007977318F);
        this.legBottomR1_1 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR1_1.mirror = true;
        this.legBottomR1_1.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR1_1.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(this.legBottomR1_1, 0.0F, 0.0F, 1.3203415791337103F);
        this.Tail2 = new AdvancedModelBox(this, 80, 76);
        this.Tail2.setPos(0.0F, 0.3F, 12.2F);
        this.Tail2.addBox(-5.0F, -2.7F, -0.1F, 10, 10, 13, 0.0F);
        this.setRotateAngle(this.Tail2, -0.091106186954104F, 0.0F, 0.0F);
        this.legMidR1 = new AdvancedModelBox(this, 11, 50);
        this.legMidR1.setPos(0.0F, 6.4F, 0.1F);
        this.legMidR1.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(this.legMidR1, 0.0F, 0.0F, 1.1383037381507017F);
        this.MandibleL = new AdvancedModelBox(this, 0, 25);
        this.MandibleL.setPos(3.4F, 3.7F, -7.7F);
        this.MandibleL.addBox(-2.0F, -2.51F, -5.1F, 4, 2, 6, 0.0F);
        this.setRotateAngle(this.MandibleL, 0.17453292519943295F, 0.18203784098300857F, 0.0F);
        this.legBottomR2 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR2.mirror = true;
        this.legBottomR2.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR2.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(this.legBottomR2, 0.0F, 0.0F, 1.3203415791337103F);
        this.legMidR3 = new AdvancedModelBox(this, 11, 50);
        this.legMidR3.setPos(0.0F, 6.4F, 0.1F);
        this.legMidR3.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(this.legMidR3, 0.0F, 0.0F, 1.1383037381507017F);
        this.EyeL = new AdvancedModelBox(this, 40, 0);
        this.EyeL.setPos(4.0F, -0.3F, -3.5F);
        this.EyeL.addBox(-1.5F, -1.0F, -3.0F, 3, 2, 6, 0.0F);
        this.setRotateAngle(this.EyeL, 0.22689280275926282F, -0.08726646259971647F, 1.5707963267948966F);
        this.Body4 = new AdvancedModelBox(this, 58, 35);
        this.Body4.setPos(0.0F, -0.4F, 7.3F);
        this.Body4.addBox(-3.0F, -2.7F, -1.5F, 6, 7, 4, 0.0F);
        this.setRotateAngle(this.Body4, 0.136659280431156F, 0.0F, 0.0F);
        this.Neck1 = new AdvancedModelBox(this, 32, 22);
        this.Neck1.setPos(0.0F, 0.0F, -6.0F);
        this.Neck1.addBox(-2.5F, -2.0F, -3.5F, 5, 5, 4, 0.0F);
        this.setRotateAngle(this.Neck1, -0.27314402793711257F, 0.0F, 0.0F);
        this.legMidR2_1 = new AdvancedModelBox(this, 11, 50);
        this.legMidR2_1.setPos(0.0F, 6.4F, 0.1F);
        this.legMidR2_1.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(this.legMidR2_1, 0.0F, 0.0F, 1.1383037381507017F);
        this.legTopR3 = new AdvancedModelBox(this, 0, 54);
        this.legTopR3.mirror = true;
        this.legTopR3.setPos(-3.3F, 1.0F, 1.6F);
        this.legTopR3.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(this.legTopR3, 0.5009094953223726F, 0.22759093446006054F, 0.7740535232594852F);
        this.Tail1 = new AdvancedModelBox(this, 80, 51);
        this.Tail1.setPos(0.0F, -0.4F, 1.2F);
        this.Tail1.addBox(-5.5F, -2.7F, -0.1F, 11, 11, 13, 0.0F);
        this.setRotateAngle(this.Tail1, -0.091106186954104F, 0.0F, 0.0F);
        this.Body5 = new AdvancedModelBox(this, 82, 35);
        this.Body5.setPos(0.0F, -0.4F, 4.2F);
        this.Body5.addBox(-3.5F, -2.5F, -2.1F, 7, 8, 6, 0.0F);
        this.setRotateAngle(this.Body5, -0.045553093477052F, 0.0F, 0.0F);
        this.crest2 = new AdvancedModelBox(this, 0, 88);
        this.crest2.setPos(1.2F, -0.1F, -1.0F);
        this.crest2.addBox(-1.5F, -2.51F, -0.1F, 3, 1, 6, 0.0F);
        this.setRotateAngle(this.crest2, 0.40980330836826856F, 0.17453292519943295F, 0.2792526803190927F);
        this.legTopR2 = new AdvancedModelBox(this, 0, 54);
        this.legTopR2.setPos(3.3F, 1.0F, 1.6F);
        this.legTopR2.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(this.legTopR2, 0.0F, 0.0F, -0.6981317007977318F);
        this.Body3 = new AdvancedModelBox(this, 0, 67);
        this.Body3.setPos(0.0F, 0.2F, 4.1F);
        this.Body3.addBox(-4.5F, -3.4F, -1.4F, 9, 9, 9, 0.0F);
        this.legMidR1_1 = new AdvancedModelBox(this, 11, 50);
        this.legMidR1_1.mirror = true;
        this.legMidR1_1.setPos(0.0F, 6.4F, 0.1F);
        this.legMidR1_1.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(this.legMidR1_1, 0.0F, 0.0F, -1.1383037381507017F);
        this.HeadBase = new AdvancedModelBox(this, 0, 0);
        this.HeadBase.setPos(0.0F, -0.1F, -2.4F);
        this.HeadBase.addBox(-4.0F, -2.51F, -10.1F, 8, 6, 10, 0.0F);
        this.setRotateAngle(this.HeadBase, 0.6373942428283291F, 0.0F, 0.0F);
        this.crestbase = new AdvancedModelBox(this, 0, 100);
        this.crestbase.setPos(0.0F, -2.9F, 1.0F);
        this.crestbase.addBox(-3.5F, -2.51F, -6.1F, 7, 2, 6, 0.0F);
        this.setRotateAngle(this.crestbase, 0.4553564018453205F, 0.0F, 0.0F);
        this.legBottomR2_1 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR2_1.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR2_1.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(this.legBottomR2_1, 0.0F, 0.0F, -1.3203415791337103F);
        this.legTopR3_1 = new AdvancedModelBox(this, 0, 54);
        this.legTopR3_1.setPos(3.3F, 1.0F, 1.6F);
        this.legTopR3_1.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(this.legTopR3_1, 0.5009094953223726F, -0.22759093446006054F, -0.7740535232594852F);
        this.legBottomR1 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR1.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR1.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(this.legBottomR1, 0.0F, 0.0F, -1.3203415791337103F);
        this.legBottomR3 = new AdvancedModelBox(this, 22, 51);
        this.legBottomR3.setPos(0.0F, 10.4F, 0.0F);
        this.legBottomR3.addBox(-1.01F, 0.0F, -0.9F, 2, 13, 2, 0.0F);
        this.setRotateAngle(this.legBottomR3, 0.0F, 0.0F, -1.3203415791337103F);
        this.EyeR = new AdvancedModelBox(this, 40, 0);
        this.EyeR.mirror = true;
        this.EyeR.setPos(-4.0F, -0.3F, -3.5F);
        this.EyeR.addBox(-1.5F, -1.0F, -3.0F, 3, 2, 6, 0.0F);
        this.setRotateAngle(this.EyeR, 0.22689280275926282F, 0.08726646259971647F, -1.5707963267948966F);
        this.legMidR2 = new AdvancedModelBox(this, 11, 50);
        this.legMidR2.mirror = true;
        this.legMidR2.setPos(0.0F, 6.4F, 0.1F);
        this.legMidR2.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(this.legMidR2, 0.0F, 0.0F, -1.1383037381507017F);
        this.legTopR1_1 = new AdvancedModelBox(this, 0, 54);
        this.legTopR1_1.setPos(3.3F, 1.0F, -1.4F);
        this.legTopR1_1.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(this.legTopR1_1, -0.5009094953223726F, 0.22759093446006054F, -0.6981317007977318F);
        this.legMidR3_1 = new AdvancedModelBox(this, 11, 50);
        this.legMidR3_1.mirror = true;
        this.legMidR3_1.setPos(0.0F, 6.4F, 0.1F);
        this.legMidR3_1.addBox(-1.5F, 0.0F, -1.0F, 3, 12, 2, 0.0F);
        this.setRotateAngle(this.legMidR3_1, 0.0F, 0.0F, -1.1383037381507017F);
        this.legTopR1 = new AdvancedModelBox(this, 0, 54);
        this.legTopR1.mirror = true;
        this.legTopR1.setPos(-3.3F, 1.0F, -1.4F);
        this.legTopR1.addBox(-1.0F, 0.0F, -1.5F, 2, 7, 3, 0.0F);
        this.setRotateAngle(this.legTopR1, -0.5009094953223726F, -0.22759093446006054F, 0.6981317007977318F);
        this.MandibleR = new AdvancedModelBox(this, 0, 25);
        this.MandibleR.mirror = true;
        this.MandibleR.setPos(-3.4F, 3.7F, -7.7F);
        this.MandibleR.addBox(-2.0F, -2.51F, -5.1F, 4, 2, 6, 0.0F);
        this.setRotateAngle(this.MandibleR, 0.17453292519943295F, -0.18203784098300857F, 0.0F);
        this.Tail2.addChild(this.Tail3);
        this.crestbase.addChild(this.crest1);
        this.legMidR3_1.addChild(this.legBottomR3_1);
        this.Tail3.addChild(this.Stinger);
        this.Body2.addChild(this.Body1);
        this.Body2.addChild(this.legTopR2_1);
        this.legMidR1_1.addChild(this.legBottomR1_1);
        this.Tail1.addChild(this.Tail2);
        this.legTopR1.addChild(this.legMidR1);
        this.HeadBase.addChild(this.MandibleL);
        this.legMidR2.addChild(this.legBottomR2);
        this.legTopR3.addChild(this.legMidR3);
        this.HeadBase.addChild(this.EyeL);
        this.Body3.addChild(this.Body4);
        this.Body1.addChild(this.Neck1);
        this.legTopR2_1.addChild(this.legMidR2_1);
        this.Body3.addChild(this.legTopR3);
        this.Body5.addChild(this.Tail1);
        this.Body4.addChild(this.Body5);
        this.crestbase.addChild(this.crest2);
        this.Body2.addChild(this.legTopR2);
        this.Body2.addChild(this.Body3);
        this.legTopR1_1.addChild(this.legMidR1_1);
        this.Neck1.addChild(this.HeadBase);
        this.HeadBase.addChild(this.crestbase);
        this.legMidR2_1.addChild(this.legBottomR2_1);
        this.Body3.addChild(this.legTopR3_1);
        this.legMidR1.addChild(this.legBottomR1);
        this.legMidR3.addChild(this.legBottomR3);
        this.HeadBase.addChild(this.EyeR);
        this.legTopR2.addChild(this.legMidR2);
        this.Body1.addChild(this.legTopR1_1);
        this.legTopR3_1.addChild(this.legMidR3_1);
        this.Body1.addChild(this.legTopR1);
        this.HeadBase.addChild(this.MandibleR);
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
                this.Tail1, this.Tail2, this.Tail3, this.Stinger, this.legMidR3, this.legBottomR3, this.legMidR3_1, this.legBottomR3_1, this.Neck1,
                this.legTopR1, this.legTopR1_1, this.HeadBase, this.EyeR, this.MandibleL, this.MandibleR, this.EyeL, this.crestbase, this.crest1, this.crest2,
                this.legMidR1, this.legBottomR1, this.legMidR1_1, this.legBottomR1_1, this.legMidR2, this.legBottomR2, this.legMidR2_1, this.legBottomR2_1);
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        this.animator.update(entity);
        if (this.animator.setAnimation(EntityMyrmexQueen.ANIMATION_DIGNEST)) {
            this.animator.startKeyframe(15);
            ModelUtils.rotateFrom(this.animator, this.Body2, 26, 0, 0);
            ModelUtils.rotateFrom(this.animator, this.Neck1, -5, 0, 0);
            ModelUtils.rotateFrom(this.animator, this.Body4, -8, 0, 0);
            ModelUtils.rotateFrom(this.animator, this.Body5, -2, 0, 0);
            ModelUtils.rotateFrom(this.animator, this.Tail1, -13, 0, 0);
            ModelUtils.rotateFrom(this.animator, this.Tail2, -18, 0, 0);
            ModelUtils.rotateFrom(this.animator, this.Tail3, -13, 0, 0);
            this.digPose();
            this.animator.endKeyframe();
            this.animator.startKeyframe(10);
            this.animator.move(this.Body2, 0, 5, 0);
            ModelUtils.rotateFrom(this.animator, this.Body2, 45, 0, 0);
            ModelUtils.rotateFrom(this.animator, this.Neck1, -5, 0, 0);
            ModelUtils.rotateFrom(this.animator, this.Body4, -8, 0, 0);
            ModelUtils.rotateFrom(this.animator, this.Body5, -2, 0, 0);
            ModelUtils.rotateFrom(this.animator, this.Tail1, -13, 0, 0);
            ModelUtils.rotateFrom(this.animator, this.Tail2, -18, 0, 0);
            ModelUtils.rotateFrom(this.animator, this.Tail3, -13, 0, 0);
            this.digPose();
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.animator.move(this.Body2, 0, 30, 0);
            ModelUtils.rotateFrom(this.animator, this.Body2, 78, 0, 0);
            this.digPose();
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            this.animator.move(this.Body2, 0, 80, 0);
            ModelUtils.rotateFrom(this.animator, this.Body2, 90, 0, 0);
            this.digPose();
            this.animator.endKeyframe();
            this.animator.startKeyframe(10);
            this.animator.move(this.Body2, 0, 80, 0);
            ModelUtils.rotateFrom(this.animator, this.Body2, 90, 0, 0);
            this.digPose();
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
        if (this.animator.setAnimation(EntityMyrmexQueen.ANIMATION_BITE)) {
            this.animator.startKeyframe(5);
            ModelUtils.rotate(this.animator, this.Neck1, -50, 0, 0);
            ModelUtils.rotate(this.animator, this.HeadBase, 50, 0, 0);
            ModelUtils.rotate(this.animator, this.MandibleR, 0, 35, 0);
            ModelUtils.rotate(this.animator, this.MandibleL, 0, -35, 0);
            this.animator.endKeyframe();
            this.animator.startKeyframe(5);
            ModelUtils.rotate(this.animator, this.Neck1, 30, 0, 0);
            ModelUtils.rotate(this.animator, this.HeadBase, -30, 0, 0);
            ModelUtils.rotate(this.animator, this.MandibleR, 0, -50, 0);
            ModelUtils.rotate(this.animator, this.MandibleL, 0, 50, 0);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(5);
        }
        if (this.animator.setAnimation(EntityMyrmexQueen.ANIMATION_STING)) {
            this.animator.startKeyframe(5);
            this.animator.move(this.Body2, 0, -10, 0);
            ModelUtils.rotate(this.animator, this.Body3, -35, 0, 0);
            ModelUtils.rotate(this.animator, this.Body4, -49, 0, 0);
            ModelUtils.rotate(this.animator, this.Body5, -25, 0, 0);
            ModelUtils.rotate(this.animator, this.Tail1, -25, 0, 0);
            ModelUtils.rotate(this.animator, this.Tail2, -40, 0, 0);
            ModelUtils.rotate(this.animator, this.Tail3, -40, 0, 0);
            ModelUtils.rotate(this.animator, this.Stinger, 90, 0, 0);
            ModelUtils.rotateFrom(this.animator, this.legTopR3, 64, -7, 44);
            ModelUtils.rotateFrom(this.animator, this.legTopR3_1, 64, 7, -44);
            ModelUtils.rotateFrom(this.animator, this.legMidR3, 0, 0, 30);
            ModelUtils.rotateFrom(this.animator, this.legMidR2, 0, 0, -25);
            ModelUtils.rotateFrom(this.animator, this.legMidR1, 0, 0, 25);
            ModelUtils.rotateFrom(this.animator, this.legMidR3_1, 0, 0, -30);
            ModelUtils.rotateFrom(this.animator, this.legMidR2_1, 0, 0, 25);
            ModelUtils.rotateFrom(this.animator, this.legMidR1_1, 0, 0, -25);
            this.animator.endKeyframe();
            this.animator.resetKeyframe(10);
        }
        EntityMyrmexQueen myrmexQueen = (EntityMyrmexQueen) entity;

        if (myrmexQueen.getAnimation() == EntityMyrmexQueen.ANIMATION_EGG) {
            int animationTick = MathHelper.clamp(myrmexQueen.getAnimationTick(), 0, 20);
            float swellToPi = (float) (animationTick / 20F * Math.PI);
            this.increaseScale(this.Body5, 0.5F * Math.abs(MathHelper.sin(swellToPi + 0.5F)));
            this.increaseScale(this.Tail1, 0.75F * Math.abs(MathHelper.sin(swellToPi)));
            this.increaseScale(this.Tail2, 0.75F * Math.abs(MathHelper.sin(swellToPi - 0.5F)));
            this.increaseScale(this.Tail3, 0.75F * Math.abs(MathHelper.sin(swellToPi - 1.0F)));
            this.Stinger.rotationPointZ += 10 * Math.abs(MathHelper.sin(swellToPi - 1.0F));
        }
    }

    @Override
    public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.animate((IAnimatedEntity) entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch, 1);
        this.Body5.setScale(1.0F, 1.0F, 1.0F);
        this.Tail1.setScale(1.0F, 1.0F, 1.0F);
        this.Tail2.setScale(1.0F, 1.0F, 1.0F);
        this.Tail3.setScale(1.0F, 1.0F, 1.0F);
        AdvancedModelBox[] GASTER = new AdvancedModelBox[]{this.Body4, this.Body5, this.Tail1, this.Tail2, this.Tail3, this.Stinger};
        AdvancedModelBox[] NECK = new AdvancedModelBox[]{this.Neck1, this.HeadBase};
        AdvancedModelBox[] LEGR1 = new AdvancedModelBox[]{this.legTopR1, this.legMidR1, this.legBottomR1};
        AdvancedModelBox[] LEGR2 = new AdvancedModelBox[]{this.legTopR2, this.legMidR2, this.legBottomR2};
        AdvancedModelBox[] LEGR3 = new AdvancedModelBox[]{this.legTopR3, this.legMidR3, this.legBottomR3};
        AdvancedModelBox[] LEGL1 = new AdvancedModelBox[]{this.legTopR1_1, this.legMidR1_1, this.legBottomR1_1};
        AdvancedModelBox[] LEGL2 = new AdvancedModelBox[]{this.legTopR2_1, this.legMidR2_1, this.legBottomR2_1};
        AdvancedModelBox[] LEGL3 = new AdvancedModelBox[]{this.legTopR3_1, this.legMidR3_1, this.legBottomR3_1};
        float speed_walk = 1.8F;
        float speed_idle = 0.05F;
        float degree_walk = 0.7F;
        float degree_idle = 0.15F;
        float gasterSwell1 = -0.05F + (0.2F * Math.abs(MathHelper.sin(entity.age * 0.15F + 1.0F)));
        float gasterSwell2 = -0.05F + (0.2F * Math.abs(MathHelper.sin(entity.age * 0.15F + 0.5F)));
        float gasterSwell3 = -0.05F + (0.2F * Math.abs(MathHelper.sin(entity.age * 0.15F)));
        EntityMyrmexQueen myrmexQueen = (EntityMyrmexQueen) entity;
        if (myrmexQueen.getAnimation() != EntityMyrmexQueen.ANIMATION_EGG) {
            this.increaseScale(this.Tail1, gasterSwell1);
            this.increaseScale(this.Tail2, gasterSwell2);
            this.increaseScale(this.Tail3, gasterSwell3);
            this.Stinger.rotationPointZ += 20 * gasterSwell3;
        }
        if (myrmexQueen.getAnimation() == EntityMyrmexQueen.ANIMATION_DIGNEST) {
            this.animateLeg(LEGR1, speed_walk * 0.5F, degree_walk * 0.5F, false, 0, 1, animationProgress, 1);
            this.animateLeg(LEGR3, speed_walk * 0.5F, degree_walk * 0.5F, false, 0, 1, animationProgress, 1);
            this.animateLeg(LEGR2, speed_walk * 0.5F, degree_walk * 0.5F, true, 0, 1, animationProgress, 1);

            this.animateLeg(LEGL1, speed_walk * 0.5F, degree_walk * 0.5F, false, 1, -1, animationProgress, 1);
            this.animateLeg(LEGL3, speed_walk * 0.5F, degree_walk * 0.5F, false, 1, -1, animationProgress, 1);
            this.animateLeg(LEGL2, speed_walk * 0.5F, degree_walk * 0.5F, true, 1, -1, animationProgress, 1);
        }
        if (entity.getPassengerList().isEmpty()) {
            this.faceTarget(headYaw, headPitch, 2, NECK);
        }
        this.chainWave(GASTER, speed_idle, degree_idle * 0.15F, 0, animationProgress, 1);
        this.chainWave(NECK, speed_idle, degree_idle * -0.15F, 2, animationProgress, 1);
        this.swing(this.MandibleR, speed_idle * 2F, degree_idle * -0.75F, false, 1, 0.2F, animationProgress, 1);
        this.swing(this.MandibleL, speed_idle * 2F, degree_idle * -0.75F, true, 1, 0.2F, animationProgress, 1);
        this.animateLeg(LEGR1, speed_walk, degree_walk, false, 0, 1, limbAngle, limbDistance);
        this.animateLeg(LEGR3, speed_walk, degree_walk, false, 0, 1, limbAngle, limbDistance);
        this.animateLeg(LEGR2, speed_walk, degree_walk, true, 0, 1, limbAngle, limbDistance);

        this.animateLeg(LEGL1, speed_walk, degree_walk, false, 1, -1, limbAngle, limbDistance);
        this.animateLeg(LEGL3, speed_walk, degree_walk, false, 1, -1, limbAngle, limbDistance);
        this.animateLeg(LEGL2, speed_walk, degree_walk, true, 1, -1, limbAngle, limbDistance);
    }

    private void animateLeg(AdvancedModelBox[] models, float speed, float degree, boolean reverse, float offset, float weight, float f, float f1) {
        this.flap(models[0], speed, degree * 0.4F, reverse, offset, weight * 0.2F, f, f1);
        this.flap(models[1], speed, degree * 2, reverse, offset, weight * -0.4F, f, f1);
        this.flap(models[1], speed, -degree * 1.2F, reverse, offset, weight * 0.5F, f, f1);
        this.walk(models[0], speed, degree, reverse, offset, 0F, f, f1);

    }

    public void increaseScale(AdvancedModelBox box, float scale) {
        box.scaleX += scale;
        box.scaleY += scale;
        box.scaleZ += scale;
    }

    @Override
    public AdvancedModelBox[] getHeadParts() {
        return new AdvancedModelBox[]{this.Neck1, this.HeadBase};
    }

    @Override
    public void renderStatue(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, Entity living) {
        this.render(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        this.Body5.setScale(1.0F, 1.0F, 1.0F);
        this.Tail1.setScale(1.0F, 1.0F, 1.0F);
        this.Tail2.setScale(1.0F, 1.0F, 1.0F);
        this.Tail3.setScale(1.0F, 1.0F, 1.0F);
    }

    private void digPose() {
        ModelUtils.rotateFrom(this.animator, this.legTopR1, -28, -13, 36);
        ModelUtils.rotateFrom(this.animator, this.legTopR2, -7, 15, -24);
        ModelUtils.rotateFrom(this.animator, this.legTopR3, -2, 31, 13);
        ModelUtils.rotateFrom(this.animator, this.legMidR1, 0, 0, 80);
        ModelUtils.rotateFrom(this.animator, this.legMidR2, 0, 0, -83);
        ModelUtils.rotateFrom(this.animator, this.legMidR3, 0, 0, 65);
        ModelUtils.rotateFrom(this.animator, this.legBottomR1, 0, 0, -75);
        ModelUtils.rotateFrom(this.animator, this.legBottomR2, 0, 0, 75);
        ModelUtils.rotateFrom(this.animator, this.legBottomR3, 0, 0, -54);

        ModelUtils.rotateFrom(this.animator, this.legTopR1_1, -28, 13, -36);
        ModelUtils.rotateFrom(this.animator, this.legTopR2_1, -7, -15, 24);
        ModelUtils.rotateFrom(this.animator, this.legTopR3_1, -2, -31, -13);
        ModelUtils.rotateFrom(this.animator, this.legMidR1_1, 0, 0, -80);
        ModelUtils.rotateFrom(this.animator, this.legMidR2_1, 0, 0, 83);
        ModelUtils.rotateFrom(this.animator, this.legMidR3_1, 0, 0, -65);
        ModelUtils.rotateFrom(this.animator, this.legBottomR1_1, 0, 0, 75);
        ModelUtils.rotateFrom(this.animator, this.legBottomR2_1, 0, 0, -75);
        ModelUtils.rotateFrom(this.animator, this.legBottomR3_1, 0, 0, 54);
    }
}
