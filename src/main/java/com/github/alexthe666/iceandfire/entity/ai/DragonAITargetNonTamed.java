package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.function.Predicate;

public class DragonAITargetNonTamed<T extends LivingEntity> extends ActiveTargetGoal<T> {
    private final EntityDragonBase dragon;

    public DragonAITargetNonTamed(EntityDragonBase entityIn, Class<T> classTarget, boolean checkSight, Predicate<LivingEntity> targetSelector) {
        super(entityIn, classTarget, 5, checkSight, false, targetSelector);
        this.setControls(EnumSet.of(Control.TARGET));
        this.dragon = entityIn;
    }

    @Override
    public boolean canStart() {
        if (this.dragon.isTamed()) {
            return false;
        }

        if (this.dragon.lookingForRoostAIFlag) {
            return false;
        }

        boolean canUse = super.canStart();
        boolean isSleeping = this.dragon.isSleeping();

        if (canUse) {
            if (isSleeping && this.targetEntity instanceof PlayerEntity) {
                return this.dragon.squaredDistanceTo(this.targetEntity) <= 16;
            }

            return !isSleeping;
        }

        return false;
    }

    @Override
    protected Box getSearchBox(double targetDistance) {
        return this.dragon.getBoundingBox().expand(targetDistance, targetDistance, targetDistance);
    }

    @Override
    protected double getFollowRange() {
        EntityAttributeInstance iattributeinstance = this.mob.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE);
        return iattributeinstance == null ? 128.0D : iattributeinstance.getValue();
    }
}