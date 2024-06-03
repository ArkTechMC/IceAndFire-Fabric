package com.github.alexthe666.iceandfire.entity.util;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class DragonPositionGenerator {

    public static Vec3d findRandomTargetBlock(MobEntity MobEntityIn, int xz, int y, Vec3d targetVec3) {
        Vec3d vec = generateRandomPos(MobEntityIn, xz, y, targetVec3, false);
        return vec == null ? MobEntityIn.getPos() : vec;
    }

    public static Vec3d generateRandomPos(MobEntity mob, int xz, int y, Vec3d vec, boolean skipWater) {
        EntityNavigation pathnavigate = mob.getNavigation();
        Random random = mob.getRandom();
        boolean flag;

        if (mob.hasPositionTarget()) {
            double d0 = mob.getPositionTarget().getSquaredDistanceFromCenter(MathHelper.floor(mob.getX()), MathHelper.floor(mob.getY()), MathHelper.floor(mob.getZ())) + 4.0D;
            double d1 = mob.getPositionTargetRange() + (float) xz;
            flag = d0 < d1 * d1;
        } else {
            flag = false;
        }

        boolean flag1 = false;
        float f = -99999.0F;
        int k1 = 0;
        int i = 0;
        int j = 0;

        for (int k = 0; k < 10; ++k) {
            int l = random.nextInt(2 * xz + 1) - xz;
            int i1 = random.nextInt(2 * y + 1) - y;
            int j1 = random.nextInt(2 * xz + 1) - xz;

            if (vec == null || (double) l * vec.x + (double) j1 * vec.z >= 0.0D) {
                if (mob.hasPositionTarget() && xz > 1) {
                    BlockPos blockpos = mob.getPositionTarget();

                    if (mob.getX() > (double) blockpos.getX()) {
                        l -= random.nextInt(xz / 2);
                    } else {
                        l += random.nextInt(xz / 2);
                    }

                    if (mob.getZ() > (double) blockpos.getZ()) {
                        j1 -= random.nextInt(xz / 2);
                    } else {
                        j1 += random.nextInt(xz / 2);
                    }
                }

                BlockPos blockpos1 = new BlockPos(l + mob.getBlockX(), i1 + mob.getBlockY(), j1 + mob.getBlockZ());

                if ((!flag || mob.isInWalkTargetRange(blockpos1)) && pathnavigate.isValidPosition(blockpos1)) {
                    if (skipWater) {
                        blockpos1 = moveAboveSolid(blockpos1, mob);
                        if (isWaterDestination(blockpos1, mob)) {
                            continue;
                        }
                    }

                    float f1 = 0.0F;

                    if (f1 > f) {
                        f = f1;
                        k1 = l;
                        i = i1;
                        j = j1;
                        flag1 = true;
                    }
                }
            }
        }

        if (flag1) {
            return new Vec3d((double) k1 + mob.getX(), (double) i + mob.getY(), (double) j + mob.getZ());
        } else {
            return null;
        }
    }

    private static BlockPos moveAboveSolid(BlockPos pos, MobEntity mob) {
        if (!mob.getWorld().getBlockState(pos).isSolid()) {
            return pos;
        } else {
            BlockPos blockpos;

            for (blockpos = pos.up(); blockpos.getY() < mob.getWorld().getTopY() && mob.getWorld().getBlockState(blockpos).isSolid(); blockpos = blockpos.up()) {
            }

            return blockpos;
        }
    }

    private static boolean isWaterDestination(BlockPos pos, MobEntity mob) {
        return mob.getWorld().getBlockState(pos).isOf(Blocks.WATER);
    }
}
