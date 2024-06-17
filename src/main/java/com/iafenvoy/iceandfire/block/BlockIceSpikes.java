package com.iafenvoy.iceandfire.block;

import com.iafenvoy.iceandfire.entity.EntityIceDragon;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class BlockIceSpikes extends Block {
    protected static final VoxelShape VOXEL_SHAPE = Block.createCuboidShape(1, 0, 1, 15, 8, 15);

    public BlockIceSpikes() {
        super(Settings.create().mapColor(MapColor.PALE_PURPLE).nonOpaque().dynamicBounds().ticksRandomly().sounds(BlockSoundGroup.GLASS).strength(2.5F).requiresTool());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess worldIn, BlockPos currentPos, BlockPos facingPos) {
        return !stateIn.canPlaceAt(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView worldIn, BlockPos pos) {
        BlockPos blockpos = pos.down();
        return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos);
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView reader, BlockPos pos) {
        return true;
    }

    private boolean isValidGround(BlockState blockState, WorldView worldIn, BlockPos blockpos) {
        return blockState.isOpaque();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
        return VOXEL_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
        return VOXEL_SHAPE;
    }

    @Override
    public void onSteppedOn(World worldIn, BlockPos pos, BlockState pState, Entity entityIn) {
        if (!(entityIn instanceof EntityIceDragon)) {
            entityIn.damage(worldIn.getDamageSources().cactus(), 1);
            if (entityIn instanceof LivingEntity livingEntity && entityIn.getVelocity().x != 0 && entityIn.getVelocity().z != 0)
                livingEntity.takeKnockback(0.5F, entityIn.getVelocity().x, entityIn.getVelocity().z);
        }
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

}
