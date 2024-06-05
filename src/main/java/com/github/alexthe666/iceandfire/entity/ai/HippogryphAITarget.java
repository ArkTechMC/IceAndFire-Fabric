package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Predicate;

public class HippogryphAITarget<T extends LivingEntity> extends ActiveTargetGoal<T> {
    private final EntityHippogryph hippogryph;

    public HippogryphAITarget(EntityHippogryph entityIn, Class<T> classTarget, boolean checkSight, Predicate<LivingEntity> targetPredicate) {
        super(entityIn, classTarget, 20, checkSight, false, targetPredicate);
        this.hippogryph = entityIn;
    }

    public HippogryphAITarget(EntityHippogryph entityIn, Class<T> classTarget, int i, boolean checkSight, Predicate<LivingEntity> targetPredicate) {
        super(entityIn, classTarget, i, checkSight, false, targetPredicate);
        this.hippogryph = entityIn;
    }


    @Override
    public boolean canStart() {
        if (super.canStart() && this.targetEntity != null && !this.targetEntity.getClass().equals(this.hippogryph.getClass())) {
            if (this.hippogryph.getWidth() >= this.targetEntity.getWidth()) {
                if (this.targetEntity instanceof PlayerEntity) {
                    return !this.hippogryph.isTamed();
                } else {
                    if (!this.hippogryph.isOwner(this.targetEntity) && this.hippogryph.canMove() && this.targetEntity instanceof AnimalEntity) {
                        if (this.hippogryph.isTamed()) {
                            return DragonUtils.canTameDragonAttack(this.hippogryph, this.targetEntity);
                        } else {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}