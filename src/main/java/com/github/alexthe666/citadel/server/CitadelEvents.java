package com.github.alexthe666.citadel.server;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.CitadelConstants;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CitadelEvents {
    private int updateTimer;

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if(event.getLevel().getBlockState(event.getPos()).is(Blocks.LECTERN) && LecternBooks.isLecternBook(event.getItemStack())){
            event.getEntity().getCooldowns().addCooldown(event.getItemStack().getItem(), 1);
            BlockState oldLectern = event.getLevel().getBlockState(event.getPos());
            if(event.getLevel().getBlockEntity(event.getPos()) instanceof LecternBlockEntity oldBe && !oldBe.hasBook()){
                BlockState newLectern = Citadel.LECTERN.get().defaultBlockState().setValue(CitadelLecternBlock.FACING, oldLectern.get(LecternBlock.FACING)).setValue(CitadelLecternBlock.POWERED, oldLectern.get(LecternBlock.POWERED)).setValue(CitadelLecternBlock.HAS_BOOK, true);
                event.getLevel().setBlockAndUpdate(event.getPos(), newLectern);
                CitadelLecternBlockEntity newBe = new CitadelLecternBlockEntity(event.getPos(), newLectern);
                ItemStack bookCopy = event.getItemStack().copy();
                bookCopy.setCount(1);
                newBe.setBook(bookCopy);
                if(!event.getEntity().isCreative()){
                    event.getItemStack().shrink(1);
                }
                event.getLevel().setBlockEntity(newBe);
                event.getEntity().swing(event.getHand(), true);
                event.getLevel().playSound((PlayerEntity)null, event.getPos(), SoundEvents.ITEM_BOOK_PUT, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getOriginal() != null && CitadelEntityData.getCitadelTag(event.getOriginal()) != null) {
            CitadelEntityData.setCitadelTag(event.getEntity(), CitadelEntityData.getCitadelTag(event.getOriginal()));
        }
    }
}
