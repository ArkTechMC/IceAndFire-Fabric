package com.iafenvoy.iceandfire.screen.handler;

import com.iafenvoy.iceandfire.entity.EntityHippogryph;
import com.iafenvoy.iceandfire.registry.IafScreenHandlers;
import com.iafenvoy.uranus.data.EntityPropertyDelegate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class HippogryphScreenHandler extends ScreenHandler {
    private final Inventory hippogryphInventory;
    private EntityHippogryph hippogryph;
    private final EntityPropertyDelegate propertyDelegate;

    public HippogryphScreenHandler(int i, PlayerInventory playerInventory) {
        this(i, new SimpleInventory(18), playerInventory, new EntityPropertyDelegate());
    }

    public HippogryphScreenHandler(int id, Inventory hippogryphInventory, PlayerInventory playerInventory, EntityPropertyDelegate propertyDelegate) {
        super(IafScreenHandlers.HIPPOGRYPH_SCREEN, id);
        this.hippogryphInventory = hippogryphInventory;
        this.propertyDelegate = propertyDelegate;
        this.addProperties(this.propertyDelegate);
        PlayerEntity player = playerInventory.player;
        this.hippogryphInventory.onOpen(player);
        this.hippogryph = (EntityHippogryph) player.getWorld().getEntityById(propertyDelegate.entityId);
        this.addSlot(new Slot(this.hippogryphInventory, 0, 8, 18) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() == Items.SADDLE && !this.hasStack();
            }

            @Override
            public void markDirty() {
                super.markDirty();
                if (HippogryphScreenHandler.this.hippogryph != null)
                    HippogryphScreenHandler.this.hippogryph.setSaddled(this.hasStack() && this.getStack().isOf(Items.SADDLE));
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        });
        this.addSlot(new Slot(this.hippogryphInventory, 1, 8, 36) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.CHEST) && !this.hasStack();
            }

            @Override
            public void markDirty() {
                super.markDirty();
                if (HippogryphScreenHandler.this.hippogryph != null)
                    HippogryphScreenHandler.this.hippogryph.setChested(this.hasStack() && this.getStack().isOf(Items.CHEST));
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        });
        this.addSlot(new Slot(this.hippogryphInventory, 2, 8, 52) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return EntityHippogryph.getIntFromArmor(stack) != 0;
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public void markDirty() {
                super.markDirty();
                if (HippogryphScreenHandler.this.hippogryph != null)
                    HippogryphScreenHandler.this.hippogryph.setArmor(this.hasStack() ? EntityHippogryph.getIntFromArmor(this.getStack()) : 0);
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        });

        for (int k = 0; k < 3; ++k)
            for (int l = 0; l < 5; ++l)
                this.addSlot(new Slot(this.hippogryphInventory, 3 + l + k * 5, 80 + l * 18, 18 + k * 18) {
                    @Override
                    public boolean isEnabled() {
                        HippogryphScreenHandler.this.refreshEntity(player);
                        return HippogryphScreenHandler.this.hippogryph != null && HippogryphScreenHandler.this.hippogryph.isChested();
                    }

                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return HippogryphScreenHandler.this.hippogryph != null && HippogryphScreenHandler.this.hippogryph.isChested();
                    }
                });

        for (int i1 = 0; i1 < 3; ++i1)
            for (int k1 = 0; k1 < 9; ++k1)
                this.addSlot(new Slot(playerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 - 18));

        for (int j1 = 0; j1 < 9; ++j1)
            this.addSlot(new Slot(playerInventory, j1, 8 + j1 * 18, 142));
    }


    @Override
    public ItemStack quickMove(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < this.hippogryphInventory.size()) {
                if (!this.insertItem(itemstack1, this.hippogryphInventory.size(), this.slots.size(), true))
                    return ItemStack.EMPTY;
            } else if (this.getSlot(1).canInsert(itemstack1) && !this.getSlot(1).hasStack()) {
                if (!this.insertItem(itemstack1, 1, 2, false))
                    return ItemStack.EMPTY;
            } else if (this.getSlot(2).canInsert(itemstack1) && !this.getSlot(2).hasStack()) {
                if (!this.insertItem(itemstack1, 2, 3, false))
                    return ItemStack.EMPTY;
            } else if (this.getSlot(0).canInsert(itemstack1)) {
                if (!this.insertItem(itemstack1, 0, 1, false))
                    return ItemStack.EMPTY;
            } else if (this.hippogryphInventory.size() <= 3 || !this.insertItem(itemstack1, 3, this.hippogryphInventory.size(), false))
                return ItemStack.EMPTY;
            if (itemstack1.isEmpty())
                slot.setStackNoCallbacks(ItemStack.EMPTY);
            else
                slot.markDirty();
        }
        return itemstack;
    }

    @Override
    public boolean canUse(PlayerEntity playerIn) {
        this.refreshEntity(playerIn);
        return this.hippogryphInventory.canPlayerUse(playerIn) && this.hippogryph.isAlive() && this.hippogryph.distanceTo(playerIn) < 8.0F;
    }

    @Override
    public void onClosed(PlayerEntity playerIn) {
        super.onClosed(playerIn);
        this.hippogryphInventory.onClose(playerIn);
    }

    public int getHippogryphId() {
        return this.propertyDelegate.entityId;
    }

    private void refreshEntity(PlayerEntity player) {
        if (player.getWorld().getEntityById(this.getHippogryphId()) instanceof EntityHippogryph h)
            this.hippogryph = h;
    }
}