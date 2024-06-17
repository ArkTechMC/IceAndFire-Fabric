package com.iafenvoy.iceandfire.client.model.util;

import com.iafenvoy.iceandfire.entity.EntityDragonBase;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

/*
       Code from JurassiCraft, used with permission
       By paul101
 */
public class LegSolver {
    public final Leg[] legs;

    public LegSolver(Leg... legs) {
        this.legs = legs;
    }

    public final void update(EntityDragonBase entity, float scale) {
        this.update(entity, entity.bodyYaw, scale);
    }

    public final void update(EntityDragonBase entity, float yaw, float scale) {
        double sideTheta = yaw / (180 / Math.PI);
        double sideX = MathHelper.cos((float) sideTheta) * scale;
        double sideZ = MathHelper.sin((float) sideTheta) * scale;
        double forwardTheta = sideTheta + Math.PI / 2;
        double forwardX = MathHelper.cos((float) forwardTheta) * scale;
        double forwardZ = MathHelper.sin((float) forwardTheta) * scale;
        for (Leg leg : this.legs)
            leg.update(entity, sideX, sideZ, forwardX, forwardZ, scale);
    }

    public static final class Leg {
        public final float forward;
        public final float side;
        private final float range;
        private float height;
        private float prevHeight;

        public Leg(float forward, float side, float range, boolean isWing) {
            this.forward = forward;
            this.side = side;
            this.range = range;
        }

        public float getHeight(float delta) {
            return this.prevHeight + (this.height - this.prevHeight) * delta;
        }

        public void update(EntityDragonBase entity, double sideX, double sideZ, double forwardX, double forwardZ, float scale) {
            this.prevHeight = this.height;
            double posY = entity.getY();
            float settledHeight = this.settle(entity, entity.getX() + sideX * this.side + forwardX * this.forward, posY, entity.getZ() + sideZ * this.side + forwardZ * this.forward, this.height);
            this.height = MathHelper.clamp(settledHeight, -this.range * scale, this.range * scale);
        }


        private float settle(EntityDragonBase entity, double x, double y, double z, float height) {
            BlockPos pos = BlockPos.ofFloored(x, y + 1e-3, z);
            float dist = this.getDistance(entity.getWorld(), pos);
            if (1 - dist < 1e-3) dist = this.getDistance(entity.getWorld(), pos.down()) + (float) y % 1;
            else dist -= (float) (1 - (y % 1));
            if (entity.isOnGround() && height <= dist)
                return height == dist ? height : Math.min(height + this.getFallSpeed(), dist);
            else if (height > 0) return Math.max(height - this.getRiseSpeed(), dist);
            return height;
        }

        private float getDistance(World world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);
            VoxelShape aabb = state.getCollisionShape(world, pos);
            return aabb.isEmpty() ? 1 : 1 - Math.min((float) aabb.getEndingCoord(Direction.Axis.Y, 0.5D, 0.5D), 1);
        }

        private float getFallSpeed() {
            return 0.25F;
        }

        private float getRiseSpeed() {
            return 0.25F;
        }
    }
}