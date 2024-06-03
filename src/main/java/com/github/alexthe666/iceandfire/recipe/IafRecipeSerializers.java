package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.iceandfire.IceAndFire;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;

public class IafRecipeSerializers {
    public static final LazyRegistrar<RecipeSerializer<?>> SERIALIZERS = LazyRegistrar.create(Registries.RECIPE_SERIALIZER, IceAndFire.MOD_ID);
    public static final RegistryObject<RecipeSerializer<?>> DRAGONFORGE_SERIALIZER = SERIALIZERS.register("dragonforge", DragonForgeRecipe.Serializer::new);

}
