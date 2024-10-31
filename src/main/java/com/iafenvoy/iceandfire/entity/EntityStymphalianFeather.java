package com.iafenvoy.iceandfire.entity;

import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.registry.IafItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class EntityStymphalianFeather extends PersistentProjectileEntity {

    public EntityStymphalianFeather(EntityType<? extends PersistentProjectileEntity> t, World worldIn) {
        super(t, worldIn);
    }

    public EntityStymphalianFeather(EntityType<? extends PersistentProjectileEntity> t, World worldIn, LivingEntity shooter) {
        super(t, shooter, worldIn);
        this.setDamage(IafCommonConfig.INSTANCE.stymphalianBird.featherAttackDamage.getValue());
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (IafCommonConfig.INSTANCE.stymphalianBird.featherDropChance.getValue() > 0) {
            if (this.getWorld().isClient) {
                if (this.random.nextDouble() < IafCommonConfig.INSTANCE.stymphalianBird.featherDropChance.getValue()) {
                    this.dropStack(this.asItemStack(), 0.1F);
                }
            }
        }

    }

    @Override
    public void tick() {
        super.tick();
        if (this.age > 100) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHit) {
        Entity shootingEntity = this.getOwner();
        if (!(shootingEntity instanceof EntityStymphalianBird) || entityHit.getEntity() == null || !(entityHit.getEntity() instanceof EntityStymphalianBird)) {
            super.onEntityHit(entityHit);
            if (entityHit.getEntity() != null && entityHit.getEntity() instanceof EntityStymphalianBird LivingEntity) {
                LivingEntity.setStuckArrowCount(LivingEntity.getStuckArrowCount() - 1);
                ItemStack itemstack1 = LivingEntity.isUsingItem() ? LivingEntity.getActiveItem() : ItemStack.EMPTY;
                if (itemstack1.getItem() instanceof ShieldItem)
                    this.damageShield(LivingEntity);
            }
        }
    }

    protected void damageShield(LivingEntity entity) {
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(IafItems.STYMPHALIAN_BIRD_FEATHER);
    }
}
