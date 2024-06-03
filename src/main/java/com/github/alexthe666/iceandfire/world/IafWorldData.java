package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.gen.TypedFeature;
import com.mojang.datafixers.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

public class IafWorldData extends PersistentState {
    public enum FeatureType {
        SURFACE,
        UNDERGROUND,
        OCEAN
    }

    private static final String IDENTIFIER = IceAndFire.MOD_ID + "_general";
    private static final Map<FeatureType, List<Pair<String, BlockPos>>> LAST_GENERATED = new HashMap<>();

    public IafWorldData() { /* Nothing to do */ }

    public IafWorldData(final NbtCompound tag) {
        this.load(tag);
    }

    public static IafWorldData get(final World world) {
        if (world instanceof ServerWorld) {
            ServerWorld overworld = world.getServer().getWorld(world.getRegistryKey());
            PersistentStateManager storage = overworld.getPersistentStateManager();
            IafWorldData data = storage.getOrCreate(IafWorldData::new, IafWorldData::new, IDENTIFIER);
            data.markDirty();

            return data;
        }

        return null;
    }

    public boolean check(final TypedFeature feature, final BlockPos position, final String id) {
        return check(feature.getFeatureType(), position, id);
    }

    public boolean check(final FeatureType type, final BlockPos position, final String id) {
        List<Pair<String, BlockPos>> entries = LAST_GENERATED.computeIfAbsent(type, key -> new ArrayList<>());

        boolean canGenerate = true;
        Pair<String, BlockPos> toRemove = null;

        for (Pair<String, BlockPos> entry : entries) {
            if (entry.getFirst().equals(id)) {
                toRemove = entry;
            }

            canGenerate = position.getSquaredDistance(entry.getSecond()) > IafConfig.dangerousWorldGenSeparationLimit * IafConfig.dangerousWorldGenSeparationLimit;
        }

        if (toRemove != null) {
            entries.remove(toRemove);
        }

        entries.add(Pair.of(id, position));

        return canGenerate;
    }

    public IafWorldData load(final NbtCompound tag) {
        FeatureType[] types = FeatureType.values();

        for (FeatureType type : types) {
            NbtList list = tag.getList(type.toString(), NbtList.COMPOUND_TYPE);

            for (int i = 0; i < list.size(); i++) {
                NbtCompound entry = list.getCompound(i);
                String id = entry.getString("id");
                BlockPos position = NbtHelper.toBlockPos(entry.getCompound("position"));
                LAST_GENERATED.computeIfAbsent(type, key -> new ArrayList<>()).add(Pair.of(id, position));
            }
        }

        return this;
    }

    @Override
    public @NotNull NbtCompound writeNbt(@NotNull final NbtCompound tag) {
        LAST_GENERATED.forEach((key, value) -> {
            NbtList listTag = new NbtList();

            value.forEach(entry -> {
                NbtCompound subTag = new NbtCompound();
                subTag.putString("id", entry.getFirst());
                subTag.put("position", NbtHelper.fromBlockPos(entry.getSecond()));

                listTag.add(subTag);
            });

            tag.put(key.toString(), listTag);
        });

        return tag;
    }
}
