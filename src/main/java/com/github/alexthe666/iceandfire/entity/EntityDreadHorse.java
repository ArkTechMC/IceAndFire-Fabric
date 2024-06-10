package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.util.IDreadMob;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.world.EntityView;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class EntityDreadHorse extends SkeletonHorseEntity implements IDreadMob {

    protected static final TrackedData<Optional<UUID>> COMMANDER_UNIQUE_ID = DataTracker.registerData(EntityDreadHorse.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    public EntityDreadHorse(EntityType type, World worldIn) {
        super(type, worldIn);
    }


    public static DefaultAttributeContainer.Builder bakeAttributes() {
        return createBaseHorseAttributes()
                //HEALTH
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 25.0D)
                //SPEED
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                //ARMOR
                .add(EntityAttributes.GENERIC_ARMOR, 4.0D);
    }


    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(COMMANDER_UNIQUE_ID, Optional.empty());
    }

    @Override
    public void writeCustomDataToNbt(@NotNull NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        if (this.getCommanderId() != null) {
            compound.putUuid("CommanderUUID", this.getCommanderId());
        }
    }

    @Override
    public void readCustomDataFromNbt(@NotNull NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        UUID uuid;
        if (compound.containsUuid("CommanderUUID")) {
            uuid = compound.getUuid("CommanderUUID");
        } else {
            String s = compound.getString("CommanderUUID");
            uuid = ServerConfigHandler.getPlayerUuidByName(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setCommanderId(uuid);
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public EntityData initialize(@NotNull ServerWorldAccess worldIn, @NotNull LocalDifficulty difficultyIn, @NotNull SpawnReason reason, EntityData spawnDataIn, NbtCompound dataTag) {
        EntityData data = super.initialize(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setBreedingAge(24000);
        return data;
    }

    @Override
    public boolean isTeammate(@NotNull Entity entityIn) {
        return entityIn instanceof IDreadMob || super.isTeammate(entityIn);
    }

    public UUID getCommanderId() {
        return this.dataTracker.get(COMMANDER_UNIQUE_ID).orElse(null);
    }

    public void setCommanderId(UUID uuid) {
        this.dataTracker.set(COMMANDER_UNIQUE_ID, Optional.ofNullable(uuid));
    }

    @Override
    public Entity getCommander() {
        try {
            UUID uuid = this.getCommanderId();
            return uuid == null ? null : this.getWorld().getPlayerByUuid(uuid);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    @Override
    public @NotNull EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    @Override
    public EntityView method_48926() {
        return this.getWorld();
    }
}
