package com.iafenvoy.iceandfire.screen.handler;

import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.iceandfire.registry.IafScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Hand;

public class BestiaryScreenHandler extends ScreenHandler {
    private ItemStack bookStack = ItemStack.EMPTY;

    public BestiaryScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(IafScreenHandlers.BESTIARY_SCREEN, syncId);
    }

    public BestiaryScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory);
        this.bookStack = ItemStack.fromNbt(buf.readNbt());
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getStackInHand(Hand.MAIN_HAND).isOf(IafItems.BESTIARY) || player.getStackInHand(Hand.OFF_HAND).isOf(IafItems.BESTIARY);
    }

    public ItemStack getBook() {
        return this.bookStack;
    }
}
