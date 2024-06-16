package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityDragonArrow;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDragonArrow extends ArrowItem {
    public ItemDragonArrow() {
        super(new Settings());
    }

    @Override
    public PersistentProjectileEntity createArrow(final World level, final ItemStack arrow, final LivingEntity shooter) {
        return new EntityDragonArrow(IafEntityRegistry.DRAGON_ARROW, shooter, level);
    }
}
