package com.iafenvoy.iceandfire.world.gen;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import com.iafenvoy.iceandfire.registry.IafEntities;
import com.iafenvoy.iceandfire.registry.tag.CommonTags;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class WorldGenLightningDragonRoosts extends WorldGenDragonRoosts {
    private static final Identifier DRAGON_CHEST = new Identifier(IceAndFire.MOD_ID, "chest/lightning_dragon_roost");

    public WorldGenLightningDragonRoosts(final Codec<DefaultFeatureConfig> configuration) {
        super(configuration, IafBlocks.COPPER_PILE);
    }

    @Override
    protected EntityType<? extends EntityDragonBase> getDragonType() {
        return IafEntities.LIGHTNING_DRAGON;
    }

    @Override
    protected Identifier getRoostLootTable() {
        return DRAGON_CHEST;
    }

    @Override
    protected BlockState transform(final BlockState state) {
        Block block = null;

        if (state.isOf(Blocks.GRASS_BLOCK))
            block = IafBlocks.CRACKLED_GRASS;
        else if (state.isOf(Blocks.DIRT_PATH))
            block = IafBlocks.CRACKLED_DIRT_PATH;
        else if (state.isIn(CommonTags.Blocks.GRAVEL))
            block = IafBlocks.CRACKLED_GRAVEL;
        else if (state.isIn(BlockTags.DIRT))
            block = IafBlocks.CRACKLED_DIRT;
        else if (state.isIn(CommonTags.Blocks.STONE))
            block = IafBlocks.CRACKLED_STONE;
        else if (state.isIn(CommonTags.Blocks.COBBLESTONE))
            block = IafBlocks.CRACKLED_COBBLESTONE;
        else if (state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.PLANKS))
            block = IafBlocks.ASH;
        else if (state.isOf(Blocks.GRASS) || state.isIn(BlockTags.LEAVES) || state.isIn(BlockTags.FLOWERS) || state.isIn(BlockTags.CROPS))
            block = Blocks.AIR;

        if (block != null)
            return block.getDefaultState();

        return state;
    }

    @Override
    protected void handleCustomGeneration(final FeatureContext<DefaultFeatureConfig> context, final BlockPos position, double distance) {
        if (distance > 0.05D && context.getRandom().nextInt(800) == 0) {
            // FIXME
            new WorldGenRoostSpire().generate(context.getWorld(), context.getRandom(), this.getSurfacePosition(context.getWorld(), position));
        }

        if (distance > 0.05D && context.getRandom().nextInt(1000) == 0) {
            // FIXME
            new WorldGenRoostSpike(HORIZONTALS[context.getRandom().nextInt(3)]).generate(context.getWorld(), context.getRandom(), this.getSurfacePosition(context.getWorld(), position));
        }
    }
}
