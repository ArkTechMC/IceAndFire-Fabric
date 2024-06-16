package com.github.alexthe666.iceandfire.item.food;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemGenericFood extends Item {
    private final int healAmount;
    private final float saturation;

    public ItemGenericFood(int amount, float saturation, boolean isWolfFood, boolean eatFast, boolean alwaysEdible) {
        super(new Settings().food(createFood(amount, saturation, isWolfFood, eatFast, alwaysEdible, null)));
        this.healAmount = amount;
        this.saturation = saturation;
    }

    public ItemGenericFood(int amount, float saturation, boolean isWolfFood, boolean eatFast, boolean alwaysEdible, int stackSize) {
        super(new Settings().food(createFood(amount, saturation, isWolfFood, eatFast, alwaysEdible, null)).maxCount(stackSize));
        this.healAmount = amount;
        this.saturation = saturation;
    }

    public static FoodComponent createFood(int amount, float saturation, boolean isWolfFood, boolean eatFast, boolean alwaysEdible, StatusEffectInstance potion) {
        FoodComponent.Builder builder = new FoodComponent.Builder();
        builder.hunger(amount);
        builder.saturationModifier(saturation);
        if (isWolfFood) {
            builder.meat();
        }
        if (eatFast) {
            builder.snack();
        }
        if (alwaysEdible) {
            builder.alwaysEdible();
        }
        if (potion != null) {
            builder.statusEffect(potion, 1.0F);
        }
        return builder.build();
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World worldIn, LivingEntity LivingEntity) {
        this.onFoodEaten(stack, worldIn, LivingEntity);
        return super.finishUsing(stack, worldIn, LivingEntity);
    }

    public void onFoodEaten(ItemStack stack, World worldIn, LivingEntity livingEntity) {
    }
}
