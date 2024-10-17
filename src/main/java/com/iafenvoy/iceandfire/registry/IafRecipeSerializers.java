package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.recipe.DragonForgeRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class IafRecipeSerializers {
    public static final RecipeSerializer<?> DRAGONFORGE_SERIALIZER = register("dragonforge", new DragonForgeRecipe.Serializer());

    private static RecipeSerializer<?> register(String name, RecipeSerializer<?> serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(IceAndFire.MOD_ID, name), serializer);
    }

    public static void init() {
    }
}
