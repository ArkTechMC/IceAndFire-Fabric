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
        player = dragon.getRidingPlayer();

        return player != null;
    }

    @Override
    public void start() {
        dragon.getNavigation().stop();
    }

    @Override
    public void tick() {
        dragon.getNavigation().stop();
        dragon.setTarget(null);
        double x = dragon.getX();
        double y = dragon.getY();
        if (dragon instanceof EntityDeathWorm) {
            y = ((EntityDeathWorm) dragon).processRiderY(y);
        }
        double z = dragon.getZ();
        double speed = 1.8F * dragon.getRideSpeedModifier();
        if (player.sidewaysSpeed != 0 || player.forwardSpeed != 0) {
            Vec3d lookVec = player.getRotationVector();
            if (player.forwardSpeed < 0) {
                lookVec = lookVec.rotateY((float) Math.PI);
            } else if (player.sidewaysSpeed > 0) {
                lookVec = lookVec.rotateY((float) Math.PI * 0.5f);
            } else if (player.sidewaysSpeed < 0) {
                lookVec = lookVec.rotateY((float) Math.PI * -0.5f);
            }
            if (Math.abs(player.sidewaysSpeed) > 0.0) {
                speed *= 0.25D;
            }
            if (player.forwardSpeed < 0.0) {
                speed *= 0.15D;
            }
            x += lookVec.x * 10;
            z += lookVec.z * 10;
        }
        dragon.getMoveControl().moveTo(x, y, z, speed);
    }
}
