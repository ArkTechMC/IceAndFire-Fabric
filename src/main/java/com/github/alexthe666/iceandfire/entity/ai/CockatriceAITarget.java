package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Difficulty;

import java.util.EnumSet;
import java.util.function.Predicate;

public class CockatriceAITarget<T extends LivingEntity> extends ActiveTargetGoal<T> {
    private final EntityCockatrice cockatrice;

    public CockatriceAITarget(EntityCockatrice entityIn, Class<T> classTarget, boolean checkSight, Predicate<LivingEntity> targetSelector) {
        super(entityIn, classTarget, 0, checkSight, false, targetSelector);
        this.cockatrice = entityIn;
        this.setControls(EnumSet.of(Control.TARGET));
    }

    @Override
    public boolean canStart() {
        if (this.mob.getRandom().nextInt(20) != 0 || this.cockatrice.getWorld().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (super.canStart() && targetEntity != null && !targetEntity.getClass().equals(this.cockatrice.getClass())) {
            if (targetEntity instanceof PlayerEntity && !cockatrice.isOwner(targetEntity)) {
                return !cockatrice.isTamed();
            } else {
                return !cockatrice.isOwner(targetEntity) && cockatrice.canMove();
            }
        }
        return false;
    }
}