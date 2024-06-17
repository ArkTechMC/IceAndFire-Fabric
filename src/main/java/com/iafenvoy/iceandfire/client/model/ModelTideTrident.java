package com.iafenvoy.iceandfire.client.model;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.citadel.client.model.AdvancedEntityModel;
import com.iafenvoy.citadel.client.model.AdvancedModelBox;
import com.iafenvoy.citadel.client.model.basic.BasicModelPart;
import net.minecraft.client.model.ModelPart;
import net.minecraft.entity.Entity;

public class ModelTideTrident extends AdvancedEntityModel<Entity> {
    public final AdvancedModelBox shaft;
    public final AdvancedModelBox base;
    public final AdvancedModelBox blade_B;
    public final AdvancedModelBox blade_C;
    public final AdvancedModelBox blade_A;
    public final AdvancedModelBox fins;
    public final AdvancedModelBox blade_C_2;
    public final AdvancedModelBox blade_A_2;

    public ModelTideTrident() {
        this.texWidth = 64;
        this.texHeight = 32;
        this.fins = new AdvancedModelBox(this, 5, 12);
        this.fins.setPos(0.0F, 0.0F, 0.0F);
        this.fins.addBox(-5.5F, -1.0F, 0.0F, 11, 7, 0, 0.0F);
        this.blade_A_2 = new AdvancedModelBox(this, 28, 0);
        this.blade_A_2.mirror = true;
        this.blade_A_2.setPos(0.0F, -4.0F, 0.0F);
        this.blade_A_2.addBox(0.5F, -2.0F, -0.5F, 1, 1, 1, 0.0F);
        this.setRotateAngle(this.blade_A_2, 0.0F, 0.0F, 0.03490658503988659F);
        this.shaft = new AdvancedModelBox(this, 0, 0);
        this.shaft.setPos(0.0F, 14.0F, 0.0F);
        this.shaft.addBox(-0.5F, -11.0F, -0.5F, 1, 24, 1, 0.0F);
        this.blade_A = new AdvancedModelBox(this, 17, 0);
        this.blade_A.setPos(-1.4F, 0.0F, 0.0F);
        this.blade_A.addBox(-0.5F, -5.0F, -0.5F, 1, 4, 1, 0.0F);
        this.setRotateAngle(this.blade_A, 0.0F, 0.0F, -0.3839724354387525F);
        this.base = new AdvancedModelBox(this, 5, 5);
        this.base.setPos(0.0F, -12.0F, 0.0F);
        this.base.addBox(-1.5F, -2.0F, -0.5F, 3, 3, 1, 0.0F);
        this.setRotateAngle(this.base, 0.0F, 1.5707963267948966F, 0.0F);
        this.blade_B = new AdvancedModelBox(this, 23, 0);
        this.blade_B.setPos(0.0F, -2.0F, 0.0F);
        this.blade_B.addBox(-0.5F, -5.0F, -0.5F, 1, 5, 1, 0.0F);
        this.blade_C = new AdvancedModelBox(this, 17, 0);
        this.blade_C.mirror = true;
        this.blade_C.setPos(1.4F, 0.0F, 0.0F);
        this.blade_C.addBox(-0.5F, -5.0F, -0.5F, 1, 4, 1, 0.0F);
        this.setRotateAngle(this.blade_C, 0.0F, 0.0F, 0.3839724354387525F);
        this.blade_C_2 = new AdvancedModelBox(this, 28, 0);
        this.blade_C_2.mirror = true;
        this.blade_C_2.setPos(0.0F, -4.0F, 0.0F);
        this.blade_C_2.addBox(-1.5F, -2.0F, -0.5F, 1, 1, 1, 0.0F);
        this.setRotateAngle(this.blade_C_2, 0.0F, 0.0F, 0.03490658503988659F);
        this.base.addChild(this.fins);
        this.blade_A.addChild(this.blade_A_2);
        this.base.addChild(this.blade_A);
        this.shaft.addChild(this.base);
        this.base.addChild(this.blade_B);
        this.base.addChild(this.blade_C);
        this.blade_C.addChild(this.blade_C_2);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.shaft, this.base, this.blade_B, this.blade_C, this.blade_A, this.fins, this.blade_C_2, this.blade_A_2);
    }

    @Override
    public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.shaft);
    }

    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.pitch = x;
        modelRenderer.yaw = y;
        modelRenderer.roll = z;
    }
}
