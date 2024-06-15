package com.github.alexthe666.iceandfire.inventory;

import com.github.alexthe666.iceandfire.data.delegate.EntityPropertyDelegate;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.item.ItemDragonArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class ContainerDragon extends ScreenHandler {
    private final Inventory dragonInventory;
    private final EntityPropertyDelegate propertyDelegate;

    public ContainerDragon(int i, PlayerInventory playerInventory) {
        this(i, new SimpleInventory(5), playerInventory, new EntityPropertyDelegate());
    }

    public ContainerDragon(int id, Inventory dragonInventory, PlayerInventory playerInventory, EntityPropertyDelegate propertyDelegate) {
        super(IafContainerRegistry.DRAGON_CONTAINER.get(), id);
        this.dragonInventory = dragonInventory;
        this.propertyDelegate = propertyDelegate;
        this.addProperties(this.propertyDelegate);
        byte b0 = 3;
        dragonInventory.onOpen(playerInventory.player);
        int i = (b0 - 4) * 18;
        this.addSlot(new Slot(dragonInventory, 0, 8, 54) {
            @Override
            public void markDirty() {
                this.inventory.markDirty();
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return super.canInsert(stack) && stack.getItem() instanceof BannerItem;
            }
        });
        this.addSlot(new Slot(dragonInventory, 1, 8, 18) {
            @Override
            public void markDirty() {
                this.inventory.markDirty();
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return super.canInsert(stack) && !stack.isEmpty() && stack.getItem() instanceof ItemDragonArmor && ((ItemDragonArmor) stack.getItem()).dragonSlot == 0;
            }
        });
        this.addSlot(new Slot(dragonInventory, 2, 8, 36) {
            @Override
            public void markDirty() {
                this.inventory.markDirty();
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return super.canInsert(stack) && !stack.isEmpty() && stack.getItem() instanceof ItemDragonArmor && ((ItemDragonArmor) stack.getItem()).dragonSlot == 1;
            }

        });
        this.addSlot(new Slot(dragonInventory, 3, 153, 18) {
            @Override
            public void markDirty() {
                this.inventory.markDirty();
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return super.canInsert(stack) && !stack.isEmpty() && stack.getItem() instanceof ItemDragonArmor && ((ItemDragonArmor) stack.getItem()).dragonSlot == 2;
            }
        });
        this.addSlot(new Slot(dragonInventory, 4, 153, 36) {
            @Override
            public void markDirty() {
                this.inventory.markDirty();
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return super.canInsert(stack) && !stack.isEmpty() && stack.getItem() instanceof ItemDragonArmor && ((ItemDragonArmor) stack.getItem()).dragonSlot == 3;
            }
        });
        int j;
        int k;
        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 150 + j * 18 + i));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 208 + i));
        }
    }

    @Override
    public boolean canUse(PlayerEntity playerIn) {
        Entity entity = playerIn.getWorld().getEntityById(this.getDragonId());
        return entity instanceof EntityDragonBase dragon && !dragon.hasInventoryChanged(this.dragonInventory) && this.dragonInventory.canPlayerUse(playerIn) && dragon.isAlive() && dragon.distanceTo(playerIn) < 8.0F;
    }

    @Override
    public ItemStack quickMove(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < this.dragonInventory.size()) {
                if (!this.insertItem(itemstack1, this.dragonInventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).canInsert(itemstack1) && !this.getSlot(1).hasStack()) {
                if (!this.insertItem(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (this.getSlot(2).canInsert(itemstack1) && !this.getSlot(2).hasStack()) {
                if (!this.insertItem(itemstack1, 2, 3, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (this.getSlot(3).canInsert(itemstack1) && !this.getSlot(3).hasStack()) {
                if (!this.insertItem(itemstack1, 3, 4, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (this.getSlot(4).canInsert(itemstack1) && !this.getSlot(4).hasStack()) {
                if (!this.insertItem(itemstack1, 4, 5, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (this.getSlot(0).canInsert(itemstack1)) {
                if (!this.insertItem(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.dragonInventory.size() <= 5 || !this.insertItem(itemstack1, 5, this.dragonInventory.size(), false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.setStackNoCallbacks(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return itemstack;
    }

    @Override
    public void onClosed(PlayerEntity playerIn) {
        super.onClosed(playerIn);
        this.dragonInventory.onClose(playerIn);
    }

    public int getDragonId() {
        return this.propertyDelegate.entityId;
    }
}