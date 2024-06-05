package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.TameableEntity;

public class HippocampusAIWander extends WanderAroundGoal {

    public HippocampusAIWander(PathAwareEntity creatureIn, double speedIn) {
        super(creatureIn, speedIn);
    }

    @Override
    public boolean canStart() {
        return !(this.mob instanceof TameableEntity && ((TameableEntity) this.mob).isSitting()) && !this.mob.isTouchingWater() && super.canStart();
    }

    @Override
    public boolean shouldContinue() {
        return !(this.mob instanceof TameableEntity && ((TameableEntity) this.mob).isSitting()) && !this.mob.isTouchingWater() && super.shouldContinue();
    }
}