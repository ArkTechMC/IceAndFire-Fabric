package com.iafenvoy.iceandfire.screen.handler;

import com.iafenvoy.iceandfire.registry.IafScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;

public class MyrmexStaffScreenHandler extends ScreenHandler {
    private ItemStack staff = ItemStack.EMPTY;

    public MyrmexStaffScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(IafScreenHandlers.MYRMEX_STAFF_SCREEN, syncId);
    }

    public MyrmexStaffScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory);
        this.staff = ItemStack.fromNbt(buf.readNbt());
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public ItemStack getStaff() {
        return staff;
    }
}
