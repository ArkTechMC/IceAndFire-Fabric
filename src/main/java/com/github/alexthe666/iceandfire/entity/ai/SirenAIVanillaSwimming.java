package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.MobNavigation;

import java.util.EnumSet;

public class SirenAIVanillaSwimming extends Goal {
    private final EntitySiren entity;

    public SirenAIVanillaSwimming(EntitySiren entityIn) {
        this.entity = entityIn;
        this.setControls(EnumSet.of(Control.MOVE));
        if (entityIn.getNavigation() instanceof MobNavigation) {
            entityIn.getNavigation().setCanSwim(true);
        }
    }

    @Override
    public boolean canStart() {
        return (this.entity.isTouchingWater() || this.entity.isInLava()) && this.entity.wantsToSing();
    }

    @Override
    public void tick() {
        if (this.entity.getRandom().nextFloat() < 0.8F) {
            this.entity.getJumpControl().setActive();
        }
    }
}