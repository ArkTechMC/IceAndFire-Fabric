package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityPixie;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.EnumSet;

public class PixieAIFollowOwner extends Goal {
    private final EntityPixie tameable;
    World world;
    float maxDist;
    float minDist;
    private LivingEntity owner;
    private int timeToRecalcPath;
    private float oldWaterCost;

    public PixieAIFollowOwner(EntityPixie tameableIn, double followSpeedIn, float minDistIn, float maxDistIn) {
        this.tameable = tameableIn;
        this.world = tameableIn.getWorld();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        LivingEntity LivingEntity = this.tameable.getOwner();

        if (LivingEntity == null) {
            return false;
        } else if (LivingEntity instanceof PlayerEntity && LivingEntity.isSpectator()) {
            return false;
        } else if (this.tameable.isPixieSitting()) {
            return false;
        } else if (this.tameable.squaredDistanceTo(LivingEntity) < this.minDist * this.minDist) {
            return false;
        } else {
            this.owner = LivingEntity;
            return true;
        }
    }

    @Override
    public boolean shouldContinue() {
        // first check sitting to save distance calculation in case pixie indeed is
        return !this.tameable.isPixieSitting() && this.tameable.squaredDistanceTo(this.owner) > this.maxDist * this.maxDist;
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.tameable.getPathfindingPenalty(PathNodeType.WATER);
        this.tameable.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.tameable.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterCost);
        this.tameable.slowSpeed = false;
    }

    private boolean isEmptyBlock(BlockPos pos) {
        BlockState BlockState = this.world.getBlockState(pos);
        return BlockState.isAir() || !BlockState.isOpaque();
    }

    @Override
    public void tick() {
        this.tameable.getLookControl().lookAt(this.owner, 10.0F,
                this.tameable.getMaxLookPitchChange());

        if (!this.tameable.isPixieSitting()) {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;

                this.tameable.getMoveControl().moveTo(this.owner.getX(), this.owner.getY() + this.owner.getStandingEyeHeight(), this.owner.getZ(), 0.25D);
                this.tameable.slowSpeed = true;
                if (!this.tameable.isLeashed()) {
                    if (this.tameable.squaredDistanceTo(this.owner) >= 50.0D) {
                        final int i = MathHelper.floor(this.owner.getX()) - 2;
                        final int j = MathHelper.floor(this.owner.getZ()) - 2;
                        final int k = MathHelper.floor(this.owner.getBoundingBox().minY);

                        for (int l = 0; l <= 4; ++l) {
                            for (int i1 = 0; i1 <= 4; ++i1) {
                                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.isEmptyBlock(new BlockPos(i + l, k, j + i1)) && this.isEmptyBlock(new BlockPos(i + l, k + 1, j + i1))) {
                                    this.tameable.refreshPositionAndAngles(i + l + 0.5F, k + 1.5, j + i1 + 0.5F,
                                            this.tameable.getYaw(), this.tameable.getPitch());
                                    return;
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}