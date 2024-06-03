package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
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
import org.jetbrains.annotations.NotNull;

public class WorldGenIceDragonRoosts extends WorldGenDragonRoosts {
    private static final Identifier DRAGON_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/ice_dragon_roost");

    public WorldGenIceDragonRoosts(final Codec<DefaultFeatureConfig> configuration) {
        super(configuration, IafBlockRegistry.SILVER_PILE.get());
    }

    @Override
    protected EntityType<? extends EntityDragonBase> getDragonType() {
        return IafEntityRegistry.ICE_DRAGON.get();
    }

    @Override
    protected Identifier getRoostLootTable() {
        return DRAGON_CHEST;
    }

    @Override
    protected BlockState transform(final BlockState state) {
        Block block = null;

        if (state.isOf(Blocks.GRASS_BLOCK)) {
            block = IafBlockRegistry.FROZEN_GRASS.get();
        } else if (state.isOf(Blocks.DIRT_PATH)) {
            block = IafBlockRegistry.FROZEN_DIRT_PATH.get();
        } else if (state.isIn(Tags.Blocks.GRAVEL)) {
            block = IafBlockRegistry.FROZEN_GRAVEL.get();
        } else if (state.isIn(BlockTags.DIRT)) {
            block = IafBlockRegistry.FROZEN_DIRT.get();
        } else if (state.isIn(Tags.Blocks.STONE)) {
            block = IafBlockRegistry.FROZEN_STONE.get();
        } else if (state.isIn(Tags.Blocks.COBBLESTONE)) {
            block = IafBlockRegistry.FROZEN_COBBLESTONE.get();
        } else if (state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.PLANKS)) {
            block = IafBlockRegistry.FROZEN_SPLINTERS.get();
        } else if (state.isOf(Blocks.GRASS) || state.isIn(BlockTags.LEAVES) || state.isIn(BlockTags.FLOWERS) || state.isIn(BlockTags.CROPS)) {
            block = Blocks.AIR;
        }

        if (block != null) {
            return block.getDefaultState();
        }

        return state;
    }

    @Override
    protected void handleCustomGeneration(@NotNull final FeatureContext<DefaultFeatureConfig> context, final BlockPos position, double distance) {
        if (context.getRandom().nextInt(1000) == 0) {
            generateRoostPile(context.getWorld(), context.getRandom(), getSurfacePosition(context.getWorld(), position), IafBlockRegistry.DRAGON_ICE.get());
        }
    }
}
