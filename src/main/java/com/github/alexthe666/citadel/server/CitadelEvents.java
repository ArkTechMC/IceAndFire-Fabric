package com.github.alexthe666.citadel.server;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.block.CitadelLecternBlock;
import com.github.alexthe666.citadel.server.block.CitadelLecternBlockEntity;
import com.github.alexthe666.citadel.server.block.LecternBooks;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CitadelEvents {
    public static ActionResult onRightClickBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getStackInHand(hand);
        BlockPos pos = hitResult.getBlockPos();
        if (world.getBlockState(pos).isOf(Blocks.LECTERN) && LecternBooks.isLecternBook(stack)) {
            player.getItemCooldownManager().set(stack.getItem(), 1);
            BlockState oldLectern = world.getBlockState(pos);
            if (world.getBlockEntity(pos) instanceof LecternBlockEntity oldBe && !oldBe.hasBook()) {
                BlockState newLectern = Citadel.LECTERN.get().getDefaultState().with(CitadelLecternBlock.FACING, oldLectern.get(LecternBlock.FACING)).with(CitadelLecternBlock.POWERED, oldLectern.get(LecternBlock.POWERED)).with(CitadelLecternBlock.HAS_BOOK, true);
                world.setBlockState(pos, newLectern);
                CitadelLecternBlockEntity newBe = new CitadelLecternBlockEntity(pos, newLectern);
                ItemStack bookCopy = stack.copy();
                bookCopy.setCount(1);
                newBe.setBook(bookCopy);
                if (!player.isCreative())
                    stack.decrement(1);
                world.addBlockEntity(newBe);
                player.swingHand(hand, true);
                world.playSound(null, pos, SoundEvents.ITEM_BOOK_PUT, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }

    public static void onPlayerClone(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        if (oldPlayer != null && CitadelEntityData.getCitadelTag(oldPlayer) != null)
            CitadelEntityData.setCitadelTag(newPlayer, CitadelEntityData.getCitadelTag(oldPlayer));
    }
}
