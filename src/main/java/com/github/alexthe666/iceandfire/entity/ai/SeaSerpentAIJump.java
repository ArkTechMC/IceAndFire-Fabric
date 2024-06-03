package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import net.minecraft.entity.ai.goal.DiveJumpingGoal;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class SeaSerpentAIJump extends DiveJumpingGoal {

    private static final int[] JUMP_DISTANCES = new int[] {
        0, 2, 4, 5, 6, 7
    };
    private final EntitySeaSerpent serpent;
    private final int chance;
    private boolean inWater;

    public SeaSerpentAIJump(EntitySeaSerpent dolphin, int chance) {
        this.serpent = dolphin;
        this.chance = chance;
    }

    @Override
    public boolean canStart() {
        if (this.serpent.getRandom().nextInt(this.chance) != 0 || serpent.getTarget() != null
            || serpent.jumpCooldown != 0) {
            return false;
        } else {
            Direction direction = this.serpent.getMovementDirection();
            final int i = direction.getOffsetX();
            final int j = direction.getOffsetZ();
            BlockPos blockpos = this.serpent.getBlockPos();
            for (int k : JUMP_DISTANCES) {
                if (!this.canJumpTo(blockpos, i, j, k) || !this.isAirAbove(blockpos, i, j, k)) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean canJumpTo(BlockPos pos, int dx, int dz, int scale) {
        BlockPos blockpos = pos.add(dx * scale, 0, dz * scale);
        return this.serpent.getWorld().getFluidState(blockpos).isIn(FluidTags.WATER)
            && !this.serpent.getWorld().getBlockState(blockpos).blocksMovement();
    }

    private boolean isAirAbove(BlockPos pos, int dx, int dz, int scale) {
        return this.serpent.getWorld().getBlockState(pos.add(dx * scale, 1, dz * scale)).isAir()
            && this.serpent.getWorld().getBlockState(pos.add(dx * scale, 2, dz * scale)).isAir();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinue() {
        double d0 = this.serpent.getVelocity().y;
        return serpent.jumpCooldown > 0 && (d0 * d0 >= 0.03F || this.serpent.getPitch() == 0.0F
            || Math.abs(this.serpent.getPitch()) >= 10.0F || !this.serpent.isTouchingWater())
            && !this.serpent.isOnGround();
    }

    @Override
    public boolean canStop() {
        return false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
        Direction direction = this.serpent.getMovementDirection();
        final float up = 1F + serpent.getRandom().nextFloat() * 0.8F;
        this.serpent
            .setVelocity(this.serpent.getVelocity().add(direction.getOffsetX() * 0.6D, up, direction.getOffsetZ() * 0.6D));
        this.serpent.setJumpingOutOfWater(true);
        this.serpent.getNavigation().stop();
        this.serpent.jumpCooldown = serpent.getRandom().nextInt(100) + 100;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by
     * another one
     */
    @Override
    public void stop() {
        this.serpent.setJumpingOutOfWater(false);
        this.serpent.setPitch(0.0F);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        final boolean flag = this.inWater;
        if (!flag) {
            FluidState fluidstate = this.serpent.getWorld().getFluidState(this.serpent.getBlockPos());
            this.inWater = fluidstate.isIn(FluidTags.WATER);
        }

        if (this.inWater && !flag) {
            this.serpent.playSound(SoundEvents.ENTITY_DOLPHIN_JUMP, 1.0F, 1.0F);
        }

        Vec3d vector3d = this.serpent.getVelocity();
        if (vector3d.y * vector3d.y < 0.1F && this.serpent.getPitch() != 0.0F) {
            this.serpent.setPitch(MathHelper.lerpAngleDegrees(this.serpent.getPitch(), 0.0F, 0.2F));
        } else {
            final double d0 = vector3d.horizontalLength();
            final double d1 = Math.signum(-vector3d.y) * Math.acos(d0 / vector3d.length()) * (180F / (float) Math.PI);
            this.serpent.setPitch((float) d1);
        }

    }
}
