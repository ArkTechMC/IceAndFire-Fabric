package com.github.alexthe666.iceandfire.world.gen.processor;

import com.github.alexthe666.iceandfire.world.IafProcessors;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.block.Waterloggable;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;

public class GorgonTempleProcessor extends StructureProcessor {

    public static final GorgonTempleProcessor INSTANCE = new GorgonTempleProcessor();
    public static final Codec<GorgonTempleProcessor> CODEC = Codec.unit(() -> INSTANCE);

    public GorgonTempleProcessor() {
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(WorldView worldReader, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo infoIn2, StructurePlacementData settings) {

        // Workaround for https://bugs.mojang.com/browse/MC-130584
        // Due to a hardcoded field in Templates, any waterloggable blocks in structures replacing water in the world will become waterlogged.
        // Idea of workaround is detect if we are placing a waterloggable block and if so, remove the water in the world instead.
        if (infoIn2.state().getBlock() instanceof Waterloggable) {
            if (worldReader.getFluidState(infoIn2.pos()).isIn(FluidTags.WATER)) {
                ChunkPos currentChunk = new ChunkPos(infoIn2.pos());
                worldReader.getChunk(currentChunk.x, currentChunk.z).setBlockState(infoIn2.pos(), Blocks.AIR.getDefaultState(), false);
            }
        }
        return infoIn2;
    }

    @Override
    protected StructureProcessorType getType() {
        return IafProcessors.GORGONTEMPLEPROCESSOR.get();
    }
}
