package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class IafRecipeSerializers {
    public static final RecipeSerializer<?> DRAGONFORGE_SERIALIZER = register("dragonforge", new DragonForgeRecipe.Serializer());

    public static RecipeSerializer<?> register(String name, RecipeSerializer<?> serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(IceAndFire.MOD_ID, name), serializer);
    }

    public static void init() {
    }
}
