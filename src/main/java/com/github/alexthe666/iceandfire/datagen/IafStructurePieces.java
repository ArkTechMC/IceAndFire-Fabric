package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dev.arktechmc.iafextra.util.IdUtil;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;

public class IafStructurePieces {
    public static final RegistryKey<StructurePool> GRAVEYARD_START = createKey("graveyard/start_pool");
    public static final RegistryKey<StructurePool> MAUSOLEUM_START = createKey("mausoleum/start_pool");
    public static final RegistryKey<StructurePool> GORGON_TEMPLE_START = createKey("gorgon_temple/start_pool");

    private static RegistryKey<StructurePool> createKey(String name) {
        return RegistryKey.of(RegistryKeys.TEMPLATE_POOL, new Identifier(IceAndFire.MOD_ID, name));
    }

    public static void registerGraveyard(Registerable<StructurePool> pContext) {
        RegistryEntryLookup<StructureProcessorList> processorListHolderGetter = pContext.getRegistryLookup(RegistryKeys.PROCESSOR_LIST);
        RegistryEntry<StructureProcessorList> graveyardProcessor = processorListHolderGetter.getOrThrow(IafProcessorLists.GRAVEYARD_PROCESSORS);
        RegistryEntryLookup<StructurePool> templatePoolHolderGetter = pContext.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);
        RegistryEntry<StructurePool> fallback = templatePoolHolderGetter.getOrThrow(StructurePools.EMPTY);
        pContext.register(GRAVEYARD_START, new StructurePool(fallback, ImmutableList.of(Pair.of(StructurePoolElement.ofProcessedSingle(IdUtil.build(IceAndFire.MOD_ID, "graveyard/graveyard_top"), graveyardProcessor), 1)), StructurePool.Projection.RIGID));
        // We don't need direct access to this so register it here
        pContext.register(createKey("graveyard/bottom_pool"), new StructurePool(fallback, ImmutableList.of(Pair.of(StructurePoolElement.ofProcessedSingle(IdUtil.build(IceAndFire.MOD_ID, "graveyard/graveyard_bottom"), graveyardProcessor), 1)), StructurePool.Projection.RIGID));
    }

    public static void registerMausoleum(Registerable<StructurePool> pContext) {
        RegistryEntryLookup<StructureProcessorList> processorListHolderGetter = pContext.getRegistryLookup(RegistryKeys.PROCESSOR_LIST);
        RegistryEntry<StructureProcessorList> graveyardProcessor = processorListHolderGetter.getOrThrow(IafProcessorLists.MAUSOLEUM_PROCESSORS);
        RegistryEntryLookup<StructurePool> templatePoolHolderGetter = pContext.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);
        RegistryEntry<StructurePool> fallback = templatePoolHolderGetter.getOrThrow(StructurePools.EMPTY);
        pContext.register(MAUSOLEUM_START, new StructurePool(fallback, ImmutableList.of(Pair.of(StructurePoolElement.ofProcessedSingle(IdUtil.build(IceAndFire.MOD_ID, "mausoleum/building"), graveyardProcessor), 1)), StructurePool.Projection.RIGID));
    }

    public static void registerGorgonTemple(Registerable<StructurePool> pContext) {
        RegistryEntryLookup<StructureProcessorList> processorListHolderGetter = pContext.getRegistryLookup(RegistryKeys.PROCESSOR_LIST);
        RegistryEntry<StructureProcessorList> graveyardProcessor = processorListHolderGetter.getOrThrow(IafProcessorLists.GORGON_TEMPLE_PROCESSORS);
        RegistryEntryLookup<StructurePool> templatePoolHolderGetter = pContext.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);
        RegistryEntry<StructurePool> fallback = templatePoolHolderGetter.getOrThrow(StructurePools.EMPTY);
        pContext.register(GORGON_TEMPLE_START, new StructurePool(fallback, ImmutableList.of(Pair.of(StructurePoolElement.ofProcessedSingle(IdUtil.build(IceAndFire.MOD_ID, "gorgon_temple/building"), graveyardProcessor), 1)), StructurePool.Projection.RIGID));
        // We don't need direct access to this so register it here
        pContext.register(createKey("gorgon_temple/bottom_pool"), new StructurePool(fallback, ImmutableList.of(Pair.of(StructurePoolElement.ofProcessedSingle(IdUtil.build(IceAndFire.MOD_ID, "gorgon_temple/basement"), graveyardProcessor), 1)), StructurePool.Projection.RIGID));
        pContext.register(createKey("gorgon_temple/gorgon_pool"), new StructurePool(fallback, ImmutableList.of(Pair.of(StructurePoolElement.ofProcessedSingle(IdUtil.build(IceAndFire.MOD_ID, "gorgon_temple/gorgon"), graveyardProcessor), 1)), StructurePool.Projection.RIGID));

    }

    public static void bootstrap(Registerable<StructurePool> pContext) {
        registerGraveyard(pContext);
        registerMausoleum(pContext);
        registerGorgonTemple(pContext);
    }
}
