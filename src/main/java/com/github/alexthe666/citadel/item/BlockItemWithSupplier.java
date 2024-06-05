package com.github.alexthe666.citadel.item;

import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class BlockItemWithSupplier extends BlockItem {
    private final RegistryObject<Block> blockSupplier;

    public BlockItemWithSupplier(RegistryObject<Block> blockSupplier, Settings props) {
        super(null, props);
        this.blockSupplier = blockSupplier;
    }

    @Override
    public Block getBlock() {
        return this.blockSupplier.get();
    }

    public boolean canBeNested() {
        return !(this.blockSupplier.get() instanceof ShulkerBoxBlock);
    }

    public void onItemEntityDestroyed(ItemEntity p_150700_) {
        if (this.blockSupplier.get() instanceof ShulkerBoxBlock) {
            ItemStack itemstack = p_150700_.getStack();
            NbtCompound compoundtag = getBlockEntityNbt(itemstack);
            if (compoundtag != null && compoundtag.contains("Items", 9)) {
                NbtList listtag = compoundtag.getList("Items", 10);
                ItemUsage.spawnItemContents(p_150700_, listtag.stream().map(NbtCompound.class::cast).map(ItemStack::fromNbt));
            }
        }
    }
}
