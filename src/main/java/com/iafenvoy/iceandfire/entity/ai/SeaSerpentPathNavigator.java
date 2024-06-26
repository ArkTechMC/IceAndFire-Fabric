package com.iafenvoy.iceandfire.entity.ai;

import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeNavigator;
import net.minecraft.entity.ai.pathing.WaterPathNodeMaker;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.util.Util;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class SeaSerpentPathNavigator extends EntityNavigation {
    public SeaSerpentPathNavigator(MobEntity entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
    }

    @Override
    protected PathNodeNavigator createPathNodeNavigator(int p_179679_1_) {
        this.nodeMaker = new WaterPathNodeMaker(true);
        return new PathNodeNavigator(this.nodeMaker, p_179679_1_);
    }

    @Override
    protected boolean isAtValidPosition() {
        return true;
    }

    @Override
    protected Vec3d getPos() {
        return new Vec3d(this.entity.getX(), this.entity.getBodyY(0.5D), this.entity.getZ());
    }

    @Override
    public void tick() {
        ++this.tickCount;
        if (this.inRecalculationCooldown)
            this.recalculatePath();

        if (!this.isIdle()) {
            if (this.isAtValidPosition())
                this.continueFollowingPath();
            else if (this.currentPath != null && !this.currentPath.isFinished()) {
                Vec3d vec3d = this.currentPath.getNodePosition(this.entity);
                if (MathHelper.floor(this.entity.getX()) == MathHelper.floor(vec3d.x) && MathHelper.floor(this.entity.getY()) == MathHelper.floor(vec3d.y) && MathHelper.floor(this.entity.getZ()) == MathHelper.floor(vec3d.z))
                    this.currentPath.next();
            }

            DebugInfoSender.sendPathfindingData(this.world, this.entity, this.currentPath, this.nodeReachProximity);
            if (!this.isIdle()) {
                assert this.currentPath != null;
                Vec3d vec3d = this.currentPath.getNodePosition(this.entity);
                this.entity.getMoveControl().moveTo(vec3d.x, vec3d.y, vec3d.z, this.speed);
            }
        }
    }

    @Override
    protected void continueFollowingPath() {
        if (this.currentPath != null) {
            Vec3d entityPos = this.getPos();
            final float entityWidth = this.entity.getWidth();
            float range = entityWidth > 0.75F ? entityWidth / 2.0F : 0.75F - entityWidth / 2.0F;
            Vec3d velocity = this.entity.getVelocity();
            if (Math.abs(velocity.x) > 0.2D || Math.abs(velocity.z) > 0.2D)
                range = (float) (range * velocity.length() * 6.0D);
            Vec3d center = Vec3d.ofCenter(this.currentPath.getCurrentNodePos());
            if (Math.abs(this.entity.getX() - center.x) < range
                    && Math.abs(this.entity.getZ() - center.z) < range
                    && Math.abs(this.entity.getY() - center.y) < range * 2.0F)
                this.currentPath.next();

            for (int lvt_7_1_ = Math.min(this.currentPath.getCurrentNodeIndex() + 6, this.currentPath.getLength() - 1); lvt_7_1_ > this.currentPath.getCurrentNodeIndex(); --lvt_7_1_) {
                center = this.currentPath.getNodePosition(this.entity, lvt_7_1_);
                if (center.squaredDistanceTo(entityPos) <= 36.0D && this.canPathDirectlyThrough(entityPos, center)) {
                    this.currentPath.setCurrentNodeIndex(lvt_7_1_);
                    break;
                }
            }

            this.checkTimeouts(entityPos);
        }
    }

    @Override
    protected void checkTimeouts(Vec3d positionVec3) {
        if (this.tickCount - this.pathStartTime > 100) {
            if (positionVec3.squaredDistanceTo(this.pathStartPos) < 2.25D)
                this.stop();

            this.pathStartTime = this.tickCount;
            this.pathStartPos = positionVec3;
        }

        if (this.currentPath != null && !this.currentPath.isFinished()) {
            Vec3i pos = this.currentPath.getCurrentNodePos();
            if (pos.equals(this.lastNodePosition))
                this.currentNodeMs += Util.getMeasuringTimeMs() - this.lastActiveTickMs;
            else {
                this.lastNodePosition = pos;
                final double lvt_3_1_ = positionVec3.distanceTo(Vec3d.ofCenter(this.lastNodePosition));
                this.currentNodeTimeout = this.entity.getMovementSpeed() > 0.0F
                        ? lvt_3_1_ / this.entity.getMovementSpeed() * 100.0D
                        : 0.0D;
            }

            if (this.currentNodeTimeout > 0.0D && this.currentNodeMs > this.currentNodeTimeout * 2.0D) {
                this.lastNodePosition = Vec3i.ZERO;
                this.currentNodeMs = 0L;
                this.currentNodeTimeout = 0.0D;
                this.stop();
            }

            this.lastActiveTickMs = Util.getMeasuringTimeMs();
        }

    }

    @Override
    protected boolean canPathDirectlyThrough(Vec3d posVec31, Vec3d posVec32) {
        Vec3d lvt_6_1_ = new Vec3d(posVec32.x, posVec32.y + this.entity.getHeight() * 0.5D, posVec32.z);
        return this.world.raycast(new RaycastContext(posVec31, lvt_6_1_, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this.entity)).getType() == HitResult.Type.MISS;
    }

    @Override
    public boolean isValidPosition(BlockPos pos) {
        return !this.world.getBlockState(pos).isOpaqueFullCube(this.world, pos);
    }

    @Override
    public void setCanSwim(boolean canSwim) {
    }
}
