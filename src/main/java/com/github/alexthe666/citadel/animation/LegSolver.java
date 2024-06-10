package com.github.alexthe666.citadel.animation;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import java.util.Optional;

/**
 * @author paul101
 * @since 1.0.0
 * Code used with permission from JurassiCraft 1
 */
public class LegSolver {
    public final Leg[] legs;

    public LegSolver(Leg... legs) {
        this.legs = legs;
    }

    public final void update(LivingEntity entity, float scale) {
        this.update(entity, entity.bodyYaw, scale);
    }

    public final void update(LivingEntity entity, float yaw, float scale) {
        double sideTheta = yaw / (180 / Math.PI);
        double sideX = Math.cos(sideTheta) * scale;
        double sideZ = Math.sin(sideTheta) * scale;
        double forwardTheta = sideTheta + Math.PI / 2;
        double forwardX = Math.cos(forwardTheta) * scale;
        double forwardZ = Math.sin(forwardTheta) * scale;
        for (Leg leg : this.legs) {
            leg.update(entity, sideX, sideZ, forwardX, forwardZ, scale);
        }
    }

    public static final class Leg {

        public final float forward;
        public final float side;
        private final float range;
        private final boolean isWing;
        private float height;
        private float prevHeight;

        public Leg(float forward, float side, float range, boolean isWing) {
            this.forward = forward;
            this.side = side;
            this.range = range;
            this.isWing = isWing;
        }

        public float getHeight(float delta) {
            return this.prevHeight + (this.height - this.prevHeight) * delta;
        }

        public void update(LivingEntity entity, double sideX, double sideZ, double forwardX, double forwardZ, float scale) {
            this.prevHeight = this.height;
            double posY = entity.getY();
            float settledHeight = this.settle(entity, entity.getX() + sideX * this.side + forwardX * this.forward, posY, entity.getZ() + sideZ * this.side + forwardZ * this.forward, this.height);
            this.height = MathHelper.clamp(settledHeight, -this.range * scale, this.range * scale);
        }

        private float settle(LivingEntity entity, double x, double y, double z, float height) {
            BlockPos pos = new BlockPos((int) Math.floor(x), (int) Math.floor(y + 1e-3), (int) Math.floor(z));
            Vec3d vec3 = new Vec3d(x, y, z);
            float dist = this.getDistance(entity.getWorld(), pos, vec3);
            if ((double) (1.0F - dist) < 0.001D) {
                dist = this.getDistance(entity.getWorld(), pos.down(), vec3) + (float) y % 1;
            } else {
                dist = (float) ((double) dist - (1.0D - y % 1.0D));
            }
            if (entity.isOnGround() && height <= dist) {
                return height == dist ? height : Math.min(height + this.getFallSpeed(), dist);
            } else if (height > 0) {
                return height == dist ? height : Math.max(height - this.getRiseSpeed(), dist);
            }
            return height;
        }

        private float getDistance(World world, BlockPos pos, Vec3d position) {
            BlockState state = world.getBlockState(pos);
            VoxelShape shape = state.getCollisionShape(world, pos);
            if (shape.isEmpty()) {
                return 1.0F;
            }
            Optional<Vec3d> closest = shape.getClosestPointTo(position);
            if (closest.isEmpty()) {
                return 1.0F;
            } else {
                float closestY = Math.min((float) closest.get().y, 1.0F);
                return position.y < 0.0 ? closestY : 1.0F - closestY;
            }
        }

        private float getFallSpeed() {
            return 0.25F;
        }

        private float getRiseSpeed() {
            return 0.25F;
        }
    }
}