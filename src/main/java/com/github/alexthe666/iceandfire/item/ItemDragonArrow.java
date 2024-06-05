package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityDragonArrow;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ItemDragonArrow extends ArrowItem {
    public ItemDragonArrow() {
        super(new Settings());
    }

    @Override
    public @NotNull PersistentProjectileEntity createArrow(@NotNull final World level, @NotNull final ItemStack arrow, @NotNull final LivingEntity shooter) {
        return new EntityDragonArrow(IafEntityRegistry.DRAGON_ARROW.get(), shooter, level);
    }

    @Override
    public boolean isInfinite(@NotNull final ItemStack arrow, @NotNull final ItemStack bow, @NotNull final PlayerEntity player) {
        // Technically this would always return false - it's more a compat layer for Apotheosis' Endless Quiver enchantment
        boolean isInfinite = super.isInfinite(arrow, bow, player);

        if (!isInfinite) {
            isInfinite = EnchantmentHelper.getLevel(Enchantments.INFINITY, bow) > 0 && this.getClass() == ItemDragonArrow.class;
        }

        return isInfinite;
    }
}
