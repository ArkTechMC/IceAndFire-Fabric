package com.github.alexthe666.iceandfire.entity.props;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class MiscData {
    public int loveTicks;
    public int lungeTicks;
    public boolean hasDismounted;

    // Transient data
    public List<LivingEntity> targetedByScepter;
    private List<Integer> targetedByScepterIds;

    private boolean isInitialized;
    private boolean triggerClientUpdate;

    public void tickMisc(final LivingEntity entity) {
        if (!isInitialized) {
            initialize(entity.getWorld());
        }

        if (loveTicks > 0) {
            loveTicks--;

            if (loveTicks == 0) {
                triggerClientUpdate = true;
                return;
            }

            if (entity instanceof MobEntity mob) {
                mob.setAttacking(null);
                mob.setAttacker(null);
                mob.setTarget(null);
                mob.setAttacking(false);
            }

            createLoveParticles(entity);
        }
    }

    public List<LivingEntity> getTargetedByScepter() {
        return Objects.requireNonNullElse(targetedByScepter, Collections.emptyList());
    }

    public void addScepterTarget(final LivingEntity target) {
        if (targetedByScepter == null) {
            targetedByScepter = new ArrayList<>();
        } else if (targetedByScepter.contains(target)) {
            return;
        }

        targetedByScepter.add(target);
        triggerClientUpdate = true;
    }

    public void removeScepterTarget(final LivingEntity target) {
        if (targetedByScepter == null) {
            return;
        }

        targetedByScepter.remove(target);
        triggerClientUpdate = true;
    }

    public void setLoveTicks(int loveTicks) {
        this.loveTicks = loveTicks;
        triggerClientUpdate = true;
    }

    public void setLungeTicks(int lungeTicks) {
        this.lungeTicks = lungeTicks;
        triggerClientUpdate = true;
    }

    public void setDismounted(boolean hasDismounted) {
        this.hasDismounted = hasDismounted;
        triggerClientUpdate = true;
    }

    public void serialize(final NbtCompound tag) {
        NbtCompound miscData = new NbtCompound();
        miscData.putInt("loveTicks", loveTicks);
        miscData.putInt("lungeTicks", lungeTicks);
        miscData.putBoolean("hasDismounted", hasDismounted);

        if (targetedByScepter != null) {
            int[] ids = new int[targetedByScepter.size()];

            for (int i = 0; i < targetedByScepter.size(); i++) {
                ids[i] = targetedByScepter.get(i).getId();
            }

            tag.putIntArray("targetedByScepterIds", ids);
        }

        tag.put("miscData", miscData);
    }

    public void deserialize(final NbtCompound tag) {
        NbtCompound miscData = tag.getCompound("miscData");
        loveTicks = miscData.getInt("loveTicks");
        lungeTicks = miscData.getInt("lungeTicks");
        hasDismounted = miscData.getBoolean("hasDismounted");
        int[] loadedChainedToIds = miscData.getIntArray("targetedByScepterIds");

        isInitialized = false;

        if (loadedChainedToIds.length > 0) {
            targetedByScepterIds = new ArrayList<>();

            for (int loadedChainedToId : loadedChainedToIds) {
                targetedByScepterIds.add(loadedChainedToId);
            }
        }
    }

    public boolean doesClientNeedUpdate() {
        if (triggerClientUpdate) {
            triggerClientUpdate = false;
            return true;
        }

        return false;
    }

    private void createLoveParticles(final LivingEntity entity) {
        if (entity.getRandom().nextInt(7) == 0) {
            for (int i = 0; i < 5; i++) {
                entity.getWorld().addParticle(ParticleTypes.HEART,
                        entity.getX() + ((entity.getRandom().nextDouble() - 0.5D) * 3),
                        entity.getY() + ((entity.getRandom().nextDouble() - 0.5D) * 3),
                        entity.getZ() + ((entity.getRandom().nextDouble() - 0.5D) * 3), 0, 0, 0);
            }
        }
    }

    private void initialize(final World level) {
        List<LivingEntity> entities = new ArrayList<>();

        if (targetedByScepterIds != null) {
            for (int id : targetedByScepterIds) {
                if (id == -1) {
                    continue;
                }

                Entity entity = level.getEntityById(id);

                if (entity instanceof LivingEntity livingEntity) {
                    entities.add(livingEntity);
                }
            }
        }

        if (!entities.isEmpty()) {
            targetedByScepter = entities;
        } else {
            targetedByScepter = null;
        }

        targetedByScepterIds = null;
        isInitialized = true;
    }
}
