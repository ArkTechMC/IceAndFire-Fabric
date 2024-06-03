package com.github.alexthe666.citadel.server.item;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.recipe.SmithingRecipe;

public class CitadelRecipes {

    private static final List<SmithingRecipe> smithingRecipes = new ArrayList<>();

    public static void registerSmithingRecipe(SmithingRecipe recipe){
        smithingRecipes.add(recipe);
    }

    public static List<SmithingRecipe> getSmithingRecipes(){
        return smithingRecipes;
    }
}
