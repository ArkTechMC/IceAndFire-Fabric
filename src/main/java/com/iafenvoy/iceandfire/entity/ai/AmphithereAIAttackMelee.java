package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

import java.util.EnumSet;

public class AmphithereAIAttackMelee extends Goal {
    protected final EntityAmphithere attacker;
    /**
     * The speed with which the mob will approach the target
     */
    final double speedTowardsTarget;
    /**
     * When true, the mob will continue chasing its target, even if it can't find a path to them right now.
     */
    final boolean longMemory;
    private final boolean canPenalize = false;
    /**
     * An amount of decrementing ticks that allows the entity to attack once the tick reaches 0.
     */
    protected int attackTick;
    /**
     * The PathEntity of our entity.
     */
    Path path;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int failedPathFindingPenalty = 0;

    public AmphithereAIAttackMelee(EntityAmphithere amphithere, double speedIn, boolean useLongMemory) {
        this.attacker = amphithere;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.setControls(EnumSet.of(Control.MOVE, Control.TARGET));
    }

    /**
     * Returns whether the Goal should begin execution.
     */
    @Override
    public boolean canStart() {
        LivingEntity LivingEntity = this.attacker.getTarget();
        if (!this.attacker.canMove()) return false;
        if (LivingEntity == null) return false;
        else if (!LivingEntity.isAlive()) return false;
        else {
            if (this.canPenalize)
                if (--this.delayCounter <= 0) {
                    this.path = this.attacker.getNavigation().findPathTo(LivingEntity, 0);
                    this.delayCounter = 4 + this.attacker.getRandom().nextInt(7);
                    return this.path != null;
                } else return true;
            this.path = this.attacker.getNavigation().findPathTo(LivingEntity, 0);

            if (this.path != null) return true;
            else
                return this.getAttackReachSqr(LivingEntity) >= this.attacker.squaredDistanceTo(LivingEntity.getX(), LivingEntity.getBoundingBox().minY, LivingEntity.getZ());
        }
    }

    /**
     * Returns whether an in-progress Goal should continue executing
     */
    @Override
    public boolean shouldContinue() {
        LivingEntity living = this.attacker.getTarget();
        if (living == null) return false;
        else if (!living.isAlive()) return false;
        else if (!this.longMemory) return !this.attacker.getNavigation().isIdle();
        else if (!this.attacker.isInWalkTargetRange(living.getBlockPos())) return false;
        else return !(living instanceof PlayerEntity) || !living.isSpectator() && !((PlayerEntity) living).isCreative();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        if (this.attacker.isFlying())
            this.attacker.getMoveControl().moveTo(this.targetX, this.targetY, this.targetZ, 0.1F);
        else this.attacker.getNavigation().startMovingAlong(this.path, this.speedTowardsTarget);
        this.delayCounter = 0;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void stop() {
        LivingEntity LivingEntity = this.attacker.getTarget();
        if (LivingEntity instanceof PlayerEntity && (LivingEntity.isSpectator() || ((PlayerEntity) LivingEntity).isCreative()))
            this.attacker.setTarget(null);
        this.attacker.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity LivingEntity = this.attacker.getTarget();
        if (this.attacker.isFlying()) {
            assert LivingEntity != null;
            this.attacker.getMoveControl().moveTo(LivingEntity.getX(), LivingEntity.getY() + LivingEntity.getStandingEyeHeight(), LivingEntity.getZ(), 0.1D);
        }
        this.attacker.getLookControl().lookAt(LivingEntity, 30.0F, 30.0F);
        assert LivingEntity != null;
        double d0 = this.attacker.squaredDistanceTo(LivingEntity.getX(), LivingEntity.getBoundingBox().minY, LivingEntity.getZ());
        --this.delayCounter;

        if ((this.longMemory || this.attacker.getVisibilityCache().canSee(LivingEntity)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || LivingEntity.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRandom().nextFloat() < 0.05F)) {
            this.targetX = LivingEntity.getX();
            this.targetY = LivingEntity.getBoundingBox().minY;
            this.targetZ = LivingEntity.getZ();
            this.delayCounter = 4 + this.attacker.getRandom().nextInt(7);

            if (this.canPenalize) {
                this.delayCounter += this.failedPathFindingPenalty;
                if (this.attacker.getNavigation().getCurrentPath() != null) {
                    PathNode finalPathPoint = this.attacker.getNavigation().getCurrentPath().getEnd();
                    if (finalPathPoint != null && LivingEntity.squaredDistanceTo(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                        this.failedPathFindingPenalty = 0;
                    else this.failedPathFindingPenalty += 10;
                } else this.failedPathFindingPenalty += 10;
            }

            if (d0 > 1024.0D) this.delayCounter += 10;
            else if (d0 > 256.0D) this.delayCounter += 5;

            if (!this.attacker.getNavigation().startMovingTo(LivingEntity, this.speedTowardsTarget))
                this.delayCounter += 15;
        }
        this.attackTick = Math.max(this.attackTick - 1, 0);
        this.checkAndPerformAttack(LivingEntity, d0);
    }

    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double d0 = this.getAttackReachSqr(enemy);
        if (distToEnemySqr <= d0) {
            this.attackTick = 20;
            this.attacker.swingHand(Hand.MAIN_HAND);
            this.attacker.tryAttack(enemy);
        }
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F + attackTarget.getWidth();
    }
}