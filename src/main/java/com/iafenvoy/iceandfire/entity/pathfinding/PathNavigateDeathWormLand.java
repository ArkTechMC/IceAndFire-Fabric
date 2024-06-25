package com.iafenvoy.iceandfire.entity.pathfinding;

import com.iafenvoy.iceandfire.entity.EntityDeathWorm;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateDeathWormLand extends EntityNavigation {
    private final EntityDeathWorm worm;
    private boolean shouldAvoidSun;

    public PathNavigateDeathWormLand(EntityDeathWorm worm, World worldIn) {
        super(worm, worldIn);
        this.worm = worm;
    }

    @Override
    protected PathNodeNavigator createPathNodeNavigator(int i) {
        this.nodeMaker = new LandPathNodeMaker();
        this.nodeMaker.setCanEnterOpenDoors(true);
        this.nodeMaker.setCanSwim(true);
        return new PathNodeNavigator(this.nodeMaker, i);
    }

    /**
     * If on ground or swimming and can swim
     */
    @Override
    protected boolean isAtValidPosition() {
        return this.entity.isOnGround() || this.worm.isInSand() || this.entity.hasVehicle();
    }

    @Override
    protected Vec3d getPos() {
        return new Vec3d(this.entity.getX(), this.getPathablePosY(), this.entity.getZ());
    }

    /**
     * Returns path to given BlockPos
     */
    @Override
    public Path findPathTo(BlockPos pos, int i) {
        if (this.world.getBlockState(pos).isAir()) {
            BlockPos blockpos;

            for (blockpos = pos.down(); blockpos.getY() > 0 && this.world.getBlockState(blockpos).isAir(); blockpos = blockpos.down()) {
            }

            if (blockpos.getY() > 0) {
                return super.findPathTo(blockpos.up(), i);
            }

            while (blockpos.getY() < this.world.getTopY() && this.world.getBlockState(blockpos).isAir()) {
                blockpos = blockpos.up();
            }

            pos = blockpos;
        }

        if (!this.world.getBlockState(pos).isSolid()) {
            return super.findPathTo(pos, i);
        } else {
            BlockPos blockpos1;

            for (blockpos1 = pos.up(); blockpos1.getY() < this.world.getTopY() && this.world.getBlockState(blockpos1).isSolid(); blockpos1 = blockpos1.up()) {
            }

            return super.findPathTo(blockpos1, i);
        }
    }

    /**
     * Returns the path to the given LivingEntity. Args : entity
     */
    @Override
    public Path findPathTo(Entity entityIn, int i) {
        return this.findPathTo(entityIn.getBlockPos(), i);
    }

    /**
     * Gets the safe pathing Y position for the entity depending on if it can path swim or not
     */
    private int getPathablePosY() {
        if (this.worm.isInSand()) {
            int i = (int) this.entity.getBoundingBox().minY;
            BlockState blockstate = this.world.getBlockState(new BlockPos(this.entity.getBlockX(), i, this.entity.getBlockZ()));
            int j = 0;

            while (blockstate.isIn(BlockTags.SAND)) {
                ++i;
                blockstate = this.world.getBlockState(new BlockPos(this.entity.getBlockX(), i, this.entity.getBlockZ()));
                ++j;

                if (j > 16) {
                    return (int) this.entity.getBoundingBox().minY;
                }
            }

            return i;
        } else {
            return (int) (this.entity.getBoundingBox().minY + 0.5D);
        }
    }

    protected void removeSunnyPath() {

        if (this.shouldAvoidSun) {
            if (this.world.isSkyVisible(BlockPos.ofFloored(this.entity.getBlockX(), this.entity.getBoundingBox().minY + 0.5D, this.entity.getBlockZ()))) {
                return;
            }

            for (int i = 0; i < this.currentPath.getLength(); ++i) {
                PathNode pathpoint = this.currentPath.getNode(i);

                if (this.world.isSkyVisible(new BlockPos(pathpoint.x, pathpoint.y, pathpoint.z))) {
                    this.currentPath.setLength(i - 1);
                    return;
                }
            }
        }
    }

    /**
     * Checks if the specified entity can safely walk to the specified location.
     */
    @Override
    protected boolean canPathDirectlyThrough(Vec3d posVec31, Vec3d posVec32) {
        int i = MathHelper.floor(posVec31.x);
        int j = MathHelper.floor(posVec31.z);
        double d0 = posVec32.x - posVec31.x;
        double d1 = posVec32.z - posVec31.z;
        double d2 = d0 * d0 + d1 * d1;
        int sizeX = (int) this.worm.getBoundingBox().getXLength();
        int sizeY = (int) this.worm.getBoundingBox().getYLength();
        int sizeZ = (int) this.worm.getBoundingBox().getZLength();


        if (d2 < 1.0E-8D) {
            return false;
        } else {
            double d3 = 1.0D / Math.sqrt(d2);
            d0 = d0 * d3;
            d1 = d1 * d3;
            sizeX = sizeX + 2;
            sizeZ = sizeZ + 2;

            if (!this.isSafeToStandAt(i, (int) posVec31.y, j, sizeX, sizeY, sizeZ, posVec31, d0, d1)) {
                return false;
            } else {
                sizeX = sizeX - 2;
                sizeZ = sizeZ - 2;
                double d4 = 1.0D / Math.abs(d0);
                double d5 = 1.0D / Math.abs(d1);
                double d6 = (double) i - posVec31.x;
                double d7 = (double) j - posVec31.z;

                if (d0 >= 0.0D) {
                    ++d6;
                }

                if (d1 >= 0.0D) {
                    ++d7;
                }

                d6 = d6 / d0;
                d7 = d7 / d1;
                int k = d0 < 0.0D ? -1 : 1;
                int l = d1 < 0.0D ? -1 : 1;
                int i1 = MathHelper.floor(posVec32.x);
                int j1 = MathHelper.floor(posVec32.z);
                int k1 = i1 - i;
                int l1 = j1 - j;

                while (k1 * k > 0 || l1 * l > 0) {
                    if (d6 < d7) {
                        d6 += d4;
                        i += k;
                        k1 = i1 - i;
                    } else {
                        d7 += d5;
                        j += l;
                        l1 = j1 - j;
                    }

                    if (!this.isSafeToStandAt(i, (int) posVec31.y, j, sizeX, sizeY, sizeZ, posVec31, d0, d1)) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    /**
     * Returns true when an entity could stand at a position, including solid blocks under the entire entity.
     */
    private boolean isSafeToStandAt(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vec3d vec31, double p_179683_8_, double p_179683_10_) {
        int i = x - sizeX / 2;
        int j = z - sizeZ / 2;

        if (!this.isPositionClear(i, y, j, sizeX, sizeY, sizeZ, vec31, p_179683_8_, p_179683_10_)) {
            return false;
        } else {
            for (int k = i; k < i + sizeX; ++k) {
                for (int l = j; l < j + sizeZ; ++l) {
                    double d0 = (double) k + 0.5D - vec31.x;
                    double d1 = (double) l + 0.5D - vec31.z;

                    if (d0 * p_179683_8_ + d1 * p_179683_10_ >= 0.0D) {
                        PathNodeType pathnodetype = this.nodeMaker.getNodeType(this.world, k, y - 1, l, this.entity);
                        if (pathnodetype == PathNodeType.LAVA) {
                            return false;
                        }

                        pathnodetype = this.nodeMaker.getNodeType(this.world, k, y, l, this.entity);
                        float f = this.entity.getPathfindingPenalty(pathnodetype);

                        if (f < 0.0F || f >= 8.0F) {
                            return false;
                        }

                        if (pathnodetype == PathNodeType.DAMAGE_FIRE || pathnodetype == PathNodeType.DANGER_FIRE || pathnodetype == PathNodeType.DAMAGE_OTHER) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    /**
     * Returns true if an entity does not collide with any solid blocks at the position.
     */
    private boolean isPositionClear(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vec3d p_179692_7_, double p_179692_8_, double p_179692_10_) {
        for (BlockPos blockpos : BlockPos.stream(new BlockPos(x, y, z), new BlockPos(x + sizeX - 1, y + sizeY - 1, z + sizeZ - 1)).toList()) {
            double d0 = (double) blockpos.getX() + 0.5D - p_179692_7_.x;
            double d1 = (double) blockpos.getZ() + 0.5D - p_179692_7_.z;

            if (d0 * p_179692_8_ + d1 * p_179692_10_ >= 0.0D) {
                Block block = this.world.getBlockState(blockpos).getBlock();

                if (this.world.getBlockState(blockpos).blocksMovement() || this.world.getBlockState(blockpos).isIn(BlockTags.SAND)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void setBreakDoors(boolean canBreakDoors) {
        this.nodeMaker.setCanOpenDoors(canBreakDoors);
    }

    public boolean getEnterDoors() {
        return this.nodeMaker.canEnterOpenDoors();
    }

    public void setEnterDoors(boolean enterDoors) {
        this.nodeMaker.setCanEnterOpenDoors(enterDoors);
    }

    @Override
    public boolean canSwim() {
        return this.nodeMaker.canSwim();
    }

    @Override
    public void setCanSwim(boolean canSwim) {
        this.nodeMaker.setCanSwim(canSwim);
    }

    public void setAvoidSun(boolean avoidSun) {
        this.shouldAvoidSun = avoidSun;
    }
}