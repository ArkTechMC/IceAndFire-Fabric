package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.block.BlockEntityMyrmexCocoon;
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
import net.minecraft.world.World;

public class BlockMyrmexCocoon extends BlockWithEntity {


    public BlockMyrmexCocoon() {
        super(Settings.create().mapColor(MapColor.DIRT_BROWN).strength(2.5F).nonOpaque().dynamicBounds().sounds(BlockSoundGroup.SLIME));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onStateReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof Inventory) {
            ItemScatterer.spawn(worldIn, pos, (Inventory) tileentity);
            worldIn.updateComparators(pos, this);
        }
        super.onStateReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockHitResult hit) {
        if (!player.isSneaking()) {
            if (!worldIn.isClient) {
                NamedScreenHandlerFactory screenHandlerFactory = this.createScreenHandlerFactory(state, worldIn, pos);
                if (screenHandlerFactory != null)
                    player.openHandledScreen(screenHandlerFactory);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityMyrmexCocoon(pos, state);
    }
}
