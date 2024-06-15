package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.google.common.base.Predicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class DeathWormAITarget<T extends LivingEntity> extends ActiveTargetGoal<T> {
    private final EntityDeathWorm deathworm;

    public DeathWormAITarget(EntityDeathWorm entityIn, Class<T> classTarget, boolean checkSight, Predicate<LivingEntity> targetPredicate) {
        super(entityIn, classTarget, 20, checkSight, false, targetPredicate);
        this.deathworm = entityIn;
        this.setControls(EnumSet.of(Control.TARGET));
    }

    @Override
    public boolean canStart() {
        boolean canUse = super.canStart();

        if (canUse && this.targetEntity != null && this.targetEntity.getType() != (IafEntityRegistry.DEATH_WORM.get())) {
            if (this.targetEntity instanceof PlayerEntity && !this.deathworm.isOwner(this.targetEntity)) {
                return !this.deathworm.isTamed();
            } else if (this.deathworm.isOwner(this.targetEntity)) {
                return false;
            }

            if (this.targetEntity instanceof HostileEntity && this.deathworm.getWormAge() > 2) {
                if (this.targetEntity instanceof PathAwareEntity) {
                    return this.deathworm.getWormAge() > 3;
                }

                return true;
            }
        }

        return false;
    }

    @Override
    protected Box getSearchBox(double targetDistance) {
        // Increasing the y-range too much makes it target entities in caves etc., which will be unreachable (thus no target will be set)
        return this.deathworm.getBoundingBox().expand(targetDistance, 6, targetDistance);
    }
}