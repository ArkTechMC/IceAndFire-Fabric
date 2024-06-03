package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

public class IafFeatureBiomeModifier implements BiomeModifier {
    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = new RegistryObject<>(new Identifier(IceAndFire.MOD_ID, "iaf_features"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, IceAndFire.MOD_ID);
    private final RegistryEntryList<PlacedFeature> features;
    public final HashMap<String, RegistryEntry<PlacedFeature>> featureMap = new HashMap<>();

    public IafFeatureBiomeModifier(RegistryEntryList<PlacedFeature> features) {
        this.features = features;
        this.features.forEach(feature -> featureMap.put(feature.getKey().get().getValue().toString(), feature));
    }

    @Override
    public void modify(RegistryEntry<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            IafWorldRegistry.addFeatures(biome, featureMap, builder);
        }
    }

    public Codec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }

    public static Codec<IafFeatureBiomeModifier> makeCodec() {
        return RecordCodecBuilder.create(config -> config.group(PlacedFeature.LIST_CODEC.fieldOf("features").forGetter((otherConfig) -> otherConfig.features)).apply(config, IafFeatureBiomeModifier::new));
    }
}