package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class MyrmexAITradePlayer extends Goal {
    private final EntityMyrmexBase myrmex;

    public MyrmexAITradePlayer(EntityMyrmexBase myrmex) {
        this.myrmex = myrmex;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    /**
     * Returns whether the Goal should begin execution.
     */
    @Override
    public boolean canStart() {
        if (!this.myrmex.isAlive()) return false;
        else if (this.myrmex.isTouchingWater()) return false;
        else if (!this.myrmex.isOnGround()) return false;
        else if (this.myrmex.velocityModified) return false;
        else {
            PlayerEntity PlayerEntity = this.myrmex.getCustomer();
            if (PlayerEntity == null) return false;
            else if (this.myrmex.squaredDistanceTo(PlayerEntity) > 16.0D)
                return false;
            else if (this.myrmex.getHive() != null && !this.myrmex.getHive().isPlayerReputationTooLowToTrade(PlayerEntity.getUuid()))
                return false;
            else return PlayerEntity.currentScreenHandler != null;
        }
    }

    @Override
    public void tick() {
        this.myrmex.getNavigation().stop();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void stop() {
        this.myrmex.setCustomer(null);
    }
}
