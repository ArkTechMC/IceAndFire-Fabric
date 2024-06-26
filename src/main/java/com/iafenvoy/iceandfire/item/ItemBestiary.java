package com.iafenvoy.iceandfire.item;

import com.google.common.primitives.Ints;
import com.iafenvoy.iceandfire.enums.EnumBestiaryPages;
import com.iafenvoy.iceandfire.screen.handler.BestiaryScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public class ItemBestiary extends Item {
    public ItemBestiary() {
        super(new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/.maxCount(1));
    }

    @Override
    public void onCraft(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        stack.getOrCreateNbt().putIntArray("Pages", new int[]{0});
    }

/*    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
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
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.openHandledScreen(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                ItemStack stack = playerIn.getStackInHand(handIn);
                NbtCompound compound = new NbtCompound();
                stack.writeNbt(compound);
                buf.writeNbt(compound);
            }

            @Override
            public Text getDisplayName() {
                return Text.translatable("bestiary_gui");
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                return new BestiaryScreenHandler(syncId, playerInventory);
            }
        });
        return new TypedActionResult<>(ActionResult.PASS, playerIn.getStackInHand(handIn));
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getNbt() == null) {
            stack.setNbt(new NbtCompound());
            stack.getNbt().putIntArray("Pages", new int[]{EnumBestiaryPages.INTRODUCTION.ordinal()});
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        if (stack.getNbt() != null) {
            if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 340) || InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 344)) {
                tooltip.add(Text.translatable("bestiary.contains").formatted(Formatting.GRAY));
                final Set<EnumBestiaryPages> pages = EnumBestiaryPages.containedPages(Ints.asList(stack.getNbt().getIntArray("Pages")));
                for (EnumBestiaryPages page : pages)
                    tooltip.add(Text.literal(Formatting.WHITE + "-").append(Text.translatable("bestiary." + EnumBestiaryPages.values()[page.ordinal()].toString().toLowerCase())).formatted(Formatting.GRAY));
            } else
                tooltip.add(Text.translatable("bestiary.hold_shift").formatted(Formatting.GRAY));
        }
    }
}
