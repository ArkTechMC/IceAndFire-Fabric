package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityDreadLichSkull;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ItemLichStaff extends Item {

    public ItemLichStaff() {
        super(new Settings().maxDamage(100));
    }

    @Override
    public boolean canRepair(@NotNull ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == IafItemRegistry.DREAD_SHARD.get() || super.canRepair(toRepair, repair);
    }

    @Override
    public @NotNull TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, @NotNull Hand hand) {
        ItemStack itemStackIn = playerIn.getStackInHand(hand);
        if (!worldIn.isClient) {
            playerIn.setCurrentHand(hand);
            playerIn.swingHand(hand);
            double d2 = playerIn.getRotationVector().x;
            double d3 = playerIn.getRotationVector().y;
            double d4 = playerIn.getRotationVector().z;
            float inaccuracy = 1.0F;
            d2 = d2 + playerIn.getRandom().nextGaussian() * 0.007499999832361937D * inaccuracy;
            d3 = d3 + playerIn.getRandom().nextGaussian() * 0.007499999832361937D * inaccuracy;
            d4 = d4 + playerIn.getRandom().nextGaussian() * 0.007499999832361937D * inaccuracy;
            EntityDreadLichSkull charge = new EntityDreadLichSkull(IafEntityRegistry.DREAD_LICH_SKULL.get(), worldIn,
                playerIn, 6);
            charge.setVelocity(playerIn.getPitch(), playerIn.getYaw(), 0.0F, 7.0F, 1.0F);
            charge.setPosition(playerIn.getX(), playerIn.getY() + 1, playerIn.getZ());
            worldIn.spawnEntity(charge);
            charge.setVelocity(d2, d3, d4, 1, 1);
            playerIn.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1F, 0.75F + 0.5F * playerIn.getRandom().nextFloat());
            itemStackIn.damage(1, playerIn, (player) -> {
                player.sendToolBreakStatus(hand);
            });
            playerIn.getItemCooldownManager().set(this, 4);
        }
        return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, itemStackIn);
    }
}