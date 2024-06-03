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
        if (super.canStart() && targetEntity != null && !targetEntity.getClass().equals(this.hippogryph.getClass())) {
            if (this.hippogryph.getWidth() >= targetEntity.getWidth()) {
                if (targetEntity instanceof PlayerEntity) {
                    return !hippogryph.isTamed();
                } else {
                    if (!hippogryph.isOwner(targetEntity) && hippogryph.canMove() && targetEntity instanceof AnimalEntity) {
                        if (hippogryph.isTamed()) {
                            return DragonUtils.canTameDragonAttack(hippogryph, targetEntity);
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