package com.iafenvoy.iceandfire.screen.slot;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class BannerSlot extends Slot {
    public BannerSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return super.canInsert(stack) && stack.getItem() instanceof BannerItem;
    }
}
