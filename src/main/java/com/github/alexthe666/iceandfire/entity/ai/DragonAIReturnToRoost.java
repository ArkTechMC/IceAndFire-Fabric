package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class DragonAIReturnToRoost extends Goal {

    private final EntityDragonBase dragon;

    public DragonAIReturnToRoost(EntityDragonBase entityIn, double movementSpeedIn) {
        this.dragon = entityIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return this.dragon.canMove() && this.dragon.lookingForRoostAIFlag
            && (dragon.getTarget() == null || !dragon.getTarget().isAlive())
            && dragon.getPositionTarget() != null
            && DragonUtils.isInHomeDimension(dragon)
            && dragon.getDistanceSquared(Vec3d.ofCenter(dragon.getPositionTarget())) > dragon.getWidth()
            * dragon.getWidth();
    }

    @Override
    public void tick() {
        if (this.dragon.getPositionTarget() != null) {
            final double dist = Math.sqrt(dragon.getDistanceSquared(Vec3d.ofCenter(dragon.getPositionTarget())));
            final double xDist = Math.abs(dragon.getX() - dragon.getPositionTarget().getX() - 0.5F);
            final double zDist = Math.abs(dragon.getZ() - dragon.getPositionTarget().getZ() - 0.5F);
            final double xzDist = Math.sqrt(xDist * xDist + zDist * zDist);

            if (dist < this.dragon.getWidth()) {
                this.dragon.setFlying(false);
                this.dragon.setHovering(false);
                this.dragon.getNavigation().startMovingTo(this.dragon.getPositionTarget().getX(),
                    this.dragon.getPositionTarget().getY(), this.dragon.getPositionTarget().getZ(), 1.0F);
            } else {
                double yAddition = 15 + dragon.getRandom().nextInt(3);
                if (xzDist < 40) {
                    yAddition = 0;
                    if (this.dragon.isOnGround()) {
                        this.dragon.setFlying(false);
                        this.dragon.setHovering(false);
                        this.dragon.flightManager.setFlightTarget(
                            Vec3d.ofCenter(this.dragon.getPositionTarget(), yAddition));
                        this.dragon.getNavigation().startMovingTo(this.dragon.getPositionTarget().getX(),
                            this.dragon.getPositionTarget().getY(), this.dragon.getPositionTarget().getZ(), 1.0F);
                        return;
                    }
                }
                if (!this.dragon.isFlying() && !this.dragon.isHovering() && xzDist > 40) {
                    this.dragon.setHovering(true);
                }
                if (this.dragon.isFlying()) {
                    this.dragon.flightManager.setFlightTarget(
                        Vec3d.ofCenter(this.dragon.getPositionTarget(), yAddition));
                    this.dragon.getNavigation().startMovingTo(this.dragon.getPositionTarget().getX(),
                        yAddition + this.dragon.getPositionTarget().getY(), this.dragon.getPositionTarget().getZ(), 1F);
                }
                this.dragon.flyTicks = 0;
            }

        }

    }

    @Override
    public boolean shouldContinue() {
        return canStart();
    }

}
