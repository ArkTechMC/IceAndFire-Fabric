package com.iafenvoy.iceandfire.item;

import com.iafenvoy.iceandfire.enums.BestiaryPages;
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
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
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
import java.util.Locale;
import java.util.Set;

public class ItemBestiary extends Item {
    public ItemBestiary() {
        super(new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/.maxCount(1));
    }

    @Override
    public void onCraft(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        stack.getOrCreateNbt().putIntArray("Pages", new int[]{0});
    }

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
            NbtList list = new NbtList();
            list.add(NbtString.of(BestiaryPages.INTRODUCTION.getName()));
            stack.getOrCreateNbt().put("Pages", list);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        if (stack.getNbt() != null)
            if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 340) || InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 344)) {
                tooltip.add(Text.translatable("bestiary.contains").formatted(Formatting.GRAY));
                final Set<BestiaryPages> pages = BestiaryPages.containedPages(stack.getNbt().getList("Pages", NbtElement.STRING_TYPE).stream().map(NbtElement::asString).toList());
                for (BestiaryPages page : pages)
                    tooltip.add(Text.literal(Formatting.WHITE + "-").append(Text.translatable("bestiary." + page.getName().toLowerCase(Locale.ROOT))).formatted(Formatting.GRAY));
            } else
                tooltip.add(Text.translatable("bestiary.hold_shift").formatted(Formatting.GRAY));
    }
}
