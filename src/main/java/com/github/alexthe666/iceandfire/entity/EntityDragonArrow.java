package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class EntityDragonArrow extends PersistentProjectileEntity {

    public EntityDragonArrow(EntityType<? extends PersistentProjectileEntity> typeIn, World worldIn) {
        super(typeIn, worldIn);
        this.setDamage(10);
    }

    public EntityDragonArrow(EntityType<? extends PersistentProjectileEntity> typeIn, double x, double y, double z,
                             World world) {
        super(typeIn, x, y, z, world);
        this.setDamage(10);
    }

    public EntityDragonArrow(PlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(IafEntityRegistry.DRAGON_ARROW.get(), worldIn);
    }

    public EntityDragonArrow(EntityType<? extends PersistentProjectileEntity> typeIn, LivingEntity shooter, World worldIn) {
        super(typeIn, shooter, worldIn);
        this.setDamage(10.0F);
    }

    @Override
    public void writeCustomDataToNbt(@NotNull NbtCompound tagCompound) {
        super.writeCustomDataToNbt(tagCompound);
        tagCompound.putDouble("damage", 10);
    }

    @Override
    public void readCustomDataFromNbt(@NotNull NbtCompound tagCompund) {
        super.readCustomDataFromNbt(tagCompund);
        this.setDamage(tagCompund.getDouble("damage"));
    }

    @Override
    protected @NotNull ItemStack asItemStack() {
        return new ItemStack(IafItemRegistry.DRAGONBONE_ARROW.get());
    }

}