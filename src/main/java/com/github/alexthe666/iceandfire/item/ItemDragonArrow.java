package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityDragonArrow;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.entity.LivingEntity;
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
}
