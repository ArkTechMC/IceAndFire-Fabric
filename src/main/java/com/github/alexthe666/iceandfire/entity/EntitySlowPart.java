package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.registry.IafEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;


public class EntitySlowPart extends EntityMutlipartPart {

    public EntitySlowPart(EntityType<?> t, World world) {
        super(t, world);
    }

    public EntitySlowPart(EntityType<?> t, LivingEntity parent, float radius, float angleYaw, float offsetY, float sizeX,
                          float sizeY, float damageMultiplier) {
        super(t, parent, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
    }

    public EntitySlowPart(Entity parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(IafEntities.SLOW_MULTIPART, parent, radius, angleYaw, offsetY, sizeX, sizeY,
                damageMultiplier);
    }

    @Override
    protected boolean isSlowFollow() {
        return true;
    }
}
