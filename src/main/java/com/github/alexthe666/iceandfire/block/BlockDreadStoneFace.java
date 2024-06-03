package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;

public class BlockDreadStoneFace extends HorizontalFacingBlock implements IDreadBlock, IDragonProof {
    public static final BooleanProperty PLAYER_PLACED = BooleanProperty.of("player_placed");

    public BlockDreadStoneFace() {
        super(
            Settings
                .create()
                .mapColor(MapColor.STONE_GRAY)
                .instrument(Instrument.BASEDRUM)
                .sounds(BlockSoundGroup.STONE)
                .strength(-1F, 10000F)
        );

        this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(PLAYER_PLACED, Boolean.FALSE));
    }

    @SuppressWarnings("deprecation")
    @Override
    public float calcBlockBreakingDelta(BlockState state, @NotNull PlayerEntity player, @NotNull BlockView worldIn, @NotNull BlockPos pos) {
        if (state.get(PLAYER_PLACED)) {
            float f = 8f;
            //Code from super method
            return player.getDigSpeed(state, pos) / f / (float) 30;
        }
        return super.calcBlockBreakingDelta(state, player, worldIn, pos);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing().getOpposite()).with(PLAYER_PLACED, true);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(PLAYER_PLACED);
    }
}
