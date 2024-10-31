package com.iafenvoy.iceandfire.world;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.world.gen.TypedFeature;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class IafWorldData extends PersistentState {
    private static final String IDENTIFIER = IceAndFire.MOD_ID + "_general";
    private static final Map<FeatureType, CopyOnWriteArrayList<Pair<String, BlockPos>>> LAST_GENERATED = new HashMap<>();

    public IafWorldData() { /* Nothing to do */ }

    public IafWorldData(final NbtCompound tag) {
        this.load(tag);
    }

    public static IafWorldData get(final World world) {
        if (world instanceof ServerWorld) {
            ServerWorld overworld = world.getServer().getWorld(world.getRegistryKey());
            assert overworld != null;
            PersistentStateManager storage = overworld.getPersistentStateManager();
            IafWorldData data = storage.getOrCreate(IafWorldData::new, IafWorldData::new, IDENTIFIER);
            data.markDirty();
            return data;
        }
        return null;
    }

    public boolean check(final TypedFeature feature, final BlockPos position, final String id) {
        return this.check(feature.getFeatureType(), position, id);
    }

    public boolean check(final FeatureType type, final BlockPos position, final String id) {
        CopyOnWriteArrayList<Pair<String, BlockPos>> entries = LAST_GENERATED.computeIfAbsent(type, key -> new CopyOnWriteArrayList<>());

        boolean canGenerate = true;
        Pair<String, BlockPos> toRemove = null;

        for (Pair<String, BlockPos> entry : entries) {
            if (entry.getFirst().equals(id)) {
                toRemove = entry;
            }

            canGenerate = Math.sqrt(position.getSquaredDistance(entry.getSecond())) > IafCommonConfig.INSTANCE.worldGen.dangerousSeparationLimit.getValue();
        }

        if (toRemove != null) {
            entries.remove(toRemove);
        }

        entries.add(Pair.of(id, position));

        return canGenerate;
    }

    public void load(final NbtCompound tag) {
        FeatureType[] types = FeatureType.values();

        for (FeatureType type : types) {
            NbtList list = tag.getList(type.toString(), NbtList.COMPOUND_TYPE);

            for (int i = 0; i < list.size(); i++) {
                NbtCompound entry = list.getCompound(i);
                String id = entry.getString("id");
                BlockPos position = NbtHelper.toBlockPos(entry.getCompound("position"));
                LAST_GENERATED.computeIfAbsent(type, key -> new CopyOnWriteArrayList<>()).add(Pair.of(id, position));
            }
        }

    }

    @Override
    public NbtCompound writeNbt(final NbtCompound tag) {
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

    public enum FeatureType {
        SURFACE,
        UNDERGROUND,
        OCEAN
    }
}
