package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Predicate;

public class CockatriceAIAggroLook extends ActiveTargetGoal<PlayerEntity> {
    private final EntityCockatrice cockatrice;
    private final TargetPredicate predicate;
    private PlayerEntity player;

    public CockatriceAIAggroLook(EntityCockatrice cockatriceIn) {
        super(cockatriceIn, PlayerEntity.class, false);
        this.cockatrice = cockatriceIn;
        Predicate<LivingEntity> LIVING_ENTITY_SELECTOR = (target) -> EntityGorgon.isEntityLookingAt(target, this.cockatrice,
                EntityCockatrice.VIEW_RADIUS) && this.cockatrice.distanceTo(target) < this.getFollowRange();
        this.predicate = TargetPredicate.createAttackable().setBaseMaxDistance(25.0D).setPredicate(LIVING_ENTITY_SELECTOR);
    }

    /**
     * Returns whether the Goal should begin execution.
     */
    @Override
    public boolean canStart() {
        if (this.cockatrice.isTamed()) return false;
        this.player = this.cockatrice.getWorld().getClosestPlayer(this.predicate, this.cockatrice.getX(), this.cockatrice.getY(), this.cockatrice.getZ());
        return this.player != null;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by
     * another one
     */
    @Override
    public void stop() {
        this.player = null;
        super.stop();
    }

    /**
     * Returns whether an in-progress Goal should continue executing
     */
    @Override
    public boolean shouldContinue() {
        if (this.player != null && !this.player.isCreative() && !this.player.isSpectator()) {
            if (!EntityGorgon.isEntityLookingAt(this.player, this.cockatrice, 0.4F))
                return false;
            else {
                this.cockatrice.lookAtEntity(this.player, 10.0F, 10.0F);
                if (!this.cockatrice.isTamed()) {
                    this.cockatrice.setTargetedEntity(this.player.getId());
                    this.cockatrice.setTarget(this.player);
                }
                return true;
            }
        } else return this.target != null && this.target.isAlive() || super.shouldContinue();
    }
}
