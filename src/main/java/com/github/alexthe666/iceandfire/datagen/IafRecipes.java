package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemDragonArmor;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Generates recipes without advancements
 */
public class IafRecipes extends RecipeProvider {

    public IafRecipes(DataOutput output) {
        super(output);
    }

    private static Identifier location(final String path) {
        return new Identifier(IceAndFire.MOD_ID, path);
    }

    private static String locationString(final String path) {
        return IceAndFire.MOD_ID + ":" + path;
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> consumer) {
        this.createShaped(consumer);
        this.createShapeless(consumer);
    }

    private void createShaped(@NotNull final Consumer<RecipeJsonProvider> consumer) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, IafItemRegistry.AMPHITHERE_ARROW.get(), 4)
                .pattern("X")
                .pattern("#")
                .pattern("Y")
                .input('#', Tags.Items.RODS_WOODEN)
                .input('X', Items.FLINT)
                .input('Y', IafItemRegistry.AMPHITHERE_FEATHER.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.AMPHITHERE_FEATHER.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, IafItemRegistry.AMPHITHERE_MACUAHUITL.get())
                .pattern("OXO")
                .pattern("FXF")
                .pattern("OSO")
                .input('X', ItemTags.PLANKS)
                .input('S', Tags.Items.RODS_WOODEN)
                .input('O', Tags.Items.OBSIDIAN)
                .input('F', IafItemRegistry.AMPHITHERE_FEATHER.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.AMPHITHERE_FEATHER.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, Items.CHARCOAL)
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .input('B', IafBlockRegistry.ASH.get())
                .criterion("has_item", conditionsFromItem(IafBlockRegistry.ASH.get()))
                .offerTo(consumer, location("ash_to_charcoal"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, IafItemRegistry.BLINDFOLD.get())
                .pattern("SLS")
                .input('L', Tags.Items.LEATHER)
                .input('S', Tags.Items.STRING)
                .criterion("has_item", conditionsFromTag(Tags.Items.LEATHER))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, IafItemRegistry.CHAIN.get())
                .pattern("S")
                .pattern("S")
                .pattern("S")
                .input('S', Items.CHAIN)
                .criterion("has_item", conditionsFromItem(Items.CHAIN))
                .offerTo(consumer);

        // FIXME :: Currently uses `minecraft` namespace
        this.armorSet(consumer, Items.CHAIN,
                Items.CHAINMAIL_HELMET,
                Items.CHAINMAIL_CHESTPLATE,
                Items.CHAINMAIL_LEGGINGS,
                Items.CHAINMAIL_BOOTS
        );

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, IafItemRegistry.ITEM_COCKATRICE_SCEPTER.get())
                .pattern("S")
                .pattern("E")
                .pattern("W")
                .input('W', IafItemTags.BONES_WITHER)
                .input('S', IafItemRegistry.WITHER_SHARD.get())
                .input('E', IafItemRegistry.COCKATRICE_EYE.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.COCKATRICE_EYE.get()))
                .offerTo(consumer);

        this.armorSet(consumer, Tags.Items.INGOTS_COPPER,
                IafItemRegistry.COPPER_HELMET.get(),
                IafItemRegistry.COPPER_CHESTPLATE.get(),
                IafItemRegistry.COPPER_LEGGINGS.get(),
                IafItemRegistry.COPPER_BOOTS.get()
        );

        this.toolSet(consumer, Tags.Items.INGOTS_COPPER,
                IafItemRegistry.COPPER_SWORD.get(),
                IafItemRegistry.COPPER_PICKAXE.get(),
                IafItemRegistry.COPPER_AXE.get(),
                IafItemRegistry.COPPER_SHOVEL.get(),
                IafItemRegistry.COPPER_HOE.get()
        );

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, IafItemRegistry.DEATHWORM_GAUNTLET_RED.get())
                .pattern(" T ")
                .pattern("CHC")
                .pattern("CCC")
                .input('C', IafItemRegistry.DEATH_WORM_CHITIN_RED.get())
                .input('H', IafItemRegistry.CHAIN.get())
                .input('T', IafItemRegistry.DEATHWORM_TOUNGE.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.DEATHWORM_TOUNGE.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, IafItemRegistry.DEATHWORM_GAUNTLET_WHITE.get())
                .pattern(" T ")
                .pattern("CHC")
                .pattern("CCC")
                .input('C', IafItemRegistry.DEATH_WORM_CHITIN_WHITE.get())
                .input('H', IafItemRegistry.CHAIN.get())
                .input('T', IafItemRegistry.DEATHWORM_TOUNGE.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.DEATHWORM_TOUNGE.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, IafItemRegistry.DEATHWORM_GAUNTLET_YELLOW.get())
                .pattern(" T ")
                .pattern("CHC")
                .pattern("CCC")
                .input('C', IafItemRegistry.DEATH_WORM_CHITIN_YELLOW.get())
                .input('H', IafItemRegistry.CHAIN.get())
                .input('T', IafItemRegistry.DEATHWORM_TOUNGE.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.DEATHWORM_TOUNGE.get()))
                .offerTo(consumer);

        this.armorSet(consumer, IafItemRegistry.DEATH_WORM_CHITIN_RED.get(),
                IafItemRegistry.DEATHWORM_RED_HELMET.get(),
                IafItemRegistry.DEATHWORM_RED_CHESTPLATE.get(),
                IafItemRegistry.DEATHWORM_RED_LEGGINGS.get(),
                IafItemRegistry.DEATHWORM_RED_BOOTS.get()
        );

        this.armorSet(consumer, IafItemRegistry.DEATH_WORM_CHITIN_WHITE.get(),
                IafItemRegistry.DEATHWORM_WHITE_HELMET.get(),
                IafItemRegistry.DEATHWORM_WHITE_CHESTPLATE.get(),
                IafItemRegistry.DEATHWORM_WHITE_LEGGINGS.get(),
                IafItemRegistry.DEATHWORM_WHITE_BOOTS.get()
        );

        this.armorSet(consumer, IafItemRegistry.DEATH_WORM_CHITIN_YELLOW.get(),
                IafItemRegistry.DEATHWORM_YELLOW_HELMET.get(),
                IafItemRegistry.DEATHWORM_YELLOW_CHESTPLATE.get(),
                IafItemRegistry.DEATHWORM_YELLOW_LEGGINGS.get(),
                IafItemRegistry.DEATHWORM_YELLOW_BOOTS.get()
        );

        this.dragonArmorSet(consumer, Tags.Items.STORAGE_BLOCKS_COPPER,
                IafItemRegistry.DRAGONARMOR_COPPER_0.get(),
                IafItemRegistry.DRAGONARMOR_COPPER_1.get(),
                IafItemRegistry.DRAGONARMOR_COPPER_2.get(),
                IafItemRegistry.DRAGONARMOR_COPPER_3.get()
        );

        this.dragonArmorSet(consumer, Tags.Items.STORAGE_BLOCKS_IRON,
                IafItemRegistry.DRAGONARMOR_IRON_0.get(),
                IafItemRegistry.DRAGONARMOR_IRON_1.get(),
                IafItemRegistry.DRAGONARMOR_IRON_2.get(),
                IafItemRegistry.DRAGONARMOR_IRON_3.get()
        );

        this.dragonArmorSet(consumer, IafItemTags.STORAGE_BLOCKS_SILVER,
                IafItemRegistry.DRAGONARMOR_SILVER_0.get(),
                IafItemRegistry.DRAGONARMOR_SILVER_1.get(),
                IafItemRegistry.DRAGONARMOR_SILVER_2.get(),
                IafItemRegistry.DRAGONARMOR_SILVER_3.get()
        );

        this.dragonArmorSet(consumer, Tags.Items.STORAGE_BLOCKS_DIAMOND,
                IafItemRegistry.DRAGONARMOR_DIAMOND_0.get(),
                IafItemRegistry.DRAGONARMOR_DIAMOND_1.get(),
                IafItemRegistry.DRAGONARMOR_DIAMOND_2.get(),
                IafItemRegistry.DRAGONARMOR_DIAMOND_3.get()
        );

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, IafItemRegistry.IRON_HIPPOGRYPH_ARMOR.get())
                .pattern("FDF")
                .input('F', Tags.Items.FEATHERS)
                .input('D', Items.IRON_HORSE_ARMOR)
                .criterion("has_item", conditionsFromItem(Items.IRON_HORSE_ARMOR))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, IafItemRegistry.GOLD_HIPPOGRYPH_ARMOR.get())
                .pattern("FDF")
                .input('F', Tags.Items.FEATHERS)
                .input('D', Items.GOLDEN_HORSE_ARMOR)
                .criterion("has_item", conditionsFromItem(Items.GOLDEN_HORSE_ARMOR))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, IafItemRegistry.DIAMOND_HIPPOGRYPH_ARMOR.get())
                .pattern("FDF")
                .input('F', Tags.Items.FEATHERS)
                .input('D', Items.DIAMOND_HORSE_ARMOR)
                .criterion("has_item", conditionsFromItem(Items.DIAMOND_HORSE_ARMOR))
                .offerTo(consumer);

        offerReversibleCompactingRecipes(consumer, RecipeCategory.MISC, IafItemRegistry.DRAGON_BONE.get(), RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DRAGON_BONE_BLOCK.get()
                , locationString("dragon_bone_block"), null
                , locationString("dragonbone"), null);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, IafBlockRegistry.DRAGON_BONE_BLOCK_WALL.get())
                .pattern("BBB")
                .pattern("BBB")
                .input('B', IafItemRegistry.DRAGON_BONE.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.DRAGON_BONE.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, IafItemRegistry.DRAGON_FLUTE.get())
                .pattern("B  ")
                .pattern(" B ")
                .pattern("  I")
                .input('I', Tags.Items.INGOTS_IRON)
                .input('B', IafItemRegistry.DRAGON_BONE.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.DRAGON_BONE.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, IafItemRegistry.DRAGON_HORN.get())
                .pattern("  B")
                .pattern(" BB")
                .pattern("IB ")
                .input('I', Tags.Items.RODS_WOODEN)
                .input('B', IafItemRegistry.DRAGON_BONE.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.DRAGON_BONE.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, IafBlockRegistry.DRAGON_ICE_SPIKES.get(), 4)
                .pattern("I I")
                .pattern("I I")
                .input('I', IafBlockRegistry.DRAGON_ICE.get())
                .criterion("has_item", conditionsFromItem(IafBlockRegistry.DRAGON_ICE.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, IafBlockRegistry.NEST.get(), 8)
                .pattern("HHH")
                .pattern("HBH")
                .pattern("HHH")
                .input('H', Blocks.HAY_BLOCK)
                .input('B', IafItemRegistry.DRAGON_BONE.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.DRAGON_BONE.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, IafItemRegistry.DRAGON_STAFF.get())
                .pattern("S")
                .pattern("T")
                .pattern("T")
                .input('T', Tags.Items.RODS_WOODEN)
                .input('S', IafItemTags.DRAGON_SKULLS)
                .criterion("has_item", conditionsFromTag(IafItemTags.DRAGON_SKULLS))
                .offerTo(consumer);

        this.toolSet(consumer, IafItemRegistry.DRAGON_BONE.get(), IafItemTags.BONES_WITHER,
                IafItemRegistry.DRAGONBONE_SWORD.get(),
                IafItemRegistry.DRAGONBONE_PICKAXE.get(),
                IafItemRegistry.DRAGONBONE_AXE.get(),
                IafItemRegistry.DRAGONBONE_SHOVEL.get(),
                IafItemRegistry.DRAGONBONE_HOE.get()
        );

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, IafItemRegistry.DRAGON_BOW.get())
                .pattern(" DS")
                .pattern("W S")
                .pattern(" DS")
                .input('S', Tags.Items.STRING)
                .input('W', IafItemTags.BONES_WITHER)
                .input('D', IafItemRegistry.DRAGON_BONE.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.DRAGON_BONE.get()))
                .offerTo(consumer);

        this.forgeBrick(consumer, IafItemTags.STORAGE_BLOCKS_SCALES_DRAGON_FIRE, IafBlockRegistry.DRAGONFORGE_FIRE_BRICK.get());
        this.forgeCore(consumer, IafBlockRegistry.DRAGONFORGE_FIRE_BRICK.get(), IafItemRegistry.FIRE_DRAGON_HEART.get(), IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get());
        this.forgeInput(consumer, IafBlockRegistry.DRAGONFORGE_FIRE_BRICK.get(), IafBlockRegistry.DRAGONFORGE_FIRE_INPUT.get());

        this.forgeBrick(consumer, IafItemTags.STORAGE_BLOCKS_SCALES_DRAGON_ICE, IafBlockRegistry.DRAGONFORGE_ICE_BRICK.get());
        this.forgeCore(consumer, IafBlockRegistry.DRAGONFORGE_ICE_BRICK.get(), IafItemRegistry.ICE_DRAGON_HEART.get(), IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.get());
        this.forgeInput(consumer, IafBlockRegistry.DRAGONFORGE_ICE_BRICK.get(), IafBlockRegistry.DRAGONFORGE_ICE_INPUT.get());

        this.forgeBrick(consumer, IafItemTags.STORAGE_BLOCKS_SCALES_DRAGON_LIGHTNING, IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK.get());
        this.forgeCore(consumer, IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK.get(), IafItemRegistry.LIGHTNING_DRAGON_HEART.get(), IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.get());
        this.forgeInput(consumer, IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK.get(), IafBlockRegistry.DRAGONFORGE_LIGHTNING_INPUT.get());

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, IafItemRegistry.DRAGON_MEAL.get())
                .pattern("BMB")
                .pattern("MBM")
                .pattern("BMB")
                .input('B', Tags.Items.BONES)
                .input('M', IafItemTags.DRAGON_FOOD_MEAT)
                .criterion("has_item", conditionsFromTag(IafItemTags.DRAGON_FOOD_MEAT))
                .offerTo(consumer);

        this.compact(consumer, IafItemRegistry.DRAGONSCALES_RED.get(), IafBlockRegistry.DRAGON_SCALE_RED.get());
        this.compact(consumer, IafItemRegistry.DRAGONSCALES_GREEN.get(), IafBlockRegistry.DRAGON_SCALE_GREEN.get());
        this.compact(consumer, IafItemRegistry.DRAGONSCALES_BRONZE.get(), IafBlockRegistry.DRAGON_SCALE_BRONZE.get());
        this.compact(consumer, IafItemRegistry.DRAGONSCALES_GRAY.get(), IafBlockRegistry.DRAGON_SCALE_GRAY.get());

        this.compact(consumer, IafItemRegistry.DRAGONSCALES_BLUE.get(), IafBlockRegistry.DRAGON_SCALE_BLUE.get());
        this.compact(consumer, IafItemRegistry.DRAGONSCALES_WHITE.get(), IafBlockRegistry.DRAGON_SCALE_WHITE.get());
        this.compact(consumer, IafItemRegistry.DRAGONSCALES_SAPPHIRE.get(), IafBlockRegistry.DRAGON_SCALE_SAPPHIRE.get());
        this.compact(consumer, IafItemRegistry.DRAGONSCALES_SILVER.get(), IafBlockRegistry.DRAGON_SCALE_SILVER.get());

        this.compact(consumer, IafItemRegistry.DRAGONSCALES_ELECTRIC.get(), IafBlockRegistry.DRAGON_SCALE_ELECTRIC.get());
        this.compact(consumer, IafItemRegistry.DRAGONSCALES_AMYTHEST.get(), IafBlockRegistry.DRAGON_SCALE_AMYTHEST.get());
        this.compact(consumer, IafItemRegistry.DRAGONSCALES_COPPER.get(), IafBlockRegistry.DRAGON_SCALE_COPPER.get());
        this.compact(consumer, IafItemRegistry.DRAGONSCALES_BLACK.get(), IafBlockRegistry.DRAGON_SCALE_BLACK.get());

        for (EnumDragonArmor type : EnumDragonArmor.values()) {
            this.armorSet(consumer, type.armorMaterial.getRepairIngredient(),
                    type.helmet.get(),
                    type.chestplate.get(),
                    type.leggings.get(),
                    type.boots.get()
            );
        }

        for (EnumSeaSerpent type : EnumSeaSerpent.values()) {
            this.armorSet(consumer, type.scale.get(),
                    type.helmet.get(),
                    type.chestplate.get(),
                    type.leggings.get(),
                    type.boots.get()
            );

            this.compact(consumer, type.scale.get(), type.scaleBlock.get());
        }

        this.compact(consumer, IafItemRegistry.DRAGONSTEEL_FIRE_INGOT.get(), IafBlockRegistry.DRAGONSTEEL_FIRE_BLOCK.get());

        this.toolSet(consumer, IafItemRegistry.DRAGONSTEEL_FIRE_INGOT.get(), IafItemTags.BONES_WITHER,
                IafItemRegistry.DRAGONSTEEL_FIRE_SWORD.get(),
                IafItemRegistry.DRAGONSTEEL_FIRE_PICKAXE.get(),
                IafItemRegistry.DRAGONSTEEL_FIRE_AXE.get(),
                IafItemRegistry.DRAGONSTEEL_FIRE_SHOVEL.get(),
                IafItemRegistry.DRAGONSTEEL_FIRE_HOE.get()
        );

        this.armorSet(consumer, IafItemRegistry.DRAGONSTEEL_FIRE_INGOT.get(),
                IafItemRegistry.DRAGONSTEEL_FIRE_HELMET.get(),
                IafItemRegistry.DRAGONSTEEL_FIRE_CHESTPLATE.get(),
                IafItemRegistry.DRAGONSTEEL_FIRE_LEGGINGS.get(),
                IafItemRegistry.DRAGONSTEEL_FIRE_BOOTS.get()
        );

        this.dragonArmorSet(consumer, IafBlockRegistry.DRAGONSTEEL_FIRE_BLOCK.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_FIRE_0.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_FIRE_1.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_FIRE_2.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_FIRE_3.get()
        );

        this.compact(consumer, IafItemRegistry.DRAGONSTEEL_ICE_INGOT.get(), IafBlockRegistry.DRAGONSTEEL_ICE_BLOCK.get());

        this.toolSet(consumer, IafItemRegistry.DRAGONSTEEL_ICE_INGOT.get(), IafItemTags.BONES_WITHER,
                IafItemRegistry.DRAGONSTEEL_ICE_SWORD.get(),
                IafItemRegistry.DRAGONSTEEL_ICE_PICKAXE.get(),
                IafItemRegistry.DRAGONSTEEL_ICE_AXE.get(),
                IafItemRegistry.DRAGONSTEEL_ICE_SHOVEL.get(),
                IafItemRegistry.DRAGONSTEEL_ICE_HOE.get()
        );

        this.armorSet(consumer, IafItemRegistry.DRAGONSTEEL_ICE_INGOT.get(),
                IafItemRegistry.DRAGONSTEEL_ICE_HELMET.get(),
                IafItemRegistry.DRAGONSTEEL_ICE_CHESTPLATE.get(),
                IafItemRegistry.DRAGONSTEEL_ICE_LEGGINGS.get(),
                IafItemRegistry.DRAGONSTEEL_ICE_BOOTS.get()
        );

        this.dragonArmorSet(consumer, IafBlockRegistry.DRAGONSTEEL_ICE_BLOCK.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_ICE_0.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_ICE_1.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_ICE_2.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_ICE_3.get()
        );

        this.compact(consumer, IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT.get(), IafBlockRegistry.DRAGONSTEEL_LIGHTNING_BLOCK.get());

        this.toolSet(consumer, IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT.get(), IafItemTags.BONES_WITHER,
                IafItemRegistry.DRAGONSTEEL_LIGHTNING_SWORD.get(),
                IafItemRegistry.DRAGONSTEEL_LIGHTNING_PICKAXE.get(),
                IafItemRegistry.DRAGONSTEEL_LIGHTNING_AXE.get(),
                IafItemRegistry.DRAGONSTEEL_LIGHTNING_SHOVEL.get(),
                IafItemRegistry.DRAGONSTEEL_LIGHTNING_HOE.get()
        );

        this.armorSet(consumer, IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT.get(),
                IafItemRegistry.DRAGONSTEEL_LIGHTNING_HELMET.get(),
                IafItemRegistry.DRAGONSTEEL_LIGHTNING_CHESTPLATE.get(),
                IafItemRegistry.DRAGONSTEEL_LIGHTNING_LEGGINGS.get(),
                IafItemRegistry.DRAGONSTEEL_LIGHTNING_BOOTS.get()
        );

        this.dragonArmorSet(consumer, IafBlockRegistry.DRAGONSTEEL_LIGHTNING_BLOCK.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_LIGHTNING_0.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_LIGHTNING_1.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_LIGHTNING_2.get(),
                IafItemRegistry.DRAGONARMOR_DRAGONSTEEL_LIGHTNING_3.get()
        );

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREAD_STONE.get(), 8)
                .pattern("DDD")
                .pattern("DSD")
                .pattern("DDD")
                .input('S', Tags.Items.STONE)
                .input('D', IafItemRegistry.DREAD_SHARD.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.DREAD_SHARD.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREAD_STONE_BRICKS.get(), 4)
                .pattern("DD")
                .pattern("DD")
                .input('D', IafBlockRegistry.DREAD_STONE.get())
                .criterion("has_item", conditionsFromItem(IafBlockRegistry.DREAD_STONE.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREAD_STONE_BRICKS_CHISELED.get())
                .pattern("D")
                .pattern("D")
                .input('D', IafBlockRegistry.DREAD_STONE_BRICKS_SLAB.get())
                .criterion("has_item", conditionsFromItem(IafBlockRegistry.DREAD_STONE_BRICKS_SLAB.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREAD_STONE_FACE.get(), 8)
                .pattern("DDD")
                .pattern("DSD")
                .pattern("DDD")
                .input('S', Items.SKELETON_SKULL)
                .input('D', IafBlockRegistry.DREAD_STONE_BRICKS.get())
                .criterion("has_item", conditionsFromItem(IafBlockRegistry.DREAD_STONE_BRICKS.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREAD_STONE_BRICKS_SLAB.get(), 6)
                .pattern("DDD")
                .input('D', IafBlockRegistry.DREAD_STONE_BRICKS.get())
                .criterion("has_item", conditionsFromItem(IafBlockRegistry.DREAD_STONE_BRICKS.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREAD_STONE_BRICKS_STAIRS.get(), 4)
                .pattern("D  ")
                .pattern("DD ")
                .pattern("DDD")
                .input('D', IafBlockRegistry.DREAD_STONE_BRICKS.get())
                .criterion("has_item", conditionsFromItem(IafBlockRegistry.DREAD_STONE_BRICKS.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREAD_STONE_TILE.get(), 8)
                .pattern("DDD")
                .pattern("D D")
                .pattern("DDD")
                .input('D', IafBlockRegistry.DREAD_STONE_BRICKS.get())
                .criterion("has_item", conditionsFromItem(IafBlockRegistry.DREAD_STONE_BRICKS.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREAD_TORCH.get(), 4)
                .pattern("D")
                .pattern("S")
                .input('S', Tags.Items.RODS_WOODEN)
                .input('D', IafItemRegistry.DREAD_SHARD.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.DREAD_SHARD.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, IafItemRegistry.EARPLUGS.get())
                .pattern("B B")
                .input('B', ItemTags.PLANKS)
                .criterion("has_item", conditionsFromTag(ItemTags.PLANKS))
                .offerTo(consumer);

        for (EnumTroll type : EnumTroll.values()) {
            this.armorSet(consumer, type.leather.get(),
                    type.chestplate.get(),
                    type.leggings.get(),
                    type.boots.get()
            );

            ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, type.helmet.get())
                    .pattern("TTT")
                    .pattern("U U")
                    .input('T', type.leather.get())
                    .input('U', IafItemRegistry.TROLL_TUSK.get())
                    .criterion("has_item", conditionsFromItem(IafItemRegistry.TROLL_TUSK.get()))
                    .offerTo(consumer);
        }

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, IafBlockRegistry.GHOST_CHEST.get())
                .pattern(" E ")
                .pattern("ECE")
                .pattern(" E ")
                .input('C', Tags.Items.RODS_WOODEN)
                .input('E', IafItemRegistry.ECTOPLASM.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.ECTOPLASM.get()))
                .offerTo(consumer);

        this.dragonArmorSet(consumer, Tags.Items.STORAGE_BLOCKS_GOLD,
                IafItemRegistry.DRAGONARMOR_GOLD_0.get(),
                IafItemRegistry.DRAGONARMOR_GOLD_1.get(),
                IafItemRegistry.DRAGONARMOR_GOLD_2.get(),
                IafItemRegistry.DRAGONARMOR_GOLD_3.get()
        );

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.GRAVEYARD_SOIL.get())
                .pattern(" E ")
                .pattern("ECE")
                .pattern(" E ")
                .input('C', Items.COARSE_DIRT)
                .input('E', IafItemRegistry.ECTOPLASM.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.ECTOPLASM.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, IafBlockRegistry.MYRMEX_DESERT_RESIN.get())
                .pattern("RR")
                .pattern("RR")
                .input('R', IafItemRegistry.MYRMEX_DESERT_RESIN.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.MYRMEX_DESERT_RESIN.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, IafBlockRegistry.MYRMEX_JUNGLE_RESIN.get())
                .pattern("RR")
                .pattern("RR")
                .input('R', IafItemRegistry.MYRMEX_JUNGLE_RESIN.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.MYRMEX_JUNGLE_RESIN.get()))
                .offerTo(consumer);

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, IafItemRegistry.SEA_SERPENT_ARROW.get(), 4)
                .pattern("X")
                .pattern("#")
                .pattern("Y")
                .input('#', Tags.Items.RODS_WOODEN)
                .input('X', IafItemRegistry.SERPENT_FANG.get())
                .input('Y', IafItemTags.SCALES_SEA_SERPENT)
                .criterion("has_item", conditionsFromItem(IafItemRegistry.SERPENT_FANG.get()))
                .offerTo(consumer);

        this.armorSet(consumer, IafItemRegistry.MYRMEX_DESERT_CHITIN.get(),
                IafItemRegistry.MYRMEX_DESERT_HELMET.get(),
                IafItemRegistry.MYRMEX_DESERT_CHESTPLATE.get(),
                IafItemRegistry.MYRMEX_DESERT_LEGGINGS.get(),
                IafItemRegistry.MYRMEX_DESERT_BOOTS.get()
        );

        this.toolSet(consumer, IafItemRegistry.MYRMEX_DESERT_CHITIN.get(), IafItemTags.BONES_WITHER,
                IafItemRegistry.MYRMEX_DESERT_SWORD.get(),
                IafItemRegistry.MYRMEX_DESERT_PICKAXE.get(),
                IafItemRegistry.MYRMEX_DESERT_AXE.get(),
                IafItemRegistry.MYRMEX_DESERT_SHOVEL.get(),
                IafItemRegistry.MYRMEX_DESERT_HOE.get()
        );

        this.armorSet(consumer, IafItemRegistry.MYRMEX_JUNGLE_CHITIN.get(),
                IafItemRegistry.MYRMEX_JUNGLE_HELMET.get(),
                IafItemRegistry.MYRMEX_JUNGLE_CHESTPLATE.get(),
                IafItemRegistry.MYRMEX_JUNGLE_LEGGINGS.get(),
                IafItemRegistry.MYRMEX_JUNGLE_BOOTS.get()
        );

        this.toolSet(consumer, IafItemRegistry.MYRMEX_JUNGLE_CHITIN.get(), IafItemTags.BONES_WITHER,
                IafItemRegistry.MYRMEX_JUNGLE_SWORD.get(),
                IafItemRegistry.MYRMEX_JUNGLE_PICKAXE.get(),
                IafItemRegistry.MYRMEX_JUNGLE_AXE.get(),
                IafItemRegistry.MYRMEX_JUNGLE_SHOVEL.get(),
                IafItemRegistry.MYRMEX_JUNGLE_HOE.get()
        );

        CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(IafItemRegistry.RAW_SILVER.get()), RecipeCategory.TOOLS, IafItemRegistry.SILVER_INGOT.get(), 0.7f, 200)
                .group("raw_silver")
                .criterion(hasItem(IafItemRegistry.RAW_SILVER.get()), conditionsFromItem(IafItemRegistry.RAW_SILVER.get())).offerTo(consumer, location(getItemPath(IafItemRegistry.SILVER_INGOT.get())) + "_from_smelting_" + getItemPath(IafItemRegistry.RAW_SILVER.get()));
        CookingRecipeJsonBuilder.createBlasting(Ingredient.ofItems(IafItemRegistry.RAW_SILVER.get()), RecipeCategory.TOOLS, IafItemRegistry.SILVER_INGOT.get(), 0.7f, 100)
                .group("raw_silver")
                .criterion(hasItem(IafItemRegistry.RAW_SILVER.get()), conditionsFromItem(IafItemRegistry.RAW_SILVER.get())).offerTo(consumer, location(getItemPath(IafItemRegistry.SILVER_INGOT.get())) + "_from_blasting_" + getItemPath(IafItemRegistry.RAW_SILVER.get()));
        this.compact(consumer, IafItemRegistry.SILVER_INGOT.get(), IafBlockRegistry.SILVER_BLOCK.get());
        this.compact(consumer, IafItemRegistry.RAW_SILVER.get(), IafBlockRegistry.RAW_SILVER_BLOCK.get());
        this.compact(consumer, IafItemRegistry.SILVER_NUGGET.get(), IafItemRegistry.SILVER_INGOT.get());

        this.armorSet(consumer, IafItemTags.INGOTS_SILVER,
                IafItemRegistry.SILVER_HELMET.get(),
                IafItemRegistry.SILVER_CHESTPLATE.get(),
                IafItemRegistry.SILVER_LEGGINGS.get(),
                IafItemRegistry.SILVER_BOOTS.get()
        );

        this.toolSet(consumer, IafItemTags.INGOTS_SILVER,
                IafItemRegistry.SILVER_SWORD.get(),
                IafItemRegistry.SILVER_PICKAXE.get(),
                IafItemRegistry.SILVER_AXE.get(),
                IafItemRegistry.SILVER_SHOVEL.get(),
                IafItemRegistry.SILVER_HOE.get()
        );

        this.compact(consumer, IafItemRegistry.SAPPHIRE_GEM.get(), IafBlockRegistry.SAPPHIRE_BLOCK.get());

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, IafItemRegistry.TIDE_TRIDENT.get())
                .pattern("TTT")
                .pattern("SDS")
                .pattern(" B ")
                .input('D', Tags.Items.GEMS_DIAMOND)
                .input('S', IafItemTags.SCALES_SEA_SERPENT)
                .input('T', IafItemRegistry.SERPENT_FANG.get())
                .input('B', IafItemRegistry.DRAGON_BONE.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.SERPENT_FANG.get()))
                .offerTo(consumer);
    }

    private void createShapeless(@NotNull final Consumer<RecipeJsonProvider> consumer) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, IafItemRegistry.AMBROSIA.get())
                .input(IafItemRegistry.PIXIE_DUST.get())
                .input(Items.BOWL)
                .criterion("has_item", conditionsFromItem(IafItemRegistry.PIXIE_DUST.get()))
                .offerTo(consumer);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.ASH.get())
                .input(Ingredient.fromTag(IafItemTags.CHARRED_BLOCKS), 9)
                .criterion("has_item", conditionsFromTag(IafItemTags.CHARRED_BLOCKS))
                .offerTo(consumer);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, IafItemRegistry.BESTIARY.get())
                .input(IafItemRegistry.MANUSCRIPT.get(), 3)
                .criterion("has_item", conditionsFromItem(IafItemRegistry.MANUSCRIPT.get()))
                .offerTo(consumer);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, IafItemRegistry.CHAIN_STICKY.get())
                .input(Tags.Items.SLIMEBALLS)
                .input(IafItemRegistry.CHAIN.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.CHAIN.get()))
                .offerTo(consumer);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.COPPER_INGOT)
                .input(Ingredient.fromTag(IafItemTags.NUGGETS_COPPER), 9)
                .criterion("has_item", conditionsFromTag(IafItemTags.NUGGETS_COPPER))
                .offerTo(consumer, location("copper_nuggets_to_ingot"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, IafItemRegistry.COPPER_NUGGET.get(), 9)
                .input(Tags.Items.INGOTS_COPPER)
                .criterion("has_item", conditionsFromTag(Tags.Items.INGOTS_COPPER))
                .offerTo(consumer, location("copper_ingot_to_nuggets"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, IafBlockRegistry.COPPER_PILE.get())
                .input(Ingredient.fromTag(IafItemTags.NUGGETS_COPPER), 2)
                .criterion("has_item", conditionsFromTag(IafItemTags.NUGGETS_COPPER))
                .offerTo(consumer);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, IafBlockRegistry.DRAGON_ICE.get())
                .input(Ingredient.fromTag(IafItemTags.FROZEN_BLOCKS), 9)
                .criterion("has_item", conditionsFromTag(IafItemTags.FROZEN_BLOCKS))
                .offerTo(consumer);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.BONE_MEAL, 5)
                .input(IafItemTags.MOB_SKULLS)
                .criterion("has_item", conditionsFromTag(IafItemTags.MOB_SKULLS))
                .offerTo(consumer, location("skull_to_bone_meal"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, IafItemRegistry.DRAGONBONE_ARROW.get(), 5)
                .input(IafItemRegistry.DRAGON_BONE.get())
                .input(IafItemRegistry.WITHER_SHARD.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.WITHER_SHARD.get()))
                .offerTo(consumer);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREAD_STONE_BRICKS_MOSSY.get())
                .input(Items.VINE)
                .input(IafBlockRegistry.DREAD_STONE_BRICKS.get())
                .criterion("has_item", conditionsFromItem(IafBlockRegistry.DREAD_STONE_BRICKS.get()))
                .offerTo(consumer);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, IafBlockRegistry.DREADWOOD_PLANKS.get(), 4)
                .input(IafBlockRegistry.DREADWOOD_LOG.get())
                .criterion("has_item", conditionsFromItem(IafBlockRegistry.DREADWOOD_LOG.get()))
                .offerTo(consumer);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, IafItemRegistry.FIRE_STEW.get())
                .input(Items.BOWL)
                .input(Items.BLAZE_ROD)
                .input(IafBlockRegistry.FIRE_LILY.get())
                .criterion("has_item", conditionsFromItem(IafBlockRegistry.FIRE_LILY.get()))
                .offerTo(consumer);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, IafItemRegistry.FROST_STEW.get())
                .input(Items.BOWL)
                .input(Items.PRISMARINE_CRYSTALS)
                .input(IafBlockRegistry.FROST_LILY.get())
                .criterion("has_item", conditionsFromItem(IafBlockRegistry.FROST_LILY.get()))
                .offerTo(consumer);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, IafBlockRegistry.GOLD_PILE.get())
                .input(Ingredient.fromTag(Tags.Items.NUGGETS_GOLD), 2)
                .criterion("has_item", conditionsFromTag(Tags.Items.NUGGETS_GOLD))
                .offerTo(consumer);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Items.GRAVEL)
                .input(Ingredient.fromTag(IafItemTags.CRACKLED_BLOCKS), 9)
                .criterion("has_item", conditionsFromTag(IafItemTags.CRACKLED_BLOCKS))
                .offerTo(consumer, location("crackled_to_gravel"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, IafItemRegistry.DRAGONBONE_SWORD_FIRE.get())
                .input(IafItemRegistry.DRAGONBONE_SWORD.get())
                .input(IafItemRegistry.FIRE_DRAGON_BLOOD.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.FIRE_DRAGON_BLOOD.get()))
                .offerTo(consumer, location("dragonbone_sword_fire"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, IafItemRegistry.DRAGONBONE_SWORD_ICE.get())
                .input(IafItemRegistry.DRAGONBONE_SWORD.get())
                .input(IafItemRegistry.ICE_DRAGON_BLOOD.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.ICE_DRAGON_BLOOD.get()))
                .offerTo(consumer, location("dragonbone_sword_ice"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, IafItemRegistry.DRAGONBONE_SWORD_LIGHTNING.get())
                .input(IafItemRegistry.DRAGONBONE_SWORD.get())
                .input(IafItemRegistry.LIGHTNING_DRAGON_BLOOD.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.LIGHTNING_DRAGON_BLOOD.get()))
                .offerTo(consumer, location("dragonbone_sword_lightning"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.COMBAT, IafItemRegistry.GHOST_SWORD.get())
                .input(IafItemRegistry.DRAGONBONE_SWORD.get())
                .input(IafItemRegistry.GHOST_INGOT.get())
                .criterion("has_item", conditionsFromItem(IafItemRegistry.GHOST_INGOT.get()))
                .offerTo(consumer, location("ghost_sword"));
    }

    private void compact(@NotNull final Consumer<RecipeJsonProvider> consumer, final ItemConvertible unpacked, final ItemConvertible packed) {
        String packedPath = Registries.ITEM.getId(packed.asItem()).getPath();
        String unpackedPath = Registries.ITEM.getId(unpacked.asItem()).getPath();


        offerReversibleCompactingRecipes(consumer, RecipeCategory.MISC, unpacked, RecipeCategory.BUILDING_BLOCKS, packed
                , locationString(unpackedPath + "_to_" + packedPath), null
                , locationString(packedPath + "_to_" + unpackedPath), null);
    }

    private void toolSet(@NotNull final Consumer<RecipeJsonProvider> consumer, final TagKey<Item> material, final ItemConvertible... items) {
        this.toolSet(consumer, Ingredient.fromTag(material), Ingredient.fromTag(Tags.Items.RODS_WOODEN), items);
    }

    private void toolSet(@NotNull final Consumer<RecipeJsonProvider> consumer, final ItemConvertible material, final TagKey<Item> handle, final ItemConvertible... items) {
        this.toolSet(consumer, Ingredient.ofItems(material), Ingredient.fromTag(handle), items);
    }

    private void toolSet(@NotNull final Consumer<RecipeJsonProvider> consumer, final TagKey<Item> material, final ItemConvertible handle, final ItemConvertible... items) {
        this.toolSet(consumer, Ingredient.fromTag(material), Ingredient.ofItems(handle), items);
    }

    private void toolSet(@NotNull final Consumer<RecipeJsonProvider> consumer, final ItemConvertible material, final ItemConvertible handle, final ItemConvertible... items) {
        this.toolSet(consumer, Ingredient.ofItems(material), Ingredient.ofItems(handle), items);
    }

    private void toolSet(@NotNull final Consumer<RecipeJsonProvider> consumer, final Ingredient material, final Ingredient handle, final ItemConvertible... results) {
        for (ItemConvertible result : results) {
            Item item = result.asItem();

            if (item instanceof SwordItem) {
                this.sword(consumer, material, handle, result);
            } else if (item instanceof PickaxeItem) {
                this.pickaxe(consumer, material, handle, result);
            } else if (item instanceof AxeItem) {
                this.axe(consumer, material, handle, result);
            } else if (item instanceof ShovelItem) {
                this.shovel(consumer, material, handle, result);
            } else if (item instanceof HoeItem) {
                this.hoe(consumer, material, handle, result);
            } else {
                throw new IllegalArgumentException("Result is not a valid tool: [" + result + "]");
            }
        }
    }

    private void armorSet(@NotNull final Consumer<RecipeJsonProvider> consumer, final TagKey<Item> tag, final ItemConvertible... results) {
        this.armorSet(consumer, Ingredient.fromTag(tag), results);
    }

    private void armorSet(@NotNull final Consumer<RecipeJsonProvider> consumer, final ItemConvertible item, final ItemConvertible... results) {
        this.armorSet(consumer, Ingredient.ofItems(item), results);
    }

    private void armorSet(@NotNull final Consumer<RecipeJsonProvider> consumer, final Ingredient ingredient, final ItemConvertible... results) {
        for (ItemConvertible result : results) {
            if (result.asItem() instanceof ArmorItem armorItem) {
                switch (armorItem.getType()) {
                    case HELMET -> this.helmet(consumer, ingredient, result);
                    case CHESTPLATE -> this.chestPlate(consumer, ingredient, result);
                    case LEGGINGS -> this.leggings(consumer, ingredient, result);
                    case BOOTS -> this.boots(consumer, ingredient, result);
                    default -> throw new IllegalArgumentException("Result is not a valid armor item: [" + result + "]");
                }
            } else {
                throw new IllegalArgumentException("Result is not an armor item: [" + result + "]");
            }
        }
    }

    private void helmet(@NotNull final Consumer<RecipeJsonProvider> consumer, final Ingredient ingredient, final ItemConvertible result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .pattern("###")
                .pattern("# #")
                .input('#', ingredient)
                .criterion("has_item", conditionsFromItem(Arrays.stream(ingredient.getMatchingStacks()).findFirst().get().getItem()))
                .offerTo(consumer);
    }

    private void chestPlate(@NotNull final Consumer<RecipeJsonProvider> consumer, final Ingredient ingredient, final ItemConvertible result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .input('#', ingredient)
                .criterion("has_item", conditionsFromItem(Arrays.stream(ingredient.getMatchingStacks()).findFirst().get().getItem()))
                .offerTo(consumer);
    }

    private void leggings(@NotNull final Consumer<RecipeJsonProvider> consumer, final Ingredient ingredient, final ItemConvertible result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .input('#', ingredient)
                .criterion("has_item", conditionsFromItem(Arrays.stream(ingredient.getMatchingStacks()).findFirst().get().getItem()))
                .offerTo(consumer);
    }

    private void boots(@NotNull final Consumer<RecipeJsonProvider> consumer, final Ingredient ingredient, final ItemConvertible result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .pattern("# #")
                .pattern("# #")
                .input('#', ingredient)
                .criterion("has_item", conditionsFromItem(Arrays.stream(ingredient.getMatchingStacks()).findFirst().get().getItem()))
                .offerTo(consumer);
    }

    private void sword(@NotNull final Consumer<RecipeJsonProvider> consumer, final Ingredient material, final Ingredient handle, final ItemConvertible result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, result)
                .pattern("M")
                .pattern("M")
                .pattern("H")
                .input('M', material)
                .input('H', handle)
                .criterion("has_item", conditionsFromItem(Arrays.stream(material.getMatchingStacks()).findFirst().get().getItem()))
                .offerTo(consumer);
    }

    private void pickaxe(@NotNull final Consumer<RecipeJsonProvider> consumer, final Ingredient material, final Ingredient handle, final ItemConvertible result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, result)
                .pattern("MMM")
                .pattern(" H ")
                .pattern(" H ")
                .input('M', material)
                .input('H', handle)
                .criterion("has_item", conditionsFromItem(Arrays.stream(material.getMatchingStacks()).findFirst().get().getItem()))
                .offerTo(consumer);
    }

    private void axe(@NotNull final Consumer<RecipeJsonProvider> consumer, final Ingredient material, final Ingredient handle, final ItemConvertible result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, result)
                .pattern("MM")
                .pattern("MH")
                .pattern(" H")
                .input('M', material)
                .input('H', handle)
                .criterion("has_item", conditionsFromItem(Arrays.stream(material.getMatchingStacks()).findFirst().get().getItem()))
                .offerTo(consumer);
    }

    private void shovel(@NotNull final Consumer<RecipeJsonProvider> consumer, final Ingredient material, final Ingredient handle, final ItemConvertible result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, result)
                .pattern("M")
                .pattern("H")
                .pattern("H")
                .input('M', material)
                .input('H', handle)
                .criterion("has_item", conditionsFromItem(Arrays.stream(material.getMatchingStacks()).findFirst().get().getItem()))
                .offerTo(consumer);
    }

    private void hoe(@NotNull final Consumer<RecipeJsonProvider> consumer, final Ingredient material, final Ingredient handle, final ItemConvertible result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, result)
                .pattern("MM")
                .pattern(" H")
                .pattern(" H")
                .input('M', material)
                .input('H', handle)
                .criterion("has_item", conditionsFromItem(Arrays.stream(material.getMatchingStacks()).findFirst().get().getItem()))
                .offerTo(consumer);
    }

    private void dragonArmorSet(@NotNull final Consumer<RecipeJsonProvider> consumer, final ItemConvertible material, final ItemConvertible... results) {
        this.dragonArmorSet(consumer, Ingredient.ofItems(material), results);
    }

    private void dragonArmorSet(@NotNull final Consumer<RecipeJsonProvider> consumer, final TagKey<Item> tag, final ItemConvertible... results) {
        this.dragonArmorSet(consumer, Ingredient.fromTag(tag), results);
    }

    private void dragonArmorSet(@NotNull final Consumer<RecipeJsonProvider> consumer, final Ingredient ingredient, final ItemConvertible... results) {
        for (ItemConvertible result : results) {
            if (result instanceof ItemDragonArmor dragonArmor) {
                switch (dragonArmor.dragonSlot) {
                    case 0 -> this.dragonHead(consumer, ingredient, result);
                    case 1 -> this.dragonNeck(consumer, ingredient, result);
                    case 2 -> this.dragonBody(consumer, ingredient, result);
                    case 3 -> this.dragonTail(consumer, ingredient, result);
                    default ->
                            throw new IllegalArgumentException("Result is not a valid dragon armor [" + result + "]");
                }
            } else {
                throw new IllegalArgumentException("Result is not a dragon armor [" + result + "]");
            }
        }
    }

    private void dragonHead(@NotNull final Consumer<RecipeJsonProvider> consumer, final Ingredient ingredient, final ItemConvertible result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .pattern("   ")
                .pattern(" ##")
                .pattern("###")
                .input('#', ingredient)
                .criterion("has_item", conditionsFromItem(Arrays.stream(ingredient.getMatchingStacks()).findFirst().get().getItem()))
                .offerTo(consumer);
    }

    private void dragonNeck(@NotNull final Consumer<RecipeJsonProvider> consumer, final Ingredient ingredient, final ItemConvertible result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .pattern("   ")
                .pattern("###")
                .pattern(" ##")
                .input('#', ingredient)
                .criterion("has_item", conditionsFromItem(Arrays.stream(ingredient.getMatchingStacks()).findFirst().get().getItem()))
                .offerTo(consumer);
    }

    private void dragonBody(@NotNull final Consumer<RecipeJsonProvider> consumer, final Ingredient ingredient, final ItemConvertible result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .pattern("###")
                .pattern("###")
                .pattern("# #")
                .input('#', ingredient)
                .criterion("has_item", conditionsFromItem(Arrays.stream(ingredient.getMatchingStacks()).findFirst().get().getItem()))
                .offerTo(consumer);
    }

    private void dragonTail(@NotNull final Consumer<RecipeJsonProvider> consumer, final Ingredient ingredient, final ItemConvertible result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, result)
                .pattern("   ")
                .pattern("  #")
                .pattern("## ")
                .input('#', ingredient)
                .criterion("has_item", conditionsFromItem(Arrays.stream(ingredient.getMatchingStacks()).findFirst().get().getItem()))
                .offerTo(consumer);
    }

    private void forgeBrick(@NotNull final Consumer<RecipeJsonProvider> consumer, final TagKey<Item> scales, final ItemConvertible result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, result, 4)
                .pattern("SBS")
                .pattern("BSB")
                .pattern("SBS")
                .input('S', Ingredient.fromTag(scales))
                .input('B', Items.STONE_BRICKS)
                .criterion("has_item", conditionsFromItem(((ItemConvertible) Items.STONE_BRICKS).asItem()))
                .offerTo(consumer);
    }

    private void forgeCore(@NotNull final Consumer<RecipeJsonProvider> consumer, final ItemConvertible brick, final ItemConvertible heart, final ItemConvertible result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, result)
                .pattern("BBB")
                .pattern("BHB")
                .pattern("BBB")
                .input('H', heart)
                .input('B', brick)
                .criterion("has_item", conditionsFromItem(brick.asItem()))
                .offerTo(consumer);
    }

    private void forgeInput(@NotNull final Consumer<RecipeJsonProvider> consumer, final ItemConvertible brick, final ItemConvertible result) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, result)
                .pattern("BIB")
                .pattern("I I")
                .pattern("BIB")
                .input('I', Ingredient.fromTag(Tags.Items.INGOTS_IRON))
                .input('B', brick)
                .criterion("has_item", conditionsFromItem(brick.asItem()))
                .offerTo(consumer);
    }

}
