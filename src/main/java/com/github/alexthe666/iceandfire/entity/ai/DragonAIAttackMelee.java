package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

import java.util.EnumSet;

public class DragonAIAttackMelee extends Goal {
    private final boolean longMemory;
    private final double speedTowardsTarget;
    protected final EntityDragonBase dragon;
    private int attackTick;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int failedPathFindingPenalty = 0;

    public DragonAIAttackMelee(EntityDragonBase dragon, double speedIn, boolean useLongMemory) {
        this.dragon = dragon;
        this.longMemory = useLongMemory;
        this.speedTowardsTarget = speedIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        LivingEntity livingEntity = this.dragon.getTarget();
        if (!(this.dragon.getNavigation() instanceof AdvancedPathNavigate)) {
            return false;
        }
        if (livingEntity == null) {
            return false;
        } else if (!livingEntity.isAlive()) {
            return false;
        } else if (!this.dragon.canMove() || this.dragon.isHovering() || this.dragon.isFlying()) {
            return false;
        } else {
            ((AdvancedPathNavigate) this.dragon.getNavigation()).moveToLivingEntity(livingEntity, this.speedTowardsTarget);
            return true;
        }
    }

    @Override
    public boolean shouldContinue() {
        if (!(this.dragon.getNavigation() instanceof AdvancedPathNavigate)) {
            return false;
        }
        LivingEntity livingEntity = this.dragon.getTarget();
        if (livingEntity != null && !livingEntity.isAlive()) {
            this.stop();
            return false;
        }

        return livingEntity != null && livingEntity.isAlive() && !this.dragon.isFlying() && !this.dragon.isHovering();
    }

    @Override
    public void start() {
        this.delayCounter = 0;
    }

    @Override
    public void stop() {
        LivingEntity LivingEntity = this.dragon.getTarget();
        if (LivingEntity instanceof PlayerEntity && (LivingEntity.isSpectator() || ((PlayerEntity) LivingEntity).isCreative())) {
            this.dragon.setTarget(null);
        }
        this.dragon.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity entity = this.dragon.getTarget();
        if (this.delayCounter > 0) {
            this.delayCounter--;
        }
        if (entity != null) {
            if (this.dragon.getAnimation() == EntityDragonBase.ANIMATION_SHAKEPREY) {
                this.stop();
                return;
            }

            ((AdvancedPathNavigate) this.dragon.getNavigation()).moveToLivingEntity(entity, this.speedTowardsTarget);

            final double d0 = this.dragon.squaredDistanceTo(entity.getX(), entity.getBoundingBox().minY, entity.getZ());
            final double d1 = this.getAttackReachSqr(entity);
            --this.delayCounter;
            if ((this.longMemory || this.dragon.getVisibilityCache().canSee(entity)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || entity.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.dragon.getRandom().nextFloat() < 0.05F)) {
                this.targetX = entity.getX();
                this.targetY = entity.getBoundingBox().minY;
                this.targetZ = entity.getZ();
                this.delayCounter = 4 + this.dragon.getRandom().nextInt(7);

                boolean canPenalize = false;
                if (canPenalize) {
                    this.delayCounter += this.failedPathFindingPenalty;
                    if (this.dragon.getNavigation().getCurrentPath() != null) {
                        net.minecraft.entity.ai.pathing.PathNode finalPathPoint = this.dragon.getNavigation().getCurrentPath().getEnd();
                        if (finalPathPoint != null && entity.squaredDistanceTo(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                            this.failedPathFindingPenalty = 0;
                        else
                            this.failedPathFindingPenalty += 10;
                    } else {
                        this.failedPathFindingPenalty += 10;
                    }
                }

                if (d0 > 1024.0D) {
                    this.delayCounter += 10;
                } else if (d0 > 256.0D) {
                    this.delayCounter += 5;
                }
                if (this.dragon.canMove()) {
                    this.delayCounter += 15;
                }
            }

            this.attackTick = Math.max(this.attackTick - 1, 0);

            if (d0 <= d1 && this.attackTick == 0) {
                this.attackTick = 20;
                this.dragon.swingHand(Hand.MAIN_HAND);
                this.dragon.tryAttack(entity);
            }
        }
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return this.dragon.getWidth() * 2.0F * this.dragon.getWidth() * 2.0F + attackTarget.getWidth();
    }
}