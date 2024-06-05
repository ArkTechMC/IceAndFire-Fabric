package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class DeathWormAIFindSandTarget extends Goal {
    private final EntityDeathWorm mob;
    private int range;

    public DeathWormAIFindSandTarget(EntityDeathWorm mob, int range) {
        this.mob = mob;
        this.range = range;
    }

    @Override
    public boolean canStart() {
        if (this.mob.getTarget() != null) {
            return false;
        }

        if (!this.mob.isInSand() || this.mob.hasVehicle() || this.mob.hasPassengers()) {
            return false;
        }
        if (this.mob.getRandom().nextFloat() < 0.5F) {
            final Path path = this.mob.getNavigation().getCurrentPath();
            if (path != null /*
             * || !this.mob.getNavigator().noPath() && !isDirectPathBetweenPoints(this.mob,
             * this.mob.getPositionVec(), new Vector3d(path.getFinalPathPoint().x,
             * path.getFinalPathPoint().y, path.getFinalPathPoint().z))
             */) {
                this.mob.getNavigation().stop();
            }
            if (this.mob.getNavigation().isIdle() /* && !this.mob.getMoveControl().hasWanted()*/) {
                BlockPos vec3 = this.findSandTarget();
                if (vec3 != null) {
                    this.mob.getMoveControl().moveTo(vec3.getX(), vec3.getY(), vec3.getZ(), 1.0);
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

    public BlockPos findSandTarget() {
        if (this.mob.getTarget() == null || !this.mob.getTarget().isAlive()) {
            List<BlockPos> sand = new ArrayList<>();
            if (this.mob.isTamed() && this.mob.getWormHome() != null) {
                this.range = 25;
                for (int x = this.mob.getWormHome().getX() - this.range; x < this.mob.getWormHome().getX() + this.range; x++) {
                    for (int y = this.mob.getWormHome().getY() - this.range; y < this.mob.getWormHome().getY() + this.range; y++) {
                        for (int z = this.mob.getWormHome().getZ() - this.range; z < this.mob.getWormHome().getZ() + this.range; z++) {
                            if (this.mob.getWorld().getBlockState(new BlockPos(x, y, z)).isIn(BlockTags.SAND) && this.isDirectPathBetweenPoints(this.mob, this.mob.getPos(), new Vec3d(x, y, z))) {
                                sand.add(new BlockPos(x, y, z));
                            }
                        }
                    }
                }
            } else {
                for (int x = (int) this.mob.getX() - this.range; x < (int) this.mob.getX() + this.range; x++) {
                    for (int y = (int) this.mob.getY() - this.range; y < (int) this.mob.getY() + this.range; y++) {
                        for (int z = (int) this.mob.getZ() - this.range; z < (int) this.mob.getZ() + this.range; z++) {
                            if (this.mob.getWorld().getBlockState(new BlockPos(x, y, z)).isIn(BlockTags.SAND) && this.isDirectPathBetweenPoints(this.mob, this.mob.getPos(), new Vec3d(x, y, z))) {
                                sand.add(new BlockPos(x, y, z));
                            }

                        }
                    }
                }
            }

            if (!sand.isEmpty()) {
                return sand.get(this.mob.getRandom().nextInt(sand.size()));
            }
        } else {
            BlockPos blockpos1 = this.mob.getTarget().getBlockPos();
            return new BlockPos(blockpos1.getX(), blockpos1.getY() - 1, blockpos1.getZ());
        }
        return null;
    }

    public boolean isDirectPathBetweenPoints(Entity entity, Vec3d vec1, Vec3d vec2) {
        return true;
    }
}