package com.iafenvoy.iceandfire.render.armor;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.util.dragon.DragonType;
import com.iafenvoy.iceandfire.enums.EnumDragonArmor;
import com.iafenvoy.iceandfire.item.armor.ItemScaleArmor;
import com.iafenvoy.iceandfire.render.model.armor.ModelFireDragonScaleArmor;
import com.iafenvoy.iceandfire.render.model.armor.ModelIceDragonScaleArmor;
import com.iafenvoy.iceandfire.render.model.armor.ModelLightningDragonScaleArmor;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ScaleArmorRenderer implements ArmorRenderer {
    public BipedEntityModel<?> getHumanoidArmorModel(ItemStack itemStack, EquipmentSlot armorSlot, BipedEntityModel<?> _default) {
        boolean inner = armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD;
        if (itemStack.getItem() instanceof ItemScaleArmor) {
            DragonType dragonType = ((ItemScaleArmor) itemStack.getItem()).armor_type.eggType.dragonType;

            if (DragonType.FIRE == dragonType)
                return new ModelFireDragonScaleArmor(inner);
            if (DragonType.ICE == dragonType)
                return new ModelIceDragonScaleArmor(inner);
            if (DragonType.LIGHTNING == dragonType)
                return new ModelLightningDragonScaleArmor(inner);
        }
        return _default;

    }

    public Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
        EnumDragonArmor armor_type = ((ItemScaleArmor) stack.getItem()).armor_type;
        return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/" + armor_type.name() + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, this.getHumanoidArmorModel(stack, slot, contextModel), this.getArmorTexture(stack, slot));
    }
}
