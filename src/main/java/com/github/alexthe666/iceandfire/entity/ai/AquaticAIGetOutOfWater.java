package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.EnumSet;

public class AquaticAIGetOutOfWater extends Goal {
    private final MobEntity creature;
    private final double movementSpeed;
    private final World world;
    private double shelterX;
    private double shelterY;
    private double shelterZ;

    public AquaticAIGetOutOfWater(MobEntity theCreatureIn, double movementSpeedIn) {
        this.creature = theCreatureIn;
        this.movementSpeed = movementSpeedIn;
        this.world = theCreatureIn.getWorld();
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!creature.isTouchingWater() || !((EntitySiren) creature).wantsToSing()) {
            return false;
        } else {
            Vec3d Vector3d = this.findPossibleShelter();

            if (Vector3d == null) {
                return false;
            } else {
                this.shelterX = Vector3d.x;
                this.shelterY = Vector3d.y;
                this.shelterZ = Vector3d.z;
                return true;
            }
        }
    }

    @Override
    public boolean shouldContinue() {
        return !this.creature.getNavigation().isIdle();
    }

    @Override
    public void start() {
        this.creature.getNavigation().startMovingTo(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }

    private Vec3d findPossibleShelter() {
        Random random = this.creature.getRandom();
        BlockPos blockpos = BlockPos.ofFloored(this.creature.getBlockX(), this.creature.getBoundingBox().minY, this.creature.getBlockZ());

        for (int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
            if (this.world.getBlockState(blockpos1).isOpaqueFullCube(world, blockpos1)) {
                return new Vec3d(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
            }
        }

        return null;
    }
}