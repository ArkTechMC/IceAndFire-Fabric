package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityDragonPart extends EntityMutlipartPart {
    private EntityDragonBase dragon;

    public EntityDragonPart(EntityType<?> t, World world) {
        super(t, world);
    }

    public EntityDragonPart(EntityType<?> type, EntityDragonBase dragon, float radius, float angleYaw, float offsetY,
                            float sizeX, float sizeY, float damageMultiplier) {
        super(type, dragon, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
        this.dragon = dragon;
    }

    public EntityDragonPart(EntityDragonBase parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(IafEntityRegistry.DRAGON_MULTIPART, parent, radius, angleYaw, offsetY, sizeX, sizeY,
                damageMultiplier);
        this.dragon = parent;
    }

    @Override
    public void collideWithNearbyEntities() {
    }
}
