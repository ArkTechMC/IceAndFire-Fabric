package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathResult;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

import java.util.EnumSet;

public class MyrmexAIAttackMelee extends Goal {
    protected EntityMyrmexBase myrmex;
    private int attackTick;
    private final double speedTowardsTarget;
    private final boolean longMemory;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int failedPathFindingPenalty = 0;
    private final boolean canPenalize = false;
    private PathResult attackPath;

    public MyrmexAIAttackMelee(EntityMyrmexBase dragon, double speedIn, boolean useLongMemory) {
        this.myrmex = dragon;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        LivingEntity LivingEntity = this.myrmex.getTarget();
        if (!(this.myrmex.getNavigation() instanceof AdvancedPathNavigate)) {
            return false;
        }
        if (LivingEntity instanceof PlayerEntity && this.myrmex.getHive() != null) {
            if (!this.myrmex.getHive().isPlayerReputationLowEnoughToFight(LivingEntity.getUuid())) {
                return false;
            }
        }
        if (LivingEntity == null) {
            return false;
        } else if (!LivingEntity.isAlive()) {
            return false;
        } else if (!myrmex.canMove()) {
            return false;
        } else {

            attackPath = ((AdvancedPathNavigate) this.myrmex.getNavigation()).moveToLivingEntity(LivingEntity, speedTowardsTarget);
            if (this.attackPath != null) {
                return true;
            } else {
                return this.getAttackReachSqr(LivingEntity) >= this.myrmex.squaredDistanceTo(LivingEntity.getX(), LivingEntity.getBoundingBox().minY, LivingEntity.getZ());
            }
        }
    }

    @Override
    public boolean shouldContinue() {
        LivingEntity LivingEntity = this.myrmex.getTarget();
        if (this.myrmex.getAttacking() != null && this.myrmex.getAttacking().isAlive()) {
            LivingEntity = this.myrmex.getAttacking();
        }
        if (LivingEntity != null && !LivingEntity.isAlive() || !(this.myrmex.getNavigation() instanceof AdvancedPathNavigate)) {
            this.stop();
            return false;
        }

        return LivingEntity != null && (LivingEntity.isAlive() && (!(LivingEntity instanceof PlayerEntity) || !LivingEntity.isSpectator() && !((PlayerEntity) LivingEntity).isCreative()));
    }

    @Override
    public void start() {
        this.delayCounter = 0;
    }

    @Override
    public void stop() {
        LivingEntity LivingEntity = this.myrmex.getTarget();
        if (LivingEntity instanceof PlayerEntity && (LivingEntity.isSpectator() || ((PlayerEntity) LivingEntity).isCreative())) {
            this.myrmex.setTarget(null);
            this.myrmex.onAttacking(null);
        }
    }

    @Override
    public void tick() {
        LivingEntity entity = this.myrmex.getTarget();
        if (entity != null) {
            this.myrmex.getNavigation().startMovingTo(entity, speedTowardsTarget);
            final double d0 = this.myrmex.squaredDistanceTo(entity.getX(), entity.getBoundingBox().minY, entity.getZ());
            final double d1 = this.getAttackReachSqr(entity);
            --this.delayCounter;
            if ((this.longMemory || this.myrmex.getVisibilityCache().canSee(entity)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || entity.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.myrmex.getRandom().nextFloat() < 0.05F)) {
                this.targetX = entity.getX();
                this.targetY = entity.getBoundingBox().minY;
                this.targetZ = entity.getZ();
                this.delayCounter = 4 + this.myrmex.getRandom().nextInt(7);

                if (this.canPenalize) {
                    this.delayCounter += failedPathFindingPenalty;
                    if (this.myrmex.getNavigation().getCurrentPath() != null) {
                        net.minecraft.entity.ai.pathing.PathNode finalPathPoint = this.myrmex.getNavigation().getCurrentPath().getEnd();
                        if (finalPathPoint != null && entity.squaredDistanceTo(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                            failedPathFindingPenalty = 0;
                        else
                            failedPathFindingPenalty += 10;
                    } else {
                        failedPathFindingPenalty += 10;
                    }
                }

                if (d0 > 1024.0D) {
                    this.delayCounter += 10;
                } else if (d0 > 256.0D) {
                    this.delayCounter += 5;
                }
                if (this.myrmex.canMove()) {
                    this.delayCounter += 15;
                }
            }

            this.attackTick = Math.max(this.attackTick - 1, 0);

            if (d0 <= d1 && this.attackTick <= 0) {
                this.attackTick = 20;
                this.myrmex.swingHand(Hand.MAIN_HAND);
                this.myrmex.tryAttack(entity);
            }
        }
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return this.myrmex.getWidth() * 2.0F * this.myrmex.getWidth() * 2.0F + attackTarget.getWidth();
    }
}