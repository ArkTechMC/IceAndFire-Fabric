package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;

public class DragonAILookIdle extends Goal {
    private final EntityDragonBase dragon;
    private double lookX;
    private double lookZ;
    private int idleTime;

    public DragonAILookIdle(EntityDragonBase prehistoric) {
        this.dragon = prehistoric;
        this.setControls(EnumSet.of(Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (!this.dragon.canMove() || this.dragon.getAnimation() == EntityDragonBase.ANIMATION_SHAKEPREY || this.dragon.isFuelingForge())
            return false;
        return this.dragon.getRandom().nextFloat() < 0.02F;
    }

    @Override
    public boolean shouldContinue() {
        return this.idleTime >= 0 && this.dragon.canMove();
    }

    @Override
    public void start() {
        final double d0 = (Math.PI * 2D) * this.dragon.getRandom().nextDouble();
        this.lookX = MathHelper.cos((float) d0);
        this.lookZ = MathHelper.sin((float) d0);
        this.idleTime = 20 + this.dragon.getRandom().nextInt(20);
    }

    @Override
    public void tick() {
        if (this.idleTime > 0) --this.idleTime;
        this.dragon.getLookControl().lookAt(this.dragon.getX() + this.lookX, this.dragon.getY() + this.dragon.getStandingEyeHeight(), this.dragon.getZ() + this.lookZ, this.dragon.getMaxHeadRotation(), this.dragon.getMaxLookPitchChange());
    }
}