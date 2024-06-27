package com.iafenvoy.iceandfire.item.armor;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IArmorFinder extends Equipment {
    default boolean isEquipped(LivingEntity player, ItemStack stack) {
        return player.getEquippedStack(this.getSlotType()) == stack;
    }

    default boolean isEquipped(LivingEntity player, Item item) {
        return player.getEquippedStack(this.getSlotType()).isOf(item);
    }
}
