package com.iafenvoy.iceandfire.world.gen;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import com.iafenvoy.iceandfire.registry.IafEntities;
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

public class WorldGenIceDragonRoosts extends WorldGenDragonRoosts {
    private static final Identifier DRAGON_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/ice_dragon_roost");

    public WorldGenIceDragonRoosts(final Codec<DefaultFeatureConfig> configuration) {
        super(configuration, IafBlocks.SILVER_PILE);
    }

    @Override
    protected EntityType<? extends EntityDragonBase> getDragonType() {
        return IafEntities.ICE_DRAGON;
    }

    @Override
    protected Identifier getRoostLootTable() {
        return DRAGON_CHEST;
    }

    @Override
    protected BlockState transform(final BlockState state) {
        Block block = null;

        if (state.isOf(Blocks.GRASS_BLOCK))
            block = IafBlocks.FROZEN_GRASS;
        else if (state.isOf(Blocks.DIRT_PATH))
            block = IafBlocks.FROZEN_DIRT_PATH;
        else if (state.isIn(Tags.Blocks.GRAVEL))
            block = IafBlocks.FROZEN_GRAVEL;
        else if (state.isIn(BlockTags.DIRT))
            block = IafBlocks.FROZEN_DIRT;
        else if (state.isIn(Tags.Blocks.STONE))
            block = IafBlocks.FROZEN_STONE;
        else if (state.isIn(Tags.Blocks.COBBLESTONE))
            block = IafBlocks.FROZEN_COBBLESTONE;
        else if (state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.PLANKS))
            block = IafBlocks.FROZEN_SPLINTERS;
        else if (state.isOf(Blocks.GRASS) || state.isIn(BlockTags.LEAVES) || state.isIn(BlockTags.FLOWERS) || state.isIn(BlockTags.CROPS))
            block = Blocks.AIR;


        if (block != null) {
            return block.getDefaultState();
        }

        return state;
    }

    @Override
    protected void handleCustomGeneration(final FeatureContext<DefaultFeatureConfig> context, final BlockPos position, double distance) {
        if (context.getRandom().nextInt(1000) == 0)
            this.generateRoostPile(context.getWorld(), context.getRandom(), this.getSurfacePosition(context.getWorld(), position), IafBlocks.DRAGON_ICE);
    }
}
