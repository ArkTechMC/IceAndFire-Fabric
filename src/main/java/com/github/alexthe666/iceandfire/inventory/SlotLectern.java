package com.github.alexthe666.iceandfire.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;

public class SlotLectern extends Slot {

    public SlotLectern(Inventory inv, int slotIndex, int xPosition, int yPosition) {
        super(inv, slotIndex, xPosition, yPosition);
    }

    @Override
    public void markDirty() {
        this.inventory.markDirty();
    }

    @Override
    public void onTakeItem(@NotNull PlayerEntity playerIn, @NotNull ItemStack stack) {
        this.onCrafted(stack);
        super.onTakeItem(playerIn, stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes,
     * not ore and wood. Typically increases an internal count then calls
     * onCrafting(item).
     */
    @Override
    protected void onCrafted(@NotNull ItemStack stack, int amount) {
        this.onCrafted(stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes,
     * not ore and wood.
     */
    @Override
    protected void onCrafted(@NotNull ItemStack stack) {
        // thePlayer.addStat(StatList.objectCraftStats[Item.getIdFromItem(stack.getItem())],
        // stack.stackSize);
    }
}