package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class EntityHippogryphEgg extends EggEntity {

    private ItemStack itemstack;

    public EntityHippogryphEgg(EntityType<? extends EggEntity> type, World world) {
        super(type, world);
    }

    public EntityHippogryphEgg(EntityType<? extends EggEntity> type, World worldIn, double x, double y, double z,
                               ItemStack stack) {
        this(type, worldIn);
        this.setPosition(x, y, z);
        this.itemstack = stack;
    }

    public EntityHippogryphEgg(EntityType<? extends EggEntity> type, World worldIn, LivingEntity throwerIn,
                               ItemStack stack) {
        this(type, worldIn);
        this.setPosition(throwerIn.getX(), throwerIn.getEyeY() - 0.1F, throwerIn.getZ());
        this.itemstack = stack;
        this.setOwner(throwerIn);
    }

    @Override
    public void handleStatus(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                this.getWorld().addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, this.getStack()), this.getX(), this.getY(), this.getZ(), (this.random.nextFloat() - 0.5D) * 0.08D, (this.random.nextFloat() - 0.5D) * 0.08D, (this.random.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }

    @Override
    protected void onCollision(HitResult result) {
        Entity thrower = this.getOwner();
        if (result.getType() == HitResult.Type.ENTITY) {
            ((EntityHitResult) result).getEntity().damage(this.getWorld().getDamageSources().thrown(this, thrower), 0.0F);
        }

        if (!this.getWorld().isClient) {
            EntityHippogryph hippogryph = new EntityHippogryph(IafEntityRegistry.HIPPOGRYPH.get(), this.getWorld());
            hippogryph.setBreedingAge(-24000);
            hippogryph.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
            if (this.itemstack != null) {
                int variant = 0;
                NbtCompound tag = this.itemstack.getNbt();
                if (tag != null) {
                    variant = tag.getInt("EggOrdinal");
                }
                hippogryph.setVariant(variant);
            }

            if (thrower instanceof PlayerEntity) {
                hippogryph.setOwner((PlayerEntity) thrower);
            }

            this.getWorld().spawnEntity(hippogryph);
        }

        this.getWorld().sendEntityStatus(this, (byte) 3);
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected Item getDefaultItem() {
        return IafItemRegistry.HIPPOGRYPH_EGG.get();
    }
}
