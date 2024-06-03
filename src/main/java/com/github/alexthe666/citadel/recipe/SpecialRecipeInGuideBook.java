package com.github.alexthe666.citadel.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.collection.DefaultedList;

public interface SpecialRecipeInGuideBook {
    DefaultedList<Ingredient> getDisplayIngredients();
    ItemStack getDisplayResultFor(DefaultedList<ItemStack> stacks);
}
