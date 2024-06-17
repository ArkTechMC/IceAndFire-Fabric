package com.iafenvoy.iceandfire.util;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class ToolMaterialUtil {
    public static ToolMaterial of(int uses, float speed, float attackDamageBonus, int level, int enchantmentLevel, ItemConvertible... repairIngredients) {
        return new ToolMaterial() {
            @Override
            public int getDurability() {
                return uses;
            }

            @Override
            public float getMiningSpeedMultiplier() {
                return speed;
            }

            @Override
            public float getAttackDamage() {
                return attackDamageBonus;
            }

            @Override
            public int getMiningLevel() {
                return level;
            }

            @Override
            public int getEnchantability() {
                return enchantmentLevel;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return Ingredient.ofItems(repairIngredients);
            }
        };
    }
}
