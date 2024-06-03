package com.github.alexthe666.citadel.server.tick.modifier;

import com.github.alexthe666.citadel.server.entity.IModifiesTime;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LocalEntityTickRateModifier extends LocalTickRateModifier {

    private int entityId;
    private EntityType expectedEntityType;
    private final boolean isEntityValid = true;

    public LocalEntityTickRateModifier(int entityId, EntityType expectedEntityType, double range, RegistryKey<World> dimension, int durationInMasterTicks, float tickRateMultiplier) {
        super(TickRateModifierType.LOCAL_ENTITY, range, dimension, durationInMasterTicks, tickRateMultiplier);
        this.entityId = entityId;
        this.expectedEntityType = expectedEntityType;
    }

    public LocalEntityTickRateModifier(NbtCompound tag) {
        super(tag);
        this.entityId = tag.getInt("EntityId");
        this.expectedEntityType = Registries.ENTITY_TYPE.get(new Identifier(tag.getString("EntityType")));
    }

    @Override
    public Vec3d getCenter(World level) {
        Entity entity = level.getEntityById(this.entityId);
        if (isEntityValid(level) && entity != null) {
            return entity.getPos();
        }
        return Vec3d.ZERO;
    }

    @Override
    public boolean appliesTo(World level, double x, double y, double z) {
        return super.appliesTo(level, x, y, z) && isEntityValid(level);
    }

    public boolean isEntityValid(World level) {
        Entity entity = level.getEntityById(this.entityId);
        return entity != null && entity.getType().equals(expectedEntityType) && entity.isAlive() && (!(entity instanceof IModifiesTime) || ((IModifiesTime) entity).isTimeModificationValid(this));
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = super.toTag();
        tag.putInt("EntityId", entityId);
        Identifier resourcelocation = Registries.ENTITY_TYPE.getId(this.expectedEntityType);
        tag.putString("EntityType", resourcelocation.toString());
        return tag;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public EntityType getExpectedEntityType() {
        return expectedEntityType;
    }

    public void setExpectedEntityType(EntityType expectedEntityType) {
        this.expectedEntityType = expectedEntityType;
    }
}
