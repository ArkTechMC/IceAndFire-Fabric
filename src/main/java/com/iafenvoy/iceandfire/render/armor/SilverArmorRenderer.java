package com.iafenvoy.iceandfire.render.armor;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.render.model.armor.ModelSilverArmor;
import com.iafenvoy.uranus.client.render.armor.ArmorModelBase;
import com.iafenvoy.uranus.client.render.armor.ArmorRendererImpl;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SilverArmorRenderer extends ArmorRendererImpl {
    @Override
    public ArmorModelBase getHumanoidArmorModel(ItemStack stack, EquipmentSlot armorSlot) {
        return new ModelSilverArmor(armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD);
    }

    @Override
    public Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
        return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/" + (slot == EquipmentSlot.LEGS ? "armor_silver_metal_layer_2" : "armor_silver_metal_layer_1") + ".png");
    }
}
