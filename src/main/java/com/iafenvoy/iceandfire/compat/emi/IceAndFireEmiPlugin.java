package com.iafenvoy.iceandfire.compat.emi;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.util.Identifier;

public class IceAndFireEmiPlugin implements EmiPlugin {
    private static final ForgeRecipeHolder FIRE = new ForgeRecipeHolder(new Identifier(IceAndFire.MOD_ID, "fire_forge"), "fire", new EmiTexture(new Identifier(IceAndFire.MOD_ID, "textures/gui/dragonforge_fire.png"), 3, 4, 170, 79), EmiStack.of(IafBlocks.DRAGONFORGE_FIRE_CORE));
    private static final ForgeRecipeHolder ICE = new ForgeRecipeHolder(new Identifier(IceAndFire.MOD_ID, "ice_forge"), "ice", new EmiTexture(new Identifier(IceAndFire.MOD_ID, "textures/gui/dragonforge_ice.png"), 3, 4, 170, 79), EmiStack.of(IafBlocks.DRAGONFORGE_ICE_CORE));
    private static final ForgeRecipeHolder LIGHTNING = new ForgeRecipeHolder(new Identifier(IceAndFire.MOD_ID, "lightning_forge"), "lightning", new EmiTexture(new Identifier(IceAndFire.MOD_ID, "textures/gui/dragonforge_lightning.png"), 3, 4, 170, 79), EmiStack.of(IafBlocks.DRAGONFORGE_LIGHTNING_CORE));

    @Override
    public void register(EmiRegistry registry) {
        FIRE.register(registry);
        ICE.register(registry);
        LIGHTNING.register(registry);
    }

//    private void addDescription(IRecipeRegistration registry, ItemStack itemStack) {
//        registry.addIngredientInfo(itemStack, VanillaTypes.ITEM_STACK, Text.translatable(itemStack.getTranslationKey() + ".jei_desc"));
//    }

//    @Override
//    public void registerRecipes(IRecipeRegistration registry) {
//
//        this.addDescription(registry, new ItemStack(IafItemRegistry.FIRE_DRAGON_BLOOD.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.ICE_DRAGON_BLOOD.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.LIGHTNING_DRAGON_BLOOD.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_RED.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_BRONZE.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_GRAY.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_GREEN.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_BLUE.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_WHITE.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_SAPPHIRE.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_SILVER.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_ELECTRIC.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_AMYTHEST.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_COPPER.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_BLACK.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGON_SKULL_FIRE.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGON_SKULL_ICE.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.DRAGON_SKULL_LIGHTNING.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.FIRE_STEW.get()));
//        this.addDescription(registry, new ItemStack(IafItemRegistry.FROST_STEW.get()));
//
//        for (EnumSkullType skull : EnumSkullType.values()) {
//            this.addDescription(registry, new ItemStack(skull.skull_item.get()));
//        }
//
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_FIRE.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_ICE.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_LIGHTNING.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_FIRE_HEAD.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_ICE_HEAD.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_LIGHTNING_HEAD.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_AMPHITHERE.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_BIRD.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_EYE.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_FAE.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_FEATHER.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_GORGON.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_HIPPOCAMPUS.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_HIPPOGRYPH_HEAD.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_MERMAID.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_SEA_SERPENT.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_TROLL.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_WEEZER.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//        registry.addIngredientInfo(IafItemRegistry.PATTERN_DREAD.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Text.translatable("item.iceandfire.custom_banner.jei_desc"));
//    }
}
