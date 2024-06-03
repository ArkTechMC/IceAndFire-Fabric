package com.github.alexthe666.citadel.mixin;

import com.github.alexthe666.citadel.CitadelConstants;
import com.github.alexthe666.citadel.server.item.CitadelRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.world.World;

@Mixin(SmithingScreenHandler.class)
public class SmithingMenuMixin {

    @Redirect(
            method = "Lnet/minecraft/world/inventory/SmithingMenu;createResult()V",
            remap = CitadelConstants.REMAPREFS, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeManager;getRecipesFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/List;")
    )
    private List<SmithingRecipe> citadel_getRecipesFor(RecipeManager recipeManager, RecipeType<SmithingRecipe> type, Inventory container, World level) {
        List<SmithingRecipe> list = new ArrayList<>();
        list.addAll(recipeManager.getAllMatches(type, container, level));
        if(type == RecipeType.SMITHING && container.size() >= 2 && !container.getStack(0).isEmpty()&& !container.getStack(1).isEmpty()){
            list.addAll(CitadelRecipes.getSmithingRecipes());
        }
        return list;
    }
}