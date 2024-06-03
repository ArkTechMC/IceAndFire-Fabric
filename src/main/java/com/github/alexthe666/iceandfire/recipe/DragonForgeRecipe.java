package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.citadel.client.model.container.JsonUtils;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;


public class DragonForgeRecipe implements Recipe<TileEntityDragonforge> {
    private final Ingredient input;
    private final Ingredient blood;
    private final ItemStack result;
    private final String dragonType;
    private final int cookTime;
    private final Identifier recipeId;

    public DragonForgeRecipe(Identifier recipeId, Ingredient input, Ingredient blood, ItemStack result, String dragonType, int cookTime) {
        this.recipeId = recipeId;
        this.input = input;
        this.blood = blood;
        this.result = result;
        this.dragonType = dragonType;
        this.cookTime = cookTime;
    }

    public Ingredient getInput() {
        return input;
    }

    public Ingredient getBlood() {
        return blood;
    }

    public int getCookTime() {
        return cookTime;
    }

    public String getDragonType() {
        return dragonType;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    public boolean matches(TileEntityDragonforge inv, @NotNull World worldIn) {
        return this.input.test(inv.getStack(0)) && this.blood.test(inv.getStack(1)) && this.dragonType.equals(inv.getTypeID());
    }

    public boolean isValidInput(ItemStack stack) {
        return this.input.test(stack);
    }

    public boolean isValidBlood(ItemStack blood) {
        return this.blood.test(blood);
    }

    @Override
    public @NotNull ItemStack getOutput(DynamicRegistryManager registryAccess) {
        return result;
    }

    public @NotNull ItemStack getResultItem() {
        return result;
    }

    @Override
    public @NotNull ItemStack craft(@NotNull TileEntityDragonforge dragonforge, DynamicRegistryManager registryAccess) {
        return result;
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    @Override
    public @NotNull Identifier getId() {
        return this.recipeId;
    }

    @Override
    public @NotNull ItemStack createIcon() {
        return new ItemStack(IafBlockRegistry.DRAGONFORGE_FIRE_CORE.get());
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return IafRecipeSerializers.DRAGONFORGE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return IafRecipeRegistry.DRAGON_FORGE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<DragonForgeRecipe> {
        @Override
        public @NotNull DragonForgeRecipe read(@NotNull Identifier recipeId, @NotNull JsonObject json) {
            String dragonType = JsonUtils.getString(json, "dragon_type");
            Ingredient input = Ingredient.fromJson(JsonUtils.getJsonObject(json, "input"));
            Ingredient blood = Ingredient.fromJson(JsonUtils.getJsonObject(json, "blood"));
            int cookTime = JsonUtils.getInt(json, "cook_time");
            ItemStack result = ShapedRecipe.outputFromJson(JsonUtils.getJsonObject(json, "result"));
            return new DragonForgeRecipe(recipeId, input, blood, result, dragonType, cookTime);
        }

        @Override
        public DragonForgeRecipe read(@NotNull Identifier recipeId, PacketByteBuf buffer) {
            int cookTime = buffer.readInt();
            String dragonType = buffer.readString();
            Ingredient input = Ingredient.fromPacket(buffer);
            Ingredient blood = Ingredient.fromPacket(buffer);
            ItemStack result = buffer.readItemStack();
            return new DragonForgeRecipe(recipeId, input, blood, result, dragonType, cookTime);
        }

        @Override
        public void write(PacketByteBuf buffer, DragonForgeRecipe recipe) {
            buffer.writeInt(recipe.cookTime);
            buffer.writeString(recipe.dragonType);
            recipe.input.write(buffer);
            recipe.blood.write(buffer);
            buffer.writeItemStack(recipe.result);
        }
    }

}
