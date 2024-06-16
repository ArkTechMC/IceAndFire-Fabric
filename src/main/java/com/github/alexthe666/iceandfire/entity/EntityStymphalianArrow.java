package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.registry.IafItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityStymphalianArrow extends PersistentProjectileEntity {

    public EntityStymphalianArrow(EntityType<? extends PersistentProjectileEntity> t, World worldIn) {
        super(t, worldIn);
        this.setDamage(3.5F);
    }

    public EntityStymphalianArrow(EntityType<? extends PersistentProjectileEntity> t, World worldIn, double x, double y,
                                  double z) {
        this(t, worldIn);
        this.setPosition(x, y, z);
        this.setDamage(3.5F);
    }

    public EntityStymphalianArrow(EntityType t, World worldIn, LivingEntity shooter) {
        super(t, shooter, worldIn);
        this.setDamage(3.5F);
    }

    @Override
    public void tick() {
        super.tick();
        float sqrt = MathHelper.sqrt((float) (this.getVelocity().x * this.getVelocity().x + this.getVelocity().z * this.getVelocity().z));
        if (sqrt < 0.1F) {
            this.setVelocity(this.getVelocity().add(0, -0.01F, 0));
        }
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(IafItems.STYMPHALIAN_ARROW);
    }
}
