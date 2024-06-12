package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import net.minecraft.entity.ai.goal.DiveJumpingGoal;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class DeathWormAIJump extends DiveJumpingGoal {

    private static final int[] JUMP_DISTANCES = new int[]{
            0, 1, 4, 5, 6, 7
    };
    private final EntityDeathWorm dolphin;
    private final int chance;
    private boolean inWater;
    private int jumpCooldown;

    public DeathWormAIJump(EntityDeathWorm dolphin, int p_i50329_2_) {
        this.dolphin = dolphin;
        this.chance = p_i50329_2_;
    }

    @Override
    public boolean canStart() {
        if (this.jumpCooldown > 0) {
            this.jumpCooldown--;
        }
        if (this.dolphin.getRandom().nextInt(this.chance) != 0 || this.dolphin.hasPassengers()
                || this.dolphin.getTarget() != null) {
            return false;
        } else {
            Direction direction = this.dolphin.getMovementDirection();
            final int i = direction.getOffsetX();
            final int j = direction.getOffsetZ();
            BlockPos blockpos = this.dolphin.getBlockPos();
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
        return this.dolphin.getWorld().getBlockState(blockpos).isIn(BlockTags.SAND);
    }

    private boolean isAirAbove(BlockPos pos, int dx, int dz, int scale) {
        return this.dolphin.getWorld().getBlockState(pos.add(dx * scale, 1, dz * scale)).isAir()
                && this.dolphin.getWorld().getBlockState(pos.add(dx * scale, 2, dz * scale)).isAir();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinue() {
        final double d0 = this.dolphin.getVelocity().y;
        return this.jumpCooldown > 0 && (d0 * d0 >= 0.03F || this.dolphin.getPitch() == 0.0F
                || Math.abs(this.dolphin.getPitch()) >= 10.0F || !this.dolphin.isInSand()) && !this.dolphin.isOnGround();
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
        Direction direction = this.dolphin.getMovementDirection();
        final float up = (this.dolphin.getScaleFactor() > 3 ? 0.7F : 0.4F) + this.dolphin.getRandom().nextFloat() * 0.4F;
        this.dolphin
                .setVelocity(this.dolphin.getVelocity().add(direction.getOffsetX() * 0.6D, up, direction.getOffsetZ() * 0.6D));
        this.dolphin.getNavigation().stop();
        this.dolphin.setWormJumping(30);
        this.jumpCooldown = this.dolphin.getRandom().nextInt(65) + 32;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by
     * another one
     */
    @Override
    public void stop() {
        this.dolphin.setPitch(0.0F);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        final boolean flag = this.inWater;
        if (!flag) {
            this.inWater = this.dolphin.getWorld().getBlockState(this.dolphin.getBlockPos()).isIn(BlockTags.SAND);
        }
        Vec3d vector3d = this.dolphin.getVelocity();
        if (vector3d.y * vector3d.y < 0.1F && this.dolphin.getPitch() != 0.0F) {
            this.dolphin.setPitch(MathHelper.lerpAngleDegrees(this.dolphin.getPitch(), 0.0F, 0.2F));
        } else {

            final double d0 = (vector3d.horizontalLength());
            final double d1 = Math.signum(-vector3d.y) * Math.acos(d0 / vector3d.length()) * (180F / (float) Math.PI);
            this.dolphin.setPitch((float) d1);
        }

    }
}
