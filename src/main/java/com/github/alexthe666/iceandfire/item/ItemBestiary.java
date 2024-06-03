package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.google.common.primitives.Ints;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class ItemBestiary extends Item {

    public ItemBestiary() {
        super(new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/.maxCount(1));
    }

    @Override
    public void onCraft(ItemStack stack, @NotNull World worldIn, @NotNull PlayerEntity playerIn) {
        stack.setNbt(new NbtCompound());
        stack.getNbt().putIntArray("Pages", new int[]{0});

    }

/*    @Override
    public void fillItemCategory(@NotNull CreativeModeTab group, @NotNull NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            items.add(new ItemStack(this));
            ItemStack stack = new ItemStack(IafItemRegistry.BESTIARY.get());
            stack.setTag(new CompoundTag());
            int[] pages = new int[EnumBestiaryPages.values().length];
            for (int i = 0; i < EnumBestiaryPages.values().length; i++) {
                pages[i] = i;
            }
            stack.getTag().putIntArray("Pages", pages);
            items.add(stack);
        }
    }*/

    @Override
    public @NotNull TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, @NotNull Hand handIn) {
        ItemStack itemStackIn = playerIn.getStackInHand(handIn);
        if (worldIn.isClient) {
            IceAndFire.PROXY.openBestiaryGui(itemStackIn);
        }
        return new TypedActionResult<>(ActionResult.PASS, itemStackIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, @NotNull World worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getNbt() == null) {
            stack.setNbt(new NbtCompound());
            stack.getNbt().putIntArray("Pages", new int[]{EnumBestiaryPages.INTRODUCTION.ordinal()});

        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, @NotNull List<Text> tooltip, @NotNull TooltipContext flagIn) {
        if (stack.getNbt() != null) {
            if (IceAndFire.PROXY.shouldSeeBestiaryContents()) {
                tooltip.add(Text.translatable("bestiary.contains").formatted(Formatting.GRAY));
                final Set<EnumBestiaryPages> pages = EnumBestiaryPages
                    .containedPages(Ints.asList(stack.getNbt().getIntArray("Pages")));
                for (EnumBestiaryPages page : pages) {
                    tooltip.add(Text.literal(Formatting.WHITE + "-").append(Text.translatable("bestiary." + EnumBestiaryPages.values()[page.ordinal()].toString().toLowerCase())).formatted(Formatting.GRAY));
                }
            } else {
                tooltip.add(Text.translatable("bestiary.hold_shift").formatted(Formatting.GRAY));
            }

        }
    }

}
