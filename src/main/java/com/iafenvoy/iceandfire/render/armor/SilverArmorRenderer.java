package com.iafenvoy.iceandfire.render.armor;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.render.model.armor.ModelSilverArmor;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SilverArmorRenderer implements ArmorRenderer {
    public BipedEntityModel<?> getHumanoidArmorModel(EquipmentSlot armorSlot) {
        return new ModelSilverArmor(armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD);
    }

    public Identifier getArmorTexture(EquipmentSlot slot) {
        return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/" + (slot == EquipmentSlot.LEGS ? "armor_silver_metal_layer_2" : "armor_silver_metal_layer_1") + ".png");
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, this.getHumanoidArmorModel(slot), this.getArmorTexture(slot));
    }
}
