package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.client.particle.IafParticleRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityHydraArrow extends PersistentProjectileEntity {

    public EntityHydraArrow(EntityType<? extends PersistentProjectileEntity> t, World worldIn) {
        super(t, worldIn);
        this.setDamage(5F);
    }

    public EntityHydraArrow(EntityType<? extends PersistentProjectileEntity> t, World worldIn, double x, double y, double z) {
        this(t, worldIn);
        this.setPosition(x, y, z);
        this.setDamage(5F);
    }


    public EntityHydraArrow(EntityType t, World worldIn, LivingEntity shooter) {
        super(t, shooter, worldIn);
        this.setDamage(5F);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient && !this.inGround) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            double d3 = 10.0D;
            double xRatio = this.getVelocity().x * this.getHeight();
            double zRatio = this.getVelocity().z * this.getHeight();
            this.getWorld().addParticle(IafParticleRegistry.HYDRA_BREATH, this.getX() + xRatio + (double) (this.random.nextFloat() * this.getWidth() * 1.0F) - (double) this.getWidth() - d0 * 10.0D, this.getY() + (double) (this.random.nextFloat() * this.getHeight()) - d1 * 10.0D, this.getZ() + zRatio + (double) (this.random.nextFloat() * this.getWidth() * 1.0F) - (double) this.getWidth() - d2 * 10.0D, 0.1D, 1.0D, 0.1D);
            this.getWorld().addParticle(IafParticleRegistry.HYDRA_BREATH, this.getX() + xRatio + (double) (this.random.nextFloat() * this.getWidth() * 1.0F) - (double) this.getWidth() - d0 * 10.0D, this.getY() + (double) (this.random.nextFloat() * this.getHeight()) - d1 * 10.0D, this.getZ() + zRatio + (double) (this.random.nextFloat() * this.getWidth() * 1.0F) - (double) this.getWidth() - d2 * 10.0D, 0.1D, 1.0D, 0.1D);
        }
    }

    protected void damageShield(PlayerEntity player, float damage) {
        if (damage >= 3.0F && player.getActiveItem().getItem() instanceof ShieldItem) {
            ItemStack copyBeforeUse = player.getActiveItem().copy();
            int i = 1 + MathHelper.floor(damage);
            player.getActiveItem().damage(i, player, (p_213360_0_) -> p_213360_0_.sendEquipmentBreakStatus(EquipmentSlot.CHEST));

            if (player.getActiveItem().isEmpty()) {
                Hand Hand = player.getActiveHand();

                if (Hand == net.minecraft.util.Hand.MAIN_HAND) {
                    this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
                player.clearActiveItem();
                this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.getWorld().random.nextFloat() * 0.4F);
            }
        }
    }

    @Override
    protected void onHit(LivingEntity living) {
        if (living instanceof PlayerEntity) {
            this.damageShield((PlayerEntity) living, (float) this.getDamage());
        }
        living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 300, 0));
        Entity shootingEntity = this.getOwner();
        if (shootingEntity instanceof LivingEntity) {
            ((LivingEntity) shootingEntity).heal((float) this.getDamage());
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(IafItemRegistry.HYDRA_ARROW.get());
    }
}
