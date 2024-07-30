package com.iafenvoy.iceandfire.entity;

import com.iafenvoy.iceandfire.registry.IafEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;


public class EntitySlowPart extends EntityMutlipartPart {
    private final float baseRadius, baseOffsetY, baseSizeX, baseSizeY;

    public EntitySlowPart(EntityType<?> t, World world) {
        super(t, world);
        this.baseRadius = 0;
        this.baseOffsetY = 0;
        this.baseSizeX = 0;
        this.baseSizeY = 0;
    }

    public EntitySlowPart(EntityType<?> t, LivingEntity parent, float baseRadius, float angleYaw, float baseOffsetY, float baseSizeX, float baseSizeY, float damageMultiplier) {
        super(t, parent, baseRadius, angleYaw, baseOffsetY, baseSizeX, baseSizeY, damageMultiplier);
        this.baseRadius = baseRadius;
        this.baseOffsetY = baseOffsetY;
        this.baseSizeX = baseSizeX;
        this.baseSizeY = baseSizeY;
    }

    public EntitySlowPart(Entity parent, float baseRadius, float angleYaw, float baseOffsetY, float baseSizeX, float baseSizeY, float damageMultiplier) {
        super(IafEntities.SLOW_MULTIPART, parent, baseRadius, angleYaw, baseOffsetY, baseSizeX, baseSizeY, damageMultiplier);
        this.baseRadius = baseRadius;
        this.baseOffsetY = baseOffsetY;
        this.baseSizeX = baseSizeX;
        this.baseSizeY = baseSizeY;
    }

    public void updateScale(float scale) {
        this.radius = this.baseRadius * scale;
        this.offsetY = this.baseOffsetY * scale;
        this.setScaleX(this.baseSizeX * scale);
        this.setScaleY(this.baseSizeY * scale);
    }

    @Override
    protected boolean isSlowFollow() {
        return true;
    }
}
