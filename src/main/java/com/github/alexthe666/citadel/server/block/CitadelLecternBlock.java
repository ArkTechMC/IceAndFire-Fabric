package com.github.alexthe666.citadel.server.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CitadelLecternBlock extends LecternBlock {

    public CitadelLecternBlock(Settings properties) {
        super(properties);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState blockState) {
        return new CitadelLecternBlockEntity(pos, blockState);
    }

    public ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (level.isClient && blockEntity instanceof CitadelLecternBlockEntity lecternBlockEntity && lecternBlockEntity.hasBook()) {
            ItemStack book = lecternBlockEntity.getBook();
            if (!book.isEmpty() && !player.getItemCooldownManager().isCoolingDown(book.getItem())) {
                book.use(level, player, hand);
            }
        }
        return ActionResult.success(level.isClient);

    }


    @Override
    public int getComparatorOutput(BlockState state, World level, BlockPos pos) {
        if (state.get(HAS_BOOK)) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof CitadelLecternBlockEntity) {
                return ((CitadelLecternBlockEntity) blockentity).getRedstoneSignal();
            }
        }

        return 0;
    }

    @Override
    public void onStateReplaced(BlockState state, World level, BlockPos pos, BlockState replaceState, boolean b) {
        if (!state.isOf(replaceState.getBlock())) {
            if (state.get(HAS_BOOK)) {
                this.popCitadelBook(state, level, pos);
            }

            if (state.get(POWERED)) {
                level.updateNeighborsAlways(pos.down(), this);
            }

            super.onStateReplaced(state, level, pos, replaceState, b);
        }
    }

    private void popCitadelBook(BlockState state, World level, BlockPos pos) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof CitadelLecternBlockEntity lecternblockentity) {
            Direction direction = state.get(FACING);
            ItemStack itemstack = lecternblockentity.getBook().copy();
            float f = 0.25F * (float) direction.getOffsetX();
            float f1 = 0.25F * (float) direction.getOffsetZ();
            ItemEntity itementity = new ItemEntity(level, (double) pos.getX() + 0.5D + (double) f, pos.getY() + 1, (double) pos.getZ() + 0.5D + (double) f1, itemstack);
            itementity.setToDefaultPickupDelay();
            level.spawnEntity(itementity);
            lecternblockentity.clear();
        }

    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockView level, BlockPos pos, PlayerEntity player) {
        return new ItemStack(Items.LECTERN);
    }
}
