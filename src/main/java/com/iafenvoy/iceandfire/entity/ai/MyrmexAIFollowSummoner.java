package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.entity.EntityMyrmexSwarmer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.EnumSet;

public class MyrmexAIFollowSummoner extends Goal {
    final World world;
    final float maxDist;
    final float minDist;
    private final EntityMyrmexSwarmer tameable;
    private LivingEntity owner;
    private int timeToRecalcPath;
    private float oldWaterCost;

    public MyrmexAIFollowSummoner(EntityMyrmexSwarmer tameableIn, double followSpeedIn, float minDistIn, float maxDistIn) {
        this.tameable = tameableIn;
        this.world = tameableIn.getWorld();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        LivingEntity LivingEntity = this.tameable.getSummoner();
        if (this.tameable.getTarget() != null) return false;

        if (LivingEntity == null) return false;
        else if (LivingEntity instanceof PlayerEntity && LivingEntity.isSpectator())
            return false;
        else if (this.tameable.squaredDistanceTo(LivingEntity) < this.minDist * this.minDist)
            return false;
        else {
            this.owner = LivingEntity;
            return true;
        }
    }

    @Override
    public boolean shouldContinue() {
        return this.tameable.getTarget() == null && this.tameable.squaredDistanceTo(this.owner) > this.maxDist * this.maxDist;
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
    }

    private boolean isEmptyBlock(BlockPos pos) {
        BlockState BlockState = this.world.getBlockState(pos);
        return BlockState.isAir() || !BlockState.isOpaque();
    }

    @Override
    public void tick() {
        if (this.tameable.getTarget() != null) return;
        this.tameable.getLookControl().lookAt(this.owner, 10.0F, this.tameable.getMaxLookPitchChange());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            this.tameable.getMoveControl().moveTo(this.owner.getX(), this.owner.getY() + this.owner.getStandingEyeHeight(), this.owner.getZ(), 0.25D);
            if (!this.tameable.isLeashed() && this.tameable.squaredDistanceTo(this.owner) >= 50.0D) {
                final int i = MathHelper.floor(this.owner.getX()) - 2;
                final int j = MathHelper.floor(this.owner.getZ()) - 2;
                final int k = MathHelper.floor(this.owner.getBoundingBox().minY);

                for (int l = 0; l <= 4; ++l)
                    for (int i1 = 0; i1 <= 4; ++i1)
                        if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.isEmptyBlock(new BlockPos(i + l, k, j + i1)) && this.isEmptyBlock(new BlockPos(i + l, k + 1, j + i1))) {
                            this.tameable.refreshPositionAndAngles(i + l + 0.5F, k + 1.5, j + i1 + 0.5F, this.tameable.getYaw(), this.tameable.getPitch());
                            return;
                        }
            }
        }
    }
}