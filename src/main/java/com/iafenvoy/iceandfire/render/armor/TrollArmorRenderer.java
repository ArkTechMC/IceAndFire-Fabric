package com.iafenvoy.iceandfire.render.armor;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.enums.EnumTroll;
import com.iafenvoy.iceandfire.item.armor.ItemTrollArmor;
import com.iafenvoy.iceandfire.render.model.armor.ModelTrollArmor;
import com.iafenvoy.uranus.client.render.armor.ArmorModelBase;
import com.iafenvoy.uranus.client.render.armor.ArmorRendererImpl;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Locale;

public class TrollArmorRenderer extends ArmorRendererImpl {
    @Override
    public ArmorModelBase getHumanoidArmorModel(ItemStack itemStack, EquipmentSlot armorSlot) {
        return new ModelTrollArmor(armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD);
    }

    @Override
    public Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
        EnumTroll troll = ((ItemTrollArmor) stack.getItem()).troll;
        return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_troll_" + troll.name().toLowerCase(Locale.ROOT) + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
    }
}
