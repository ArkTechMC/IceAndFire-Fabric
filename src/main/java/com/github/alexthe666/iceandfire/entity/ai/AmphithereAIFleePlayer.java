package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.List;

public class AmphithereAIFleePlayer extends Goal {
    private final double farSpeed;
    private final double nearSpeed;
    private final float avoidDistance;
    protected EntityAmphithere entity;
    protected PlayerEntity closestLivingEntity;
    private Path path;

    private List<PlayerEntity> list = IAFMath.emptyPlayerEntityList;

    public AmphithereAIFleePlayer(EntityAmphithere entityIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        this.entity = entityIn;
        this.avoidDistance = avoidDistanceIn;
        this.farSpeed = farSpeedIn;
        this.nearSpeed = nearSpeedIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!this.entity.isFlying() && !this.entity.isTamed()) {

            if (this.entity.getWorld().getTime() % 4 == 0) // only update the list every 4 ticks
                list = this.entity.getWorld().getEntitiesByClass(PlayerEntity.class, this.entity.getBoundingBox().expand(this.avoidDistance, 6D, this.avoidDistance), EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR);

            if (list.isEmpty())
                return false;

            this.closestLivingEntity = list.get(0);
            Vec3d Vector3d = NoPenaltyTargeting.findFrom(this.entity, 20, 7, new Vec3d(this.closestLivingEntity.getX(), this.closestLivingEntity.getY(), this.closestLivingEntity.getZ()));

            if (Vector3d == null) {
                return false;
            } else if (this.closestLivingEntity.squaredDistanceTo(Vector3d) < this.closestLivingEntity.squaredDistanceTo(this.entity)) {
                return false;
            } else {
                this.path = this.entity.getNavigation().findPathTo(Vector3d.x, Vector3d.y, Vector3d.z, 0);
                return this.path != null;
            }

        } else {
            list = IAFMath.emptyPlayerEntityList;
            return false;
        }
    }

    @Override
    public boolean shouldContinue() {
        return !this.entity.getNavigation().isIdle();
    }

    @Override
    public void start() {
        this.entity.getNavigation().startMovingAlong(this.path, this.farSpeed);
    }

    @Override
    public void stop() {
        this.closestLivingEntity = null;
    }

    @Override
    public void tick() {
        if (this.entity.squaredDistanceTo(this.closestLivingEntity) < 49.0D) {
            this.entity.getNavigation().setSpeed(this.nearSpeed);
        } else {
            this.entity.getNavigation().setSpeed(this.farSpeed);
        }
    }
}