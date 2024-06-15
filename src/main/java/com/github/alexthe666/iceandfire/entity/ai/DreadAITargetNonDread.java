package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IDreadMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;

import java.util.function.Predicate;

public class DreadAITargetNonDread extends ActiveTargetGoal<LivingEntity> {

    public DreadAITargetNonDread(MobEntity entityIn, Class<LivingEntity> classTarget, boolean checkSight,
                                 Predicate<LivingEntity> targetSelector) {
        super(entityIn, classTarget, 0, checkSight, false, targetSelector);
    }

    @Override
    protected boolean canTrack(LivingEntity target, TargetPredicate targetPredicate) {
        if (super.canTrack(target, targetPredicate)) {
            return !(target instanceof IDreadMob) && DragonUtils.isAlive(target);
        }
        return false;
    }

}
