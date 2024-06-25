package com.iafenvoy.iceandfire.screen.handler;

import com.iafenvoy.iceandfire.data.delegate.DragonForgePropertyDelegate;
import com.iafenvoy.iceandfire.registry.IafScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

//TODO: All containers etc should be rewritten
public class DragonForgeScreenHandler extends ScreenHandler {
    protected final World world;
    private final Inventory tileFurnace;
    private final DragonForgePropertyDelegate propertyDelegate;
    public int fireType;

    public DragonForgeScreenHandler(int i, PlayerInventory playerInventory) {
        this(i, new SimpleInventory(3), playerInventory, new DragonForgePropertyDelegate());
    }

    public DragonForgeScreenHandler(int id, Inventory furnaceInventory, PlayerInventory playerInventory, DragonForgePropertyDelegate propertyDelegate) {
        super(IafScreenHandlers.DRAGON_FORGE_SCREEN, id);
        this.tileFurnace = furnaceInventory;
        this.world = playerInventory.player.getWorld();
        this.propertyDelegate = propertyDelegate;
        this.addProperties(this.propertyDelegate);
        this.fireType = this.propertyDelegate.fireType;
        this.addSlot(new Slot(furnaceInventory, 0, 68, 34));
        this.addSlot(new Slot(furnaceInventory, 1, 86, 34));
        this.addSlot(new FurnaceOutputSlot(playerInventory.player, furnaceInventory, 2, 148, 35));
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
        for (int k = 0; k < 9; ++k)
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
    }

    @Override
    public boolean canUse(PlayerEntity playerIn) {
        return this.tileFurnace.canPlayerUse(playerIn);
    }

    @Override
    public ItemStack quickMove(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 2) {
                if (!this.insertItem(itemstack1, 3, 39, true))
                    return ItemStack.EMPTY;
                slot.onQuickTransfer(itemstack1, itemstack);
            } else if (index != 1 && index != 0) {
                if (this.fireType == 0) {
                    if (!this.insertItem(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 30) {
                    if (!this.insertItem(itemstack1, 30, 39, false))
                        return ItemStack.EMPTY;
                } else if (index < 39 && !this.insertItem(itemstack1, 3, 30, false))
                    return ItemStack.EMPTY;
            } else if (!this.insertItem(itemstack1, 3, 39, false))
                return ItemStack.EMPTY;

            if (itemstack1.isEmpty()) slot.setStackNoCallbacks(ItemStack.EMPTY);
            else slot.markDirty();

            if (itemstack1.getCount() == itemstack.getCount())
                return ItemStack.EMPTY;

            slot.onTakeItem(playerIn, itemstack1);
        }

        return itemstack;
    }

    public DragonForgePropertyDelegate getPropertyDelegate() {
        return this.propertyDelegate;
    }
}
