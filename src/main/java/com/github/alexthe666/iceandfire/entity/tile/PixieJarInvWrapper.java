package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class PixieJarInvWrapper implements IItemHandlerModifiable {

    private final TileEntityJar tile;

    public PixieJarInvWrapper(TileEntityJar tile) {
        this.tile = tile;
    }

    public static LazyOptional<IItemHandler> create(TileEntityJar trashCan) {
        return LazyOptional.of(() -> new PixieJarInvWrapper(trashCan));
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        // this.tile.hasProduced = false;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.tile.hasProduced ? new ItemStack(IafItemRegistry.PIXIE_DUST.get()) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (this.tile.hasProduced) {
            if (!simulate)
                this.tile.hasProduced = false;
            return new ItemStack(IafItemRegistry.PIXIE_DUST.get());
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return false;
    }
}
