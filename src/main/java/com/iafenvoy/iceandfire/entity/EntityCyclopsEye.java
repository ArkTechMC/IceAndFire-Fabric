package com.iafenvoy.iceandfire.entity;

import com.iafenvoy.iceandfire.registry.IafEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.world.World;

public class EntityCyclopsEye extends EntityMutlipartPart {

    public EntityCyclopsEye(EntityType<?> t, World world) {
        super(t, world);
    }

    public EntityCyclopsEye(LivingEntity parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(IafEntities.CYCLOPS_MULTIPART, parent, radius, angleYaw, offsetY, sizeX, sizeY,
                damageMultiplier);
    }

    @Override
    public boolean damage(DamageSource source, float damage) {
        Entity parent = this.getParent();
        if (parent instanceof EntityCyclops && source.isOf(DamageTypes.ARROW)) {
            ((EntityCyclops) parent).onHitEye(source, damage);
            return true;
        } else {
            return parent != null && parent.damage(source, damage);
        }
    }
}
