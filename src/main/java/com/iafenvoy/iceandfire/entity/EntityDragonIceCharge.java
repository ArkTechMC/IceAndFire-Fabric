package com.iafenvoy.iceandfire.entity;

import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.entity.util.dragon.IafDragonDestructionManager;
import com.iafenvoy.iceandfire.registry.IafDamageTypes;
import com.iafenvoy.iceandfire.registry.IafParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityDragonIceCharge extends EntityDragonCharge {
    public EntityDragonIceCharge(EntityType<? extends AbstractFireballEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityDragonIceCharge(EntityType<? extends AbstractFireballEntity> type, World worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(type, worldIn, posX, posY, posZ, accelX, accelY, accelZ);
    }

    public EntityDragonIceCharge(EntityType<? extends AbstractFireballEntity> type, World worldIn, EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
        super(type, worldIn, shooter, accelX, accelY, accelZ);
    }

    @Override
    public void tick() {
        for (int i = 0; i < 10; ++i)
            this.getWorld().addParticle(IafParticles.DRAGON_FROST_3, this.getX() + this.random.nextDouble() * 1 * (this.random.nextBoolean() ? -1 : 1), this.getY() + this.random.nextDouble() * 1 * (this.random.nextBoolean() ? -1 : 1), this.getZ() + this.random.nextDouble() * 1 * (this.random.nextBoolean() ? -1 : 1), 0.0D, 0.0D, 0.0D);
        super.tick();
    }

    @Override
    public DamageSource causeDamage(Entity cause) {
        return IafDamageTypes.causeDragonIceDamage(cause);
    }

    @Override
    public void destroyArea(World world, BlockPos center, EntityDragonBase destroyer) {
        IafDragonDestructionManager.destroyAreaCharge(world, center, destroyer);
    }

    @Override
    public float getDamage() {
        return (float) IafCommonConfig.INSTANCE.dragon.attackDamageIce.getDoubleValue();
    }

    @Override
    protected boolean isBurning() {
        return false;
    }


}