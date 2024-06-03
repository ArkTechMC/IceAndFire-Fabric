package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityPixie;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class PixieAIFlee<T extends Entity> extends Goal {
    private final float avoidDistance;
    private final Class<T> classToAvoid;
    protected EntityPixie pixie;
    protected T closestLivingEntity;
    private Vec3d hidePlace;

    private List<T> list = Collections.emptyList();

    public PixieAIFlee(EntityPixie pixie, Class<T> classToAvoidIn, float avoidDistanceIn, Predicate<? super T> avoidTargetSelectorIn) {
        this.pixie = pixie;
        this.classToAvoid = classToAvoidIn;
        this.avoidDistance = avoidDistanceIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (this.pixie.getStackInHand(Hand.MAIN_HAND).isEmpty() || this.pixie.isTamed()) {
            list = Collections.emptyList();
            return false;
        }

        if (this.pixie.getWorld().getTime() % 4 == 0) // only update the list every 4 ticks
            list = this.pixie.getWorld().getEntitiesByClass(this.classToAvoid, this.pixie.getBoundingBox().expand(this.avoidDistance, 3.0D, this.avoidDistance), EntityPredicates.EXCEPT_SPECTATOR);

        if (list.isEmpty())
            return false;

        this.closestLivingEntity = list.get(0);
        if (closestLivingEntity != null) {
            Vec3d Vector3d = NoPenaltyTargeting.findFrom(this.pixie, 16, 4, new Vec3d(this.closestLivingEntity.getX(), this.closestLivingEntity.getY(), this.closestLivingEntity.getZ()));

            if (Vector3d == null) {
                return false;
            } else {
                Vector3d = Vector3d.add(0, 1, 0);
                this.pixie.getMoveControl().moveTo(Vector3d.x, Vector3d.y, Vector3d.z, calculateRunSpeed());
                this.pixie.getLookControl().lookAt(Vector3d.x, Vector3d.y, Vector3d.z, 180.0F, 20.0F);
                hidePlace = Vector3d;
                pixie.slowSpeed = true;
                return true;
            }
        }
        return false;
    }

    private double calculateRunSpeed() {
        if (pixie.ticksHeldItemFor > 6000) {
            return 0.1D;
        }
        if (pixie.ticksHeldItemFor > 1200) {
            return 0.25D;
        }
        if (pixie.ticksHeldItemFor > 600) {
            return 0.25D;
        }
        return 1D;
    }

    @Override
    public boolean shouldContinue() {
        return hidePlace != null && this.pixie.squaredDistanceTo(hidePlace.add(0.5, 0.5, 0.5)) < 2;
    }

    @Override
    public void start() {
        this.pixie.getMoveControl().moveTo(hidePlace.x, hidePlace.y, hidePlace.z, calculateRunSpeed());
        this.pixie.getLookControl().lookAt(hidePlace.x, hidePlace.y, hidePlace.z, 180.0F, 20.0F);
    }

    @Override
    public void stop() {
        this.closestLivingEntity = null;
        pixie.slowSpeed = false;
    }
}