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
        if (dragon.isTamed()) {
            return false;
        }

        if (dragon.lookingForRoostAIFlag) {
            return false;
        }

        boolean canUse = super.canStart();
        boolean isSleeping = dragon.isSleeping();

        if (canUse) {
            if (isSleeping && targetEntity instanceof PlayerEntity) {
                return dragon.squaredDistanceTo(targetEntity) <= 16;
            }

            return !isSleeping;
        }

        return false;
    }

    @Override
    protected @NotNull Box getSearchBox(double targetDistance) {
        return this.dragon.getBoundingBox().expand(targetDistance, targetDistance, targetDistance);
    }

    @Override
    protected double getFollowRange() {
        EntityAttributeInstance iattributeinstance = this.mob.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE);
        return iattributeinstance == null ? 128.0D : iattributeinstance.getValue();
    }
}