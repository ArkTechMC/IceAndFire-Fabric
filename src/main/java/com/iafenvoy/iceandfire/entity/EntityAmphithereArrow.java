package com.iafenvoy.iceandfire.entity;

import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.iceandfire.registry.IafSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAmphithereArrow extends PersistentProjectileEntity {
    public EntityAmphithereArrow(EntityType<? extends PersistentProjectileEntity> type, World worldIn) {
        super(type, worldIn);
        this.setDamage(2.5F);
    }

    public EntityAmphithereArrow(EntityType<? extends PersistentProjectileEntity> type, World worldIn, double x, double y,
                                 double z) {
        this(type, worldIn);
        this.setPosition(x, y, z);
        this.setDamage(2.5F);
    }

    public EntityAmphithereArrow(EntityType type, LivingEntity shooter, World worldIn) {
        super(type, shooter, worldIn);
        this.setDamage(2.5F);
    }

    @Override
    public void tick() {
        super.tick();
        if ((this.age == 1 || this.age % 70 == 0) && !this.inGround && !this.isOnGround())
            this.playSound(IafSounds.AMPHITHERE_GUST, 1, 1);
        if (this.getWorld().isClient && !this.inGround) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            double d3 = 10.0D;
            double xRatio = this.getVelocity().x * this.getWidth();
            double zRatio = this.getVelocity().z * this.getWidth();
            this.getWorld().addParticle(ParticleTypes.CLOUD, this.getX() + xRatio + this.random.nextFloat() * this.getWidth() * 1.0F - this.getWidth() - d0 * 10.0D, this.getY() + this.random.nextFloat() * this.getHeight() - d1 * 10.0D, this.getZ() + zRatio + this.random.nextFloat() * this.getWidth() * 1.0F - this.getWidth() - d2 * 10.0D, d0, d1, d2);
        }
    }

    @Override
    protected void onHit(LivingEntity living) {
        living.velocityDirty = true;
        double xRatio = this.getVelocity().x;
        double zRatio = this.getVelocity().z;
        float strength = -1.4F;
        float f = MathHelper.sqrt((float) (xRatio * xRatio + zRatio * zRatio));
        living.setVelocity(living.getVelocity().multiply(0.5D, 1, 0.5D).subtract(xRatio / f * strength, 0, zRatio / f * strength).add(0, 0.6, 0));
        this.spawnExplosionParticle();
    }

    public void spawnExplosionParticle() {
        if (this.getWorld().isClient) {
            for (int height = 0; height < 1 + this.random.nextInt(2); height++)
                for (int i = 0; i < 20; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    double d3 = 10.0D;
                    double xRatio = this.getVelocity().x * this.getWidth();
                    double zRatio = this.getVelocity().z * this.getWidth();
                    this.getWorld().addParticle(ParticleTypes.CLOUD, this.getX() + xRatio + this.random.nextFloat() * this.getWidth() * 1.0F - this.getWidth() - d0 * d3, this.getY() + this.random.nextFloat() * this.getHeight() - d1 * d3, this.getZ() + zRatio + this.random.nextFloat() * this.getWidth() * 1.0F - this.getWidth() - d2 * d3, d0, d1, d2);
                }
        } else
            this.getWorld().sendEntityStatus(this, (byte) 20);
    }

    @Override
    public void handleStatus(byte id) {
        if (id == 20) this.spawnExplosionParticle();
        else super.handleStatus(id);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(IafItems.AMPHITHERE_ARROW);
    }
}
