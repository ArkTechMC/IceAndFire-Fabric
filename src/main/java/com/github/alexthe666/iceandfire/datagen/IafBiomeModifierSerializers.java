package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.datagen.util.ListHolderSet;
import com.github.alexthe666.iceandfire.world.IafFeatureBiomeModifier;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class IafBiomeModifierSerializers {
    public static RegistryKey<BiomeModifier> FEATURES = createKey("iaf_features");

    public static RegistryKey<BiomeModifier> createKey(String name) {
        return RegistryKey.of(ForgeRegistries.Keys.BIOME_MODIFIERS, new Identifier(IceAndFire.MOD_ID, name));
    }

    private static ListHolderSet<PlacedFeature> createHolderSet(RegistryEntryLookup<PlacedFeature> holderGetter, List<RegistryKey<PlacedFeature>> features) {
        List<RegistryEntry<PlacedFeature>> holders = new ArrayList<>();
        features.forEach(feature -> holders.add(holderGetter.getOrThrow(feature)));
        return new ListHolderSet<>(holders);
    }
    public static void bootstrap(Registerable<BiomeModifier> context) {
        RegistryEntryLookup<PlacedFeature> holderGetter = context.lookup(RegistryKeys.PLACED_FEATURE);
        List<RegistryKey<PlacedFeature>> features = List.of(
                IafPlacedFeatures.PLACED_FIRE_DRAGON_ROOST,
                IafPlacedFeatures.PLACED_ICE_DRAGON_ROOST,
                IafPlacedFeatures.PLACED_LIGHTNING_DRAGON_ROOST,
                IafPlacedFeatures.PLACED_FIRE_DRAGON_CAVE,
                IafPlacedFeatures.PLACED_ICE_DRAGON_CAVE,
                IafPlacedFeatures.PLACED_LIGHTNING_DRAGON_CAVE,
                IafPlacedFeatures.PLACED_CYCLOPS_CAVE,
                IafPlacedFeatures.PLACED_PIXIE_VILLAGE,
                IafPlacedFeatures.PLACED_SIREN_ISLAND,
                IafPlacedFeatures.PLACED_HYDRA_CAVE,
                IafPlacedFeatures.PLACED_MYRMEX_HIVE_DESERT,
                IafPlacedFeatures.PLACED_MYRMEX_HIVE_JUNGLE,
                IafPlacedFeatures.PLACED_SPAWN_DEATH_WORM,
                IafPlacedFeatures.PLACED_SPAWN_DRAGON_SKELETON_L,
                IafPlacedFeatures.PLACED_SPAWN_DRAGON_SKELETON_F,
                IafPlacedFeatures.PLACED_SPAWN_DRAGON_SKELETON_I,
                IafPlacedFeatures.PLACED_SPAWN_HIPPOCAMPUS,
                IafPlacedFeatures.PLACED_SPAWN_SEA_SERPENT,
                IafPlacedFeatures.PLACED_SPAWN_STYMPHALIAN_BIRD,
                IafPlacedFeatures.PLACED_SPAWN_WANDERING_CYCLOPS,
                IafPlacedFeatures.PLACED_SILVER_ORE,
                IafPlacedFeatures.PLACED_SAPPHIRE_ORE,
                IafPlacedFeatures.PLACED_FIRE_LILY,
                IafPlacedFeatures.PLACED_FROST_LILY,
                IafPlacedFeatures.PLACED_LIGHTNING_LILY

        );
        context.register(FEATURES, new IafFeatureBiomeModifier(createHolderSet(holderGetter, features)));
    }
}
