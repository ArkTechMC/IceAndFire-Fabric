package com.iafenvoy.iceandfire.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

public class FoodUtils {
    public static int getFoodPoints(Entity entity) {
        if (entity instanceof PassiveEntity) return Math.round(entity.getWidth() * entity.getHeight() * 10);
        if (entity instanceof PlayerEntity) return 15;
        return 0;
    }

    public static int getFoodPoints(ItemStack item, boolean meatOnly, boolean includeFish) {
        if (item != null && item != ItemStack.EMPTY && item.getItem() != null && item.getItem().getFoodComponent() != null)
            if (!meatOnly || item.getItem().getFoodComponent().isMeat() || includeFish && item.isIn(ItemTags.FISHES))
                return item.getItem().getFoodComponent().getHunger() * 10;
        return 0;
    }
}
