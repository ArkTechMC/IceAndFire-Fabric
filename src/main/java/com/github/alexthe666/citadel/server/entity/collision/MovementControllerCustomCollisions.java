package com.github.alexthe666.citadel.server.entity.collision;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;

public class MovementControllerCustomCollisions extends MoveControl {

    public MovementControllerCustomCollisions(MobEntity mob) {
        super(mob);
    }

    public void tick() {
        if (this.state == State.STRAFE) {
            float f = (float) this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            float f1 = (float) this.speed * f;
            float f2 = this.forwardMovement;
            float f3 = this.sidewaysMovement;
            float f4 = MathHelper.sqrt(f2 * f2 + f3 * f3);
            if (f4 < 1.0F) {
                f4 = 1.0F;
            }

            f4 = f1 / f4;
            f2 = f2 * f4;
            f3 = f3 * f4;
            float f5 = MathHelper.sin(this.entity.getYaw() * ((float) Math.PI / 180F));
            float f6 = MathHelper.cos(this.entity.getYaw() * ((float) Math.PI / 180F));
            float f7 = f2 * f6 - f3 * f5;
            float f8 = f3 * f6 + f2 * f5;
            if (!this.isPosWalkable(f7, f8)) {
                this.forwardMovement = 1.0F;
                this.sidewaysMovement = 0.0F;
            }

            this.entity.setMovementSpeed(f1);
            this.entity.setForwardSpeed(this.forwardMovement);
            this.entity.setSidewaysSpeed(this.sidewaysMovement);
            this.state = State.WAIT;
        } else if (this.state == State.MOVE_TO) {
            this.state = State.WAIT;
            double d0 = this.targetX - this.entity.getX();
            double d1 = this.targetZ - this.entity.getZ();
            double d2 = this.targetY - this.entity.getY();
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;
            if (d3 < (double) 2.5000003E-7F) {
                this.entity.setForwardSpeed(0.0F);
                return;
            }

            float f9 = (float) (MathHelper.atan2(d1, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
            this.entity.setYaw(this.wrapDegrees(this.entity.getYaw(), f9, 90.0F));
            this.entity.setMovementSpeed((float) (this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));
            BlockPos blockpos = this.entity.getBlockPos();
            BlockState blockstate = this.entity.getWorld().getBlockState(blockpos);
            VoxelShape voxelshape = blockstate.getSidesShape(this.entity.getWorld(), blockpos);
            if (!(entity instanceof ICustomCollisions && ((ICustomCollisions) entity).canPassThrough(blockpos, blockstate, voxelshape))) {
                if (d2 > (double) this.entity.getStepHeight() && d0 * d0 + d1 * d1 < (double) Math.max(1.0F, this.entity.getWidth()) || !voxelshape.isEmpty() && this.entity.getY() < voxelshape.getMax(Direction.Axis.Y) + (double) blockpos.getY() && !blockstate.isIn(BlockTags.DOORS) && !blockstate.isIn(BlockTags.FENCES)) {
                    this.entity.getJumpControl().setActive();
                    this.state = State.JUMPING;
                }
            }
        } else if (this.state == State.JUMPING) {
            this.entity.setMovementSpeed((float) (this.speed * this.entity.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)));
            if (this.entity.isOnGround()) {
                this.state = State.WAIT;
            }
        } else {
            this.entity.setForwardSpeed(0.0F);
        }

    }

    private boolean isPosWalkable(float p_234024_1_, float p_234024_2_) {
        EntityNavigation pathnavigator = this.entity.getNavigation();
        if (pathnavigator != null) {
            PathNodeMaker nodeprocessor = pathnavigator.getNodeMaker();
            return nodeprocessor == null || nodeprocessor.getDefaultNodeType(this.entity.getWorld(), MathHelper.floor(this.entity.getX() + (double) p_234024_1_), MathHelper.floor(this.entity.getY()), MathHelper.floor(this.entity.getZ() + (double) p_234024_2_)) == PathNodeType.WALKABLE;
        }

        return true;
    }
}
