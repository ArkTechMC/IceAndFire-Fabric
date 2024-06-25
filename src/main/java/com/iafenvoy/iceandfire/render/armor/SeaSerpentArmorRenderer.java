package com.iafenvoy.iceandfire.render.armor;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.enums.EnumSeaSerpent;
import com.iafenvoy.iceandfire.item.armor.ItemSeaSerpentArmor;
import com.iafenvoy.iceandfire.render.model.armor.ModelSeaSerpentArmor;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SeaSerpentArmorRenderer implements ArmorRenderer {
    public BipedEntityModel<?> getHumanoidArmorModel(EquipmentSlot armorSlot) {
        return new ModelSeaSerpentArmor(armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD);
    }

    public Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
        EnumSeaSerpent armor_type = ((ItemSeaSerpentArmor) stack.getItem()).armor_type;
        return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_tide_" + armor_type.resourceName + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, this.getHumanoidArmorModel(slot), this.getArmorTexture(stack, slot));
    }
}
