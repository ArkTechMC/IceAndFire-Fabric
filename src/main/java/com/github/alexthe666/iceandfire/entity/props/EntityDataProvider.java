package com.github.alexthe666.iceandfire.entity.props;

import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EntityDataProvider implements ICapabilitySerializable<NbtCompound> {
    public static final Map<Integer, LazyOptional<EntityData>> SERVER_CACHE = new HashMap<>();
    public static final Map<Integer, LazyOptional<EntityData>> CLIENT_CACHE = new HashMap<>();

    private final EntityData data = new EntityData();
    private final LazyOptional<EntityData> instance = LazyOptional.of(() -> this.data);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull final Capability<T> capability, final Direction side) {
        return capability == CapabilityHandler.ENTITY_DATA_CAPABILITY ? this.instance.cast() : LazyOptional.empty();
    }

    @Override
    public void deserializeNBT(final NbtCompound tag) {
        this.instance.orElseThrow(() -> new IllegalArgumentException("Capability instance was not present")).deserialize(tag);
    }

    @Override
    public NbtCompound serializeNBT() {
        return this.instance.orElseThrow(() -> new IllegalArgumentException("Capability instance was not present")).serialize();
    }

    public static LazyOptional<EntityData> getCapability(final Entity entity) {
        if (entity instanceof LivingEntity) {
            int key = entity.getId();

            Map<Integer, LazyOptional<EntityData>> sidedCache = entity.getWorld().isClient() ? CLIENT_CACHE : SERVER_CACHE;
            LazyOptional<EntityData> capability = sidedCache.get(key);

            if (capability == null) {
                capability = entity.getCapability(CapabilityHandler.ENTITY_DATA_CAPABILITY);
                capability.addListener(ignored -> sidedCache.remove(key));

                if (capability.isPresent()) {
                    sidedCache.put(key, capability);
                }
            }

            return capability;
        }

        return LazyOptional.empty();
    }

    public static void removeCachedEntry(final Entity entity) {
        if (entity instanceof LivingEntity) {
            int key = entity.getId();

            if (entity.getWorld().isClient()) {
                if (entity == CapabilityHandler.getLocalPlayer()) {
                    // Can trigger on death or when player leaves the game (this is when we want to actually clear)
                    CLIENT_CACHE.clear();
                } else {
                    CLIENT_CACHE.remove(key);
                }
            } else {
                SERVER_CACHE.remove(key);
            }
        }
    }
}
