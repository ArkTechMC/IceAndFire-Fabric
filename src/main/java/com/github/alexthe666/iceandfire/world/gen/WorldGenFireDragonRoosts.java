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

public class WorldGenFireDragonRoosts extends WorldGenDragonRoosts {
    private static final Identifier DRAGON_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/fire_dragon_roost");

    public WorldGenFireDragonRoosts(final Codec<DefaultFeatureConfig> configuration) {
        super(configuration, IafBlockRegistry.GOLD_PILE.get());
    }

    @Override
    protected EntityType<? extends EntityDragonBase> getDragonType() {
        return IafEntityRegistry.FIRE_DRAGON.get();
    }

    @Override
    protected Identifier getRoostLootTable() {
        return DRAGON_CHEST;
    }

    @Override
    protected BlockState transform(final BlockState state) {
        Block block = null;

        if (state.isOf(Blocks.GRASS_BLOCK)) {
            block = IafBlockRegistry.CHARRED_GRASS.get();
        } else if (state.isOf(Blocks.DIRT_PATH)) {
            block = IafBlockRegistry.CHARRED_DIRT_PATH.get();
        } else if (state.isIn(Tags.Blocks.GRAVEL)) {
            block = IafBlockRegistry.CHARRED_GRAVEL.get();
        } else if (state.isIn(BlockTags.DIRT)) {
            block = IafBlockRegistry.CHARRED_DIRT.get();
        } else if (state.isIn(Tags.Blocks.STONE)) {
            block = IafBlockRegistry.CHARRED_STONE.get();
        } else if (state.isIn(Tags.Blocks.COBBLESTONE)) {
            block = IafBlockRegistry.CHARRED_COBBLESTONE.get();
        } else if (state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.PLANKS)) {
            block = IafBlockRegistry.ASH.get();
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
            generateRoostPile(context.getWorld(), context.getRandom(), getSurfacePosition(context.getWorld(), position), IafBlockRegistry.ASH.get());
        }
    }
}
