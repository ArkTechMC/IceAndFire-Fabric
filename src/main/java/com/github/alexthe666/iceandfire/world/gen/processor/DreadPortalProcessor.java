package com.github.alexthe666.iceandfire.world.gen.processor;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
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
import org.jetbrains.annotations.NotNull;

public class DreadPortalProcessor extends StructureProcessor {

    public DreadPortalProcessor(BlockPos position, StructurePlacementData settings, Biome biome) {
    }

    public static BlockState getRandomCrackedBlock(BlockState prev, Random random) {
        float rand = random.nextFloat();
        if (rand < 0.3) {
            return IafBlockRegistry.DREAD_STONE_BRICKS.get().getDefaultState();
        } else if (rand < 0.6) {
            return IafBlockRegistry.DREAD_STONE_BRICKS_CRACKED.get().getDefaultState();
        } else {
            return IafBlockRegistry.DREAD_STONE_BRICKS_MOSSY.get().getDefaultState();
        }
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo currentBlockInfo, StructurePlacementData data) {
        Random random = data.getRandom(pos);
        float integrity = 1.0F;
        if (random.nextFloat() <= integrity) {
            if (currentBlockInfo.state().getBlock() == Blocks.DIAMOND_BLOCK) {
                return new StructureTemplate.StructureBlockInfo(pos, IafBlockRegistry.DREAD_PORTAL.get().getDefaultState(), null);
            }
            if (currentBlockInfo.state().getBlock() == IafBlockRegistry.DREAD_STONE_BRICKS.get()) {
                BlockState state = getRandomCrackedBlock(null, random);
                return new StructureTemplate.StructureBlockInfo(pos, state, null);
            }
            return currentBlockInfo;
        }
        return currentBlockInfo;
    }

    @Override
    protected StructureProcessorType getType() {
        return StructureProcessorType.BLOCK_ROT;
    }

}
