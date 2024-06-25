package com.iafenvoy.iceandfire.config;

import com.iafenvoy.citadel.config.biome.BiomeEntryType;
import com.iafenvoy.citadel.config.biome.SpawnBiomeData;
import com.iafenvoy.iceandfire.registry.tag.CommonTags;
import net.minecraft.registry.tag.BiomeTags;

public class DefaultBiomes {
    public static final SpawnBiomeData OVERWORLD = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0);

    public static final SpawnBiomeData FIREDRAGON_ROOST = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_HOT_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, BiomeTags.IS_JUNGLE.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, BiomeTags.IS_BADLANDS.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, BiomeTags.IS_SAVANNA.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_PLAINS.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, CommonTags.Biomes.IS_WET_OVERWORLD.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, CommonTags.Biomes.IS_COLD_OVERWORLD.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, BiomeTags.IS_JUNGLE.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, BiomeTags.IS_BADLANDS.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, BiomeTags.IS_SAVANNA.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, BiomeTags.IS_FOREST.id().toString(), 1);
    public static final SpawnBiomeData FIREDRAGON_CAVE = FIREDRAGON_ROOST;

    public static final SpawnBiomeData ICEDRAGON_ROOST = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_COLD_OVERWORLD.id().toString(), 0);
    public static final SpawnBiomeData ICEDRAGON_CAVE = ICEDRAGON_ROOST;

    public static final SpawnBiomeData LIGHTNING_ROOST = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_JUNGLE.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_BADLANDS.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_SAVANNA.id().toString(), 2);
    public static final SpawnBiomeData LIGHTNING_CAVE = LIGHTNING_ROOST;

    public static final SpawnBiomeData VERY_HOT = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_HOT_OVERWORLD.id().toString(), 0);

    public static final SpawnBiomeData VERY_SNOWY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_SNOWY.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, CommonTags.Biomes.IS_WET_OVERWORLD.id().toString(), 0);

    public static final SpawnBiomeData MAUSOLEUM = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_SNOWY.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, CommonTags.Biomes.IS_WET_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, CommonTags.Biomes.IS_WATER.id().toString(), 0);

    public static final SpawnBiomeData SNOWY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_SNOWY.id().toString(), 0);

    public static final SpawnBiomeData VERY_HILLY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_MOUNTAIN.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_HILL.id().toString(), 1);

    public static final SpawnBiomeData WOODLAND = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_FOREST.id().toString(), 0);

    public static final SpawnBiomeData SAVANNAS = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_SAVANNA.id().toString(), 0);

    public static final SpawnBiomeData BEACHES = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_BEACH.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, BiomeTags.IS_OCEAN.id().toString(), 0);

    public static final SpawnBiomeData SWAMPS = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_SWAMP.id().toString(), 0);

    public static final SpawnBiomeData DESERT = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_DRY_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_HOT_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_SANDY.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, BiomeTags.IS_BADLANDS.id().toString(), 0);

    public static final SpawnBiomeData OCEANS = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OCEAN.id().toString(), 0);

    public static final SpawnBiomeData PIXIES = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_RARE.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_MAGICAL.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_DENSE.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_FOREST.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, CommonTags.Biomes.IS_MOUNTAIN.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, BiomeTags.IS_HILL.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, CommonTags.Biomes.IS_WET_OVERWORLD.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, CommonTags.Biomes.IS_HOT.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, CommonTags.Biomes.IS_COLD.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, CommonTags.Biomes.IS_DRY.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, BiomeTags.IS_TAIGA.id().toString(), 1);

    public static final SpawnBiomeData JUNGLE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_JUNGLE.id().toString(), 0);

    public static final SpawnBiomeData HILLS = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_BADLANDS.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_MOUNTAIN.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 2)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_HILL.id().toString(), 2);

    public static final SpawnBiomeData PLAINS = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_PLAINS.id().toString(), 0);

    public static final SpawnBiomeData GRAVEYARD = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, CommonTags.Biomes.IS_WATER.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, BiomeTags.IS_BEACH.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, true, CommonTags.Biomes.IS_UNDERGROUND.id().toString(), 0);

    public static final SpawnBiomeData HIPPOGRYPH_BLACK = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:badlands", 0);

    public static final SpawnBiomeData HIPPOGRYPH_DARK_BROWN = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_FOREST.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_MOUNTAIN.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_SNOWY.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_FOREST.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_HILL.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_SNOWY.id().toString(), 1);

    public static final SpawnBiomeData HIPPOGRYPH_WHITE = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_SPARSE.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_SNOWY.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_MOUNTAIN.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_PEAK.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_SPARSE.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_SNOWY.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_MOUNTAIN.id().toString(), 1)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_SLOPE.id().toString(), 1);

    public static final SpawnBiomeData HIPPOGRYPH_GRAY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:windswept_forest", 0)
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:windswept_hills", 1);

    public static final SpawnBiomeData HIPPOGRYPH_CHESTNUT = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_OVERWORLD.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_MOUNTAIN.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, BiomeTags.IS_BADLANDS.id().toString(), 0)
            .addBiomeEntry(BiomeEntryType.BIOME_TAG, false, CommonTags.Biomes.IS_DRY.id().toString(), 0);

    public static final SpawnBiomeData HIPPOGRYPH_CREAMY = new SpawnBiomeData()
            .addBiomeEntry(BiomeEntryType.REGISTRY_NAME, false, "minecraft:savanna_plateau", 0);
}
