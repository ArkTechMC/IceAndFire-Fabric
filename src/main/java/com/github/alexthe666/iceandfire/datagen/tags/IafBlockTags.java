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
    public static TagKey<Block> CHARRED_BLOCKS = createKey("charred_blocks");
    public static TagKey<Block> FROZEN_BLOCKS = createKey("frozen_blocks");
    public static TagKey<Block> CRACKLED_BLOCKS = createKey("crackled_blocks");
    public static TagKey<Block> DRAGON_ENVIRONMENT_BLOCKS = createKey("dragon_environment_blocks");

    public static TagKey<Block> DRAGON_CAVE_RARE_ORES = createKey("dragon_cave_rare_ores");
    public static TagKey<Block> DRAGON_CAVE_UNCOMMON_ORES = createKey("dragon_cave_uncommon_ores");
    public static TagKey<Block> DRAGON_CAVE_COMMON_ORES = createKey("dragon_cave_common_ores");

    public static TagKey<Block> FIRE_DRAGON_CAVE_ORES = createKey("fire_dragon_cave_ores");
    public static TagKey<Block> ICE_DRAGON_CAVE_ORES = createKey("ice_dragon_cave_ores");
    public static TagKey<Block> LIGHTNING_DRAGON_CAVE_ORES = createKey("lightning_dragon_cave_ores");

    public static TagKey<Block> DRAGON_BLOCK_BREAK_BLACKLIST = createKey("dragon_block_break_blacklist");
    public static TagKey<Block> DRAGON_BLOCK_BREAK_NO_DROPS = createKey("dragon_block_break_no_drops");

    public IafBlockTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> future, ExistingFileHelper helper) {
        super(output, future);
    }

    private static TagKey<Block> createKey(final String name) {
        return TagKey.of(RegistryKeys.BLOCK, new Identifier(IceAndFire.MOD_ID, name));
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup pProvider) {
        this.tag(CHARRED_BLOCKS)
                .add(IafBlockRegistry.CHARRED_COBBLESTONE.get())
                .add(IafBlockRegistry.CHARRED_DIRT.get())
                .add(IafBlockRegistry.CHARRED_DIRT_PATH.get())
                .add(IafBlockRegistry.CHARRED_GRASS.get())
                .add(IafBlockRegistry.CHARRED_GRAVEL.get())
                .add(IafBlockRegistry.CHARRED_STONE.get());

        this.tag(FROZEN_BLOCKS)
                .add(IafBlockRegistry.FROZEN_COBBLESTONE.get())
                .add(IafBlockRegistry.FROZEN_DIRT.get())
                .add(IafBlockRegistry.FROZEN_DIRT_PATH.get())
                .add(IafBlockRegistry.FROZEN_GRASS.get())
                .add(IafBlockRegistry.FROZEN_GRAVEL.get())
                .add(IafBlockRegistry.FROZEN_STONE.get())
                .add(IafBlockRegistry.FROZEN_SPLINTERS.get());

        this.tag(CRACKLED_BLOCKS)
                .add(IafBlockRegistry.CRACKLED_COBBLESTONE.get())
                .add(IafBlockRegistry.CRACKLED_DIRT.get())
                .add(IafBlockRegistry.CRACKLED_DIRT_PATH.get())
                .add(IafBlockRegistry.CRACKLED_GRASS.get())
                .add(IafBlockRegistry.CRACKLED_GRASS.get())
                .add(IafBlockRegistry.CRACKLED_STONE.get());

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
                .add(IafBlockRegistry.SILVER_ORE.get());

        this.tag(DRAGON_CAVE_COMMON_ORES)
                .add(Blocks.COAL_ORE)
                .add(Blocks.COPPER_ORE)
                .add(Blocks.IRON_ORE);

        this.tag(FIRE_DRAGON_CAVE_ORES)
                .add(Blocks.EMERALD_ORE);

        this.tag(ICE_DRAGON_CAVE_ORES)
                .add(IafBlockRegistry.SAPPHIRE_ORE.get());

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
                .add(IafBlockRegistry.SILVER_ORE.get())
                .add(IafBlockRegistry.DEEPSLATE_SILVER_ORE.get())
                .add(IafBlockRegistry.SILVER_BLOCK.get())
                .add(IafBlockRegistry.RAW_SILVER_BLOCK.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(IafBlockRegistry.SAPPHIRE_ORE.get())
                .add(IafBlockRegistry.SAPPHIRE_BLOCK.get());

        this.tag(Tags.Blocks.ORES)
                .add(IafBlockRegistry.SILVER_ORE.get())
                .add(IafBlockRegistry.DEEPSLATE_SILVER_ORE.get())
                .add(IafBlockRegistry.SAPPHIRE_ORE.get());

        this.tag(Tags.Blocks.ORES_IN_GROUND_STONE)
                .add(IafBlockRegistry.SILVER_ORE.get())
                .add(IafBlockRegistry.SAPPHIRE_ORE.get());

        this.tag(Tags.Blocks.ORE_BEARING_GROUND_DEEPSLATE)
                .add(IafBlockRegistry.DEEPSLATE_SILVER_ORE.get());

        // These are also used / created by other mods
        this.tag(TagKey.of(RegistryKeys.BLOCK, new Identifier("forge", "ores/silver"))).add(IafBlockRegistry.SILVER_ORE.get());
        this.tag(TagKey.of(RegistryKeys.BLOCK, new Identifier("forge", "ores/silver"))).add(IafBlockRegistry.DEEPSLATE_SILVER_ORE.get());
    }

    @Override
    public String getName() {
        return "Ice and Fire Block Tags";
    }
}

