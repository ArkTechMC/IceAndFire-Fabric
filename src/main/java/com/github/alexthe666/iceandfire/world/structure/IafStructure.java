package com.github.alexthe666.iceandfire.world.structure;

import com.github.alexthe666.citadel.config.biome.SpawnBiomeData;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;
import java.util.Set;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class IafStructure extends Structure {

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


    protected boolean isBiomeValid(Context pContext, Pair<String, SpawnBiomeData> validBiomes, BlockPos blockPos) {
        boolean validBiome = false;
        Set<RegistryEntry<Biome>> biomes = pContext.chunkGenerator().getBiomeSource().getBiomesInArea(blockPos.getX(), blockPos.getY(), blockPos.getZ(), this.maxDistanceFromCenter, pContext.noiseConfig().getMultiNoiseSampler());
        for (RegistryEntry<Biome> biome : biomes) {
            if (BiomeConfig.test(validBiomes, biome)) {
                validBiome = true;
                break;
            }
        }
        return validBiome;
    }

    @Override
    protected Optional<StructurePosition> getStructurePosition(Context pContext) {
        return Optional.empty();
    }

    @Override
    public StructureType<?> getType() {
        return null;
    }

}
