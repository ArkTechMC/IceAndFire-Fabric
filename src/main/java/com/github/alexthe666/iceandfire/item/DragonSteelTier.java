package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;
import java.util.function.Supplier;

public class DragonSteelTier {

    public static final TagKey<Block> DRAGONSTEEL_TIER_TAG = TagKey.of(RegistryKeys.BLOCK, new Identifier(IceAndFire.MOD_ID, "needs_dragonsteel"));
    public static final ToolMaterial DRAGONSTEEL_TIER_FIRE = createMaterialWithRepairItem(() -> Ingredient.ofItems(IafItemRegistry.DRAGONSTEEL_FIRE_INGOT.get()), "dragonsteel_tier_fire");
    public static final ToolMaterial DRAGONSTEEL_TIER_ICE = createMaterialWithRepairItem(() -> Ingredient.ofItems(IafItemRegistry.DRAGONSTEEL_ICE_INGOT.get()), "dragonsteel_tier_ice");
    public static final ToolMaterial DRAGONSTEEL_TIER_LIGHTNING = createMaterialWithRepairItem(() -> Ingredient.ofItems(IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT.get()), "dragonsteel_tier_lightning");
    //FIXME: Probably shouldn't be called dragonsteel
    public static final ToolMaterial DRAGONSTEEL_TIER_DREAD_QUEEN = createMaterialWithRepairItem(Ingredient::empty, "dragonsteel_tier_dread_queen");

    private static ToolMaterial createMaterialWithRepairItem(Supplier<Ingredient> ingredient, String name) {
        return TierSortingRegistry.registerTier(
                new ForgeTier(4, 8000, 10, 21, 10, DRAGONSTEEL_TIER_TAG, ingredient),
                new Identifier(IceAndFire.MOD_ID, name),
                List.of(ToolMaterials.NETHERITE), List.of());
    }
}
