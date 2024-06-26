package com.iafenvoy.iceandfire.screen.handler;

import com.iafenvoy.iceandfire.registry.IafScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class MyrmexAddRoomScreenHandler extends ScreenHandler {
    private ItemStack staff = ItemStack.EMPTY;
    private BlockPos interactPos = BlockPos.ORIGIN;
    private Direction facing = Direction.UP;

    public MyrmexAddRoomScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(IafScreenHandlers.MYRMEX_ADD_ROOM_SCREEN, syncId);
    }

    public MyrmexAddRoomScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory);
        this.staff = ItemStack.fromNbt(buf.readNbt());
        this.interactPos = BlockPos.fromLong(buf.readLong());
        this.facing = buf.readEnumConstant(Direction.class);
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

    public BlockPos getInteractPos() {
        return interactPos;
    }

    public Direction getFacing() {
        return facing;
    }
}
