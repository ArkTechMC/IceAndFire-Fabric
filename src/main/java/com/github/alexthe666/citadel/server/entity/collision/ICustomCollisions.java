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
                if (vec33.horizontalLengthSquared() > vec31.horizontalLengthSquared())
                    vec31 = vec33;
            }

            if (vec31.horizontalLengthSquared() > vec3.horizontalLengthSquared())
                return vec31.add(collideBoundingBox2(entity, new Vec3d(0.0D, -vec31.y + vecIN.y, 0.0D), aabb.offset(vec31), entity.getWorld(), list));
        }
        return vec3;
    }

    //1.18 logic
    private static Vec3d collideBoundingBox2(Entity entity, Vec3d vec3d, Box box, World world, List<VoxelShape> voxelShapes) {
        ImmutableList.Builder<VoxelShape> builder = ImmutableList.builderWithExpectedSize(voxelShapes.size() + 1);
        if (!voxelShapes.isEmpty()) builder.addAll(voxelShapes);

        WorldBorder worldborder = world.getWorldBorder();
        boolean flag = entity != null && worldborder.canCollide(entity, box.stretch(vec3d));
        if (flag) builder.add(worldborder.asVoxelShape());

        builder.addAll(new CustomCollisionsBlockCollisions(world, entity, box.stretch(vec3d)));
        return collideWithShapes2(vec3d, box, builder.build());
    }

    private static Vec3d collideWithShapes2(Vec3d vec3d, Box box, List<VoxelShape> voxelShapes) {
        if (voxelShapes.isEmpty())
            return vec3d;
        else {
            double d0 = vec3d.x;
            double d1 = vec3d.y;
            double d2 = vec3d.z;
            if (d1 != 0.0D) {
                d1 = VoxelShapes.calculateMaxOffset(Direction.Axis.Y, box, voxelShapes, d1);
                if (d1 != 0.0D)
                    box = box.offset(0.0D, d1, 0.0D);
            }

            boolean flag = Math.abs(d0) < Math.abs(d2);
            if (flag && d2 != 0.0D) {
                d2 = VoxelShapes.calculateMaxOffset(Direction.Axis.Z, box, voxelShapes, d2);
                if (d2 != 0.0D)
                    box = box.offset(0.0D, 0.0D, d2);
            }

            if (d0 != 0.0D) {
                d0 = VoxelShapes.calculateMaxOffset(Direction.Axis.X, box, voxelShapes, d0);
                if (!flag && d0 != 0.0D)
                    box = box.offset(d0, 0.0D, 0.0D);
            }

            if (!flag && d2 != 0.0D)
                d2 = VoxelShapes.calculateMaxOffset(Direction.Axis.Z, box, voxelShapes, d2);

            return new Vec3d(d0, d1, d2);
        }
    }

    boolean canPassThrough(BlockPos mutablePos, BlockState blockstate, VoxelShape voxelshape);
}
