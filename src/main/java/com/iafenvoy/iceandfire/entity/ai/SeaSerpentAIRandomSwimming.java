package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.entity.EntitySeaSerpent;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SeaSerpentAIRandomSwimming extends WanderAroundGoal {
    public SeaSerpentAIRandomSwimming(PathAwareEntity creature, double speed, int chance) {
        super(creature, speed, chance, false);
    }

    @Override
    public boolean canStart() {
        if (this.mob.hasPassengers() || this.mob.getTarget() != null)
            return false;
        else {
            if (!this.ignoringChance && this.mob.getRandom().nextInt(this.chance) != 0)
                return false;
            Vec3d vector3d = this.getWanderTarget();
            if (vector3d == null)
                return false;
            else {
                this.targetX = vector3d.x;
                this.targetY = vector3d.y;
                this.targetZ = vector3d.z;
                this.ignoringChance = false;
                return true;
            }
        }
    }

    @Override
    protected Vec3d getWanderTarget() {
        if (((EntitySeaSerpent) this.mob).jumpCooldown <= 0) {
            Vec3d vector3d = this.findSurfaceTarget(this.mob);
            if (vector3d != null)
                return vector3d.add(0, 1, 0);
        } else {
            BlockPos blockpos = null;
            final Random random = ThreadLocalRandom.current();
            final int range = 16;
            for (int i = 0; i < 15; i++) {
                BlockPos blockpos1 = this.mob.getBlockPos().add(random.nextInt(range) - range / 2, random.nextInt(range) - range / 2, random.nextInt(range) - range / 2);
                while (this.mob.getWorld().isAir(blockpos1) && this.mob.getWorld().getFluidState(blockpos1).isEmpty() && blockpos1.getY() > 1)
                    blockpos1 = blockpos1.down();
                if (this.mob.getWorld().getFluidState(blockpos1).isIn(FluidTags.WATER))
                    blockpos = blockpos1;
            }
            return blockpos == null ? null : new Vec3d(blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D);
        }
        return null;
    }

    private boolean canJumpTo(BlockPos pos) {
        BlockPos blockpos = pos.add(0, 0, 0);
        return this.mob.getWorld().getFluidState(blockpos).isIn(FluidTags.WATER) && !this.mob.getWorld().getBlockState(blockpos).blocksMovement();
    }

    private boolean isAirAbove(BlockPos pos) {
        return this.mob.getWorld().getBlockState(pos.add(0, 1, 0)).isAir() && this.mob.getWorld().getBlockState(pos.add(0, 2, 0)).isAir();
    }

    private Vec3d findSurfaceTarget(PathAwareEntity creature) {
        BlockPos upPos = creature.getBlockPos();
        while (creature.getWorld().getFluidState(upPos).isIn(FluidTags.WATER))
            upPos = upPos.up();
        if (this.isAirAbove(upPos.down()) && this.canJumpTo(upPos.down()))
            return new Vec3d(upPos.getX() + 0.5F, upPos.getY() + 3.5F, upPos.getZ() + 0.5F);
        return null;
    }
}
