package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.TransparentBlock;
import net.minecraft.block.enums.Instrument;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;

public class BlockMyrmexConnectedResin extends TransparentBlock {

    public static final BooleanProperty UP = BooleanProperty.of("up");
    public static final BooleanProperty DOWN = BooleanProperty.of("down");
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty WEST = BooleanProperty.of("west");

    public BlockMyrmexConnectedResin(boolean jungle, boolean glass) {
        super(
                Settings
                        .create()
                        .mapColor(MapColor.STONE_GRAY)
                        .instrument(Instrument.BASEDRUM)
                        .strength(glass ? 1.5F : 3.5F)
                        .nonOpaque()
                        .dynamicBounds()
                        .sounds(glass ? BlockSoundGroup.GLASS : BlockSoundGroup.STONE)
        );

        this.setDefaultState(this.getStateManager().getDefaultState().with(UP, Boolean.FALSE)
                .with(DOWN, Boolean.FALSE)
                .with(NORTH, Boolean.FALSE)
                .with(EAST, Boolean.FALSE)
                .with(SOUTH, Boolean.FALSE)
                .with(WEST, Boolean.FALSE)
        );
    }

    static String name(boolean glass, boolean jungle) {
        String biome = jungle ? "jungle" : "desert";
        String type = glass ? "glass" : "block";
        return "myrmex_%s_resin_%s".formatted(biome, type);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockView iblockreader = context.getWorld();
        BlockPos blockpos = context.getBlockPos();
        FluidState ifluidstate = context.getWorld().getFluidState(context.getBlockPos());
        BlockPos blockpos1 = blockpos.north();
        BlockPos blockpos2 = blockpos.east();
        BlockPos blockpos3 = blockpos.south();
        BlockPos blockpos4 = blockpos.west();
        BlockPos blockpos5 = blockpos.up();
        BlockPos blockpos6 = blockpos.down();
        BlockState blockstate = iblockreader.getBlockState(blockpos1);
        BlockState blockstate1 = iblockreader.getBlockState(blockpos2);
        BlockState blockstate2 = iblockreader.getBlockState(blockpos3);
        BlockState blockstate3 = iblockreader.getBlockState(blockpos4);
        BlockState blockstate4 = iblockreader.getBlockState(blockpos5);
        BlockState blockstate5 = iblockreader.getBlockState(blockpos6);
        return super.getPlacementState(context)
                .with(NORTH, this.canFenceConnectTo(blockstate, false, Direction.SOUTH))
                .with(EAST, this.canFenceConnectTo(blockstate1, false, Direction.WEST))
                .with(SOUTH, this.canFenceConnectTo(blockstate2, false, Direction.NORTH))
                .with(WEST, this.canFenceConnectTo(blockstate3, false, Direction.EAST))
                .with(UP, this.canFenceConnectTo(blockstate4, false, Direction.UP))
                .with(DOWN, this.canFenceConnectTo(blockstate5, false, Direction.DOWN));
    }

    @Override
    public @NotNull BlockState getStateForNeighborUpdate(@NotNull BlockState stateIn, Direction facing, @NotNull BlockState facingState, @NotNull WorldAccess worldIn, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        BooleanProperty connect = switch (facing) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            case DOWN -> DOWN;
            default -> UP;
        };
        return stateIn.with(connect, this.canFenceConnectTo(facingState, false, facing.getOpposite()));
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, WEST, SOUTH, DOWN, UP);
    }

    public boolean canFenceConnectTo(BlockState state, boolean isSideSolid, Direction direction) {
        return state.getBlock() == this;
    }

    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

}
