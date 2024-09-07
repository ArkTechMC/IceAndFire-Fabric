package com.iafenvoy.iceandfire.render.armor;

import com.iafenvoy.uranus.client.render.armor.ArmorModelBase;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public abstract class ArmorRendererImpl implements ArmorRenderer {
    @Nullable
    public abstract ArmorModelBase getHumanoidArmorModel(ItemStack itemStack, EquipmentSlot armorSlot);

    public abstract Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot);

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        ArmorModelBase model = this.getHumanoidArmorModel(stack, slot);
        if (model == null) return;
        contextModel.copyBipedStateTo(model);
        model.render(slot, matrices, vertexConsumers, light, stack, this.getArmorTexture(stack, slot));
    }
}
