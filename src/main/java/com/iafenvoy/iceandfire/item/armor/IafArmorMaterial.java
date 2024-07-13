package com.iafenvoy.iceandfire.item.armor;

import com.iafenvoy.uranus.server.item.CustomArmorMaterial;
import net.minecraft.item.ArmorItem;
import net.minecraft.sound.SoundEvent;

public class IafArmorMaterial extends CustomArmorMaterial {
    protected static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
    private final int maxDamageFactor;

    public IafArmorMaterial(String name, int durability, int[] damageReduction, int encantability, SoundEvent sound, float toughness) {
        super(name, durability, damageReduction, encantability, sound, toughness, 0);
        this.maxDamageFactor = durability;
    }

    @Override
    public int getDurability(ArmorItem.Type slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getEquipmentSlot().getEntitySlotId()] * this.maxDamageFactor;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }
}
