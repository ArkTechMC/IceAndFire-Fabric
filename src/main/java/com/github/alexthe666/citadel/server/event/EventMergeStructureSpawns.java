package com.github.alexthe666.citadel.server.event;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.structure.Structure;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;
import java.util.List;

@Event.HasResult
public class EventMergeStructureSpawns extends Event{

    private final StructureAccessor structureManager;
    private final BlockPos pos;
    private final SpawnGroup category;
    private Pool<SpawnSettings.SpawnEntry> structureSpawns;
    private final Pool<SpawnSettings.SpawnEntry> biomeSpawns;

    public EventMergeStructureSpawns(StructureAccessor structureManager, BlockPos pos, SpawnGroup category, Pool<SpawnSettings.SpawnEntry> structureSpawns, Pool<SpawnSettings.SpawnEntry> biomeSpawns) {
        this.structureManager = structureManager;
        this.pos = pos;
        this.category = category;
        this.structureSpawns = structureSpawns;
        this.biomeSpawns = biomeSpawns;
    }

    public StructureAccessor getStructureManager() {
        return structureManager;
    }

    public BlockPos getPos() {
        return pos;
    }

    public SpawnGroup getCategory(){
        return category;
    }

    public boolean isStructureTagged(TagKey<Structure> tagKey){
        return structureManager.getStructureContaining(pos, tagKey).hasChildren();
    }

    public Pool<SpawnSettings.SpawnEntry> getStructureSpawns() {
        return structureSpawns;
    }

    public void setStructureSpawns(Pool<SpawnSettings.SpawnEntry> spawns) {
        structureSpawns = spawns;
    }

    public void mergeSpawns(){
        List<SpawnSettings.SpawnEntry> list =  new ArrayList<>(biomeSpawns.getEntries());
        for(SpawnSettings.SpawnEntry structureSpawn : structureSpawns.getEntries()){
            if(!list.contains(structureSpawn)){
                list.add(structureSpawn);
            }
        }
        this.setStructureSpawns(Pool.of(list));
    }

    public Pool<SpawnSettings.SpawnEntry> getBiomeSpawns() {
        return biomeSpawns;
    }
}
