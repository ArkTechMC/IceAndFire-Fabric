package com.github.alexthe666.iceandfire.entity.props;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.*;

public class ChainData {
    public List<Entity> chainedTo;

    // These lists are only for the sync (and therefor are cleared once it happened)
    private List<Integer> chainedToIds;
    private List<UUID> chainedToUUIDs;

    private boolean isInitialized;
    private boolean triggerClientUpdate;

    public void tickChain(final LivingEntity entity) {
        if (!isInitialized) {
            initialize(entity.getWorld());
        }

        if (chainedTo == null) {
            return;
        }

        for (Entity chain : chainedTo) {
            double distance = chain.distanceTo(entity);

            if (distance > 7) {
                double x = (chain.getX() - entity.getX()) / distance;
                double y = (chain.getY() - entity.getY()) / distance;
                double z = (chain.getZ() - entity.getZ()) / distance;
                entity.setVelocity(entity.getVelocity().add(x * Math.abs(x) * 0.4D, y * Math.abs(y) * 0.2D, z * Math.abs(z) * 0.4D));
            }
        }
    }

    public List<Entity> getChainedTo() {
        return Objects.requireNonNullElse(chainedTo, Collections.emptyList());
    }

    public void clearChains() {
        if (chainedTo == null) {
            return;
        }

        chainedTo = null;
        triggerClientUpdate = true;
    }

    public void attachChain(final Entity chain) {
        if (chainedTo == null) {
            chainedTo = new ArrayList<>();
        } else if (chainedTo.contains(chain)) {
            return;
        }

        chainedTo.add(chain);
        triggerClientUpdate = true;
    }

    public void removeChain(final Entity chain) {
        if (chainedTo == null) {
            return;
        }

        chainedTo.remove(chain);
        triggerClientUpdate = true;

        if (chainedTo.isEmpty()) {
            chainedTo = null;
        }
    }

    public boolean isChainedTo(final Entity toCheck) {
        if (chainedTo == null || chainedTo.isEmpty()) {
            return false;
        }

        return chainedTo.contains(toCheck);
    }

    public void serialize(final NbtCompound tag) {
        NbtCompound chainedData = new NbtCompound();
        NbtList uuids = new NbtList();

        if (chainedTo != null) {
            int[] ids = new int[chainedTo.size()];

            for (int i = 0; i < chainedTo.size(); i++) {
                Entity entity = chainedTo.get(i);

                ids[i] = entity.getId();
                uuids.add(NbtHelper.fromUuid(entity.getUuid()));
            }

            chainedData.putIntArray("chainedToIds", ids);
            chainedData.put("chainedToUUIDs", uuids);
        }

        tag.put("chainedData", chainedData);
    }

    public void deserialize(final NbtCompound tag) {
        NbtCompound chainedData = tag.getCompound("chainedData");
        int[] loadedChainedToIds = chainedData.getIntArray("chainedToIds");
        NbtList uuids = chainedData.getList("chainedToUUIDs", NbtList.INT_ARRAY_TYPE);

        isInitialized = false;

        if (loadedChainedToIds.length > 0) {
            chainedToIds = new ArrayList<>();

            for (int loadedChainedToId : loadedChainedToIds) {
                chainedToIds.add(loadedChainedToId);
            }
        } else {
            chainedToIds = null;
        }

        if (!uuids.isEmpty()) {
            chainedToUUIDs = new ArrayList<>();

            for (NbtElement uuid : uuids) {
                chainedToUUIDs.add(NbtHelper.toUuid(uuid));
            }
        } else {
            chainedToUUIDs = null;
        }
    }

    public boolean doesClientNeedUpdate() {
        if (triggerClientUpdate) {
            triggerClientUpdate = false;
            return true;
        }

        return false;
    }

    private void initialize(final World level) {
        List<Entity> entities = new ArrayList<>();

        // Make sure server gets the new entity ids on re-join and syncs it to the client
        if (chainedToUUIDs != null && level instanceof ServerWorld serverLevel) {
            for (UUID uuid : chainedToUUIDs) {
                Entity entity = serverLevel.getEntity(uuid);

                if (entity != null) {
                    entities.add(entity);
                }
            }

            triggerClientUpdate = true;
        } else if (chainedToIds != null) {
            for (int id : chainedToIds) {
                if (id == -1) {
                    continue;
                }

                Entity entity = level.getEntityById(id);

                if (entity != null) {
                    entities.add(entity);
                }
            }
        }

        if (!entities.isEmpty()) {
            chainedTo = entities;
        } else {
            chainedTo = null;
        }

        chainedToIds = null;
        chainedToUUIDs = null;
        isInitialized = true;
    }
}
