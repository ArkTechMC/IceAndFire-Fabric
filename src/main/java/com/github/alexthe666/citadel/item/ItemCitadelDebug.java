package com.github.alexthe666.citadel.item;

import com.github.alexthe666.citadel.server.tick.ServerTickRateTracker;
import com.github.alexthe666.citadel.server.tick.modifier.CelestialTickRateModifier;
import com.github.alexthe666.citadel.server.tick.modifier.LocalPositionTickRateModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemCitadelDebug extends Item {

    public ItemCitadelDebug(Settings properties) {
        super(properties);
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStackIn = playerIn.getStackInHand(handIn);
        playerIn.getItemCooldownManager().set(this, 15);
        return new TypedActionResult<ItemStack>(ActionResult.PASS, itemStackIn);
    }

}
