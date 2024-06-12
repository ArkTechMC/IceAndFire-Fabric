package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;

public class BlockGoldPile extends Block {
    public static final IntProperty LAYERS = IntProperty.of("layers", 1, 8);
    protected static final VoxelShape[] SHAPES = new VoxelShape[]{VoxelShapes.empty(), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

    public BlockGoldPile() {
        super(
                Settings
                        .create()
                        .mapColor(MapColor.DIRT_BROWN)
                        .strength(0.3F, 1)
                        .ticksRandomly()
                        .sounds(IafBlockRegistry.SOUND_TYPE_GOLD)
        );

        this.setDefaultState(this.stateManager.getDefaultState().with(LAYERS, 1));
    }

    @Override
    public boolean canPathfindThrough(@NotNull BlockState state, @NotNull BlockView worldIn, @NotNull BlockPos pos, NavigationType type) {
        return switch (type) {
            case LAND -> state.get(LAYERS) < 5;
            case WATER -> false;
            case AIR -> false;
            default -> false;
        };
    }

    @Override
    public @NotNull VoxelShape getOutlineShape(BlockState state, @NotNull BlockView worldIn, @NotNull BlockPos pos, @NotNull ShapeContext context) {
        return SHAPES[state.get(LAYERS)];
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, @NotNull BlockView worldIn, @NotNull BlockPos pos, @NotNull ShapeContext context) {
        return SHAPES[state.get(LAYERS) - 1];
    }

    @Override
    public boolean hasSidedTransparency(@NotNull BlockState state) {
        return true;
    }

    @Override
    public boolean canPlaceAt(@NotNull BlockState state, WorldView worldIn, BlockPos pos) {
        BlockState blockstate = worldIn.getBlockState(pos.down());
        Block block = blockstate.getBlock();
        if (block != Blocks.ICE && block != Blocks.PACKED_ICE && block != Blocks.BARRIER) {
            if (block != Blocks.HONEY_BLOCK && block != Blocks.SOUL_SAND) {
                return Block.isFaceFullSquare(blockstate.getCollisionShape(worldIn, pos.down()), Direction.UP) || block instanceof BlockGoldPile && blockstate.get(LAYERS) == 8;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Deprecated
    public boolean canEntitySpawn(BlockState state, Entity entityIn) {
        return false;
    }

    @Override
    public @NotNull BlockState getStateForNeighborUpdate(BlockState stateIn, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull WorldAccess worldIn, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        return !stateIn.canPlaceAt(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }


    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState blockstate = context.getWorld().getBlockState(context.getBlockPos());
        if (blockstate.getBlock() == this) {
            int i = blockstate.get(LAYERS);
            return blockstate.with(LAYERS, Math.min(8, i + 1));
        } else {
            return super.getPlacementState(context);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }

    @Override
    public @NotNull ActionResult onUse(@NotNull BlockState state, @NotNull World worldIn, @NotNull BlockPos pos, PlayerEntity playerIn, @NotNull Hand handIn, @NotNull BlockHitResult resultIn) {
        ItemStack item = playerIn.getInventory().getMainHandStack();

        if (!item.isEmpty()) {
            if (item.getItem() != null) {
                if (item.getItem() == this.asItem()) {
                    if (!item.isEmpty()) {
                        if (state.get(LAYERS) < 8) {
                            worldIn.setBlockState(pos, state.with(LAYERS, state.get(LAYERS) + 1), 3);
                            if (!playerIn.isCreative()) {
                                item.decrement(1);

                                if (item.isEmpty()) {
                                    playerIn.getInventory().setStack(playerIn.getInventory().selectedSlot, ItemStack.EMPTY);
                                } else {
                                    playerIn.getInventory().setStack(playerIn.getInventory().selectedSlot, item);
                                }
                            }
                            return ActionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        return ActionResult.PASS;
    }
}