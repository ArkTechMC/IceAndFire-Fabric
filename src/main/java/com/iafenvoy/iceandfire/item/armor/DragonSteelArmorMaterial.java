package com.iafenvoy.iceandfire.item.armor;

import com.iafenvoy.iceandfire.IafConfig;
import net.minecraft.item.ArmorItem;
import net.minecraft.sound.SoundEvent;

public class DragonSteelArmorMaterial extends IafArmorMaterial {
    public DragonSteelArmorMaterial(String name, int durability, int[] damageReduction, int encantability, SoundEvent sound, float toughness) {
        super(name, durability, damageReduction, encantability, sound, toughness);
    }

    @Override
    public int getProtection(ArmorItem.Type slotIn) {
        int[] damageReduction = new int[]{IafConfig.dragonsteelBaseArmor - 6, IafConfig.dragonsteelBaseArmor - 3, IafConfig.dragonsteelBaseArmor, IafConfig.dragonsteelBaseArmor - 5};
        return damageReduction[slotIn.getEquipmentSlot().getEntitySlotId()];
    }

    @Override
    public float getToughness() {
        return IafConfig.dragonsteelBaseArmorToughness;
    }

    @Override
    public int getDurability(ArmorItem.Type slotIn) {
        return (int) (MAX_DAMAGE_ARRAY[slotIn.getEquipmentSlot().getEntitySlotId()] * 0.02D * IafConfig.dragonsteelBaseDurabilityEquipment);
    }
}
