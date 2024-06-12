package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class StymphalianBirdAIFlee extends Goal {
    private final Predicate<Entity> canBeSeenSelector;
    private final float avoidDistance;
    protected final EntityStymphalianBird stymphalianBird;
    protected LivingEntity closestLivingEntity;
    private Vec3d hidePlace;

    public StymphalianBirdAIFlee(EntityStymphalianBird stymphalianBird, float avoidDistanceIn) {
        this.stymphalianBird = stymphalianBird;
        this.canBeSeenSelector = entity -> entity instanceof PlayerEntity && entity.isAlive() && StymphalianBirdAIFlee.this.stymphalianBird.getVisibilityCache().canSee(entity) && !StymphalianBirdAIFlee.this.stymphalianBird.isTeammate(entity);
        this.avoidDistance = avoidDistanceIn;
        this.setControls(EnumSet.of(Control.MOVE));
    }


    @Override
    public boolean canStart() {
        if (this.stymphalianBird.getVictor() == null) {
            return false;
        }
        List<LivingEntity> list = this.stymphalianBird.getWorld().getEntitiesByClass(LivingEntity.class, this.stymphalianBird.getBoundingBox().expand(this.avoidDistance, 3.0D, this.avoidDistance),
                this.canBeSeenSelector);

        if (list.isEmpty())
            return false;

        this.closestLivingEntity = list.get(0);
        if (this.closestLivingEntity != null && this.stymphalianBird.getVictor() != null && this.closestLivingEntity.equals(this.stymphalianBird.getVictor())) {
            Vec3d Vector3d = NoPenaltyTargeting.findFrom(this.stymphalianBird, 32, 7, new Vec3d(this.closestLivingEntity.getX(), this.closestLivingEntity.getY(), this.closestLivingEntity.getZ()));

            if (Vector3d == null) {
                return false;
            } else {
                Vector3d = Vector3d.add(0, 3, 0);
                this.stymphalianBird.getMoveControl().moveTo(Vector3d.x, Vector3d.y, Vector3d.z, 3D);
                this.stymphalianBird.getLookControl().lookAt(Vector3d.x, Vector3d.y, Vector3d.z, 180.0F, 20.0F);
                this.hidePlace = Vector3d;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        return this.hidePlace != null && this.stymphalianBird.squaredDistanceTo(this.hidePlace.add(0.5, 0.5, 0.5)) < 2;
    }

    @Override
    public void start() {
        this.stymphalianBird.getMoveControl().moveTo(this.hidePlace.x, this.hidePlace.y, this.hidePlace.z, 3D);
        this.stymphalianBird.getLookControl().lookAt(this.hidePlace.x, this.hidePlace.y, this.hidePlace.z, 180.0F, 20.0F);
    }

    @Override
    public void stop() {
        this.closestLivingEntity = null;
    }
}