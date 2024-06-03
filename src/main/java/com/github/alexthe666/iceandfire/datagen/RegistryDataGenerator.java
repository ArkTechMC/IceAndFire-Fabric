package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistryDataGenerator extends DatapackBuiltinEntriesProvider {
    public static final RegistryBuilder BUILDER = new RegistryBuilder()
            .addRegistry(RegistryKeys.CONFIGURED_FEATURE, IafConfiguredFeatures::bootstrap)
            .addRegistry(RegistryKeys.PLACED_FEATURE, IafPlacedFeatures::bootstrap)
            .addRegistry(RegistryKeys.STRUCTURE, IafStructures::bootstrap)
            .addRegistry(RegistryKeys.STRUCTURE_SET, IafStructureSets::bootstrap)
            .addRegistry(RegistryKeys.PROCESSOR_LIST, IafProcessorLists::bootstrap)
            .addRegistry(RegistryKeys.TEMPLATE_POOL, IafStructurePieces::bootstrap)
            .addRegistry(ForgeRegistries.Keys.BIOME_MODIFIERS, IafBiomeModifierSerializers::bootstrap);

    public RegistryDataGenerator(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> provider) {
        super(output, provider, BUILDER, Set.of("minecraft", IceAndFire.MOD_ID));
    }
}

