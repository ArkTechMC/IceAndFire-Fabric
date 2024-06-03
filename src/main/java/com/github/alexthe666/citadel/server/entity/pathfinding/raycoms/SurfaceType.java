package com.github.alexthe666.citadel.server.entity.pathfinding.raycoms;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BambooSaplingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FireBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.MagmaBlock;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.level.block.*;
import javax.annotation.Nullable;

/**
 * Check if we can walk on a surface, drop into, or neither.
 */
public enum SurfaceType {
    WALKABLE,
    DROPABLE,
    NOT_PASSABLE,
    FLYABLE;

    /**
     * Is the block solid and can be stood upon.
     *
     * @param blockState Block to check.
     * @param pos        the position.
     * @return true if the block at that location can be walked on.
     */
    public static SurfaceType getSurfaceType(final BlockView world, final BlockState blockState, final BlockPos pos) {
        final Block block = blockState.getBlock();
        if (block instanceof FenceBlock
                || block instanceof FenceGateBlock
                || block instanceof WallBlock
                || block instanceof FireBlock
                || block instanceof CampfireBlock
                || block instanceof BambooBlock
                || block instanceof BambooSaplingBlock
                || block instanceof DoorBlock
                || block instanceof MagmaBlock
                || block instanceof PowderSnowBlock) {
            return SurfaceType.NOT_PASSABLE;
        }

        if ((block instanceof TrapdoorBlock) && !blockState.get(TrapdoorBlock.OPEN))
        {
            return SurfaceType.WALKABLE;
        }

        final VoxelShape shape = blockState.getOutlineShape(world, pos);
        if (shape.getMax(Direction.Axis.Y) > 1.0)
        {
            return SurfaceType.NOT_PASSABLE;
        }

        final FluidState fluid = world.getFluidState(pos);
        if (blockState.getBlock() == Blocks.LAVA || !fluid.isEmpty() && (fluid.getFluid() == Fluids.LAVA || fluid.getFluid() == Fluids.FLOWING_LAVA))
        {
            return SurfaceType.NOT_PASSABLE;
        }

        if (isWater(world, pos, blockState, fluid))
        {
            return SurfaceType.WALKABLE;
        }

        if (block instanceof AbstractSignBlock || block instanceof VineBlock)
        {
            return SurfaceType.DROPABLE;
        }

        if ((blockState.isSolid() && (shape.getMax(Direction.Axis.X) - shape.getMin(Direction.Axis.X)) > 0.75
                && (shape.getMax(Direction.Axis.Z) - shape.getMin(Direction.Axis.Z)) > 0.75)
                || (blockState.getBlock() == Blocks.SNOW && blockState.get(SnowBlock.LAYERS) > 1)
                || block instanceof CarpetBlock)
        {
            return SurfaceType.WALKABLE;
        }

        return SurfaceType.DROPABLE;
    }

    /**
     * Check if the block at this position is actually some kind of waterly fluid.
     *
     * @param pos the pos in the world.
     * @return true if so.
     */
    public static boolean isWater(final WorldView world, final BlockPos pos) {
        return isWater(world, pos, null, null);
    }

    /**
     * Check if the block at this position is actually some kind of waterly fluid.
     *
     * @param pos         the pos in the world.
     * @param pState      existing blockstate or null
     * @param pFluidState existing fluidstate or null
     * @return true if so.
     */
    public static boolean isWater(final BlockView world, final BlockPos pos, @Nullable BlockState pState, @Nullable FluidState pFluidState) {
        BlockState state = pState;
        if (state == null) {
            state = world.getBlockState(pos);
        }

        if (state.isOpaque()) {
            return false;
        }
        if (state.getBlock() == Blocks.WATER) {
            return true;
        }

        FluidState fluidState = pFluidState;
        if (fluidState == null) {
            fluidState = world.getFluidState(pos);
        }

        if (fluidState.isEmpty()) {
            return false;
        }

        if (state.getBlock() instanceof TrapdoorBlock || state.getBlock() instanceof HorizontalFacingBlock) {
            // getvalue() will throw an exception if the property does not exist
            if (state.contains(TrapdoorBlock.OPEN) && !state.get(TrapdoorBlock.OPEN) && state.contains(TrapdoorBlock.HALF) && state.get(TrapdoorBlock.HALF) == BlockHalf.TOP) {
                return false;
            }
        }

        final Fluid fluid = fluidState.getFluid();
        return fluid == Fluids.WATER || fluid == Fluids.FLOWING_WATER;
    }
}
