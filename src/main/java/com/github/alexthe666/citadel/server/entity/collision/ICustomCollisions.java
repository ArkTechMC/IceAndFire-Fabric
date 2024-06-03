package com.github.alexthe666.citadel.server.entity.collision;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;

import java.util.List;

public interface ICustomCollisions {
    /*
        Override Entity#getAllowedMovement with entity method
     */
    static Vec3d getAllowedMovementForEntity(Entity entity, Vec3d vecIN) {
        Box aabb = entity.getBoundingBox();
        List<VoxelShape> list = entity.getWorld().getEntityCollisions(entity, aabb.stretch(vecIN));
        Vec3d vec3 = vecIN.lengthSquared() == 0.0D ? vecIN : collideBoundingBox2(entity, vecIN, aabb, entity.getWorld(), list);
        boolean flag = vecIN.x != vec3.x;
        boolean flag1 = vecIN.y != vec3.y;
        boolean flag2 = vecIN.z != vec3.z;
        boolean flag3 = entity.isOnGround() || flag1 && vecIN.y < 0.0D;
        if (entity.getStepHeight() > 0.0F && flag3 && (flag || flag2)) {
            Vec3d vec31 = collideBoundingBox2(entity, new Vec3d(vecIN.x, entity.getStepHeight(), vecIN.z), aabb, entity.getWorld(), list);
            Vec3d vec32 = collideBoundingBox2(entity, new Vec3d(0.0D, entity.getStepHeight(), 0.0D), aabb.stretch(vecIN.x, 0.0D, vecIN.z), entity.getWorld(), list);
            if (vec32.y < (double) entity.getStepHeight()) {
                Vec3d vec33 = collideBoundingBox2(entity, new Vec3d(vecIN.x, 0.0D, vecIN.z), aabb.offset(vec32), entity.getWorld(), list).add(vec32);
                if (vec33.horizontalLengthSquared() > vec31.horizontalLengthSquared()) {
                    vec31 = vec33;
                }
            }

            if (vec31.horizontalLengthSquared() > vec3.horizontalLengthSquared()) {
                return vec31.add(collideBoundingBox2(entity, new Vec3d(0.0D, -vec31.y + vecIN.y, 0.0D), aabb.offset(vec31), entity.getWorld(), list));
            }
        }

        return vec3;
    }

    boolean canPassThrough(BlockPos mutablePos, BlockState blockstate, VoxelShape voxelshape);

    //1.18 logic
    private static Vec3d collideBoundingBox2(Entity p_198895_, Vec3d p_198896_, Box p_198897_, World p_198898_, List<VoxelShape> p_198899_) {
        ImmutableList.Builder<VoxelShape> builder = ImmutableList.builderWithExpectedSize(p_198899_.size() + 1);
        if (!p_198899_.isEmpty()) {
            builder.addAll(p_198899_);
        }

        WorldBorder worldborder = p_198898_.getWorldBorder();
        boolean flag = p_198895_ != null && worldborder.canCollide(p_198895_, p_198897_.stretch(p_198896_));
        if (flag) {
            builder.add(worldborder.asVoxelShape());
        }

        builder.addAll(new CustomCollisionsBlockCollisions(p_198898_, p_198895_, p_198897_.stretch(p_198896_)));
        return collideWithShapes2(p_198896_, p_198897_, builder.build());
    }

    private static Vec3d collideWithShapes2(Vec3d p_198901_, Box p_198902_, List<VoxelShape> p_198903_) {
        if (p_198903_.isEmpty()) {
            return p_198901_;
        } else {
            double d0 = p_198901_.x;
            double d1 = p_198901_.y;
            double d2 = p_198901_.z;
            if (d1 != 0.0D) {
                d1 = VoxelShapes.calculateMaxOffset(Direction.Axis.Y, p_198902_, p_198903_, d1);
                if (d1 != 0.0D) {
                    p_198902_ = p_198902_.offset(0.0D, d1, 0.0D);
                }
            }

            boolean flag = Math.abs(d0) < Math.abs(d2);
            if (flag && d2 != 0.0D) {
                d2 = VoxelShapes.calculateMaxOffset(Direction.Axis.Z, p_198902_, p_198903_, d2);
                if (d2 != 0.0D) {
                    p_198902_ = p_198902_.offset(0.0D, 0.0D, d2);
                }
            }

            if (d0 != 0.0D) {
                d0 = VoxelShapes.calculateMaxOffset(Direction.Axis.X, p_198902_, p_198903_, d0);
                if (!flag && d0 != 0.0D) {
                    p_198902_ = p_198902_.offset(d0, 0.0D, 0.0D);
                }
            }

            if (!flag && d2 != 0.0D) {
                d2 = VoxelShapes.calculateMaxOffset(Direction.Axis.Z, p_198902_, p_198903_, d2);
            }

            return new Vec3d(d0, d1, d2);
        }
    }

}
