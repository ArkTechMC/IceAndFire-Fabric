package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.EnumSet;

public class AmphithereAIFollowOwner extends Goal {
    private final EntityAmphithere ampithere;
    private final double followSpeed;
    World world;
    float maxDist;
    float minDist;
    private LivingEntity owner;
    private int timeToRecalcPath;
    private float oldWaterCost;

    public AmphithereAIFollowOwner(EntityAmphithere ampithereIn, double followSpeedIn, float minDistIn, float maxDistIn) {
        this.ampithere = ampithereIn;
        this.world = ampithereIn.getWorld();
        this.followSpeed = followSpeedIn;
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        LivingEntity LivingEntity = this.ampithere.getOwner();
        if (this.ampithere.getCommand() != 2) {
            return false;
        }
        if (LivingEntity == null) {
            return false;
        } else if (LivingEntity instanceof PlayerEntity && LivingEntity.isSpectator()) {
            return false;
        } else if (this.ampithere.isSitting()) {
            return false;
        } else if (this.ampithere.squaredDistanceTo(LivingEntity) < this.minDist * this.minDist) {
            return false;
        } else {
            this.owner = LivingEntity;
            return true;
        }
    }

    @Override
    public boolean shouldContinue() {
        return !this.noPath() && this.ampithere.squaredDistanceTo(this.owner) > this.maxDist * this.maxDist
                && !this.ampithere.isSitting();
    }

    private boolean noPath() {
        if (!this.ampithere.isFlying()) {
            return this.ampithere.getNavigation().isIdle();
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.ampithere.getPathfindingPenalty(PathNodeType.WATER);
        this.ampithere.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.ampithere.getNavigation().stop();
        this.ampithere.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.ampithere.getLookControl().lookAt(this.owner, 10.0F,
                this.ampithere.getMaxLookPitchChange());

        if (!this.ampithere.isSitting()) {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                this.tryMoveTo();
                if (!this.ampithere.isLeashed() && !this.ampithere.hasVehicle()) {
                    if (this.ampithere.squaredDistanceTo(this.owner) >= 144.0D) {
                        final int i = MathHelper.floor(this.owner.getX()) - 2;
                        final int j = MathHelper.floor(this.owner.getZ()) - 2;
                        final int k = MathHelper.floor(this.owner.getBoundingBox().minY);

                        for (int l = 0; l <= 4; ++l) {
                            for (int i1 = 0; i1 <= 4; ++i1) {
                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.canTeleportToBlock(new BlockPos(i, j, k))) {
                                    this.ampithere.refreshPositionAndAngles(i + l + 0.5F, k, j + i1 + 0.5F,
                                            this.ampithere.getYaw(), this.ampithere.getPitch());
                                    this.ampithere.getNavigation().stop();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected boolean canTeleportToBlock(BlockPos pos) {
        BlockState blockstate = this.world.getBlockState(pos);
        return blockstate.allowsSpawning(this.world, pos, this.ampithere.getType()) && this.world.isAir(pos.up()) && this.world.isAir(pos.up(2));
    }

    private boolean tryMoveTo() {
        if (!this.ampithere.isFlying()) {
            return this.ampithere.getNavigation().startMovingTo(this.owner, this.followSpeed);
        } else {
            this.ampithere.getMoveControl().moveTo(this.owner.getX(), this.owner.getY() + this.owner.getStandingEyeHeight() + 5 + this.ampithere.getRandom().nextInt(8), this.owner.getZ(), 0.25D);
            return true;
        }
    }
}