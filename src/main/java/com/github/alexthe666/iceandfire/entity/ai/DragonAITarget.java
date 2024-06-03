package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.api.FoodUtils;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.function.Predicate;

public class DragonAITarget<T extends LivingEntity> extends ActiveTargetGoal<T> {
    private final EntityDragonBase dragon;

    public DragonAITarget(EntityDragonBase entityIn, Class<T> classTarget, boolean checkSight, Predicate<LivingEntity> targetSelector) {
        super(entityIn, classTarget, 3, checkSight, false, targetSelector);
        this.setControls(EnumSet.of(Control.TARGET));
        this.dragon = entityIn;
    }

    @Override
    public boolean canStart() {
        if (dragon.getCommand() == 1 || dragon.getCommand() == 2 || dragon.isSleeping()) {
            return false;
        }
        if (!dragon.isTamed() && dragon.lookingForRoostAIFlag) {
            return false;
        }
        if (targetEntity != null && !targetEntity.getClass().equals(this.dragon.getClass())) {
            if (!super.canStart())
                return false;

            final float dragonSize = Math.max(this.dragon.getWidth(), this.dragon.getWidth() * dragon.getRenderSize());
            if (dragonSize >= targetEntity.getWidth()) {
                if (targetEntity instanceof PlayerEntity && !dragon.isTamed()) {
                    return true;
                }
                if (targetEntity instanceof EntityDragonBase dragon) {
                    if (dragon.getOwner() != null && this.dragon.getOwner() != null && this.dragon.isOwner(dragon.getOwner())) {
                        return false;
                    }
                    return !dragon.isModelDead();
                }
                if (targetEntity instanceof PlayerEntity && dragon.isTamed()) {
                    return false;
                } else {
                    if (!dragon.isOwner(targetEntity) && FoodUtils.getFoodPoints(targetEntity) > 0 && dragon.canMove() && (dragon.getHunger() < 90 || !dragon.isTamed() && targetEntity instanceof PlayerEntity)) {
                        if (dragon.isTamed()) {
                            return DragonUtils.canTameDragonAttack(dragon, targetEntity);
                        } else {
                            return true;
                        }
                    }
                }
            }
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
        return iattributeinstance == null ? 64.0D : iattributeinstance.getValue();
    }
}