package com.github.alexthe666.citadel.server.generation;

import com.github.alexthe666.citadel.Citadel;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class VillageHouseManager {
    public static final List<Identifier> VILLAGE_REPLACEMENT_POOLS = List.of(
            new Identifier("village/plains/houses"),
            new Identifier("village/desert/houses"),
            new Identifier("village/savanna/houses"),
            new Identifier("village/snowy/houses"),
            new Identifier("village/taiga/houses"));
    private static final List<Pair<Identifier, Consumer<StructurePool>>> REGISTRY = new ArrayList<>();

    public static void register(Identifier pool, Consumer<StructurePool> addToPool) {
        REGISTRY.add(new Pair<>(pool, addToPool));
        Citadel.LOGGER.debug("registered addition to pool: " + pool);
    }

    public static StructurePool addToPool(StructurePool pool, StructurePoolElement element, int weight) {
        if (weight > 0) {
            if (pool != null) {
                ObjectArrayList<StructurePoolElement> templates = new ObjectArrayList<>(pool.elements);
                if (!templates.contains(element)) {
                    for (int i = 0; i < weight; i++) {
                        templates.add(element);
                    }
                    List<Pair<StructurePoolElement, Integer>> rawTemplates = new ArrayList<>(pool.elementCounts);
                    rawTemplates.add(new Pair<>(element, weight));
                    pool.elements = templates;
                    pool.elementCounts = rawTemplates;
                    Citadel.LOGGER.info("Added to village structure pool");
                }
            }
        }
        return pool;
    }

    public static void addAllHouses(DynamicRegistryManager registryAccess) {
        try {
            for (Identifier villagePool : VILLAGE_REPLACEMENT_POOLS) {
                StructurePool pool = registryAccess.get(RegistryKeys.TEMPLATE_POOL).getOrEmpty(villagePool).orElse(null);
                if (pool != null) {
                    for (Pair<Identifier, Consumer<StructurePool>> pair : REGISTRY) {
                        if (villagePool.equals(pair.getFirst())) {
                            pair.getSecond().accept(pool);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Citadel.LOGGER.error("Could not add village houses!");
            e.printStackTrace();
        }
    }
}
