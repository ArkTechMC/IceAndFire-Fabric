package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.github.alexthe666.iceandfire.entity.util.dragon.DragonUtils;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class StymphalianBirdAIAirTarget extends Goal {
    private final EntityStymphalianBird bird;

    public StymphalianBirdAIAirTarget(EntityStymphalianBird bird) {
        this.bird = bird;
    }

    public static BlockPos getNearbyAirTarget(EntityStymphalianBird bird) {
        if (bird.getTarget() == null) {
            BlockPos pos = DragonUtils.getBlockInViewStymphalian(bird);
            if (pos != null && bird.getWorld().getBlockState(pos).isAir()) {
                return pos;
            }
            if (bird.flock != null && bird.flock.isLeader(bird)) {
                bird.flock.setTarget(bird.airTarget);
            }
        } else {
            return BlockPos.ofFloored(bird.getTarget().getBlockX(), bird.getTarget().getY() + bird.getTarget().getStandingEyeHeight(), bird.getTarget().getBlockZ());
        }
        return bird.getBlockPos();
    }

    @Override
    public boolean canStart() {
        if (this.bird != null) {
            if (!this.bird.isFlying()) {
                return false;
            }
            if (this.bird.isBaby() || this.bird.doesWantToLand()) {
                return false;
            }
            if (this.bird.airTarget != null && (this.bird.isTargetBlocked(Vec3d.ofCenter(this.bird.airTarget)))) {
                this.bird.airTarget = null;
            }

            if (this.bird.airTarget != null) {
                return false;
            } else {
                Vec3d vec = this.findAirTarget();

                if (vec == null) {
                    return false;
                } else {
                    this.bird.airTarget = BlockPos.ofFloored(vec);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        if (!this.bird.isFlying()) {
            return false;
        }
        if (this.bird.isBaby()) {
            return false;
        }
        return this.bird.airTarget != null;
    }

    public Vec3d findAirTarget() {
        return Vec3d.ofCenter(getNearbyAirTarget(this.bird));
    }
}