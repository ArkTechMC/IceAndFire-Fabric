package com.github.alexthe666.iceandfire.datagen.tags;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.registry.IafBlocks;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import io.github.fabricators_of_create.porting_lib.tags.data.BlockTagProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class IafBlockTags extends BlockTagProvider {
    public static final TagKey<Block> CHARRED_BLOCKS = createKey("charred_blocks");
    public static final TagKey<Block> FROZEN_BLOCKS = createKey("frozen_blocks");
    public static final TagKey<Block> CRACKLED_BLOCKS = createKey("crackled_blocks");
    public static final TagKey<Block> DRAGON_ENVIRONMENT_BLOCKS = createKey("dragon_environment_blocks");

    public static final TagKey<Block> DRAGON_CAVE_RARE_ORES = createKey("dragon_cave_rare_ores");
    public static final TagKey<Block> DRAGON_CAVE_UNCOMMON_ORES = createKey("dragon_cave_uncommon_ores");
    public static final TagKey<Block> DRAGON_CAVE_COMMON_ORES = createKey("dragon_cave_common_ores");

    public static final TagKey<Block> FIRE_DRAGON_CAVE_ORES = createKey("fire_dragon_cave_ores");
    public static final TagKey<Block> ICE_DRAGON_CAVE_ORES = createKey("ice_dragon_cave_ores");
    public static final TagKey<Block> LIGHTNING_DRAGON_CAVE_ORES = createKey("lightning_dragon_cave_ores");

    public static final TagKey<Block> DRAGON_BLOCK_BREAK_BLACKLIST = createKey("dragon_block_break_blacklist");
    public static final TagKey<Block> DRAGON_BLOCK_BREAK_NO_DROPS = createKey("dragon_block_break_no_drops");

    public IafBlockTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> future, ExistingFileHelper helper) {
        super(output, future);
    }

    private static TagKey<Block> createKey(final String name) {
        return TagKey.of(RegistryKeys.BLOCK, new Identifier(IceAndFire.MOD_ID, name));
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup pProvider) {
        this.tag(CHARRED_BLOCKS)
                .add(IafBlocks.CHARRED_COBBLESTONE)
                .add(IafBlocks.CHARRED_DIRT)
                .add(IafBlocks.CHARRED_DIRT_PATH)
                .add(IafBlocks.CHARRED_GRASS)
                .add(IafBlocks.CHARRED_GRAVEL)
                .add(IafBlocks.CHARRED_STONE);

        this.tag(FROZEN_BLOCKS)
                .add(IafBlocks.FROZEN_COBBLESTONE)
                .add(IafBlocks.FROZEN_DIRT)
                .add(IafBlocks.FROZEN_DIRT_PATH)
                .add(IafBlocks.FROZEN_GRASS)
                .add(IafBlocks.FROZEN_GRAVEL)
                .add(IafBlocks.FROZEN_STONE)
                .add(IafBlocks.FROZEN_SPLINTERS);

        this.tag(CRACKLED_BLOCKS)
                .add(IafBlocks.CRACKLED_COBBLESTONE)
                .add(IafBlocks.CRACKLED_DIRT)
                .add(IafBlocks.CRACKLED_DIRT_PATH)
                .add(IafBlocks.CRACKLED_GRASS)
                .add(IafBlocks.CRACKLED_GRASS)
                .add(IafBlocks.CRACKLED_STONE);

        this.tag(DRAGON_ENVIRONMENT_BLOCKS)
                .addTag(CHARRED_BLOCKS)
                .addTag(FROZEN_BLOCKS)
                .addTag(CRACKLED_BLOCKS);

        this.tag(DRAGON_CAVE_RARE_ORES)
                .add(Blocks.DIAMOND_ORE)
                .add(Blocks.EMERALD_ORE);

        this.tag(DRAGON_CAVE_UNCOMMON_ORES)
                .add(Blocks.LAPIS_ORE)
                .add(Blocks.REDSTONE_ORE)
                .add(Blocks.GOLD_ORE)
                .add(IafBlocks.SILVER_ORE);

        this.tag(DRAGON_CAVE_COMMON_ORES)
                .add(Blocks.COAL_ORE)
                .add(Blocks.COPPER_ORE)
                .add(Blocks.IRON_ORE);

        this.tag(FIRE_DRAGON_CAVE_ORES)
                .add(Blocks.EMERALD_ORE);

        this.tag(ICE_DRAGON_CAVE_ORES)
                .add(IafBlocks.SAPPHIRE_ORE);

        this.tag(LIGHTNING_DRAGON_CAVE_ORES)
                .add(Blocks.BUDDING_AMETHYST);

        this.tag(DRAGON_BLOCK_BREAK_BLACKLIST)
                .addTag(Tags.Blocks.CHESTS)
                .add(Blocks.END_STONE)
                .add(Blocks.IRON_BARS);

        this.tag(DRAGON_BLOCK_BREAK_NO_DROPS)
                .addTag(BlockTags.DIRT)
                .addTag(Tags.Blocks.STONE)
                .addTag(Tags.Blocks.COBBLESTONE)
                .addTag(DRAGON_ENVIRONMENT_BLOCKS)
                .add(Blocks.GRASS_BLOCK);

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(IafBlocks.SILVER_ORE)
                .add(IafBlocks.DEEPSLATE_SILVER_ORE)
                .add(IafBlocks.SILVER_BLOCK)
                .add(IafBlocks.RAW_SILVER_BLOCK);

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(IafBlocks.SAPPHIRE_ORE)
                .add(IafBlocks.SAPPHIRE_BLOCK);

        this.tag(Tags.Blocks.ORES)
                .add(IafBlocks.SILVER_ORE)
                .add(IafBlocks.DEEPSLATE_SILVER_ORE)
                .add(IafBlocks.SAPPHIRE_ORE);

        this.tag(Tags.Blocks.ORES_IN_GROUND_STONE)
                .add(IafBlocks.SILVER_ORE)
                .add(IafBlocks.SAPPHIRE_ORE);

        this.tag(Tags.Blocks.ORE_BEARING_GROUND_DEEPSLATE)
                .add(IafBlocks.DEEPSLATE_SILVER_ORE);

        // These are also used / created by other mods
        this.tag(TagKey.of(RegistryKeys.BLOCK, new Identifier("forge", "ores/silver"))).add(IafBlocks.SILVER_ORE);
        this.tag(TagKey.of(RegistryKeys.BLOCK, new Identifier("forge", "ores/silver"))).add(IafBlocks.DEEPSLATE_SILVER_ORE);
    }

    @Override
    public String getName() {
        return "Ice and Fire Block Tags";
    }
}

