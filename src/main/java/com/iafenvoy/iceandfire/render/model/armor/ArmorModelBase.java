package com.iafenvoy.iceandfire.render.model.armor;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;


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

    public void render(EquipmentSlot slot, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ItemStack stack, Identifier texture) {
        switch (slot) {
            case HEAD -> this.renderHelmet(matrices, vertexConsumers, light, stack, texture);
            case CHEST -> this.renderChestplate(matrices, vertexConsumers, light, stack, texture);
            case LEGS -> this.renderLeggings(matrices, vertexConsumers, light, stack, texture);
            case FEET -> this.renderBoots(matrices, vertexConsumers, light, stack, texture);
        }
    }

    public void renderHelmet(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ItemStack stack, Identifier texture) {
        VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(texture), false, stack.hasGlint());
        this.head.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
    }

    public void renderChestplate(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ItemStack stack, Identifier texture) {
        VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(texture), false, stack.hasGlint());
        this.body.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        this.leftArm.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        this.rightArm.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
    }

    public void renderLeggings(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ItemStack stack, Identifier texture) {
        VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(texture), false, stack.hasGlint());
        this.leftLeg.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        this.rightLeg.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
    }

    public void renderBoots(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ItemStack stack, Identifier texture) {
        VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(texture), false, stack.hasGlint());
        this.leftLeg.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
        this.rightLeg.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
    }
}
