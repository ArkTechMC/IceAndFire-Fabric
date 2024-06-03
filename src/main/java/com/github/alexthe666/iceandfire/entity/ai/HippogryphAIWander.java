package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class HippogryphAIWander extends Goal {
    private final EntityHippogryph hippo;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private final double speed;
    private int executionChance;
    private boolean mustUpdate;

    public HippogryphAIWander(EntityHippogryph creatureIn, double speedIn) {
        this(creatureIn, speedIn, 20);
    }

    public HippogryphAIWander(EntityHippogryph creatureIn, double speedIn, int chance) {
        this.hippo = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!hippo.canMove()) {
            return false;
        }
        if (hippo.isFlying() || hippo.isHovering()) {
            return false;
        }
        if (!this.mustUpdate) {
            if (this.hippo.getRandom().nextInt(executionChance) != 0) {
                return false;
            }
        }
        Vec3d Vector3d = NoPenaltyTargeting.find(this.hippo, 10, 7);
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
        return !this.hippo.getNavigation().isIdle();
    }

    @Override
    public void start() {
        this.hippo.getNavigation().startMovingTo(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    public void makeUpdate() {
        this.mustUpdate = true;
    }

    public void setExecutionChance(int newchance) {
        this.executionChance = newchance;
    }
}