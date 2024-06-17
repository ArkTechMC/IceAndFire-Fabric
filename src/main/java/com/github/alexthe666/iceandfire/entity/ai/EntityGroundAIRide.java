package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.util.IGroundMount;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class EntityGroundAIRide<T extends MobEntity & IGroundMount> extends Goal {
    private final T dragon;
    private PlayerEntity player;

    public EntityGroundAIRide(T dragon) {
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        this.dragon = dragon;
    }

    @Override
    public boolean canStart() {
        this.player = this.dragon.getRidingPlayer();
        return this.player != null;
    }

    @Override
    public void start() {
        this.dragon.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.dragon.getNavigation().stop();
        this.dragon.setTarget(null);
        double x = this.dragon.getX();
        double y = this.dragon.getY();
        if (this.dragon instanceof EntityDeathWorm worm)
            y = worm.processRiderY(y);
        double z = this.dragon.getZ();
        double speed = 1.8F * this.dragon.getRideSpeedModifier();
        if (this.player.sidewaysSpeed != 0 || this.player.forwardSpeed != 0) {
            Vec3d lookVec = this.player.getRotationVector();
            if (this.player.forwardSpeed < 0)
                lookVec = lookVec.rotateY((float) Math.PI);
            else if (this.player.sidewaysSpeed > 0)
                lookVec = lookVec.rotateY((float) Math.PI * 0.5f);
            else if (this.player.sidewaysSpeed < 0)
                lookVec = lookVec.rotateY((float) Math.PI * -0.5f);
            if (Math.abs(this.player.sidewaysSpeed) > 0.0)
                speed *= 0.25D;
            if (this.player.forwardSpeed < 0.0)
                speed *= 0.15D;
            x += lookVec.x * 10;
            z += lookVec.z * 10;
        }
        this.dragon.getMoveControl().moveTo(x, y, z, speed);
    }
}
