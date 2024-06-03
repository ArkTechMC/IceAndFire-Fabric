package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class EntityAIAttackMeleeNoCooldown extends MeleeAttackGoal {
    public EntityAIAttackMeleeNoCooldown(PathAwareEntity creature, double speed, boolean memory) {
        super(creature, speed, memory);
    }

    @Override
    public void tick() {
        // TODO: investigate why the goal is even running when the attack target is null
        // Probably has something to do with the goal switching
        if (this.mob.getTarget() != null)
            super.tick();
    }
}
