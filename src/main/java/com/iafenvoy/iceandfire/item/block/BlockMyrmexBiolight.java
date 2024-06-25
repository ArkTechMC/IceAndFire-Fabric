package com.iafenvoy.iceandfire.item.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class BlockMyrmexBiolight extends PlantBlock {
    public static final BooleanProperty CONNECTED_DOWN = BooleanProperty.of("down");

    public BlockMyrmexBiolight() {
        super(Settings.create().mapColor(MapColor.DARK_GREEN).pistonBehavior(PistonBehavior.DESTROY).nonOpaque().noCollision().dynamicBounds().strength(0).luminance((state) -> 7).sounds(BlockSoundGroup.GRASS).ticksRandomly());
        this.setDefaultState(this.getStateManager().getDefaultState().with(CONNECTED_DOWN, Boolean.FALSE));
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView worldIn, BlockPos pos) {
        BlockPos blockpos = pos.up();
        return worldIn.getBlockState(blockpos).getBlock() == this || worldIn.getBlockState(blockpos).isOpaque();
    }


    @Override
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess worldIn, BlockPos currentPos, BlockPos facingPos) {
        boolean flag3 = worldIn.getBlockState(currentPos.down()).getBlock() == this;
        return stateIn.with(CONNECTED_DOWN, flag3);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isClient)
            this.updateState(state, worldIn, pos, state.getBlock());
        if (!worldIn.getBlockState(pos.up()).isOpaque() && worldIn.getBlockState(pos.up()).getBlock() != this)
            worldIn.breakBlock(pos, true);
    }

    public void updateState(BlockState state, World worldIn, BlockPos pos, Block blockIn) {
        boolean flag2 = state.get(CONNECTED_DOWN);
        boolean flag3 = worldIn.getBlockState(pos.down()).getBlock() == this;
        if (flag2 != flag3)
            worldIn.setBlockState(pos, state.with(CONNECTED_DOWN, flag3), 3);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CONNECTED_DOWN);
    }
}