package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.util.dragon.DragonUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class CockatriceAIStareAttack extends Goal {
    private final EntityCockatrice entity;
    private final double moveSpeedAmp;
    private int seeTime;
    private BlockPos target = null;

    public CockatriceAIStareAttack(EntityCockatrice cockatrice, double speedAmplifier, int delay, float maxDistance) {
        this.entity = cockatrice;
        this.moveSpeedAmp = speedAmplifier;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    public static boolean isEntityLookingAt(LivingEntity looker, LivingEntity seen, double degree) {
        Vec3d Vector3d = looker.getRotationVec(1.0F).normalize();
        Vec3d Vector3d1 = new Vec3d(seen.getX() - looker.getX(), seen.getBoundingBox().minY + seen.getStandingEyeHeight() - (looker.getY() + looker.getStandingEyeHeight()), seen.getZ() - looker.getZ());
        Vector3d1 = Vector3d1.normalize();
        final double d0 = Vector3d1.length();
        final double d1 = Vector3d.dotProduct(Vector3d1);
        return d1 > 1.0D - degree / d0 && !looker.isSpectator();
    }

    public void setAttackCooldown(int cooldown) {
    }

    @Override
    public boolean canStart() {
        return this.entity.getTarget() != null;
    }

    @Override
    public boolean shouldContinue() {
        return this.canStart();
    }

    @Override
    public void stop() {
        super.stop();
        this.seeTime = 0;
        this.entity.clearActiveItem();
        this.entity.getNavigation().stop();
        this.target = null;
    }

    @Override
    public void tick() {
        LivingEntity LivingEntity = this.entity.getTarget();
        if (LivingEntity != null) {

            if (EntityGorgon.isStoneMob(LivingEntity) || !LivingEntity.isAlive()) {
                this.entity.setTarget(null);
                this.entity.setTargetedEntity(0);
                this.stop();
                return;
            }
            if (!isEntityLookingAt(LivingEntity, this.entity, EntityCockatrice.VIEW_RADIUS) || (LivingEntity.prevX != this.entity.getX() || LivingEntity.prevY != this.entity.getY() || LivingEntity.prevZ != this.entity.getZ())) {
                this.entity.getNavigation().stop();
                BlockPos pos = DragonUtils.getBlockInTargetsViewCockatrice(this.entity, LivingEntity);
                if (this.target == null || pos.getSquaredDistance(this.target) > 4) {
                    this.target = pos;
                }
            }
            this.entity.setTargetedEntity(LivingEntity.getId());

            this.entity.squaredDistanceTo(LivingEntity.getX(), LivingEntity.getBoundingBox().minY,
                    LivingEntity.getZ());
            final boolean flag = this.entity.getVisibilityCache().canSee(LivingEntity);
            final boolean flag1 = this.seeTime > 0;

            if (flag != flag1) {
                this.seeTime = 0;
            }

            if (flag) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }
            if (this.target != null) {
                if (this.entity.squaredDistanceTo(this.target.getX(), this.target.getY(), this.target.getZ()) > 16 && !isEntityLookingAt(LivingEntity, this.entity, EntityCockatrice.VIEW_RADIUS)) {
                    this.entity.getNavigation().startMovingTo(this.target.getX(), this.target.getY(), this.target.getZ(), this.moveSpeedAmp);
                }

            }
            this.entity.getLookControl().lookAt(LivingEntity.getX(), LivingEntity.getY() + LivingEntity.getStandingEyeHeight(), LivingEntity.getZ(), this.entity.getMaxHeadRotation(), this.entity.getMaxLookPitchChange());
        }
    }

}