package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;

import java.util.function.Predicate;

public class VillagerAIFearUntamed extends FleeEntityGoal<LivingEntity> {
    public VillagerAIFearUntamed(PathAwareEntity entityIn, Class<LivingEntity> avoidClass, float distance, double nearSpeedIn, double farSpeedIn, Predicate<LivingEntity> targetPredicate) {
        super(entityIn, avoidClass, distance, nearSpeedIn, farSpeedIn, targetPredicate);
    }
}
