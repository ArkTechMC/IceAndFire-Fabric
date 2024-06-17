package com.iafenvoy.iceandfire.entity.block;

import com.iafenvoy.iceandfire.registry.IafBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class BlockEntityMyrmexCocoon extends LootableContainerBlockEntity {
    private DefaultedList<ItemStack> chestContents = DefaultedList.ofSize(18, ItemStack.EMPTY);

    public BlockEntityMyrmexCocoon(BlockPos pos, BlockState state) {
        super(IafBlockEntities.MYRMEX_COCOON, pos, state);
    }

    @Override
    public int size() {
        return 18;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.chestContents)
            if (!itemstack.isEmpty())
                return false;
        return true;
    }

    @Override
    public void readNbt(NbtCompound compound) {
        super.readNbt(compound);
        this.chestContents = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);

        if (!this.deserializeLootTable(compound))
            Inventories.readNbt(compound, this.chestContents);
    }

    @Override
    public void writeNbt(NbtCompound compound) {
        if (!this.serializeLootTable(compound))
            Inventories.writeNbt(compound, this.chestContents);
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.myrmex_cocoon");
    }

    @Override
    protected ScreenHandler createScreenHandler(int id, PlayerInventory player) {
        return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X2, id, player, this, 2);
    }

    @Override
    public ScreenHandler createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
        return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X2, id, playerInventory, this, 2);
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }


    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return this.chestContents;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> itemsIn) {
    }

    @Override
    public void onOpen(PlayerEntity player) {
        this.checkLootInteraction(null);
        player.getWorld().playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_SLIME_JUMP, SoundCategory.BLOCKS, 1, 1, false);
    }

    @Override
    public void onClose(PlayerEntity player) {
        this.checkLootInteraction(null);
        player.getWorld().playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.BLOCKS, 1, 1, false);
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbtWithIdentifyingData();
    }

    public boolean isFull(ItemStack heldStack) {
        for (ItemStack itemstack : this.chestContents)
            if (itemstack.isEmpty() || heldStack != null && !heldStack.isEmpty() && ItemStack.areItemsEqual(itemstack, heldStack) && itemstack.getCount() + heldStack.getCount() < itemstack.getMaxCount())
                return false;
        return true;
    }
}
