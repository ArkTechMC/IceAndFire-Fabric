package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

public class DragonAIEscort extends Goal {
    private final EntityDragonBase dragon;
    private BlockPos previousPosition;

    public DragonAIEscort(EntityDragonBase entityIn, double movementSpeedIn) {
        this.dragon = entityIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return this.dragon.canMove() && this.dragon.getTarget() == null && this.dragon.getOwner() != null && this.dragon.getCommand() == 2;
    }

    @Override
    public void tick() {
        if (this.dragon.getOwner() != null) {
            final float dist = this.dragon.distanceTo(this.dragon.getOwner());
            float maxRange = 2000F;
            if (dist > maxRange) return;
            if (dist > this.dragon.getBoundingBox().getAverageSideLength() && (!this.dragon.isFlying() && !this.dragon.isHovering() || !this.dragon.isAllowedToTriggerFlight()))
                if (this.previousPosition == null || this.previousPosition.getSquaredDistance(this.dragon.getOwner().getBlockPos()) > 9) {
                    this.dragon.getNavigation().startMovingTo(this.dragon.getOwner(), 1F);
                    this.previousPosition = this.dragon.getOwner().getBlockPos();
                }
            if ((dist > 30F || this.dragon.getOwner().getY() - this.dragon.getY() > 8) && !this.dragon.isFlying() && !this.dragon.isHovering() && this.dragon.isAllowedToTriggerFlight()) {
                this.dragon.setHovering(true);
                this.dragon.setInSittingPose(false);
                this.dragon.setSitting(false);
                this.dragon.flyTicks = 0;
            }
        }
    }

    @Override
    public boolean shouldContinue() {
        return this.dragon.getCommand() == 2 && this.dragon.canMove() && this.dragon.getTarget() == null && this.dragon.getOwner() != null && this.dragon.getOwner().isAlive() && (this.dragon.distanceTo(this.dragon.getOwner()) > 15 || !this.dragon.getNavigation().isIdle());
    }
}
