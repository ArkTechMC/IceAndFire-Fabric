package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class ModelStymphalianBird extends ModelDragonBase<EntityStymphalianBird> {
    public AdvancedModelBox Body;
    public AdvancedModelBox LowerBody;
    public AdvancedModelBox Neck1;
    public AdvancedModelBox WingL;
    public AdvancedModelBox WingR;
    public AdvancedModelBox BackLegL1;
    public AdvancedModelBox BackLegR1;
    public AdvancedModelBox Lowerbodytilt;
    public AdvancedModelBox TailR1;
    public AdvancedModelBox TailL1;
    public AdvancedModelBox TailR2;
    public AdvancedModelBox TailL2;
    public AdvancedModelBox BackLegL2;
    public AdvancedModelBox ToeL3;
    public AdvancedModelBox ToeL2;
    public AdvancedModelBox ToeL4;
    public AdvancedModelBox ToeL1;
    public AdvancedModelBox BackLegR2;
    public AdvancedModelBox ToeR3;
    public AdvancedModelBox ToeL4_1;
    public AdvancedModelBox ToeR2;
    public AdvancedModelBox ToeR1;
    public AdvancedModelBox Neck2;
    public AdvancedModelBox HeadBase;
    public AdvancedModelBox HeadFront;
    public AdvancedModelBox Jaw;
    public AdvancedModelBox Crest1;
    public AdvancedModelBox uppernail;
    public AdvancedModelBox Crest2;
    public AdvancedModelBox Crest3;
    public AdvancedModelBox WingL2;
    public AdvancedModelBox WingL3;
    public AdvancedModelBox WingL21;
    public AdvancedModelBox FingerL1;
    public AdvancedModelBox FingerL2;
    public AdvancedModelBox FingerL3;
    public AdvancedModelBox FingerL4;
    public AdvancedModelBox WingR2;
    public AdvancedModelBox WingR3;
    public AdvancedModelBox WingR21;
    public AdvancedModelBox FingerR1;
    public AdvancedModelBox FingerR2;
    public AdvancedModelBox FingerR3;
    public AdvancedModelBox FingerR4;

    public AdvancedModelBox HeadPivot;
    public AdvancedModelBox NeckPivot;

    private ModelAnimator animator;

    public ModelStymphalianBird() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.WingL3 = new AdvancedModelBox(this, 96, 60);
        this.WingL3.mirror = true;
        this.WingL3.setPos(0.0F, 7.6F, 0.0F);
        this.WingL3.addBox(-0.7F, -0.1F, -2.0F, 1, 14, 10, 0.0F);
        this.setRotateAngle(WingL3, 0.12217304763960307F, 0.17453292519943295F, 0.091106186954104F);
        this.Jaw = new AdvancedModelBox(this, 14, 11);
        this.Jaw.setPos(0.0F, 0.0F, -2.6F);
        this.Jaw.addBox(-1.0F, -0.7F, -4.5F, 2, 1, 6, 0.0F);
        this.setRotateAngle(Jaw, -0.18203784098300857F, 0.0F, 0.0F);
        this.WingL2 = new AdvancedModelBox(this, 80, 90);
        this.WingL2.mirror = true;
        this.WingL2.setPos(0.4F, 7.6F, -2.8F);
        this.WingL2.addBox(-0.6F, -2.5F, -2.1F, 1, 11, 11, 0.0F);
        this.setRotateAngle(WingL2, 1.48352986419518F, 0.0F, -0.17453292519943295F);
        this.WingR2 = new AdvancedModelBox(this, 80, 90);
        this.WingR2.setPos(-0.4F, 7.6F, -2.8F);
        this.WingR2.addBox(-0.4F, -2.5F, -2.1F, 1, 11, 11, 0.0F);
        this.setRotateAngle(WingR2, 1.48352986419518F, 0.0F, 0.17453292519943295F);
        this.WingR = new AdvancedModelBox(this, 100, 107);
        this.WingR.setPos(-3.0F, -1.6F, -1.2F);
        this.WingR.addBox(-0.9F, 0.0F, -5.0F, 1, 8, 12, 0.0F);
        this.setRotateAngle(WingR, 0.08726646259971647F, 0.0F, 0.17453292519943295F);
        this.TailR1 = new AdvancedModelBox(this, 54, 5);
        this.TailR1.mirror = true;
        this.TailR1.setPos(0.0F, -0.2F, 6.1F);
        this.TailR1.addBox(-0.5F, 0.0F, 0.0F, 3, 16, 1, 0.0F);
        this.setRotateAngle(TailR1, 1.5707963267948966F, 0.03490658503988659F, 0.0F);
        this.ToeL4_1 = new AdvancedModelBox(this, 0, 43);
        this.ToeL4_1.mirror = true;
        this.ToeL4_1.setPos(0.0F, 10.8F, 0.2F);
        this.ToeL4_1.addBox(-0.5F, -0.5F, -0.7F, 1, 4, 2, 0.0F);
        this.setRotateAngle(ToeL4_1, -1.593485607070823F, 0.6108652381980153F, 0.0F);
        this.WingL21 = new AdvancedModelBox(this, 80, 90);
        this.WingL21.mirror = true;
        this.WingL21.setPos(-0.5F, 0.0F, 0.0F);
        this.WingL21.addBox(-0.4F, -2.5F, -2.1F, 1, 11, 11, 0.0F);
        this.ToeR2 = new AdvancedModelBox(this, 0, 43);
        this.ToeR2.mirror = true;
        this.ToeR2.setPos(0.0F, 10.8F, 0.2F);
        this.ToeR2.addBox(-0.5F, -0.5F, -0.7F, 1, 4, 2, 0.0F);
        this.setRotateAngle(ToeR2, -1.593485607070823F, -0.6108652381980153F, 0.0F);
        this.Crest3 = new AdvancedModelBox(this, 68, 20);
        this.Crest3.setPos(0.0F, 0.4F, 0.2F);
        this.Crest3.addBox(-0.5F, -1.21F, 0.39F, 1, 3, 8, 0.0F);
        this.setRotateAngle(Crest3, -0.31869712141416456F, 0.0F, 0.0F);
        this.FingerL2 = new AdvancedModelBox(this, 50, 80);
        this.FingerL2.setPos(-0.1F, 11.0F, 2.0F);
        this.FingerL2.addBox(-0.8F, -0.1F, -2.0F, 1, 14, 3, 0.0F);
        this.setRotateAngle(FingerL2, 0.24434609527920614F, 0.0F, 0.0F);
        this.BackLegL2 = new AdvancedModelBox(this, 17, 42);
        this.BackLegL2.mirror = true;
        this.BackLegL2.setPos(0.0F, 4.4F, 0.2F);
        this.BackLegL2.addBox(-0.5F, 0.0F, -0.7F, 1, 11, 2, 0.0F);
        this.setRotateAngle(BackLegL2, -0.4363323129985824F, 0.0F, 0.0F);
        this.FingerL1 = new AdvancedModelBox(this, 60, 80);
        this.FingerL1.setPos(0.0F, 11.0F, 0.1F);
        this.FingerL1.addBox(-0.8F, -0.1F, -2.0F, 1, 11, 3, 0.0F);
        this.setRotateAngle(FingerL1, 0.2792526803190927F, 0.0F, 0.0F);
        this.FingerR2 = new AdvancedModelBox(this, 50, 80);
        this.FingerR2.mirror = true;
        this.FingerR2.setPos(0.1F, 11.0F, 2.0F);
        this.FingerR2.addBox(-0.2F, -0.1F, -2.0F, 1, 14, 3, 0.0F);
        this.setRotateAngle(FingerR2, 0.24434609527920614F, 0.0F, 0.0F);
        this.BackLegR1 = new AdvancedModelBox(this, 20, 52);
        this.BackLegR1.setPos(-2.5F, 1.1F, 1.6F);
        this.BackLegR1.addBox(-1.0F, 0.0F, -1.5F, 2, 5, 3, 0.0F);
        this.setRotateAngle(BackLegR1, 0.5462880558742251F, 0.0F, 0.08726646259971647F);
        this.BackLegL1 = new AdvancedModelBox(this, 20, 52);
        this.BackLegL1.mirror = true;
        this.BackLegL1.setPos(2.5F, 1.1F, 1.6F);
        this.BackLegL1.addBox(-1.0F, 0.0F, -1.5F, 2, 5, 3, 0.0F);
        this.setRotateAngle(BackLegL1, 0.5462880558742251F, 0.0F, -0.08726646259971647F);
        this.HeadFront = new AdvancedModelBox(this, 0, 9);
        this.HeadFront.setPos(0.0F, -0.4F, -2.5F);
        this.HeadFront.addBox(-1.0F, -2.4F, -4.6F, 2, 2, 5, 0.0F);
        this.setRotateAngle(HeadFront, 0.045553093477052F, 0.0F, 0.0F);
        this.WingR21 = new AdvancedModelBox(this, 80, 90);
        this.WingR21.setPos(0.5F, 0.0F, 0.0F);
        this.WingR21.addBox(-0.4F, -2.5F, -2.1F, 1, 11, 11, 0.0F);
        this.ToeL1 = new AdvancedModelBox(this, 0, 43);
        this.ToeL1.mirror = true;
        this.ToeL1.setPos(0.0F, 11.1F, 0.2F);
        this.ToeL1.addBox(-0.5F, -0.5F, -0.7F, 1, 4, 2, 0.0F);
        this.setRotateAngle(ToeL1, -2.0032889154390916F, 3.141592653589793F, 0.0F);
        this.ToeR3 = new AdvancedModelBox(this, 0, 43);
        this.ToeR3.mirror = true;
        this.ToeR3.setPos(0.0F, 10.8F, -0.7F);
        this.ToeR3.addBox(-0.5F, -0.5F, -0.7F, 1, 4, 2, 0.0F);
        this.setRotateAngle(ToeR3, -1.5481070465189704F, 0.0F, 0.0F);
        this.TailL2 = new AdvancedModelBox(this, 52, 25);
        this.TailL2.setPos(0.0F, 0.1F, 7.0F);
        this.TailL2.addBox(-3.5F, 0.0F, 0.0F, 5, 12, 1, 0.0F);
        this.setRotateAngle(TailL2, 1.5707963267948966F, 0.08726646259971647F, -0.12217304763960307F);
        this.uppernail = new AdvancedModelBox(this, 11, 4);
        this.uppernail.setPos(0.0F, -0.8F, -4.8F);
        this.uppernail.addBox(-0.5F, -0.4F, -2.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(uppernail, 1.6845917940249266F, -0.0F, 0.0F);
        this.ToeR1 = new AdvancedModelBox(this, 0, 43);
        this.ToeR1.mirror = true;
        this.ToeR1.setPos(0.0F, 11.1F, 0.2F);
        this.ToeR1.addBox(-0.5F, -0.5F, -0.7F, 1, 4, 2, 0.0F);
        this.setRotateAngle(ToeR1, -2.0032889154390916F, 3.141592653589793F, 0.0F);
        this.Neck1 = new AdvancedModelBox(this, 0, 27);
        this.Neck1.setPos(0.0F, -0.5F, -5.0F);
        this.Neck1.addBox(-2.5F, -1.8F, -3.9F, 5, 5, 5, 0.0F);
        this.setRotateAngle(Neck1, -0.4553564018453205F, 0.0F, 0.0F);
        this.Crest2 = new AdvancedModelBox(this, 68, 20);
        this.Crest2.setPos(0.0F, 0.4F, 0.2F);
        this.Crest2.addBox(-0.5F, -1.21F, 0.39F, 1, 3, 8, 0.0F);
        this.setRotateAngle(Crest2, -0.31869712141416456F, 0.0F, 0.0F);

        this.LowerBody = new AdvancedModelBox(this, 23, 12);
        this.LowerBody.setPos(0.0F, 0.0F, 0.7F);
        this.LowerBody.addBox(-3.5F, -2.7F, -0.1F, 7, 4, 7, 0.0F);
        this.setRotateAngle(LowerBody, -0.091106186954104F, 0.0F, 0.0F);
        this.FingerR1 = new AdvancedModelBox(this, 60, 80);
        this.FingerR1.mirror = true;
        this.FingerR1.setPos(0.0F, 11.0F, 0.1F);
        this.FingerR1.addBox(-0.2F, -0.1F, -2.0F, 1, 11, 3, 0.0F);
        this.setRotateAngle(FingerR1, 0.2792526803190927F, 0.0F, 0.0F);
        this.WingL = new AdvancedModelBox(this, 100, 107);
        this.WingL.mirror = true;
        this.WingL.setPos(3.0F, -1.6F, -1.2F);
        this.WingL.addBox(-0.1F, 0.0F, -5.0F, 1, 8, 12, 0.0F);
        this.setRotateAngle(WingL, 0.08726646259971647F, 0.0F, -0.17453292519943295F);
        this.BackLegR2 = new AdvancedModelBox(this, 17, 42);
        this.BackLegR2.setPos(0.0F, 4.4F, 0.2F);
        this.BackLegR2.addBox(-0.5F, 0.0F, -0.7F, 1, 11, 2, 0.0F);
        this.setRotateAngle(BackLegR2, -0.4363323129985824F, 0.0F, 0.0F);
        this.FingerR4 = new AdvancedModelBox(this, 30, 80);
        this.FingerR4.mirror = true;
        this.FingerR4.setPos(0.0F, 11.6F, 6.6F);
        this.FingerR4.addBox(-0.1F, -0.1F, -2.0F, 1, 11, 3, 0.0F);
        this.ToeL2 = new AdvancedModelBox(this, 0, 43);
        this.ToeL2.mirror = true;
        this.ToeL2.setPos(0.0F, 10.8F, 0.2F);
        this.ToeL2.addBox(-0.5F, -0.5F, -0.7F, 1, 4, 2, 0.0F);
        this.setRotateAngle(ToeL2, -1.593485607070823F, 0.6108652381980153F, 0.0F);
        this.TailL1 = new AdvancedModelBox(this, 54, 5);
        this.TailL1.setPos(0.0F, -0.2F, 6.1F);
        this.TailL1.addBox(-2.5F, 0.0F, 0.0F, 3, 16, 1, 0.0F);
        this.setRotateAngle(TailL1, 1.5707963267948966F, -0.03490658503988659F, 0.0F);
        this.Neck2 = new AdvancedModelBox(this, 21, 25);
        this.Neck2.setPos(0.0F, 0F, 0F);
        this.Neck2.addBox(-1.5F, -2.21F, -9.61F, 3, 4, 10, 0.0F);
        this.NeckPivot = new AdvancedModelBox(this, 21, 25);
        this.NeckPivot.setPos(0.0F, 1.4F, -2.0F);
        this.setRotateAngle(NeckPivot, -0.6829473363053812F, 0.0F, 0.0F);
        this.FingerR3 = new AdvancedModelBox(this, 40, 80);
        this.FingerR3.mirror = true;
        this.FingerR3.setPos(0.0F, 11.0F, 4.5F);
        this.FingerR3.addBox(-0.2F, -0.1F, -2.0F, 1, 16, 3, 0.0F);
        this.setRotateAngle(FingerR3, 0.13962634015954636F, 0.0F, 0.0F);
        this.WingR3 = new AdvancedModelBox(this, 96, 60);
        this.WingR3.setPos(0.0F, 7.6F, 0.0F);
        this.WingR3.addBox(-0.3F, -0.1F, -2.0F, 1, 14, 10, 0.0F);
        this.setRotateAngle(WingR3, 0.12217304763960307F, -0.17453292519943295F, -0.08726646259971647F);
        this.Lowerbodytilt = new AdvancedModelBox(this, 0, 54);
        this.Lowerbodytilt.setPos(0.0F, 3.2F, -3.5F);
        this.Lowerbodytilt.addBox(-3.0F, 2.4F, -0.9F, 6, 8, 4, 0.0F);
        this.setRotateAngle(Lowerbodytilt, 1.730144887501979F, 0.0F, 0.0F);
        this.Body = new AdvancedModelBox(this, 34, 47);
        this.Body.setPos(0.0F, 7.0F, -4.0F);
        this.Body.addBox(-4.0F, -3.0F, -6.0F, 8, 7, 7, 0.0F);
        this.setRotateAngle(Body, -0.18203784098300857F, 0.0F, 0.0F);
        this.ToeL3 = new AdvancedModelBox(this, 0, 43);
        this.ToeL3.mirror = true;
        this.ToeL3.setPos(0.0F, 10.8F, -0.7F);
        this.ToeL3.addBox(-0.5F, -0.5F, -0.7F, 1, 4, 2, 0.0F);
        this.setRotateAngle(ToeL3, -1.593485607070823F, 0.0F, 0.0F);
        this.Crest1 = new AdvancedModelBox(this, 68, 20);
        this.Crest1.setPos(0.0F, -1.6F, -2.0F);
        this.Crest1.addBox(-0.5F, -1.21F, 1.39F, 1, 3, 8, 0.0F);
        this.setRotateAngle(Crest1, 0.5918411493512771F, 0.0F, 0.0F);
        this.HeadBase = new AdvancedModelBox(this, 0, 16);
        this.HeadBase.setPos(0.0F, 0F, 0F);
        this.HeadBase.addBox(-2.0F, -2.91F, -2.8F, 4, 4, 5, 0.0F);
        this.HeadPivot = new AdvancedModelBox(this, 0, 16);
        this.HeadPivot.setPos(0.0F, 0.2F, -8.2F);
        this.setRotateAngle(HeadPivot, 1.5025539530419183F, 0.0F, 0.0F);
        this.FingerL4 = new AdvancedModelBox(this, 30, 80);
        this.FingerL4.setPos(0.0F, 11.0F, 6.6F);
        this.FingerL4.addBox(-0.9F, -0.1F, -2.0F, 1, 11, 3, 0.0F);
        this.TailR2 = new AdvancedModelBox(this, 52, 25);
        this.TailR2.mirror = true;
        this.TailR2.setPos(0.0F, 0.1F, 7.0F);
        this.TailR2.addBox(-1.5F, 0.0F, 0.0F, 5, 12, 1, 0.0F);
        this.setRotateAngle(TailR2, 1.5707963267948966F, -0.08726646259971647F, 0.12217304763960307F);
        this.FingerL3 = new AdvancedModelBox(this, 40, 80);
        this.FingerL3.setPos(0.0F, 11.0F, 4.4F);
        this.FingerL3.addBox(-0.8F, -0.1F, -2.0F, 1, 16, 3, 0.0F);
        this.setRotateAngle(FingerL3, 0.13962634015954636F, 0.0F, 0.0F);
        this.ToeL4 = new AdvancedModelBox(this, 0, 43);
        this.ToeL4.mirror = true;
        this.ToeL4.setPos(0.0F, 10.8F, 0.2F);
        this.ToeL4.addBox(-0.5F, -0.5F, -0.7F, 1, 4, 2, 0.0F);
        this.setRotateAngle(ToeL4, -1.593485607070823F, -0.6108652381980153F, 0.0F);
        this.WingL2.addChild(this.WingL3);
        this.HeadBase.addChild(this.Jaw);
        this.WingL.addChild(this.WingL2);
        this.WingR.addChild(this.WingR2);
        this.Body.addChild(this.WingR);
        this.LowerBody.addChild(this.TailR1);
        this.BackLegR2.addChild(this.ToeL4_1);
        this.WingL2.addChild(this.WingL21);
        this.BackLegR2.addChild(this.ToeR2);
        this.Crest2.addChild(this.Crest3);
        this.WingL3.addChild(this.FingerL2);
        this.BackLegL1.addChild(this.BackLegL2);
        this.WingL3.addChild(this.FingerL1);
        this.WingR3.addChild(this.FingerR2);
        this.LowerBody.addChild(this.BackLegR1);
        this.HeadBase.addChild(this.HeadFront);
        this.WingR2.addChild(this.WingR21);
        this.BackLegL2.addChild(this.ToeL1);
        this.BackLegR2.addChild(this.ToeR3);
        this.LowerBody.addChild(this.TailL2);
        this.HeadFront.addChild(this.uppernail);
        this.BackLegR2.addChild(this.ToeR1);
        this.Body.addChild(this.Neck1);
        this.Crest1.addChild(this.Crest2);
        this.LowerBody.addChild(this.BackLegL1);
        this.Body.addChild(this.LowerBody);
        this.WingR3.addChild(this.FingerR1);
        this.Body.addChild(this.WingL);
        this.BackLegR1.addChild(this.BackLegR2);
        this.WingR3.addChild(this.FingerR4);
        this.BackLegL2.addChild(this.ToeL2);
        this.LowerBody.addChild(this.TailL1);
        this.Neck1.addChild(this.NeckPivot);
        this.NeckPivot.addChild(this.Neck2);
        this.WingR3.addChild(this.FingerR3);
        this.WingR2.addChild(this.WingR3);
        this.LowerBody.addChild(this.Lowerbodytilt);
        this.BackLegL2.addChild(this.ToeL3);
        this.HeadBase.addChild(this.Crest1);
        this.Neck2.addChild(this.HeadPivot);
        this.HeadPivot.addChild(this.HeadBase);
        this.WingL3.addChild(this.FingerL4);
        this.LowerBody.addChild(this.TailR2);
        this.WingL3.addChild(this.FingerL3);
        this.BackLegL2.addChild(this.ToeL4);
        this.HeadFront.setScale(1.01F, 1.01F, 1.01F);
        this.updateDefaultPose();
    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        animator = ModelAnimator.create();
        animator.update(entity);
        if (animator.setAnimation(EntityStymphalianBird.ANIMATION_PECK)) {
            animator.startKeyframe(5);
            this.rotate(animator, Neck1, -47, 0, 0);
            this.rotate(animator, NeckPivot, 17, 0, 0);
            this.rotate(animator, HeadPivot, 46, 0, 0);
            this.rotate(animator, Jaw, 10, 0, 0);
            animator.endKeyframe();
            animator.startKeyframe(5);
            this.rotate(animator, Neck1, 26, 0, 0);
            this.rotate(animator, NeckPivot, -18, 0, 0);
            this.rotate(animator, HeadPivot, 2, 0, 0);
            this.rotate(animator, Jaw, 33, 0, 0);
            this.rotate(animator, HeadFront, -20, 0, 0);
            animator.endKeyframe();
            animator.resetKeyframe(5);
        }
        if (animator.setAnimation(EntityStymphalianBird.ANIMATION_SPEAK)) {
            animator.startKeyframe(5);
            this.rotate(animator, Jaw, 35, 0, 0);
            animator.startKeyframe(5);
            this.rotate(animator, Jaw, 0, 0, 0);
            animator.endKeyframe();
        }
        if (animator.setAnimation(EntityStymphalianBird.ANIMATION_SHOOT_ARROWS)) {
            animator.startKeyframe(20);
            shootPosture();
            animator.endKeyframe();
            animator.resetKeyframe(10);
        }

    }

    private void shootPosture() {
        this.rotate(animator, Body, -52, 0, 0);
        this.rotate(animator, Neck1, 33, 0, 0);
        this.rotate(animator, NeckPivot, -7, 0, 0);
        this.rotate(animator, HeadPivot, 70, 0, 0);
        this.rotate(animator, HeadFront, -15, 0, 0);
        this.rotate(animator, Jaw, 36, 0, 0);
        this.rotate(animator, Crest1, 40, 0, 0);
        this.rotate(animator, Crest2, -26, 0, 0);
        this.rotate(animator, Crest3, -33, 0, 0);
        this.rotate(animator, BackLegR1, -25, 0, 15);
        this.rotate(animator, BackLegL1, -25, 0, -15);
        this.rotate(animator, WingL, 5, -10, -60);
        this.rotate(animator, WingL2, -20, 0, 50);
        this.rotate(animator, WingL3, 30, 0, 20);
        this.rotate(animator, WingR, 5, 10, 60);
        this.rotate(animator, WingR2, -20, 0, -50);
        this.rotate(animator, WingR3, 30, 0, -20);

        this.rotate(animator, ToeR1, -23, 180, 0);
        this.rotate(animator, ToeR2, -75, -45, 0);
        this.rotate(animator, ToeR3, -75, 0, 0);

        this.rotate(animator, ToeL1, -23, 180, 0);
        this.rotate(animator, ToeL2, -75, 45, 0);
        this.rotate(animator, ToeL3, -75, 0, 0);
    }

    @Override
    public void setAngles(EntityStymphalianBird entity, float f, float f1, float f2, float f3, float f4) {
        animate(entity, f, f1, f2, f3, f4, 1);
        float speed_walk = 0.3F;
        float speed_idle = 0.05F;
        float speed_fly = 0.4F;
        float degree_walk = 0.4F;
        float degree_idle = 0.5F;
        float degree_fly = 0.7F;
        AdvancedModelBox[] NECK = new AdvancedModelBox[]{Neck1, Neck2, HeadBase};
        AdvancedModelBox[] FEATHERS = new AdvancedModelBox[]{Crest1, Crest2, Crest3};
        AdvancedModelBox[] WING_LEFT = new AdvancedModelBox[]{WingL, WingL2, WingL3};
        AdvancedModelBox[] WING_RIGHT = new AdvancedModelBox[]{WingR, WingR2, WingR3};
        this.faceTarget(f3, f4, 2, HeadBase);
        this.faceTarget(f3, f4, 2, Neck2);
        if (entity.flyProgress > 0F) {
            progressRotation(WingR, entity.flyProgress, 0.08726646259971647F, 0.0F, 1.3962634015954636F);
            progressRotation(WingR2, entity.flyProgress, -0.3490658503988659F, 0.0F, 0.17453292519943295F);
            progressRotation(WingR3, entity.flyProgress, 0.5235987755982988F, 0.0F, 0.0F);
            progressRotation(WingL, entity.flyProgress, 0.08726646259971647F, 0.0F, -1.3962634015954636F);
            progressRotation(WingL2, entity.flyProgress, -0.3490658503988659F, 0.0F, -0.17453292519943295F);
            progressRotation(WingL3, entity.flyProgress, 0.5235987755982988F, 0.0F, 0.0F);

            progressRotation(TailL1, entity.flyProgress, 1.5707963267948966F, -0.03490658503988659F, 0.0F);
            progressRotation(ToeR1, entity.flyProgress, -0.40980330836826856F, 3.141592653589793F, 0.0F);
            progressRotation(TailR1, entity.flyProgress, 1.5707963267948966F, 0.03490658503988659F, 0.0F);
            progressRotation(uppernail, entity.flyProgress, 1.6845917940249266F, -0.0F, 0.0F);
            progressRotation(FingerR1, entity.flyProgress, 0.03490658503988659F, 0.0F, 0.0F);
            progressRotation(FingerR2, entity.flyProgress, 0.15707963267948966F, 0.0F, 0.0F);
            progressRotation(ToeL4_1, entity.flyProgress, -0.22759093446006054F, 0.6108652381980153F, 0.0F);
            progressRotation(Crest3, entity.flyProgress, -0.22759093446006054F, 0.0F, 0.0F);
            progressRotation(HeadFront, entity.flyProgress, 0.045553093477052F, 0.0F, 0.0F);
            progressRotation(TailR2, entity.flyProgress, 1.5707963267948966F, 0.3490658503988659F, 0.12217304763960307F);
            progressRotation(ToeR2, entity.flyProgress, -0.22759093446006054F, -0.6108652381980153F, 0.0F);
            progressRotation(FingerR4, entity.flyProgress, 0.40142572795869574F, 0.0F, 0.0F);
            progressRotation(FingerL2, entity.flyProgress, 0.15707963267948966F, 0.0F, 0.0F);
            progressRotation(ToeL2, entity.flyProgress, -0.22759093446006054F, 0.6108652381980153F, 0.0F);
            progressRotation(Crest2, entity.flyProgress, -0.22759093446006054F, 0.0F, 0.0F);
            progressRotation(FingerL3, entity.flyProgress, 0.2617993877991494F, 0.0F, 0.0F);
            progressRotation(BackLegR2, entity.flyProgress, -0.18203784098300857F, 0.0F, 0.0F);
            progressRotation(Jaw, entity.flyProgress, -0.091106186954104F, 0.0F, 0.0F);
            progressRotation(ToeR3, entity.flyProgress, -0.22759093446006054F, 0.0F, 0.0F);
            progressRotation(BackLegL2, entity.flyProgress, -0.18203784098300857F, 0.0F, 0.0F);
            progressRotation(ToeL1, entity.flyProgress, -0.40980330836826856F, 3.141592653589793F, 0.0F);
            progressRotation(TailL2, entity.flyProgress, 1.5707963267948966F, -0.3490658503988659F, -0.12217304763960307F);
            progressRotation(Lowerbodytilt, entity.flyProgress, 1.730144887501979F, 0.0F, 0.0F);
            progressRotation(FingerR3, entity.flyProgress, 0.2617993877991494F, 0.0F, 0.0F);
            progressRotation(LowerBody, entity.flyProgress, -0.091106186954104F, 0.0F, 0.0F);
            progressRotation(FingerL1, entity.flyProgress, 0.03490658503988659F, 0.0F, 0.0F);
            progressRotation(FingerL4, entity.flyProgress, 0.40142572795869574F, 0.0F, 0.0F);
            progressRotation(BackLegR1, entity.flyProgress, 1.6390387005478748F, 0.0F, 0.08726646259971647F);
            progressRotation(HeadPivot, entity.flyProgress, 0.5918411493512771F, 0.0F, 0.0F);
            progressRotation(Crest1, entity.flyProgress, 0.18203784098300857F, 0.0F, 0.0F);
            progressRotation(ToeL3, entity.flyProgress, -0.22759093446006054F, 0.0F, 0.0F);
            progressRotation(Neck1, entity.flyProgress, 0.18203784098300857F, 0.0F, 0.0F);
            progressRotation(BackLegL1, entity.flyProgress, 1.6390387005478748F, 0.0F, -0.08726646259971647F);
            progressRotation(NeckPivot, entity.flyProgress, -0.31869712141416456F, 0.0F, 0.0F);
            progressRotation(ToeL4, entity.flyProgress, -0.22759093446006054F, -0.6108652381980153F, 0.0F);

            this.chainFlap(WING_LEFT, speed_fly + (entity.getAnimation() == EntityStymphalianBird.ANIMATION_SHOOT_ARROWS ? 0.25F : 0), -degree_fly * 0.5F, 0, f2, 1);
            this.chainFlap(WING_RIGHT, speed_fly + (entity.getAnimation() == EntityStymphalianBird.ANIMATION_SHOOT_ARROWS ? 0.25F : 0), degree_fly * 0.5F, 0, f2, 1);

            if (entity.getAnimation() != EntityStymphalianBird.ANIMATION_SHOOT_ARROWS) {
                this.chainWave(NECK, speed_fly, degree_fly * 0.15F, 4, f2, 1);
                this.bob(Body, speed_fly * 0.5F, degree_fly * 2.5F, true, f2, 1);
                this.walk(BackLegL1, speed_fly, degree_fly * 0.15F, true, 1, 0.2F, f2, 1);
                this.walk(BackLegR1, speed_fly, degree_fly * 0.15F, false, 1, -0.2F, f2, 1);
            }
        } else {
            this.chainWave(NECK, speed_idle, degree_idle * 0.15F, 4, f2, 1);
            this.chainWave(FEATHERS, speed_idle, degree_idle * -0.1F, 0, f2, 1);
            this.walk(LowerBody, speed_idle, degree_idle * 0.1F, false, 0, 0.1F, f2, 1);
            this.walk(Body, speed_idle, degree_idle * 0.05F, true, 1, 0F, f2, 1);
            this.walk(BackLegR1, speed_idle, degree_idle * -0.1F, false, 0, 0.1F, f2, 1);
            this.walk(BackLegR1, speed_idle, degree_idle * -0.05F, true, 1, 0F, f2, 1);
            this.walk(BackLegL1, speed_idle, degree_idle * -0.1F, false, 0, 0.1F, f2, 1);
            this.walk(BackLegL1, speed_idle, degree_idle * -0.05F, true, 1, 0F, f2, 1);
            this.chainWave(NECK, speed_walk, degree_walk * 0.5F, -3, f, f1);
            this.chainWave(FEATHERS, speed_walk, degree_walk * -0.1F, 0, f, f1);
            this.walk(LowerBody, speed_walk, degree_walk * 0.1F, false, 0, 0F, f, f1);
            this.walk(Body, speed_walk, degree_walk * 0.25F, true, 1, 0F, f, f1);
            this.walk(BackLegR1, speed_walk, degree_walk * 0.1F, false, 0, 0F, f, f1);
            this.walk(BackLegR1, speed_walk, degree_walk * 0.25F, true, 1, 0F, f, f1);
            this.walk(BackLegL1, speed_walk, degree_walk * 0.1F, false, 0, 0F, f, f1);
            this.walk(BackLegL1, speed_walk, degree_walk * 0.25F, true, 1, 0F, f, f1);
            this.walk(BackLegL1, speed_walk, degree_walk, true, 1, -0.1F, f, f1);
            this.walk(BackLegL2, speed_walk, degree_walk, true, 1, -0.1F, f, f1);
            this.walk(BackLegR1, speed_walk, degree_walk, false, 1, 0.1F, f, f1);
            this.walk(BackLegR2, speed_walk, degree_walk, false, 1, 0.1F, f, f1);
            this.walk(ToeL1, speed_walk, degree_walk * 1.25F, false, 1, 0.1F, f, f1);
            this.walk(ToeL2, speed_walk, degree_walk * -1.75F, true, 1, 0.1F, f, f1);
            this.walk(ToeL3, speed_walk, degree_walk * -1.75F, true, 1, 0.1F, f, f1);
            this.walk(ToeL4, speed_walk, degree_walk * -1.75F, true, 1, 0.1F, f, f1);
            this.walk(ToeR1, speed_walk, degree_walk * 1.25F, true, 1, -0.1F, f, f1);
            this.walk(ToeR2, speed_walk, degree_walk * -1.75F, false, 1, -0.1F, f, f1);
            this.walk(ToeR3, speed_walk, degree_walk * -1.75F, false, 1, -0.1F, f, f1);
            this.walk(ToeL4_1, speed_walk, degree_walk * -1.75F, false, 1, -0.1F, f, f1);
        }
    }


    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(Body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(Body, LowerBody, Neck1, WingL, WingR, BackLegL1, BackLegR1, Lowerbodytilt,
            TailR1, TailL1, TailR2, TailL2, BackLegL2, ToeL3, ToeL2, ToeL4, ToeL1, BackLegR2, ToeR3,
            ToeL4_1, ToeR2, ToeR1, Neck2, HeadBase, HeadFront, Jaw, Crest1, uppernail, Crest2, Crest3, WingL2,
            WingL3, WingL21, FingerL1, FingerL2, FingerL3, FingerL4, WingR2, WingR3, WingR21, FingerR1,
            FingerR2, FingerR3, FingerR4, HeadPivot, NeckPivot);
    }

    @Override
    public void renderStatue(MatrixStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, Entity living) {
        this.render(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
