package com.iafenvoy.iceandfire.item.food;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPixieDust extends ItemGenericFood {
    public ItemPixieDust() {
        super(1, 0.3F, false, false, true);
    }

    @Override
    public void onFoodEaten(ItemStack stack, World worldIn, LivingEntity livingEntity) {
        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 100, 1));
        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 100, 1));
    }
}
