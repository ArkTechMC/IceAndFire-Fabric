package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class EntityDeathWormEgg extends ThrownItemEntity {

    private boolean giant;

    public EntityDeathWormEgg(EntityType<? extends ThrownItemEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityDeathWormEgg(EntityType<? extends ThrownItemEntity> type, LivingEntity throwerIn, World worldIn,
                              boolean giant) {
        super(type, throwerIn, worldIn);
        this.giant = giant;
    }

    public EntityDeathWormEgg(EntityType<? extends ThrownItemEntity> type, double x, double y, double z,
                              World worldIn, boolean giant) {
        super(type, x, y, z, worldIn);
        this.giant = giant;
    }

    @Override
    public void handleStatus(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                this.getWorld().addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, this.getStack()), this.getX(), this.getY(), this.getZ(), (this.random.nextFloat() - 0.5D) * 0.08D, (this.random.nextFloat() - 0.5D) * 0.08D, (this.random.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    @Override
    protected void onCollision(HitResult result) {
        Entity thrower = this.getOwner();
        if (result.getType() == HitResult.Type.ENTITY) {
            ((EntityHitResult) result).getEntity().damage(this.getWorld().getDamageSources().thrown(this, thrower), 0.0F);
        }

        if (!this.getWorld().isClient) {
            float wormSize = 0.25F + (float) (Math.random() * 0.35F);

            EntityDeathWorm deathworm = new EntityDeathWorm(IafEntityRegistry.DEATH_WORM, this.getWorld());
            deathworm.setVariant(this.random.nextInt(3));
            deathworm.setTamed(true);
            deathworm.setWormHome(this.getBlockPos());
            deathworm.setWormAge(1);
            deathworm.setDeathWormScale(this.giant ? (wormSize * 4) : wormSize);
            deathworm.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
            if (thrower instanceof PlayerEntity) {
                deathworm.setOwnerUuid(thrower.getUuid());
            }
            this.getWorld().spawnEntity(deathworm);

            this.getWorld().sendEntityStatus(this, (byte) 3);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected Item getDefaultItem() {
        return this.giant ? IafItemRegistry.DEATHWORM_EGG_GIGANTIC : IafItemRegistry.DEATHWORM_EGG;
    }
}
