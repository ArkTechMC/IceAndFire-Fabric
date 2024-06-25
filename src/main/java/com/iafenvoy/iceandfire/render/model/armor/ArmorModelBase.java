package com.iafenvoy.iceandfire.render.model.armor;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;


public class ArmorModelBase extends BipedEntityModel<LivingEntity> {
    protected static final float INNER_MODEL_OFFSET = 0.38F;
    protected static final float OUTER_MODEL_OFFSET = 0.45F;

    public ArmorModelBase(ModelPart p_170677_) {
        super(p_170677_);
    }

    @Override
    public void setAngles(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityIn instanceof ArmorStandEntity armorStand) {
            this.head.pitch = ((float) Math.PI / 180F) * armorStand.getHeadRotation().getPitch();
            this.head.yaw = ((float) Math.PI / 180F) * armorStand.getHeadRotation().getYaw();
            this.head.roll = ((float) Math.PI / 180F) * armorStand.getHeadRotation().getRoll();
            this.body.pitch = ((float) Math.PI / 180F) * armorStand.getBodyRotation().getPitch();
            this.body.yaw = ((float) Math.PI / 180F) * armorStand.getBodyRotation().getYaw();
            this.body.roll = ((float) Math.PI / 180F) * armorStand.getBodyRotation().getRoll();
            this.leftArm.pitch = ((float) Math.PI / 180F) * armorStand.getLeftArmRotation().getPitch();
            this.leftArm.yaw = ((float) Math.PI / 180F) * armorStand.getLeftArmRotation().getYaw();
            this.leftArm.roll = ((float) Math.PI / 180F) * armorStand.getLeftArmRotation().getRoll();
            this.rightArm.pitch = ((float) Math.PI / 180F) * armorStand.getRightArmRotation().getPitch();
            this.rightArm.yaw = ((float) Math.PI / 180F) * armorStand.getRightArmRotation().getYaw();
            this.rightArm.roll = ((float) Math.PI / 180F) * armorStand.getRightArmRotation().getRoll();
            this.leftLeg.pitch = ((float) Math.PI / 180F) * armorStand.getLeftLegRotation().getPitch();
            this.leftLeg.yaw = ((float) Math.PI / 180F) * armorStand.getLeftLegRotation().getYaw();
            this.leftLeg.roll = ((float) Math.PI / 180F) * armorStand.getLeftLegRotation().getRoll();
            this.rightLeg.pitch = ((float) Math.PI / 180F) * armorStand.getRightLegRotation().getPitch();
            this.rightLeg.yaw = ((float) Math.PI / 180F) * armorStand.getRightLegRotation().getYaw();
            this.rightLeg.roll = ((float) Math.PI / 180F) * armorStand.getRightLegRotation().getRoll();
            this.hat.copyTransform(this.head);
        } else
            super.setAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    //this.(?<name>.*).addChild\(this.(?<name2>.*)\);
    //partdefinition.getChild("${name}").addOrReplaceChild("${name2},

    //this.(?<name>.*) = new AdvancedModelBox\(.*, (?<texX>[0-9]*), (?<texY>[0-9]*)\);
    //.addOrReplaceChild("${name}", CubeListBuilder.create().texOffs(${texX}, ${texY})

    //this.(?<name>.*).setPos\((?<x>.*), (?<y>.*), (?<z>.*)\);
    //PartPose.offsetAndRotation(${x}, ${y}, ${z},

    //this.(?<name>.*).addBox\((?<x>.*), (?<y>.*), (?<z>.*), (?<u>.*), (?<v>.*), (?<w>.*), 0.0F\);
    //.addBox(${x}, ${y}, ${z}, ${u}, ${v}, ${w})

    //(?<main>.addOrReplaceChild\("(?<name>.*)", Cube.*)\n.*(?<part>PartPose.*)\n.*(?<box>addBox.*)\n.*this.setRotateAngle\(.*\k<name>.*, (?<aX>.*), (?<aY>.*), (?<aZ>.*)\);
    //${main}.${box}, ${part}${aX}, ${aY}, ${aZ}));

    //(?<main>.addOrReplaceChild\("(?<name>.*)", Cube.*)\n.*(?<part>PartPose.*)\n.*(?<box>addBox.*\));\n
    //${main}.${box}, ${part});
}
