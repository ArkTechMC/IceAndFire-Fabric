package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.util.IDreadMob;
import com.github.alexthe666.iceandfire.entity.util.IHumanoid;
import com.github.alexthe666.iceandfire.registry.IafEntities;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class EntityDreadMob extends HostileEntity implements IDreadMob {
    protected static final TrackedData<Optional<UUID>> COMMANDER_UNIQUE_ID = DataTracker.registerData(EntityDreadMob.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    public EntityDreadMob(EntityType<? extends HostileEntity> t, World worldIn) {
        super(t, worldIn);
    }

    public static Entity necromancyEntity(LivingEntity entity) {
        Entity lichSummoned = null;
        if (entity.getGroup() == EntityGroup.ARTHROPOD) {
            lichSummoned = new EntityDreadScuttler(IafEntities.DREAD_SCUTTLER, entity.getWorld());
            float readInScale = (entity.getWidth() / 1.5F);
            if (entity.getWorld() instanceof ServerWorldAccess) {
                ((EntityDreadScuttler) lichSummoned).initialize((ServerWorldAccess) entity.getWorld(), entity.getWorld().getLocalDifficulty(entity.getBlockPos()), SpawnReason.MOB_SUMMONED, null, null);
            }
            ((EntityDreadScuttler) lichSummoned).setSize(readInScale);
            return lichSummoned;
        }
        if (entity instanceof ZombieEntity || entity instanceof IHumanoid) {
            lichSummoned = new EntityDreadGhoul(IafEntities.DREAD_GHOUL, entity.getWorld());
            float readInScale = (entity.getWidth() / 0.6F);
            if (entity.getWorld() instanceof ServerWorldAccess) {
                ((EntityDreadGhoul) lichSummoned).initialize((ServerWorldAccess) entity.getWorld(), entity.getWorld().getLocalDifficulty(entity.getBlockPos()), SpawnReason.MOB_SUMMONED, null, null);
            }
            ((EntityDreadGhoul) lichSummoned).setSize(readInScale);
            return lichSummoned;
        }
        if (entity.getGroup() == EntityGroup.UNDEAD || entity instanceof AbstractSkeletonEntity || entity instanceof PlayerEntity) {
            lichSummoned = new EntityDreadThrall(IafEntities.DREAD_THRALL, entity.getWorld());
            EntityDreadThrall thrall = (EntityDreadThrall) lichSummoned;
            if (entity.getWorld() instanceof ServerWorldAccess) {
                thrall.initialize((ServerWorldAccess) entity.getWorld(), entity.getWorld().getLocalDifficulty(entity.getBlockPos()), SpawnReason.MOB_SUMMONED, null, null);
            }
            thrall.setCustomArmorHead(false);
            thrall.setCustomArmorChest(false);
            thrall.setCustomArmorLegs(false);
            thrall.setCustomArmorFeet(false);
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                thrall.equipStack(slot, entity.getEquippedStack(slot));
            }
            return thrall;
        }
        if (entity instanceof AbstractHorseEntity) {
            lichSummoned = new EntityDreadHorse(IafEntities.DREAD_HORSE, entity.getWorld());
            return lichSummoned;
        }
        if (entity instanceof AnimalEntity) {
            lichSummoned = new EntityDreadBeast(IafEntities.DREAD_BEAST, entity.getWorld());
            float readInScale = (entity.getWidth() / 1.2F);
            if (entity.getWorld() instanceof ServerWorldAccess) {
                ((EntityDreadBeast) lichSummoned).initialize((ServerWorldAccess) entity.getWorld(), entity.getWorld().getLocalDifficulty(entity.getBlockPos()), SpawnReason.MOB_SUMMONED, null, null);
            }
            ((EntityDreadBeast) lichSummoned).setSize(readInScale);
            return lichSummoned;
        }
        return null;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(COMMANDER_UNIQUE_ID, Optional.empty());
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        if (this.getCommanderId() != null) {
            compound.putUuid("CommanderUUID", this.getCommanderId());
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
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
    public boolean isTeammate(Entity entityIn) {
        return entityIn instanceof IDreadMob || super.isTeammate(entityIn);
    }

    public UUID getCommanderId() {
        return (UUID) ((Optional) this.dataTracker.get(COMMANDER_UNIQUE_ID)).orElse(null);
    }

    public void setCommanderId(UUID uuid) {
        this.dataTracker.set(COMMANDER_UNIQUE_ID, Optional.ofNullable(uuid));
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.getWorld().isClient && this.getCommander() instanceof EntityDreadLich lich) {
            if (lich.getTarget() != null && lich.getTarget().isAlive()) {
                this.setTarget(lich.getTarget());
            }
        }
    }

    @Override
    public Entity getCommander() {
        try {
            UUID uuid = this.getCommanderId();
            LivingEntity player = uuid == null ? null : this.getWorld().getPlayerByUuid(uuid);
            if (player != null) {
                return player;
            } else {
                if (!this.getWorld().isClient) {
                    Entity entity = this.getWorld().getServer().getWorld(this.getWorld().getRegistryKey()).getEntity(uuid);
                    if (entity instanceof LivingEntity) {
                        return entity;
                    }
                }
            }
        } catch (IllegalArgumentException var2) {
            return null;
        }
        return null;
    }

    public void onKillEntity(LivingEntity LivingEntityIn) {
        Entity commander = this instanceof EntityDreadLich ? this : this.getCommander();
        if (commander != null && !(LivingEntityIn instanceof EntityDragonBase)) {// zombie dragons!!!!
            Entity summoned = necromancyEntity(LivingEntityIn);
            if (summoned != null) {
                summoned.copyPositionAndRotation(LivingEntityIn);
                if (!this.getWorld().isClient) {
                    this.getWorld().spawnEntity(summoned);
                }
                if (commander instanceof EntityDreadLich) {
                    ((EntityDreadLich) commander).setMinionCount(((EntityDreadLich) commander).getMinionCount() + 1);
                }
                if (summoned instanceof EntityDreadMob) {
                    ((EntityDreadMob) summoned).setCommanderId(commander.getUuid());
                }
            }
        }

    }

    @Override
    public void remove(RemovalReason reason) {
        if (!this.isRemoved() && this.getCommander() != null && this.getCommander() instanceof EntityDreadLich lich) {
            lich.setMinionCount(lich.getMinionCount() - 1);
        }
        super.remove(reason);
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }
}
