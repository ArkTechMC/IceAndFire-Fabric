package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.entity.ai.goal.WanderAroundGoal;

public class SirenAIWander extends WanderAroundGoal {

    private final EntitySiren siren;

    public SirenAIWander(EntitySiren creatureIn, double speedIn) {
        super(creatureIn, speedIn);
        this.siren = creatureIn;
    }

    @Override
    public boolean canStart() {
        return !this.siren.isTouchingWater() && !siren.isSinging() && super.canStart();
    }

    @Override
    public boolean shouldContinue() {
        return !this.siren.isTouchingWater() && !siren.isSinging() && super.shouldContinue();
    }
}