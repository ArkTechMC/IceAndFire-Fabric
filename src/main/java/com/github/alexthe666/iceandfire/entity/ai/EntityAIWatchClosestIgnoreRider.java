package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.MobEntity;

public class EntityAIWatchClosestIgnoreRider extends LookAtEntityGoal {
    LivingEntity entity;

    public EntityAIWatchClosestIgnoreRider(MobEntity entity, Class<? extends LivingEntity> type, float dist) {
        super(entity, type, dist);
    }

    @Override
    public boolean canStart() {
        return super.canStart() && target != null && isRidingOrBeingRiddenBy(target, entity);
    }

    public static boolean isRidingOrBeingRiddenBy(Entity first, Entity entityIn) {
        for (Entity entity : first.getPassengerList()) {
            if (entity.equals(entityIn)) {
                return true;
            }

            if (isRidingOrBeingRiddenBy(entity, entityIn)) {
                return true;
            }
        }

        return false;
    }

}
