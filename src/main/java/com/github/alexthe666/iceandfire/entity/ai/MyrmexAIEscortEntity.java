package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSoldier;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class MyrmexAIEscortEntity extends Goal {
    private final EntityMyrmexSoldier myrmex;
    private final double movementSpeed;

    public MyrmexAIEscortEntity(EntityMyrmexSoldier entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        return this.myrmex.canMove() && this.myrmex.getTarget() == null && this.myrmex.guardingEntity != null && (this.myrmex.guardingEntity.canSeeSky() || !this.myrmex.canSeeSky()) && !this.myrmex.isEnteringHive;
    }

    @Override
    public void tick() {
        if (this.myrmex.guardingEntity != null && (this.myrmex.distanceTo(this.myrmex.guardingEntity) > 30 || this.myrmex.getNavigation().isIdle())) {
            this.myrmex.getNavigation().startMovingTo(this.myrmex.guardingEntity, this.movementSpeed);
        }
    }

    @Override
    public boolean shouldContinue() {
        return this.myrmex.canMove() && this.myrmex.getTarget() == null && this.myrmex.guardingEntity != null && this.myrmex.guardingEntity.isAlive() && (this.myrmex.distanceTo(this.myrmex.guardingEntity) < 15 || !this.myrmex.getNavigation().isIdle()) && (this.myrmex.canSeeSky() == this.myrmex.guardingEntity.canSeeSky() && !this.myrmex.guardingEntity.canSeeSky());
    }

}