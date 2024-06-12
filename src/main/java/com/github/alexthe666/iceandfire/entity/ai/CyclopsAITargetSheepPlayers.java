package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;

import java.util.EnumSet;
import java.util.function.Predicate;

public class CyclopsAITargetSheepPlayers<T extends LivingEntity> extends ActiveTargetGoal<T> {

    public CyclopsAITargetSheepPlayers(MobEntity goalOwnerIn, Class<T> targetClassIn, boolean checkSight) {
        super(goalOwnerIn, targetClassIn, 0, checkSight, true, livingEntity -> {
            return false; //TODO Sheep hunt cyclops
        });
        this.setControls(EnumSet.of(Control.TARGET));

    }


}