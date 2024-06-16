package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.registry.IafBlocks;
import com.github.alexthe666.iceandfire.registry.IafEntities;
import com.mojang.serialization.Codec;
import io.github.fabricators_of_create.porting_lib.tags.Tags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class WorldGenFireDragonRoosts extends WorldGenDragonRoosts {
    private static final Identifier DRAGON_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/fire_dragon_roost");

    public WorldGenFireDragonRoosts(final Codec<DefaultFeatureConfig> configuration) {
        super(configuration, IafBlocks.GOLD_PILE);
    }

    @Override
    protected EntityType<? extends EntityDragonBase> getDragonType() {
        return IafEntities.FIRE_DRAGON;
    }

    @Override
    protected Identifier getRoostLootTable() {
        return DRAGON_CHEST;
    }

    @Override
    protected BlockState transform(final BlockState state) {
        Block block = null;

        if (state.isOf(Blocks.GRASS_BLOCK))
            block = IafBlocks.CHARRED_GRASS;
        else if (state.isOf(Blocks.DIRT_PATH))
            block = IafBlocks.CHARRED_DIRT_PATH;
        else if (state.isIn(Tags.Blocks.GRAVEL))
            block = IafBlocks.CHARRED_GRAVEL;
        else if (state.isIn(BlockTags.DIRT))
            block = IafBlocks.CHARRED_DIRT;
        else if (state.isIn(Tags.Blocks.STONE))
            block = IafBlocks.CHARRED_STONE;
        else if (state.isIn(Tags.Blocks.COBBLESTONE))
            block = IafBlocks.CHARRED_COBBLESTONE;
        else if (state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.PLANKS))
            block = IafBlocks.ASH;
        else if (state.isOf(Blocks.GRASS) || state.isIn(BlockTags.LEAVES) || state.isIn(BlockTags.FLOWERS) || state.isIn(BlockTags.CROPS))
            block = Blocks.AIR;


        if (block != null) {
            return block.getDefaultState();
        }

        return state;
    }

    @Override
    protected void handleCustomGeneration(final FeatureContext<DefaultFeatureConfig> context, final BlockPos position, double distance) {
        if (context.getRandom().nextInt(1000) == 0) {
            this.generateRoostPile(context.getWorld(), context.getRandom(), this.getSurfacePosition(context.getWorld(), position), IafBlocks.ASH);
        }
    }
}
