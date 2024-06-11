package com.github.alexthe666.citadel.mixin;

import com.github.alexthe666.citadel.server.item.CitadelRecipes;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

@Mixin(SmithingScreenHandler.class)
public class SmithingScreenHandlerMixin {
    @Redirect(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/RecipeManager;getAllMatches(Lnet/minecraft/recipe/RecipeType;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/world/World;)Ljava/util/List;"))
    private List<SmithingRecipe> citadel_getRecipesFor(RecipeManager recipeManager, RecipeType<SmithingRecipe> type, Inventory container, World level) {
        List<SmithingRecipe> list = new ArrayList<>(recipeManager.getAllMatches(type, container, level));
        if (type == RecipeType.SMITHING && container.size() >= 2 && !container.getStack(0).isEmpty() && !container.getStack(1).isEmpty())
            list.addAll(CitadelRecipes.getSmithingRecipes());
        return list;
    }
}