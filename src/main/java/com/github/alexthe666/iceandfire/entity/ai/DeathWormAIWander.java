package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;

public class DeathWormAIWander extends WanderAroundFarGoal {

    private final EntityDeathWorm worm;

    public DeathWormAIWander(EntityDeathWorm creatureIn, double speedIn) {
        super(creatureIn, speedIn);
        this.worm = creatureIn;
    }

    @Override
    public boolean canStart() {
        return !worm.isInSand() && !worm.hasPassengers() && super.canStart();
    }

    @Override
    public boolean shouldContinue() {
        return !worm.isInSand() && !worm.hasPassengers() && super.shouldContinue();
    }
}