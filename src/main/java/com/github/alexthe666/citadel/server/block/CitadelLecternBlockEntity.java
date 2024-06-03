package com.github.alexthe666.citadel.server.block;

import com.github.alexthe666.citadel.Citadel;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.LecternScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockPos;

public class CitadelLecternBlockEntity extends BlockEntity implements Clearable, NamedScreenHandlerFactory {
    private ItemStack book = ItemStack.EMPTY;

    private final Inventory bookAccess = new Inventory() {
        public int size() {
            return 1;
        }

        public boolean isEmpty() {
            return CitadelLecternBlockEntity.this.book.isEmpty();
        }

        public ItemStack getStack(int i) {
            return i == 0 ? CitadelLecternBlockEntity.this.book : ItemStack.EMPTY;
        }

        public ItemStack removeStack(int i, int j) {
            if (i == 0) {
                ItemStack itemstack = CitadelLecternBlockEntity.this.book.split(j);
                if (CitadelLecternBlockEntity.this.book.isEmpty()) {
                    CitadelLecternBlockEntity.this.onBookItemRemove();
                }

                return itemstack;
            } else {
                return ItemStack.EMPTY;
            }
        }

        public ItemStack removeStack(int i) {
            if (i == 0) {
                ItemStack itemstack = CitadelLecternBlockEntity.this.book;
                CitadelLecternBlockEntity.this.book = ItemStack.EMPTY;
                CitadelLecternBlockEntity.this.onBookItemRemove();
                return itemstack;
            } else {
                return ItemStack.EMPTY;
            }
        }

        public void setStack(int i, ItemStack stack) {
        }

        public int getMaxCountPerStack() {
            return 1;
        }

        public void markDirty() {
            CitadelLecternBlockEntity.this.markDirty();
        }

        public boolean canPlayerUse(PlayerEntity p_59588_) {
            if (CitadelLecternBlockEntity.this.world.getBlockEntity(CitadelLecternBlockEntity.this.pos) != CitadelLecternBlockEntity.this) {
                return false;
            } else {
                return !(p_59588_.squaredDistanceTo((double) CitadelLecternBlockEntity.this.pos.getX() + 0.5D, (double) CitadelLecternBlockEntity.this.pos.getY() + 0.5D, (double) CitadelLecternBlockEntity.this.pos.getZ() + 0.5D) > 64.0D) && CitadelLecternBlockEntity.this.hasBook();
            }
        }

        public boolean isValid(int i, ItemStack stack) {
            return false;
        }

        public void clear() {
        }
    };
    //dummy container for page turning
    private final PropertyDelegate dataAccess = new PropertyDelegate() {
        public int get(int i) {
            return 0;
        }

        public void set(int i, int j) {
        }

        public int size() {
            return 1;
        }
    };

    public CitadelLecternBlockEntity(BlockPos pos, BlockState state) {
        super(Citadel.LECTERN_BE.get(), pos, state);
    }

    public ItemStack getBook() {
        return this.book;
    }

    public boolean hasBook() {
        return LecternBooks.isLecternBook(book);
    }

    public void setBook(ItemStack stack) {
        this.setBook(stack, null);
    }

    void onBookItemRemove() {
        LecternBlock.setHasBook(null, this.getWorld(), this.getPos(), this.getCachedState(), false);
    }

    public void setBook(ItemStack itemStack, @Nullable PlayerEntity player) {
        this.book = itemStack;
        this.markDirty();
    }

    public int getRedstoneSignal() {
        return this.hasBook() ? 1 : 0;
    }

    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        if (tag.contains("Book", 10)) {
            this.book = ItemStack.fromNbt(tag.getCompound("Book"));
        } else {
            this.book = ItemStack.EMPTY;
        }
    }

    protected void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        if (!this.getBook().isEmpty()) {
            tag.put("Book", this.getBook().writeNbt(new NbtCompound()));
        }
    }

    public void clear() {
        this.setBook(ItemStack.EMPTY);
    }

    public ScreenHandler createMenu(int i, PlayerInventory inventory, PlayerEntity player) {
        return new LecternScreenHandler(i, this.bookAccess, this.dataAccess);
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    public Text getDisplayName() {
        return Text.translatable("container.lectern");
    }
}