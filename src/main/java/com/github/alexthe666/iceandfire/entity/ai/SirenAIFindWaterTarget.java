package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class SirenAIFindWaterTarget extends Goal {
    private final EntitySiren mob;

    public SirenAIFindWaterTarget(EntitySiren mob) {
        this.mob = mob;
    }

    @Override
    public boolean canStart() {
        if (!this.mob.isTouchingWater()) {
            return false;
        }
        if (this.mob.getRandom().nextFloat() < 0.5F) {
            Path path = this.mob.getNavigation().getCurrentPath();
            if (path != null
                    && path.getEnd() != null /*
             * TODO: path is nullable here !this.mob.getNavigator().noPath() &&
             * !this.mob.isDirectPathBetweenPoints(this.mob.getPositionVec(),
             * new Vector3d(path.getFinalPathPoint().x,
             * path.getFinalPathPoint().y, path.getFinalPathPoint().z))
             */) {
                this.mob.getNavigation().stop();
            }
            if (this.mob.getNavigation().isIdle()) {
                Vec3d vec3 = this.findWaterTarget();
                if (vec3 != null) {
                    this.mob.getNavigation().startMovingTo(vec3.x, vec3.y, vec3.z, 1.0);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    public Vec3d findWaterTarget() {
        if (this.mob.getTarget() == null || !this.mob.getTarget().isAlive()) {
            List<Vec3d> water = new ArrayList<>();
            List<Vec3d> singTargets = new ArrayList<>();
            final int posX = (int) this.mob.getX();
            final int posY = (int) this.mob.getY();
            final int posZ = (int) this.mob.getZ();
            for (int x = posX - 5; x < posX + 5; x++) {
                for (int y = posY - 5; y < posY + 5; y++) {
                    for (int z = posZ - 5; z < posZ + 5; z++) {
                        if (this.mob.wantsToSing()) {
                            if (this.mob.getWorld().getBlockState(new BlockPos(x, y, z)).isSolid() && this.mob.getWorld().isAir(new BlockPos(x, y + 1, z)) && this.mob.isDirectPathBetweenPoints(this.mob.getPos(), new Vec3d(x, y + 1, z))) {
                                singTargets.add(new Vec3d(x, y + 1, z));
                            }
                        }
                        if (this.mob.getWorld().getBlockState(new BlockPos(x, y, z)).isOf(Blocks.WATER) && this.mob.isDirectPathBetweenPoints(this.mob.getPos(), new Vec3d(x, y, z))) {
                            water.add(new Vec3d(x, y, z));
                        }

                    }
                }
            }
            if (!singTargets.isEmpty()) {
                return singTargets.get(this.mob.getRandom().nextInt(singTargets.size()));

            }
            if (!water.isEmpty()) {
                return water.get(this.mob.getRandom().nextInt(water.size()));
            }
        } else {
            BlockPos blockpos1 = this.mob.getTarget().getBlockPos();
            return new Vec3d(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
        }
        return null;
    }
}