package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityMyrmexCocoon;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BlockMyrmexCocoon extends BlockWithEntity {


    public BlockMyrmexCocoon() {
        super(
            Settings
                .create()
                .mapColor(MapColor.DIRT_BROWN)
                .strength(2.5F)
                .nonOpaque()
                .dynamicBounds()
                .sounds(BlockSoundGroup.SLIME)
        );
    }

    @Override
    public @NotNull BlockRenderType getRenderType(@NotNull BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(@NotNull BlockState state, World worldIn, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof Inventory) {
            ItemScatterer.spawn(worldIn, pos, (Inventory) tileentity);
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
    public BlockEntity createBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TileEntityMyrmexCocoon(pos, state);
    }
}
