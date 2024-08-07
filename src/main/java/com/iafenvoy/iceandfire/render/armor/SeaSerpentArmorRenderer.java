package com.iafenvoy.iceandfire.render.armor;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.data.SeaSerpent;
import com.iafenvoy.iceandfire.item.armor.ItemSeaSerpentArmor;
import com.iafenvoy.iceandfire.render.model.armor.ModelSeaSerpentArmor;
import com.iafenvoy.uranus.client.render.armor.ArmorModelBase;
import com.iafenvoy.uranus.client.render.armor.ArmorRendererImpl;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SeaSerpentArmorRenderer extends ArmorRendererImpl {
    @Override
    public ArmorModelBase getHumanoidArmorModel(ItemStack stack, EquipmentSlot armorSlot) {
        return new ModelSeaSerpentArmor(armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD);
    }

    @Override
    public Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
        SeaSerpent armor_type = ((ItemSeaSerpentArmor) stack.getItem()).armor_type;
        return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_tide_" + armor_type.resourceName + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
    }
}
