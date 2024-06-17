package com.github.alexthe666.iceandfire.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

public class FoodUtils {
    public static int getFoodPoints(Entity entity) {
        int foodPoints = Math.round(entity.getWidth() * entity.getHeight() * 10);
        if (entity instanceof PassiveEntity) return foodPoints;
        if (entity instanceof PlayerEntity) return 15;
        return 0;
    }

    public static int getFoodPoints(ItemStack item, boolean meatOnly, boolean includeFish) {
        if (item != null && item != ItemStack.EMPTY && item.getItem() != null && item.getItem().getFoodComponent() != null) {
            int food = item.getItem().getFoodComponent().getHunger() * 10;
            if (!meatOnly) return food;
            else if (item.getItem().getFoodComponent().isMeat()) return food;
            else if (includeFish && item.isIn(ItemTags.FISHES)) return food;
        }
        return 0;
    }
}
