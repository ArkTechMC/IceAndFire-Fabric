package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.util.Hand;

public class CyclopsAIAttackMelee extends MeleeAttackGoal {
    public CyclopsAIAttackMelee(EntityCyclops creature, double speedIn, boolean useLongMemory) {
        super(creature, speedIn, useLongMemory);
    }

    @Override
    protected void attack(LivingEntity entity, double distance) {
        final double d0 = this.getSquaredMaxAttackDistance(entity);
        if (this.isCyclopsBlinded() && distance >= 36D) {
            this.stop();
            return;
        }
        if (distance <= d0) {
            this.mob.swingHand(Hand.MAIN_HAND);
            this.mob.tryAttack(entity);
        }
    }

    private boolean isCyclopsBlinded() {
        return this.mob instanceof EntityCyclops && ((EntityCyclops) this.mob).isBlinded();
    }
}
