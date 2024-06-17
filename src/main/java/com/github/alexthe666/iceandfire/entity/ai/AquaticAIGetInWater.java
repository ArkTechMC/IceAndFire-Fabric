package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.EnumSet;

public class AquaticAIGetInWater extends Goal {
    private final MobEntity creature;
    private final double movementSpeed;
    private final World world;
    private double shelterX;
    private double shelterY;
    private double shelterZ;

    public AquaticAIGetInWater(MobEntity theCreatureIn, double movementSpeedIn) {
        this.creature = theCreatureIn;
        this.movementSpeed = movementSpeedIn;
        this.world = theCreatureIn.getWorld();
        this.setControls(EnumSet.of(Control.MOVE));
    }

    protected boolean isAttackerInWater() {
        return this.creature.getTarget() != null && !this.creature.getTarget().isTouchingWater();
    }

    @Override
    public boolean canStart() {
        if (this.creature.hasPassengers() || this.creature instanceof TameableEntity tameable && tameable.isTamed()
                || this.creature.isTouchingWater() || this.isAttackerInWater() || this.creature instanceof EntitySiren siren
                && (siren.isSinging() || siren.wantsToSing()))
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
        return !this.creature.getNavigation().isIdle();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        this.creature.getNavigation().startMovingTo(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }

    public Vec3d findPossibleShelter() {
        return this.findPossibleShelter(10);
    }

    protected Vec3d findPossibleShelter(int xz) {
        Random random = this.creature.getRandom();
        BlockPos blockpos = BlockPos.ofFloored(this.creature.getBlockX(), this.creature.getBoundingBox().minY, this.creature.getBlockZ());
        for (int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.add(random.nextInt(xz * 2) - xz, random.nextInt(3 * 2) - 3, random.nextInt(xz * 2) - xz);
            if (this.world.getBlockState(blockpos1).isOf(Blocks.WATER))
                return new Vec3d(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
        }
        return null;
    }
}
