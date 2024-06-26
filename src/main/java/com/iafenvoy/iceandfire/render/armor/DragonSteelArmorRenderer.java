package com.iafenvoy.iceandfire.render.armor;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.iceandfire.render.model.armor.ArmorModelBase;
import com.iafenvoy.iceandfire.render.model.armor.ModelDragonSteelFireArmor;
import com.iafenvoy.iceandfire.render.model.armor.ModelDragonSteelIceArmor;
import com.iafenvoy.iceandfire.render.model.armor.ModelDragonSteelLightningArmor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class DragonSteelArmorRenderer extends ArmorRendererImpl {
    @Override
    public ArmorModelBase getHumanoidArmorModel(ItemStack itemStack, EquipmentSlot armorSlot) {
        boolean inner = armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD;
        if (itemStack.getItem() instanceof ArmorItem armorItem) {
            ArmorMaterial armorMaterial = armorItem.getMaterial();
            if (IafItems.DRAGONSTEEL_FIRE_ARMOR_MATERIAL.equals(armorMaterial))
                return new ModelDragonSteelFireArmor(inner);
            if (IafItems.DRAGONSTEEL_ICE_ARMOR_MATERIAL.equals(armorMaterial))
                return new ModelDragonSteelIceArmor(inner);
            if (IafItems.DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL.equals(armorMaterial))
                return new ModelDragonSteelLightningArmor(inner);
        }
        return null;
    }

    @Override
    public Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
        ArmorMaterial material = ((ArmorItem) stack.getItem()).getMaterial();
        if (material == IafItems.DRAGONSTEEL_FIRE_ARMOR_MATERIAL)
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_dragonsteel_fire" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
        else if (material == IafItems.DRAGONSTEEL_ICE_ARMOR_MATERIAL)
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_dragonsteel_ice" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
        else
            return new Identifier(IceAndFire.MOD_ID, "textures/models/armor/armor_dragonsteel_lightning" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png"));
    }
}
