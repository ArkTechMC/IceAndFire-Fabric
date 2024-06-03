package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPodium;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockPodium extends BlockWithEntity {

    protected static final VoxelShape AABB = Block.createCuboidShape(2, 0, 2, 14, 23, 14);

    public BlockPodium() {
        super(
            Settings
                .create()
                .mapColor(MapColor.OAK_TAN)
                .instrument(Instrument.BASS)
                .burnable()
                .nonOpaque()
                .dynamicBounds()
                .strength(2.0F)
                .sounds(BlockSoundGroup.WOOD)
        );
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
    public void onStateReplaced(@NotNull BlockState state, World worldIn, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof TileEntityPodium) {
            ItemScatterer.spawn(worldIn, pos, (TileEntityPodium) tileentity);
            worldIn.updateComparators(pos, this);
        }
        super.onStateReplaced(state, worldIn, pos, newState, isMoving);
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
    public @NotNull BlockRenderType getRenderType(@NotNull BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TileEntityPodium(pos, state);
    }

}