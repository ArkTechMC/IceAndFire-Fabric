package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import dev.arktechmc.iafextra.util.ToolMaterialUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class DragonSteelTier {

    public static final TagKey<Block> DRAGONSTEEL_TIER_TAG = TagKey.of(RegistryKeys.BLOCK, new Identifier(IceAndFire.MOD_ID, "needs_dragonsteel"));
    public static final ToolMaterial DRAGONSTEEL_TIER_FIRE = createMaterialWithRepairItem(IafItemRegistry.DRAGONSTEEL_FIRE_INGOT.get(), "dragonsteel_tier_fire");
    public static final ToolMaterial DRAGONSTEEL_TIER_ICE = createMaterialWithRepairItem(IafItemRegistry.DRAGONSTEEL_ICE_INGOT.get(), "dragonsteel_tier_ice");
    public static final ToolMaterial DRAGONSTEEL_TIER_LIGHTNING = createMaterialWithRepairItem(IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT.get(), "dragonsteel_tier_lightning");
    //FIXME: Probably shouldn't be called dragonsteel
    public static final ToolMaterial DRAGONSTEEL_TIER_DREAD_QUEEN = createMaterialWithRepairItem(Items.AIR, "dragonsteel_tier_dread_queen");

    private static ToolMaterial createMaterialWithRepairItem(ItemConvertible ingredient, String name) {
        return ToolMaterialUtil.of(4000, 4F, 10F, 21, 10, ingredient);
    }
}
