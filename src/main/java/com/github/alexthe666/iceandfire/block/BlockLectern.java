package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityLectern;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry.IAF_LECTERN;

public class BlockLectern extends BlockWithEntity {
    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.Type.HORIZONTAL);
    protected static final VoxelShape AABB = Block.createCuboidShape(4, 0, 4, 12, 19, 12);

    public BlockLectern() {
        super(
            Settings
                .create()
                .mapColor(MapColor.OAK_TAN)
                .instrument(Instrument.BASS)
                .burnable()
                .nonOpaque()
                .dynamicBounds()
                .strength(2, 5)
                .sounds(BlockSoundGroup.WOOD)
        );

        this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public @NotNull VoxelShape getOutlineShape(@NotNull BlockState state, @NotNull BlockView worldIn, @NotNull BlockPos pos, @NotNull ShapeContext context) {
        return AABB;
    }

    @Override
    public @NotNull BlockState rotate(BlockState p_185499_1_, BlockRotation p_185499_2_) {
        return p_185499_1_.with(FACING, p_185499_2_.rotate(p_185499_1_.get(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState p_185471_1_, BlockMirror p_185471_2_) {
        return p_185471_1_.rotate(p_185471_2_.getRotation(p_185471_1_.get(FACING)));
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockView worldIn, @NotNull BlockPos pos, @NotNull ShapeContext context) {
        return AABB;
    }


    @Override
    public void onStateReplaced(@NotNull BlockState state, World worldIn, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);

        if (tileentity instanceof TileEntityLectern) {
            ItemScatterer.spawn(worldIn, pos, (TileEntityLectern) tileentity);
            worldIn.updateComparators(pos, this);
        }
        super.onStateReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World p_153182_, @NotNull BlockState p_153183_, @NotNull BlockEntityType<T> entityType) {
        return p_153182_.isClient ? checkType(entityType, IAF_LECTERN.get(), TileEntityLectern::bookAnimationTick) : null;
    }


    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @NotNull BlockRenderType getRenderType(@NotNull BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public @NotNull ActionResult onUse(@NotNull BlockState state, @NotNull World worldIn, @NotNull BlockPos pos, PlayerEntity player, @NotNull Hand handIn, @NotNull BlockHitResult hit) {
        if (!player.isSneaking()) {
            if (worldIn.isClient) {
                IceAndFire.PROXY.setRefrencedTE(worldIn.getBlockEntity(pos));
            } else {
                NamedScreenHandlerFactory inamedcontainerprovider = this.createScreenHandlerFactory(state, worldIn, pos);
                if (inamedcontainerprovider != null) {
                    player.openHandledScreen(inamedcontainerprovider);
                }
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    @Override
    public BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TileEntityLectern(pos, state);
    }
}