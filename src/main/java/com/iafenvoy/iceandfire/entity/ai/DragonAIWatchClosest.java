package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class DragonAIWatchClosest extends LookAtEntityGoal {
    public DragonAIWatchClosest(PathAwareEntity LivingEntityIn, Class<? extends LivingEntity> watchTargetClass, float maxDistance) {
        super(LivingEntityIn, watchTargetClass, maxDistance);
    }

    @Override
    public boolean canStart() {
        if (this.mob instanceof EntityDragonBase && ((EntityDragonBase) this.mob).getAnimation() == EntityDragonBase.ANIMATION_SHAKEPREY)
            return false;
        return super.canStart();
    }

    @Override
    public boolean shouldContinue() {
        if (this.mob instanceof EntityDragonBase && !((EntityDragonBase) this.mob).canMove())
            return false;
        return super.shouldContinue();
    }
}
