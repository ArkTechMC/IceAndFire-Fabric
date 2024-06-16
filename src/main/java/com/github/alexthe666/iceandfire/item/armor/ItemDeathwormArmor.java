package com.github.alexthe666.iceandfire.item.armor;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;

public class ItemDeathwormArmor extends ArmorItem {
    public ItemDeathwormArmor(ArmorMaterial material, Type slot) {
        super(material, slot, new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
    }
}
