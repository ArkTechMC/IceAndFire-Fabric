package com.github.alexthe666.citadel.server.event;

import dev.arktechmc.iafextra.event.Event;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.structure.Structure;

import java.util.ArrayList;
import java.util.List;

@Event.HasResult
public class EventMergeStructureSpawns extends Event {

    private final StructureAccessor structureManager;
    private final BlockPos pos;
    private final SpawnGroup category;
    private final Pool<SpawnSettings.SpawnEntry> biomeSpawns;
    private Pool<SpawnSettings.SpawnEntry> structureSpawns;

    public EventMergeStructureSpawns(StructureAccessor structureManager, BlockPos pos, SpawnGroup category, Pool<SpawnSettings.SpawnEntry> structureSpawns, Pool<SpawnSettings.SpawnEntry> biomeSpawns) {
        this.structureManager = structureManager;
        this.pos = pos;
        this.category = category;
        this.structureSpawns = structureSpawns;
        this.biomeSpawns = biomeSpawns;
    }

    public StructureAccessor getStructureManager() {
        return this.structureManager;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public SpawnGroup getCategory() {
        return this.category;
    }

    public boolean isStructureTagged(TagKey<Structure> tagKey) {
        return this.structureManager.getStructureContaining(this.pos, tagKey).hasChildren();
    }

    public Pool<SpawnSettings.SpawnEntry> getStructureSpawns() {
        return this.structureSpawns;
    }

    public void setStructureSpawns(Pool<SpawnSettings.SpawnEntry> spawns) {
        this.structureSpawns = spawns;
    }

    public void mergeSpawns() {
        List<SpawnSettings.SpawnEntry> list = new ArrayList<>(this.biomeSpawns.getEntries());
        for (SpawnSettings.SpawnEntry structureSpawn : this.structureSpawns.getEntries()) {
            if (!list.contains(structureSpawn)) {
                list.add(structureSpawn);
            }
        }
        this.setStructureSpawns(Pool.of(list));
    }

    public Pool<SpawnSettings.SpawnEntry> getBiomeSpawns() {
        return this.biomeSpawns;
    }
}
