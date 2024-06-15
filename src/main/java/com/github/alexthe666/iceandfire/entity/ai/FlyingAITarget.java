package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class FlyingAITarget<T extends LivingEntity> extends ActiveTargetGoal<T> {

    public FlyingAITarget(MobEntity creature, Class<T> classTarget, boolean checkSight) {
        super(creature, classTarget, checkSight);
    }

    public FlyingAITarget(MobEntity creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby) {
        super(creature, classTarget, checkSight, onlyNearby);
    }

    public FlyingAITarget(MobEntity creature, Class<T> classTarget, int chance, boolean checkSight,
                          boolean onlyNearby, final Predicate<LivingEntity> targetSelector) {
        super(creature, classTarget, chance, checkSight, onlyNearby, targetSelector);
    }

    @Override
    protected Box getSearchBox(double targetDistance) {
        return this.mob.getBoundingBox().expand(targetDistance, targetDistance, targetDistance);
    }

    @Override
    public boolean canStart() {
        if (this.mob instanceof EntitySeaSerpent && (((EntitySeaSerpent) this.mob).isJumpingOutOfWater() || !this.mob.isTouchingWater())) {
            return false;
        }
        return super.canStart();
    }

}
