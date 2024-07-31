package com.iafenvoy.iceandfire.item.armor;

import com.iafenvoy.iceandfire.config.IafCommonConfig;
import net.minecraft.item.ArmorItem;
import net.minecraft.sound.SoundEvent;

public class DragonSteelArmorMaterial extends IafArmorMaterial {
    public DragonSteelArmorMaterial(String name, int durability, int[] damageReduction, int encantability, SoundEvent sound, float toughness) {
        super(name, durability, damageReduction, encantability, sound, toughness);
    }

    @Override
    public int getProtection(ArmorItem.Type slotIn) {
        int[] damageReduction = new int[]{IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmor - 6, IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmor - 3, IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmor, IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmor - 5};
        return damageReduction[slotIn.getEquipmentSlot().getEntitySlotId()];
    }

    @Override
    public float getToughness() {
        return IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmorToughness;
    }

    @Override
    public int getDurability(ArmorItem.Type slotIn) {
        return (int) (MAX_DAMAGE_ARRAY[slotIn.getEquipmentSlot().getEntitySlotId()] * 0.02D * IafCommonConfig.INSTANCE.armors.dragonsteel.baseDurabilityEquipment);
    }
}
