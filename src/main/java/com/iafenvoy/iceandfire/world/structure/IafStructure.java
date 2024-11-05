package com.iafenvoy.iceandfire.world.structure;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;
import java.util.Set;

public abstract class IafStructure extends Structure {
    protected final RegistryEntry<StructurePool> startPool;
    protected final Optional<Identifier> startJigsawName;
    protected final int size;
    protected final HeightProvider startHeight;
    protected final Optional<Heightmap.Type> projectStartToHeightmap;
    protected final int maxDistanceFromCenter;

    public IafStructure(Config config,
                        RegistryEntry<StructurePool> startPool,
                        Optional<Identifier> startJigsawName,
                        int size,
                        HeightProvider startHeight,
                        Optional<Heightmap.Type> projectStartToHeightmap,
                        int maxDistanceFromCenter) {
        super(config);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.size = size;
        this.startHeight = startHeight;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
    }
}
