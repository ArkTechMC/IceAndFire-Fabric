package com.iafenvoy.iceandfire.item.block;

import com.iafenvoy.iceandfire.item.block.util.IDragonProof;
import com.iafenvoy.iceandfire.item.block.util.IDreadBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class BlockDreadStoneFace extends HorizontalFacingBlock implements IDreadBlock, IDragonProof {
    public BlockDreadStoneFace() {
        super(Settings.copy(Blocks.STONE));
        this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(UNBREAKABLE, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
        builder.add(UNBREAKABLE);
    }

    @Override
    public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
        return state.get(UNBREAKABLE) ? 0 : super.calcBlockBreakingDelta(state, player, world, pos);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing().getOpposite());
    }
}
