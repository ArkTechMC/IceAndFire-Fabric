package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.registry.IafBlocks;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class BlockElementalFlower extends PlantBlock {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);
    public Item itemBlock;

    public BlockElementalFlower() {
        super(Settings.create().mapColor(MapColor.DARK_GREEN).replaceable().burnable().pistonBehavior(PistonBehavior.DESTROY).nonOpaque().noCollision().dynamicBounds().ticksRandomly().sounds(BlockSoundGroup.GRASS));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean canPlantOnTop(BlockState state, BlockView world, BlockPos pos) {
        Block block = state.getBlock();
        return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.FARMLAND || state.isIn(BlockTags.SAND);
    }

    public boolean canStay(World world, BlockPos pos) {
        BlockState soil = world.getBlockState(pos.down());
        if (this == IafBlocks.FIRE_LILY)
            return soil.isIn(BlockTags.SAND) || soil.isOf(Blocks.NETHERRACK);
        else if (this == IafBlocks.LIGHTNING_LILY)
            return soil.isIn(BlockTags.DIRT) || soil.isOf(Blocks.GRASS);
        else
            return soil.isIn(BlockTags.ICE) || soil.isIn(BlockTags.SNOW) || soil.isIn(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON);

    }

    public void updateTick(World world, BlockPos pos, BlockState state, Random rand) {
        this.checkFall(world, pos);
    }

    private void checkFall(World world, BlockPos pos) {
        if (!this.canStay(world, pos))
            world.breakBlock(pos, true);
    }

    protected boolean canSustainBush(BlockState state) {
        return true;
    }
}
