package com.iafenvoy.iceandfire.registry.tag;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import com.iafenvoy.iceandfire.registry.IafItems;
import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.ItemTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class IafItemTags extends ItemTagProvider {
    // Recipes
    public static final TagKey<Item> CHARRED_BLOCKS = createKey("charred_blocks");
    public static final TagKey<Item> FROZEN_BLOCKS = createKey("frozen_blocks");
    public static final TagKey<Item> CRACKLED_BLOCKS = createKey("crackled_blocks");
    public static final TagKey<Item> DRAGON_SKULLS = createKey("dragon_skulls");
    public static final TagKey<Item> MOB_SKULLS = createKey("mob_skulls");
    public static final TagKey<Item> SCALES_DRAGON_FIRE = createKey("scales/dragon/fire");
    public static final TagKey<Item> SCALES_DRAGON_ICE = createKey("scales/dragon/ice");
    public static final TagKey<Item> SCALES_DRAGON_LIGHTNING = createKey("scales/dragon/lightning");
    public static final TagKey<Item> SCALES_SEA_SERPENT = createKey("scales/sea_serpent");
    public static final TagKey<Item> DRAGON_FOOD_MEAT = createKey("dragon_food_meat");
    // Logic
    public static final TagKey<Item> MAKE_ITEM_DROPS_FIREIMMUNE = createKey("make_item_drops_fireimmune");
    public static final TagKey<Item> DRAGON_ARROWS = createKey("dragon_arrows");
    public static final TagKey<Item> DRAGON_BLOODS = createKey("dragon_bloods");
    public static final TagKey<Item> DRAGON_HEARTS = createKey("dragon_hearts");
    public static final TagKey<Item> BREED_AMPITHERE = createKey("breed_ampithere");
    public static final TagKey<Item> BREED_HIPPOCAMPUS = createKey("breed_hippocampus");
    public static final TagKey<Item> BREED_HIPPOGRYPH = createKey("breed_hippogryph");
    public static final TagKey<Item> HEAL_AMPITHERE = createKey("heal_ampithere");
    public static final TagKey<Item> HEAL_COCKATRICE = createKey("heal_cockatrice");
    public static final TagKey<Item> HEAL_HIPPOCAMPUS = createKey("heal_hippocampus");
    public static final TagKey<Item> HEAL_PIXIE = createKey("heal_pixie");
    public static final TagKey<Item> TAME_HIPPOGRYPH = createKey("tame_hippogryph");
    public static final TagKey<Item> TAME_PIXIE = createKey("tame_pixie");
    public static final TagKey<Item> TEMPT_DRAGON = createKey("tempt_dragon");
    public static final TagKey<Item> TEMPT_HIPPOCAMPUS = createKey("tempt_hippocampus");
    public static final TagKey<Item> TEMPT_HIPPOGRYPH = createKey("tempt_hippogryph");
    private final static String STORAGE_BLOCK_PATH = CommonTags.Items.STORAGE_BLOCKS.id().getPath();
    // Forge (+ Recipes)
    public static final TagKey<Item> STORAGE_BLOCKS_SCALES_DRAGON_FIRE = createForgeKey(STORAGE_BLOCK_PATH + "/scales/dragon/fire");
    public static final TagKey<Item> STORAGE_BLOCKS_SCALES_DRAGON_ICE = createForgeKey(STORAGE_BLOCK_PATH + "/scales/dragon/ice");
    public static final TagKey<Item> STORAGE_BLOCKS_SCALES_DRAGON_LIGHTNING = createForgeKey(STORAGE_BLOCK_PATH + "/scales/dragon/lightning");
    public static final TagKey<Item> STORAGE_BLOCKS_SILVER = createForgeKey(STORAGE_BLOCK_PATH + "/silver");
    private final static String INGOTS_PATH = CommonTags.Items.INGOTS.id().getPath();
    public static final TagKey<Item> INGOTS_SILVER = createForgeKey(INGOTS_PATH + "/silver");
    private final static String NUGGETS_PATH = CommonTags.Items.NUGGETS.id().getPath();
    public static final TagKey<Item> NUGGETS_COPPER = createForgeKey(NUGGETS_PATH + "/copper");
    public static final TagKey<Item> NUGGETS_SILVER = createForgeKey(NUGGETS_PATH + "/silver");
    private final static String BONES_PATH = CommonTags.Items.BONES.id().getPath();
    public static final TagKey<Item> BONES_WITHER = createForgeKey(BONES_PATH + "/wither");
    private final static String ORES_PATH = CommonTags.Items.ORES.id().getPath();
    private final static String GEMS = CommonTags.Items.GEMS.id().getPath();
    public static final TagKey<Item> GEMS_SAPPHIRE = createForgeKey(GEMS + "/sapphire");

    public IafItemTags(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
    }

    private static TagKey<Item> createKey(final String name) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier(IceAndFire.MOD_ID, name));
    }

    private static TagKey<Item> createForgeKey(final String name) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier("forge", name));
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup provider) {
        this.copy(IafBlockTags.CHARRED_BLOCKS, CHARRED_BLOCKS);
        this.copy(IafBlockTags.FROZEN_BLOCKS, FROZEN_BLOCKS);
        this.copy(IafBlockTags.CRACKLED_BLOCKS, CRACKLED_BLOCKS);

        this.getOrCreateTagBuilder(DRAGON_SKULLS)
                .add(IafItems.DRAGON_SKULL_FIRE)
                .add(IafItems.DRAGON_SKULL_ICE)
                .add(IafItems.DRAGON_SKULL_LIGHTNING);

        this.getOrCreateTagBuilder(MOB_SKULLS)
                .addTag(DRAGON_SKULLS);

        this.getOrCreateTagBuilder(MAKE_ITEM_DROPS_FIREIMMUNE)
                .add(IafItems.DRAGONSTEEL_LIGHTNING_SWORD)
                .add(IafItems.DRAGONBONE_SWORD_LIGHTNING)
                .add(IafItems.DRAGONSTEEL_LIGHTNING_PICKAXE)
                .add(IafItems.DRAGONSTEEL_LIGHTNING_AXE)
                .add(IafItems.DRAGONSTEEL_LIGHTNING_SHOVEL)
                .add(IafItems.DRAGONSTEEL_LIGHTNING_HOE);

        this.getOrCreateTagBuilder(DRAGON_ARROWS)
                .add(IafItems.DRAGONBONE_ARROW);

        this.getOrCreateTagBuilder(DRAGON_BLOODS)
                .add(IafItems.FIRE_DRAGON_BLOOD)
                .add(IafItems.ICE_DRAGON_BLOOD)
                .add(IafItems.LIGHTNING_DRAGON_BLOOD);

        this.getOrCreateTagBuilder(CommonTags.Items.INGOTS)
//                .add(IafItemRegistry.COPPER_INGOT)
                .add(IafItems.GHOST_INGOT)
                .add(IafItems.SILVER_INGOT)
                .add(IafItems.DRAGONSTEEL_ICE_INGOT)
                .add(IafItems.DRAGONSTEEL_FIRE_INGOT)
                .add(IafItems.DRAGONSTEEL_LIGHTNING_INGOT);

        this.getOrCreateTagBuilder(CommonTags.Items.NUGGETS)
                .add(IafItems.COPPER_NUGGET)
                .add(IafItems.SILVER_NUGGET);

        this.getOrCreateTagBuilder(CommonTags.Items.ORES)
                .add(IafBlocks.SILVER_ORE.asItem())
                .add(IafBlocks.DEEPSLATE_SILVER_ORE.asItem())
                .add(IafBlocks.SAPPHIRE_ORE.asItem());

        this.getOrCreateTagBuilder(CommonTags.Items.GEMS)
                .add(IafItems.SAPPHIRE_GEM);

        this.getOrCreateTagBuilder(CommonTags.Items.BONES)
                .add(IafItems.DRAGON_BONE)
                .add(IafItems.WITHERBONE);

        this.getOrCreateTagBuilder(CommonTags.Items.EGGS)
                .add(IafItems.HIPPOGRYPH_EGG)
                .add(IafItems.DEATHWORM_EGG)
                .add(IafItems.DEATHWORM_EGG_GIGANTIC)
                .add(IafItems.MYRMEX_DESERT_EGG)
                .add(IafItems.MYRMEX_JUNGLE_EGG);

        // Not sure if this should be in the forge namespace or not (or if the recipes should be using tags here)
        this.getOrCreateTagBuilder(STORAGE_BLOCKS_SCALES_DRAGON_FIRE)
                .add(IafBlocks.DRAGON_SCALE_RED.asItem())
                .add(IafBlocks.DRAGON_SCALE_GREEN.asItem())
                .add(IafBlocks.DRAGON_SCALE_BRONZE.asItem())
                .add(IafBlocks.DRAGON_SCALE_GRAY.asItem());

        this.getOrCreateTagBuilder(STORAGE_BLOCKS_SCALES_DRAGON_ICE)
                .add(IafBlocks.DRAGON_SCALE_BLUE.asItem())
                .add(IafBlocks.DRAGON_SCALE_WHITE.asItem())
                .add(IafBlocks.DRAGON_SCALE_SAPPHIRE.asItem())
                .add(IafBlocks.DRAGON_SCALE_SILVER.asItem());

        this.getOrCreateTagBuilder(STORAGE_BLOCKS_SCALES_DRAGON_LIGHTNING)
                .add(IafBlocks.DRAGON_SCALE_ELECTRIC.asItem())
                .add(IafBlocks.DRAGON_SCALE_AMYTHEST.asItem())
                .add(IafBlocks.DRAGON_SCALE_COPPER.asItem())
                .add(IafBlocks.DRAGON_SCALE_BLACK.asItem());
        //

        this.getOrCreateTagBuilder(CommonTags.Items.STORAGE_BLOCKS)
                .addTag(STORAGE_BLOCKS_SCALES_DRAGON_FIRE)
                .addTag(STORAGE_BLOCKS_SCALES_DRAGON_ICE)
                .addTag(STORAGE_BLOCKS_SCALES_DRAGON_LIGHTNING)
                .add(IafBlocks.DRAGONSTEEL_FIRE_BLOCK.asItem())
                .add(IafBlocks.DRAGONSTEEL_ICE_BLOCK.asItem())
                .add(IafBlocks.DRAGONSTEEL_LIGHTNING_BLOCK.asItem())
                .add(IafBlocks.SAPPHIRE_BLOCK.asItem())
                .add(IafBlocks.SILVER_BLOCK.asItem())
                .add(IafBlocks.RAW_SILVER_BLOCK.asItem())
                .add(IafBlocks.DRAGON_BONE_BLOCK.asItem());

        this.getOrCreateTagBuilder(DRAGON_FOOD_MEAT)
                // Vanilla
                .add(Items.BEEF, Items.COOKED_BEEF)
                .add(Items.CHICKEN, Items.COOKED_CHICKEN)
                .add(Items.MUTTON, Items.COOKED_MUTTON)
                .add(Items.PORKCHOP, Items.COOKED_PORKCHOP)
                // Farmer's Delight
//                .addOptionalTag(new ResourceLocation("forge", "raw_fishes"))
                .addOptionalTag(new Identifier("forge", "raw_mutton"))
                .addOptionalTag(new Identifier("forge", "raw_pork"))
                .addOptionalTag(new Identifier("forge", "raw_chicken"))
                .addOptionalTag(new Identifier("forge", "raw_beef"))
//                .addOptionalTag(new ResourceLocation("forge", "cooked_fishes"))
                .addOptionalTag(new Identifier("forge", "cooked_mutton"))
                .addOptionalTag(new Identifier("forge", "cooked_pork"))
                .addOptionalTag(new Identifier("forge", "cooked_chicken"))
                .addOptionalTag(new Identifier("forge", "cooked_beef"));


        this.getOrCreateTagBuilder(BREED_AMPITHERE)
                .add(Items.COOKIE);

        this.getOrCreateTagBuilder(BREED_HIPPOCAMPUS)
                .add(Items.PRISMARINE_CRYSTALS);

        this.getOrCreateTagBuilder(BREED_HIPPOGRYPH)
                .add(Items.RABBIT_STEW);

        this.getOrCreateTagBuilder(TAME_HIPPOGRYPH)
                .add(Items.RABBIT_FOOT);

        this.getOrCreateTagBuilder(HEAL_AMPITHERE)
                .add(Items.COCOA_BEANS);

        this.getOrCreateTagBuilder(HEAL_COCKATRICE)
                .addTag(CommonTags.Items.SEEDS)
                .add(Items.ROTTEN_FLESH);

        this.getOrCreateTagBuilder(HEAL_HIPPOCAMPUS)
                .add(Items.KELP);

        this.getOrCreateTagBuilder(HEAL_PIXIE)
                .add(Items.SUGAR);

        this.getOrCreateTagBuilder(TAME_PIXIE)
                .add(Items.CAKE);

        this.getOrCreateTagBuilder(TEMPT_DRAGON)
                .add(IafItems.FIRE_STEW);

        this.getOrCreateTagBuilder(TEMPT_HIPPOCAMPUS)
                .add(Items.KELP)
                .add(Items.PRISMARINE_CRYSTALS);

        this.getOrCreateTagBuilder(TEMPT_HIPPOGRYPH)
                .add(Items.RABBIT)
                .add(Items.COOKED_RABBIT);

        this.getOrCreateTagBuilder(SCALES_DRAGON_FIRE)
                .add(IafItems.DRAGONSCALES_RED)
                .add(IafItems.DRAGONSCALES_GREEN)
                .add(IafItems.DRAGONSCALES_BRONZE)
                .add(IafItems.DRAGONSCALES_GRAY);

        this.getOrCreateTagBuilder(SCALES_DRAGON_ICE)
                .add(IafItems.DRAGONSCALES_BLUE)
                .add(IafItems.DRAGONSCALES_WHITE)
                .add(IafItems.DRAGONSCALES_SAPPHIRE)
                .add(IafItems.DRAGONSCALES_SILVER);

        this.getOrCreateTagBuilder(SCALES_DRAGON_LIGHTNING)
                .add(IafItems.DRAGONSCALES_ELECTRIC)
                .add(IafItems.DRAGONSCALES_AMYTHEST)
                .add(IafItems.DRAGONSCALES_COPPER)
                .add(IafItems.DRAGONSCALES_BLACK);

        this.getOrCreateTagBuilder(createKey("scales/dragon"))
                .addTag(SCALES_DRAGON_FIRE)
                .addTag(SCALES_DRAGON_ICE)
                .addTag(SCALES_DRAGON_LIGHTNING);

        this.getOrCreateTagBuilder(DRAGON_HEARTS)
                .add(IafItems.FIRE_DRAGON_HEART)
                .add(IafItems.ICE_DRAGON_HEART)
                .add(IafItems.LIGHTNING_DRAGON_HEART);

//        IafItemRegistry.ITEMS.getEntries().forEach(registryObject -> {
//            Item item = registryObject;
//
//            if (item instanceof ItemSeaSerpentScales) {
//                tag(SCALES_SEA_SERPENT).add(item);
//            } else if (item instanceof ArrowItem) {
//                tag(ItemTags.ARROWS).add(item);
//            } else if (item instanceof SwordItem) {
//                tag(ItemTags.SWORDS).add(item);
//            } else if (item instanceof PickaxeItem) {
//                tag(ItemTags.PICKAXES).add(item);
//            } else if (item instanceof AxeItem) {
//                tag(ItemTags.AXES).add(item);
//            } else if (item instanceof ShovelItem) {
//                tag(ItemTags.SHOVELS).add(item);
//            } else if (item instanceof HoeItem) {
//                tag(ItemTags.HOES).add(item);
//            } else if (item instanceof BowItem) {
//                tag(Tags.Items.TOOLS_BOWS).add(item);
//            } else if (item instanceof TridentItem) {
//                tag(Tags.Items.TOOLS_TRIDENTS).add(item);
//            } else if (item instanceof ArmorItem armorItem) {
//                tag(Tags.Items.ARMORS).add(item);
//
//                switch (armorItem.getType()) {
//                    case HELMET -> tag(Tags.Items.ARMORS_HELMETS).add(item);
//                    case CHESTPLATE -> tag(Tags.Items.ARMORS_CHESTPLATES).add(item);
//                    case LEGGINGS -> tag(Tags.Items.ARMORS_LEGGINGS).add(item);
//                    case BOOTS -> tag(Tags.Items.ARMORS_BOOTS).add(item);
//                }
//            } else if (item instanceof ItemMobSkull) {
//                tag(MOB_SKULLS).add(item);
//            }
//
//            if (item instanceof TieredItem || item instanceof BowItem || item instanceof TridentItem) {
//                tag(Tags.Items.TOOLS).add(item);
//
//                if (item instanceof TridentItem) {
//                    tag(ItemTags.TOOLS).add(item);
//                }
//            }
//        });

        // Might be used by other mods
        this.getOrCreateTagBuilder(createForgeKey(ORES_PATH + "/silver")).add(IafBlocks.SILVER_ORE.asItem());
        this.getOrCreateTagBuilder(createForgeKey(ORES_PATH + "/silver")).add(IafBlocks.DEEPSLATE_SILVER_ORE.asItem());
        this.getOrCreateTagBuilder(INGOTS_SILVER).add(IafItems.SILVER_INGOT.asItem());
        this.getOrCreateTagBuilder(NUGGETS_COPPER).add(IafItems.COPPER_NUGGET);
        this.getOrCreateTagBuilder(NUGGETS_SILVER).add(IafItems.SILVER_NUGGET);
        this.getOrCreateTagBuilder(createForgeKey("raw_materials/silver")).add(IafItems.RAW_SILVER);
        this.getOrCreateTagBuilder(GEMS_SAPPHIRE).add(IafItems.SAPPHIRE_GEM);
        this.getOrCreateTagBuilder(STORAGE_BLOCKS_SILVER).add(IafBlocks.SILVER_BLOCK.asItem());
        this.getOrCreateTagBuilder(createForgeKey(STORAGE_BLOCK_PATH + "/raw_silver")).add(IafBlocks.RAW_SILVER_BLOCK.asItem());
        this.getOrCreateTagBuilder(createForgeKey(STORAGE_BLOCK_PATH + "/sapphire")).add(IafBlocks.SAPPHIRE_BLOCK.asItem());
        this.getOrCreateTagBuilder(BONES_WITHER).add(IafItems.WITHERBONE);
    }

    @Override
    public String getName() {
        return "Ice and Fire Item Tags";
    }
}
