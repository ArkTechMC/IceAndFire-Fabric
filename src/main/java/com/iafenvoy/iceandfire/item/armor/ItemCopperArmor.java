package com.iafenvoy.iceandfire.item.armor;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;

public class ItemCopperArmor extends ArmorItem {
    public ItemCopperArmor(ArmorMaterial material, Type slot) {
        super(material, slot, new FabricItemSettings());
    }
}