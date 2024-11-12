package com.iafenvoy.iceandfire.world.processor;

import com.iafenvoy.iceandfire.registry.IafBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;

public class DreadPortalProcessor extends StructureProcessor {

    public DreadPortalProcessor(BlockPos position, StructurePlacementData settings, Biome biome) {
    }

    public static BlockState getRandomCrackedBlock(BlockState prev, Random random) {
        float rand = random.nextFloat();
        if (rand < 0.3) {
            return IafBlocks.DREAD_STONE_BRICKS.getDefaultState();
        } else if (rand < 0.6) {
            return IafBlocks.DREAD_STONE_BRICKS_CRACKED.getDefaultState();
        } else {
            return IafBlocks.DREAD_STONE_BRICKS_MOSSY.getDefaultState();
        }
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo currentBlockInfo, StructurePlacementData data) {
        Random random = data.getRandom(pos);
        float integrity = 1.0F;
        if (random.nextFloat() <= integrity) {
            if (currentBlockInfo.state().getBlock() == Blocks.DIAMOND_BLOCK) {
                return new StructureTemplate.StructureBlockInfo(pos, IafBlocks.DREAD_PORTAL.getDefaultState(), null);
            }
            if (currentBlockInfo.state().getBlock() == IafBlocks.DREAD_STONE_BRICKS) {
                BlockState state = getRandomCrackedBlock(null, random);
                return new StructureTemplate.StructureBlockInfo(pos, state, null);
            }
            return currentBlockInfo;
        }
        return currentBlockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.BLOCK_ROT;
    }

}
