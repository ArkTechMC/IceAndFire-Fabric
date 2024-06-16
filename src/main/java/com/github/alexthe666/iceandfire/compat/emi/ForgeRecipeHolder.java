package com.github.alexthe666.iceandfire.compat.emi;

import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import com.github.alexthe666.iceandfire.registry.IafRecipes;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ForgeRecipeHolder {
    private final String dragonType;
    private final EmiTexture texture;
    private final EmiStack workstation;
    private final EmiRecipeCategory category;

    public ForgeRecipeHolder(Identifier id, String dragonType, EmiTexture texture, EmiStack workstation) {
        this.dragonType = dragonType;
        this.texture = texture;
        this.workstation = workstation;
        this.category = new EmiRecipeCategory(id, workstation, texture);
    }

    public void register(EmiRegistry registry) {
        registry.addCategory(this.category);
        registry.addWorkstation(this.category, this.workstation);
        List<DragonForgeRecipe> forgeRecipeList = registry.getRecipeManager().listAllOfType(IafRecipes.DRAGON_FORGE_TYPE);
        for (DragonForgeRecipe recipe : forgeRecipeList.stream().filter(item -> item.getDragonType().equals(this.dragonType)).toList())
            registry.addRecipe(new DragonForgeEmiRecipe(recipe, this.category));
    }

    public class DragonForgeEmiRecipe implements EmiRecipe {
        private final DragonForgeRecipe recipe;
        private final EmiRecipeCategory category;

        public DragonForgeEmiRecipe(DragonForgeRecipe recipe, EmiRecipeCategory category) {
            this.recipe = recipe;
            this.category = category;
        }

        @Override
        public EmiRecipeCategory getCategory() {
            return this.category;
        }

        @Override
        public @Nullable Identifier getId() {
            return this.recipe.getId();
        }

        @Override
        public List<EmiIngredient> getInputs() {
            return List.of(EmiIngredient.of(this.recipe.getInput()), EmiIngredient.of(this.recipe.getBlood()));
        }

        @Override
        public List<EmiStack> getOutputs() {
            return List.of(EmiStack.of(this.recipe.getOutput(null)));
        }

        @Override
        public int getDisplayWidth() {
            return 176;
        }

        @Override
        public int getDisplayHeight() {
            return 1120;
        }

        @Override
        public void addWidgets(WidgetHolder widgets) {
            widgets.addTexture(ForgeRecipeHolder.this.texture, 3, 4);
            widgets.addSlot(EmiIngredient.of(this.recipe.getInput()), 67, 33);
            widgets.addSlot(EmiIngredient.of(this.recipe.getBlood()), 85, 33);
            widgets.addSlot(EmiStack.of(this.recipe.getOutput(null)), 143, 30).large(true).recipeContext(this);
        }
    }
}
