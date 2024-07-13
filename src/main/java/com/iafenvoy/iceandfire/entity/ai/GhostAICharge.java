package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.uranus.animation.IAnimatedEntity;
import com.iafenvoy.iceandfire.entity.EntityGhost;
import com.iafenvoy.iceandfire.entity.util.dragon.DragonUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class GhostAICharge extends Goal {
    private final EntityGhost ghost;
    public boolean firstPhase = true;
    public Vec3d moveToPos = null;
    public Vec3d offsetOf = Vec3d.ZERO;

    public GhostAICharge(EntityGhost ghost) {
        this.setControls(EnumSet.of(Control.MOVE));
        this.ghost = ghost;
    }

    @Override
    public boolean canStart() {
        return this.ghost.getTarget() != null && !this.ghost.isCharging();
    }

    @Override
    public boolean shouldContinue() {
        return this.ghost.getTarget() != null && this.ghost.getTarget().isAlive();
    }

    @Override
    public void start() {
        this.ghost.setCharging(true);
    }

    @Override
    public void stop() {
        this.firstPhase = true;
        this.moveToPos = null;
        this.ghost.setCharging(false);
    }

    @Override
    public void tick() {
        LivingEntity target = this.ghost.getTarget();
        if (target != null) {
            if (this.ghost.getAnimation() == IAnimatedEntity.NO_ANIMATION && this.ghost.distanceTo(target) < 1.4D)
                this.ghost.setAnimation(EntityGhost.ANIMATION_HIT);
            if (this.firstPhase) {
                if (this.moveToPos == null) {
                    BlockPos moveToPos = DragonUtils.getBlockInTargetsViewGhost(this.ghost, target);
                    this.moveToPos = Vec3d.ofCenter(moveToPos);
                } else {
                    this.ghost.getNavigation().startMovingTo(this.moveToPos.x + 0.5D, this.moveToPos.y + 0.5D, this.moveToPos.z + 0.5D, 1F);
                    if (this.ghost.squaredDistanceTo(this.moveToPos.add(0.5D, 0.5D, 0.5D)) < 9D) {
                        if (this.ghost.getAnimation() == IAnimatedEntity.NO_ANIMATION)
                            this.ghost.setAnimation(EntityGhost.ANIMATION_SCARE);
                        this.firstPhase = false;
                        this.moveToPos = null;
                        this.offsetOf = target.getPos().subtract(this.ghost.getPos()).normalize();
                    }
                }
            } else {
                Vec3d fin = target.getPos();
                this.moveToPos = new Vec3d(fin.x, target.getY() + target.getStandingEyeHeight() / 2, fin.z);
                this.ghost.getNavigation().startMovingTo(target, 1.2F);
                if (this.ghost.squaredDistanceTo(this.moveToPos.add(0.5D, 0.5D, 0.5D)) < 3D)
                    this.stop();
            }
        }

    }
}
