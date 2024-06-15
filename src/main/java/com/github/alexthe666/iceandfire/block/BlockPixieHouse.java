package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;

import static com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry.PIXIE_HOUSE;

public class BlockPixieHouse extends BlockWithEntity {
    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.Type.HORIZONTAL);

    public BlockPixieHouse() {
        super(Settings.create().mapColor(MapColor.OAK_TAN).instrument(Instrument.BASS).burnable().nonOpaque().dynamicBounds().strength(2.0F, 5.0F).ticksRandomly());
        this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    static String name(String type) {
        return "pixie_house_%s".formatted(type);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public void onStateReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        this.dropPixie(worldIn, pos);
        dropStack(worldIn, pos, new ItemStack(this, 0));
        super.onStateReplaced(state, worldIn, pos, newState, isMoving);
    }

    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
        this.checkFall(worldIn, pos);
    }

    private void checkFall(World worldIn, BlockPos pos) {
        if (!this.canPlaceBlockAt(worldIn, pos)) {
            worldIn.breakBlock(pos, true);
            this.dropPixie(worldIn, pos);
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    private boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return true;
    }

    public void dropPixie(World world, BlockPos pos) {
        if (world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof TileEntityPixieHouse house && house.hasPixie)
            house.releasePixie();
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState state, BlockEntityType<T> entityType) {
        return level.isClient ? checkType(entityType, PIXIE_HOUSE.get(), TileEntityPixieHouse::tickClient) : checkType(entityType, PIXIE_HOUSE.get(), TileEntityPixieHouse::tickServer);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityPixieHouse(pos, state);
    }
}
