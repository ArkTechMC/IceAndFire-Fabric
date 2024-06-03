package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.mojang.serialization.Codec;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class IafMobSpawnBiomeModifier implements BiomeModifier {
    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = new RegistryObject<>(new Identifier(IceAndFire.MOD_ID, "iaf_mob_spawns"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, IceAndFire.MOD_ID);

    public IafMobSpawnBiomeModifier() {
    }

    @Override
    public void modify(RegistryEntry<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            IafEntityRegistry.addSpawners(biome, builder);
        }
    }


    public Codec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }

    public static Codec<IafMobSpawnBiomeModifier> makeCodec() {
        return Codec.unit(IafMobSpawnBiomeModifier::new);
    }
}