package com.iafenvoy.iceandfire.entity.ai;

import com.iafenvoy.iceandfire.entity.EntityDeathWorm;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class DeathWormAIAttack extends Goal {
    private final EntityDeathWorm worm;
    private int jumpCooldown = 0;

    public DeathWormAIAttack(EntityDeathWorm worm) {
        this.worm = worm;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (this.jumpCooldown > 0)
            this.jumpCooldown--;
        return !(this.worm.getTarget() == null || this.worm.hasPassengers() || !this.worm.isOnGround() && !this.worm.isInSandStrict() || this.jumpCooldown > 0);
    }

    @Override
    public boolean shouldContinue() {
        return this.worm.getTarget() != null && this.worm.getTarget().isAlive();
    }

    @Override
    public boolean canStop() {
        return false;
    }

    @Override
    public void start() {
        LivingEntity target = this.worm.getTarget();
        if (target != null) {
            if (this.worm.isInSand()) {
                BlockPos topSand = this.worm.getBlockPos();
                while (this.worm.getWorld().getBlockState(topSand.up()).isIn(BlockTags.SAND))
                    topSand = topSand.up();
                this.worm.setPosition(this.worm.getX(), topSand.getY() + 0.5F, this.worm.getZ());
            }
            if (this.shouldJump()) this.jumpAttack();
            else this.worm.getNavigation().startMovingTo(target, 1.0F);
        }
    }

    public boolean shouldJump() {
        LivingEntity target = this.worm.getTarget();
        if (target != null) {
            final double distanceXZ = this.worm.squaredDistanceTo(target.getX(), this.worm.getY(), target.getZ());
            final float distanceXZSqrt = (float) Math.sqrt(distanceXZ);
            double d0 = this.worm.getVelocity().y;
            if (distanceXZSqrt < 12 && distanceXZSqrt > 2)
                return this.jumpCooldown <= 0
                        && (d0 * d0 >= 0.03F || this.worm.getPitch() == 0.0F || Math.abs(this.worm.getPitch()) >= 10.0F
                        || !this.worm.isTouchingWater())
                        && !this.worm.isOnGround();
        }
        return false;
    }

    public void jumpAttack() {
        LivingEntity target = this.worm.getTarget();
        if (target == null) return;
        this.worm.lookAtEntity(target, 260, 30);
        final double smoothX = MathHelper.clamp(Math.abs(target.getX() - this.worm.getX()), 0, 1);
        //MathHelper.clamp(Math.abs(target.getPosY() - worm.getPosY()), 0, 1);
        final double smoothZ = MathHelper.clamp(Math.abs(target.getZ() - this.worm.getZ()), 0, 1);
        final double d0 = (target.getX() - this.worm.getX()) * 0.2 * smoothX;
        //Math.signum(target.getPosY() - this.worm.getPosY());
        final double d2 = (target.getZ() - this.worm.getZ()) * 0.2 * smoothZ;
        final float up = (this.worm.getScaleFactor() > 3 ? 0.8F : 0.5F) + this.worm.getRandom().nextFloat() * 0.5F;
        this.worm.setVelocity(this.worm.getVelocity().add(d0 * 0.3D, up, d2 * 0.3D));
        this.worm.getNavigation().stop();
        this.worm.setWormJumping(20);
        this.jumpCooldown = this.worm.getRandom().nextInt(32) + 64;
    }

    @Override
    public void stop() {
        this.worm.setPitch(0.0F);
    }

    @Override
    public void tick() {
        if (this.jumpCooldown > 0) this.jumpCooldown--;
        LivingEntity target = this.worm.getTarget();
        if (target != null && this.worm.canSee(target))
            if (this.worm.distanceTo(target) < 3F)
                this.worm.tryAttack(target);

        Vec3d vector3d = this.worm.getVelocity();
        if (vector3d.y * vector3d.y < 0.1F && this.worm.getPitch() != 0.0F)
            this.worm.setPitch(MathHelper.lerpAngleDegrees(this.worm.getPitch(), 0.0F, 0.2F));
        else {
            final double d0 = vector3d.horizontalLength();
            final double d1 = Math.signum(-vector3d.y) * Math.acos(d0 / vector3d.length()) * (180F / (float) Math.PI);
            this.worm.setPitch((float) d1);
        }
        if (this.shouldJump()) this.jumpAttack();
        else if (this.worm.getNavigation().isIdle())
            this.worm.getNavigation().startMovingTo(target, 1.0F);
    }
}
