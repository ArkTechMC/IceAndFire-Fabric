package com.github.alexthe666.iceandfire.datagen.tags;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
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
                .add(IafBlockRegistry.CHARRED_COBBLESTONE)
                .add(IafBlockRegistry.CHARRED_DIRT)
                .add(IafBlockRegistry.CHARRED_DIRT_PATH)
                .add(IafBlockRegistry.CHARRED_GRASS)
                .add(IafBlockRegistry.CHARRED_GRAVEL)
                .add(IafBlockRegistry.CHARRED_STONE);

        this.tag(FROZEN_BLOCKS)
                .add(IafBlockRegistry.FROZEN_COBBLESTONE)
                .add(IafBlockRegistry.FROZEN_DIRT)
                .add(IafBlockRegistry.FROZEN_DIRT_PATH)
                .add(IafBlockRegistry.FROZEN_GRASS)
                .add(IafBlockRegistry.FROZEN_GRAVEL)
                .add(IafBlockRegistry.FROZEN_STONE)
                .add(IafBlockRegistry.FROZEN_SPLINTERS);

        this.tag(CRACKLED_BLOCKS)
                .add(IafBlockRegistry.CRACKLED_COBBLESTONE)
                .add(IafBlockRegistry.CRACKLED_DIRT)
                .add(IafBlockRegistry.CRACKLED_DIRT_PATH)
                .add(IafBlockRegistry.CRACKLED_GRASS)
                .add(IafBlockRegistry.CRACKLED_GRASS)
                .add(IafBlockRegistry.CRACKLED_STONE);

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
                .add(IafBlockRegistry.SILVER_ORE);

        this.tag(DRAGON_CAVE_COMMON_ORES)
                .add(Blocks.COAL_ORE)
                .add(Blocks.COPPER_ORE)
                .add(Blocks.IRON_ORE);

        this.tag(FIRE_DRAGON_CAVE_ORES)
                .add(Blocks.EMERALD_ORE);

        this.tag(ICE_DRAGON_CAVE_ORES)
                .add(IafBlockRegistry.SAPPHIRE_ORE);

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
                .add(IafBlockRegistry.SILVER_ORE)
                .add(IafBlockRegistry.DEEPSLATE_SILVER_ORE)
                .add(IafBlockRegistry.SILVER_BLOCK)
                .add(IafBlockRegistry.RAW_SILVER_BLOCK);

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(IafBlockRegistry.SAPPHIRE_ORE)
                .add(IafBlockRegistry.SAPPHIRE_BLOCK);

        this.tag(Tags.Blocks.ORES)
                .add(IafBlockRegistry.SILVER_ORE)
                .add(IafBlockRegistry.DEEPSLATE_SILVER_ORE)
                .add(IafBlockRegistry.SAPPHIRE_ORE);

        this.tag(Tags.Blocks.ORES_IN_GROUND_STONE)
                .add(IafBlockRegistry.SILVER_ORE)
                .add(IafBlockRegistry.SAPPHIRE_ORE);

        this.tag(Tags.Blocks.ORE_BEARING_GROUND_DEEPSLATE)
                .add(IafBlockRegistry.DEEPSLATE_SILVER_ORE);

        // These are also used / created by other mods
        this.tag(TagKey.of(RegistryKeys.BLOCK, new Identifier("forge", "ores/silver"))).add(IafBlockRegistry.SILVER_ORE);
        this.tag(TagKey.of(RegistryKeys.BLOCK, new Identifier("forge", "ores/silver"))).add(IafBlockRegistry.DEEPSLATE_SILVER_ORE);
    }

    @Override
    public String getName() {
        return "Ice and Fire Block Tags";
    }
}

