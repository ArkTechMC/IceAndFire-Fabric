package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityPixieCharge;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemPixieWand extends Item {

    public ItemPixieWand() {
        super(new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/.maxCount(1).maxDamage(500));
    }


    @Override
    public @NotNull TypedActionResult<ItemStack> use(@NotNull World worldIn, PlayerEntity playerIn, @NotNull Hand hand) {
        ItemStack itemStackIn = playerIn.getStackInHand(hand);
        boolean flag = playerIn.isCreative() || EnchantmentHelper.getLevel(Enchantments.INFINITY, itemStackIn) > 0;
        ItemStack itemstack = this.findAmmo(playerIn);
        playerIn.setCurrentHand(hand);
        playerIn.swingHand(hand);
        if (!itemstack.isEmpty() || flag) {
            boolean flag1 = playerIn.isCreative() || this.isInfinite(itemstack, itemStackIn, playerIn);
            if (!flag1) {
                itemstack.decrement(1);
                if (itemstack.isEmpty()) {
                    playerIn.getInventory().removeOne(itemstack);
                }
            }
            double d2 = playerIn.getRotationVector().x;
            double d3 = playerIn.getRotationVector().y;
            double d4 = playerIn.getRotationVector().z;
            float inaccuracy = 1.0F;
            d2 = d2 + playerIn.getRandom().nextGaussian() * 0.007499999832361937D * inaccuracy;
            d3 = d3 + playerIn.getRandom().nextGaussian() * 0.007499999832361937D * inaccuracy;
            d4 = d4 + playerIn.getRandom().nextGaussian() * 0.007499999832361937D * inaccuracy;
            EntityPixieCharge charge = new EntityPixieCharge(IafEntityRegistry.PIXIE_CHARGE.get(), worldIn, playerIn,
                    d2, d3, d4);
            charge.setPosition(playerIn.getX(), playerIn.getY() + 1, playerIn.getZ());
            if (!worldIn.isClient) {
                worldIn.spawnEntity(charge);
            }
            playerIn.playSound(IafSoundRegistry.PIXIE_WAND, 1F, 0.75F + 0.5F * playerIn.getRandom().nextFloat());
            itemstack.damage(1, playerIn, (player) -> {
                player.sendToolBreakStatus(playerIn.getActiveHand());
            });
            playerIn.getItemCooldownManager().set(this, 5);
        }
        return new TypedActionResult<ItemStack>(ActionResult.PASS, itemStackIn);
    }

    public boolean isInfinite(ItemStack stack, ItemStack bow, PlayerEntity player) {
        int enchant = EnchantmentHelper.getLevel(Enchantments.INFINITY, bow);
        return enchant > 0 && stack.getItem() == IafItemRegistry.PIXIE_DUST.get();
    }

    private ItemStack findAmmo(PlayerEntity player) {
        if (this.isAmmo(player.getStackInHand(Hand.OFF_HAND))) {
            return player.getStackInHand(Hand.OFF_HAND);
        } else if (this.isAmmo(player.getStackInHand(Hand.MAIN_HAND))) {
            return player.getStackInHand(Hand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.getInventory().size(); ++i) {
                ItemStack itemstack = player.getInventory().getStack(i);

                if (this.isAmmo(itemstack)) {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }

    protected boolean isAmmo(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() == IafItemRegistry.PIXIE_DUST.get();
    }


    @Override
    public void appendTooltip(@NotNull ItemStack stack, World worldIn, List<Text> tooltip, @NotNull TooltipContext flagIn) {
        tooltip.add(Text.translatable("item.iceandfire.legendary_weapon.desc").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.iceandfire.pixie_wand.desc_0").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.iceandfire.pixie_wand.desc_1").formatted(Formatting.GRAY));
    }
}
