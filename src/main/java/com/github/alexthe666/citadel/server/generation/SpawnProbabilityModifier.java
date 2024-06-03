package com.github.alexthe666.citadel.server.generation;

import com.github.alexthe666.citadel.config.ServerConfig;
import com.mojang.serialization.Codec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SpawnProbabilityModifier implements BiomeModifier {

    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(new Identifier("citadel:mob_spawn_probability"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, "citadel");

    @Override
    public void modify(RegistryEntry<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        float probability = (float) (ServerConfig.chunkGenSpawnModifierVal) * builder.getMobSpawnSettings().getProbability();
        if (phase == Phase.MODIFY) {
            builder.getMobSpawnSettings().creatureGenerationProbability(MathHelper.clamp(probability, 0F, 1F));
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }

    public static Codec<SpawnProbabilityModifier> makeCodec(){
        return Codec.unit(SpawnProbabilityModifier::new);
    }
}
