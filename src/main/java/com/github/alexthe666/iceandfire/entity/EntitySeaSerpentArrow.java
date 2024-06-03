package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class EntitySeaSerpentArrow extends PersistentProjectileEntity {

    public EntitySeaSerpentArrow(EntityType<? extends PersistentProjectileEntity> t, World worldIn) {
        super(t, worldIn);
        this.setDamage(3F);
    }

    public EntitySeaSerpentArrow(EntityType<? extends PersistentProjectileEntity> t, World worldIn, double x, double y,
                                 double z) {
        this(t, worldIn);
        this.setPosition(x, y, z);
        this.setDamage(3F);
    }

    public EntitySeaSerpentArrow(PlayMessages.SpawnEntity spawnEntity, World world) {
        this(IafEntityRegistry.SEA_SERPENT_ARROW.get(), world);
    }

    @Override
    public @NotNull Packet<ClientPlayPacketListener> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    public EntitySeaSerpentArrow(EntityType t, World worldIn, LivingEntity shooter) {
        super(t, shooter, worldIn);
        this.setDamage(3F);
    }

    @Override
    public void tick() {
        super.tick();
        if (getWorld().isClient && !this.inGround) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            double d3 = 10.0D;
            double xRatio = this.getVelocity().x * this.getHeight();
            double zRatio = this.getVelocity().z * this.getHeight();
            this.getWorld().addParticle(ParticleTypes.BUBBLE, this.getX() + xRatio + this.random.nextFloat() * this.getWidth() * 1.0F - this.getWidth() - d0 * 10.0D, this.getY() + this.random.nextFloat() * this.getHeight() - d1 * 10.0D, this.getZ() + zRatio + this.random.nextFloat() * this.getWidth() * 1.0F - this.getWidth() - d2 * 10.0D, d0, d1, d2);
            this.getWorld().addParticle(ParticleTypes.SPLASH, this.getX() + xRatio + this.random.nextFloat() * this.getWidth() * 1.0F - this.getWidth() - d0 * 10.0D, this.getY() + this.random.nextFloat() * this.getHeight() - d1 * 10.0D, this.getZ() + zRatio + this.random.nextFloat() * this.getWidth() * 1.0F - this.getWidth() - d2 * 10.0D, d0, d1, d2);

        }
    }

    @Override
    public boolean isTouchingWater() {
        return false;
    }

    @Override
    protected @NotNull ItemStack asItemStack() {
        return new ItemStack(IafItemRegistry.SEA_SERPENT_ARROW.get());
    }
}
