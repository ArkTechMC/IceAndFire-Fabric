package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexRoyal;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class MyrmexAIMoveToMate extends Goal {
    private final EntityMyrmexRoyal myrmex;
    private final double movementSpeed;

    public MyrmexAIMoveToMate(EntityMyrmexRoyal entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return this.myrmex.canMove() && this.myrmex.getTarget() == null && this.myrmex.mate != null && this.myrmex.canSeeSky();
    }

    @Override
    public void tick() {
        if (this.myrmex.mate != null && (this.myrmex.distanceTo(this.myrmex.mate) > 30 || this.myrmex.getNavigation().isIdle())) {
            this.myrmex.getMoveControl().moveTo(this.myrmex.mate.getX(), this.myrmex.getY(), this.myrmex.mate.getZ(), this.movementSpeed);
        }
    }

    @Override
    public boolean shouldContinue() {
        return this.myrmex.canMove() && this.myrmex.getTarget() == null && this.myrmex.mate != null && this.myrmex.mate.isAlive() && (this.myrmex.distanceTo(this.myrmex.mate) < 15 || !this.myrmex.getNavigation().isIdle());
    }

}