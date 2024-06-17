package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.entity.EntityTroll;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.EnumSet;

public class TrollAIFleeSun extends Goal {
    private final EntityTroll troll;
    private final double movementSpeed;
    private final World world;
    private double shelterX;
    private double shelterY;
    private double shelterZ;

    public TrollAIFleeSun(EntityTroll theCreatureIn, double movementSpeedIn) {
        this.troll = theCreatureIn;
        this.movementSpeed = movementSpeedIn;
        this.world = theCreatureIn.getWorld();
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!this.world.isDay()) return false;
        else if (!this.world.isSkyVisible(BlockPos.ofFloored(this.troll.getBlockX(), this.troll.getBoundingBox().minY, this.troll.getBlockZ())))
            return false;
        else {
            Vec3d Vector3d = this.findPossibleShelter();
            if (Vector3d == null) return false;
            else {
                this.shelterX = Vector3d.x;
                this.shelterY = Vector3d.y;
                this.shelterZ = Vector3d.z;
                return true;
            }
        }
    }

    /**
     * Returns whether an in-progress Goal should continue executing
     */
    @Override
    public boolean shouldContinue() {
        return !this.troll.getNavigation().isIdle();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        this.troll.getNavigation().startMovingTo(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }

    private Vec3d findPossibleShelter() {
        Random random = this.troll.getRandom();
        BlockPos blockpos = BlockPos.ofFloored(this.troll.getBlockX(), this.troll.getBoundingBox().minY, this.troll.getBlockZ());
        for (int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
            if (!this.world.isSkyVisible(blockpos1) && this.troll.getPathfindingFavor(blockpos1) < 0.0F)
                return new Vec3d(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
        }
        return null;
    }
}