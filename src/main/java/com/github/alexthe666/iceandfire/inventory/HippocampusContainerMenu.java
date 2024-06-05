package com.github.alexthe666.iceandfire.inventory;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.NotNull;

public class HippocampusContainerMenu extends ScreenHandler {
    private final Inventory hippocampusInventory;
    private final EntityHippocampus hippocampus;
    private final PlayerEntity player;

    public HippocampusContainerMenu(int i, PlayerInventory playerInventory) {
        this(i, new SimpleInventory(18), playerInventory, null);
    }

    public HippocampusContainerMenu(int id, Inventory hippoInventory, PlayerInventory playerInventory, EntityHippocampus hippocampus) {
        super(IafContainerRegistry.HIPPOCAMPUS_CONTAINER.get(), id);
        this.hippocampusInventory = hippoInventory;
        if (hippocampus == null && IceAndFire.PROXY.getReferencedMob() instanceof EntityHippocampus) {
            hippocampus = (EntityHippocampus) IceAndFire.PROXY.getReferencedMob();
        }
        this.hippocampus = hippocampus;
        this.player = playerInventory.player;
        this.hippocampusInventory.onOpen(this.player);

        // Saddle slot
        this.addSlot(new Slot(this.hippocampusInventory, 0, 8, 18) {
            @Override
            public boolean canInsert(@NotNull ItemStack stack) {
                return stack.getItem() == Items.SADDLE && !this.hasStack();
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        });

        // Chest slot
        this.addSlot(new Slot(this.hippocampusInventory, 1, 8, 36) {
            @Override
            public boolean canInsert(@NotNull ItemStack stack) {
                return stack.getItem() == Blocks.CHEST.asItem() && !this.hasStack();
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        });

        // Armor slot
        this.addSlot(new Slot(this.hippocampusInventory, 2, 8, 52) {

            @Override
            public boolean canInsert(@NotNull ItemStack stack) {
                return EntityHippocampus.getIntFromArmor(stack) != 0;
            }

            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        });

        // Create the slots for the inventory
        if (this.hippocampus.isChested()) {
            for (int k = 0; k < 3; ++k) {
                for (int l = 0; l < (hippocampus).getInventoryColumns(); ++l) {
                    this.addSlot(new Slot(hippoInventory, 3 + l + k * (hippocampus).getInventoryColumns(), 80 + l * 18, 18 + k * 18));
                }
            }
        }

        for (int i1 = 0; i1 < 3; ++i1) {
            for (int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(this.player.getInventory(), k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + -18));
            }
        }

        for (int j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new Slot(this.player.getInventory(), j1, 8 + j1 * 18, 142));
        }

    }

    @Override
    public @NotNull ItemStack quickMove(@NotNull PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            int containerSize = this.hippocampusInventory.size();
            if (index < containerSize) {
                if (!this.insertItem(itemstack1, containerSize, this.slots.size(), true)) {
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

            } else if (this.getSlot(0).canInsert(itemstack1)) {
                if (!this.insertItem(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (containerSize <= 3 || !this.insertItem(itemstack1, 3, containerSize, false)) {
                int j = containerSize + 27;
                int k = j + 9;
                if (index >= j && index < k) {
                    if (!this.insertItem(itemstack1, containerSize, j, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= containerSize && index < j) {
                    if (!this.insertItem(itemstack1, j, k, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.insertItem(itemstack1, j, j, false)) {
                    return ItemStack.EMPTY;
                }

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
    public boolean canUse(@NotNull PlayerEntity playerIn) {
        return !this.hippocampus.hasInventoryChanged(this.hippocampusInventory) && this.hippocampusInventory.canPlayerUse(playerIn) && this.hippocampus.isAlive() && this.hippocampus.distanceTo(playerIn) < 8.0F;
    }

    @Override
    public void onClosed(@NotNull PlayerEntity playerIn) {
        super.onClosed(playerIn);
        this.hippocampusInventory.onClose(playerIn);
    }
}