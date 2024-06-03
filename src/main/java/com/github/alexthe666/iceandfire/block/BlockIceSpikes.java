package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;

public class BlockIceSpikes extends Block {
    protected static final VoxelShape AABB = Block.createCuboidShape(1, 0, 1, 15, 8, 15);
    public Item itemBlock;

    public BlockIceSpikes() {
        super(
            Settings
                .create()
                .mapColor(MapColor.PALE_PURPLE)
                .nonOpaque()
                .dynamicBounds()
                .ticksRandomly()
                .sounds(BlockSoundGroup.GLASS)
                .strength(2.5F)
                .requiresTool()
        );
    }

    @Override
    public @NotNull BlockState getStateForNeighborUpdate(BlockState stateIn, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull WorldAccess worldIn, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        return !stateIn.canPlaceAt(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean canPlaceAt(@NotNull BlockState state, @NotNull WorldView worldIn, BlockPos pos) {
        BlockPos blockpos = pos.down();
        return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos);
    }

    @Override
    public boolean isTransparent(@NotNull BlockState state, @NotNull BlockView reader, @NotNull BlockPos pos) {
        return true;
    }

    private boolean isValidGround(BlockState blockState, WorldView worldIn, BlockPos blockpos) {
        return blockState.isOpaque();
    }

    @Override
    public @NotNull VoxelShape getOutlineShape(@NotNull BlockState state, @NotNull BlockView worldIn, @NotNull BlockPos pos, @NotNull ShapeContext context) {
        return AABB;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockView worldIn, @NotNull BlockPos pos, @NotNull ShapeContext context) {
        return AABB;
    }

    @Override
    public void onSteppedOn(World worldIn, BlockPos pos, BlockState pState, Entity entityIn) {
        if (!(entityIn instanceof EntityIceDragon)) {
            entityIn.damage(worldIn.getDamageSources().cactus(), 1);
            if (entityIn instanceof LivingEntity && entityIn.getVelocity().x != 0 && entityIn.getVelocity().z != 0) {
                ((LivingEntity) entityIn).takeKnockback(0.5F, entityIn.getVelocity().x, entityIn.getVelocity().z);
            }
        }
    }

    @Override
    public boolean hasSidedTransparency(@NotNull BlockState state) {
        return true;
    }

}
