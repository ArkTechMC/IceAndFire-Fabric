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
import org.jetbrains.annotations.NotNull;

public class EntityCockatriceEgg extends ThrownItemEntity {

    public EntityCockatriceEgg(EntityType<? extends ThrownItemEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityCockatriceEgg(EntityType<? extends ThrownItemEntity> type, World worldIn, LivingEntity throwerIn) {
        super(type, throwerIn, worldIn);
    }

    public EntityCockatriceEgg(EntityType<? extends ThrownItemEntity> type, double x, double y, double z,
                               World worldIn) {
        super(type, x, y, z, worldIn);
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
            if (this.random.nextInt(4) == 0) {
                int i = 1;

                if (this.random.nextInt(32) == 0) {
                    i = 4;
                }

                for (int j = 0; j < i; ++j) {
                    EntityCockatrice cockatrice = new EntityCockatrice(IafEntityRegistry.COCKATRICE.get(), this.getWorld());
                    cockatrice.setBreedingAge(-24000);
                    cockatrice.setHen(this.random.nextBoolean());
                    cockatrice.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
                    if (thrower instanceof PlayerEntity) {
                        cockatrice.setOwner((PlayerEntity) thrower);
                    }
                    this.getWorld().spawnEntity(cockatrice);
                }
            }

            this.getWorld().sendEntityStatus(this, (byte) 3);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return IafItemRegistry.ROTTEN_EGG.get();
    }
}
