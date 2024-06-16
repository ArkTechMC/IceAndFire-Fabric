package com.github.alexthe666.iceandfire.world.structure;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import com.github.alexthe666.iceandfire.datagen.IafBiomeTagGenerator;
import com.github.alexthe666.iceandfire.datagen.IafStructurePieces;
import com.github.alexthe666.iceandfire.registry.IafStructureTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.HashMap;
import java.util.Optional;

public class MausoleumStructure extends IafStructure {

    public static final Codec<MausoleumStructure> ENTRY_CODEC = RecordCodecBuilder.<MausoleumStructure>mapCodec(instance ->
            instance.group(MausoleumStructure.configCodecBuilder(instance),
                    StructurePool.REGISTRY_CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
                    Identifier.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
                    Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
                    Heightmap.Type.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter)
            ).apply(instance, MausoleumStructure::new)).codec();

    public MausoleumStructure(Config config, RegistryEntry<StructurePool> startPool, Optional<Identifier> startJigsawName, int size, HeightProvider startHeight, Optional<Heightmap.Type> projectStartToHeightmap, int maxDistanceFromCenter) {
        super(config, startPool, startJigsawName, size, startHeight, projectStartToHeightmap, maxDistanceFromCenter);
    }

    public static MausoleumStructure buildStructureConfig(Registerable<Structure> context) {
        RegistryEntryLookup<StructurePool> templatePoolHolderGetter = context.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);
        RegistryEntry<StructurePool> graveyardHolder = templatePoolHolderGetter.getOrThrow(IafStructurePieces.MAUSOLEUM_START);

        return new MausoleumStructure(
                new Config(
                        context.getRegistryLookup(RegistryKeys.BIOME).getOrThrow(IafBiomeTagGenerator.HAS_MAUSOLEUM),
                        new HashMap<>(),
                        GenerationStep.Feature.SURFACE_STRUCTURES,
                        StructureTerrainAdaptation.BEARD_THIN
                ),
                graveyardHolder,
                Optional.empty(),
                1,
                ConstantHeightProvider.ZERO,
                Optional.of(Heightmap.Type.WORLD_SURFACE_WG),
                16
        );
    }

    @Override
    protected Optional<StructurePosition> getStructurePosition(Context pContext) {
        if (!IafConfig.generateMausoleums)
            return Optional.empty();

        ChunkPos pos = pContext.chunkPos();
        BlockPos blockpos = pos.getCenterAtY(1);

        if (!this.isBiomeValid(pContext, BiomeConfig.mausoleumBiomes, blockpos))
            return Optional.empty();

        return StructurePoolBasedGenerator.generate(
                pContext, // Used for JigsawPlacement to get all the proper behaviors done.
                this.startPool, // The starting pool to use to create the structure layout from
                this.startJigsawName, // Can be used to only spawn from one Jigsaw block. But we don't need to worry about this.
                this.size, // How deep a branch of pieces can go away from center piece. (5 means branches cannot be longer than 5 pieces from center piece)
                blockpos, // Where to spawn the structure.
                false, // "useExpansionHack" This is for legacy villages to generate properly. You should keep this false always.
                this.projectStartToHeightmap, // Adds the terrain height's y value to the passed in blockpos's y value. (This uses WORLD_SURFACE_WG heightmap which stops at top water too)
                // Here, blockpos's y value is 60 which means the structure spawn 60 blocks above terrain height.
                // Set this to false for structure to be place only at the passed in blockpos's Y value instead.
                // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
                this.maxDistanceFromCenter);
    }

    @Override
    public StructureType<?> getType() {
        return IafStructureTypes.MAUSOLEUM;
    }

}
