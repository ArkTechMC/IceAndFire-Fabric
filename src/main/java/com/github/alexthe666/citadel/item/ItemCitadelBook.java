package com.github.alexthe666.citadel.item;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.client.gui.GuiCitadelBook;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemCitadelBook extends Item {

    public ItemCitadelBook(Settings properties) {
        super(properties);
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStackIn = playerIn.getStackInHand(handIn);
        if (worldIn.isClient) {
            MinecraftClient.getInstance().setScreen(new GuiCitadelBook(itemStackIn));
        }
        return new TypedActionResult<>(ActionResult.PASS, itemStackIn);
    }

}
