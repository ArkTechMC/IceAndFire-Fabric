package com.iafenvoy.iceandfire.item.armor;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;

public class ItemSilverArmor extends ArmorItem {
    public ItemSilverArmor(ArmorMaterial material, Type slot) {
        super(material, slot, new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
    }
}