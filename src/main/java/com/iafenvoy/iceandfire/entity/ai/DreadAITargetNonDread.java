package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.entity.util.IDreadMob;
import com.iafenvoy.iceandfire.entity.util.dragon.DragonUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;

import java.util.function.Predicate;

public class DreadAITargetNonDread extends ActiveTargetGoal<LivingEntity> {
    public DreadAITargetNonDread(MobEntity entityIn, Class<LivingEntity> classTarget, boolean checkSight, Predicate<LivingEntity> targetSelector) {
        super(entityIn, classTarget, 0, checkSight, false, targetSelector);
    }

    @Override
    protected boolean canTrack(LivingEntity target, TargetPredicate targetPredicate) {
        if (super.canTrack(target, targetPredicate))
            return !(target instanceof IDreadMob) && DragonUtils.isAlive(target);
        return false;
    }
}
