package com.iafenvoy.iceandfire.entity;

import com.iafenvoy.iceandfire.config.IafConfig;
import com.iafenvoy.iceandfire.entity.util.dragon.IDragonProjectile;
import com.iafenvoy.iceandfire.entity.util.dragon.IafDragonDestructionManager;
import com.iafenvoy.iceandfire.registry.IafDamageTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityDragonLightningCharge extends EntityDragonCharge implements IDragonProjectile {
    public EntityDragonLightningCharge(EntityType<? extends AbstractFireballEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityDragonLightningCharge(EntityType<? extends AbstractFireballEntity> type, World worldIn, double posX,
                                       double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(type, worldIn, posX, posY, posZ, accelX, accelY, accelZ);
    }

    public EntityDragonLightningCharge(EntityType<? extends AbstractFireballEntity> type, World worldIn,
                                       EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
        super(type, worldIn, shooter, accelX, accelY, accelZ);
    }

    @Override
    public DamageSource causeDamage(Entity cause) {
        return IafDamageTypes.causeDragonLightningDamage(cause);
    }

    @Override
    public void destroyArea(World world, BlockPos center, EntityDragonBase destroyer) {
        IafDragonDestructionManager.destroyAreaCharge(world, center, destroyer);
    }

    @Override
    public float getDamage() {
        return (float) IafConfig.getInstance().dragonAttackDamageLightning;
    }


}