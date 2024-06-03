package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class DragonAIWander extends Goal {
    private final EntityDragonBase dragon;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private final double speed;
    private int executionChance;
    private boolean mustUpdate;

    public DragonAIWander(EntityDragonBase creatureIn, double speedIn) {
        this(creatureIn, speedIn, 20);
    }

    public DragonAIWander(EntityDragonBase creatureIn, double speedIn, int chance) {
        this.dragon = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setControls(EnumSet.of(Control.MOVE));

    }

    @Override
    public boolean canStart() {
        if (!dragon.canMove() || dragon.isFuelingForge()) {
            return false;
        }
        if (dragon.isFlying() || dragon.isHovering()) {
            return false;
        }
        if (!this.mustUpdate) {
            if (this.dragon.getRandom().nextInt(executionChance) != 0) {
                return false;
            }
        }
        Vec3d Vector3d = NoPenaltyTargeting.find(this.dragon, 10, 7);
        if (Vector3d == null) {
            return false;
        } else {
            this.xPosition = Vector3d.x;
            this.yPosition = Vector3d.y;
            this.zPosition = Vector3d.z;
            this.mustUpdate = false;

            return true;
        }
    }

    @Override
    public boolean shouldContinue() {
        return !this.dragon.getNavigation().isIdle();
    }

    @Override
    public void start() {
        this.dragon.getNavigation().startMovingTo(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    public void makeUpdate() {
        this.mustUpdate = true;
    }

    public void setExecutionChance(int newchance) {
        this.executionChance = newchance;
    }
}