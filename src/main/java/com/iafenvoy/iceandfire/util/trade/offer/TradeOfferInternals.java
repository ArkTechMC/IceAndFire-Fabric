package com.iafenvoy.iceandfire.util.trade.offer;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

// From object builder api v1
public final class TradeOfferInternals {
    private static final Logger LOGGER = LoggerFactory.getLogger("fabric-villager-api-v1");

    private TradeOfferInternals() {
    }

    // synchronized guards against concurrent modifications - Vanilla does not mutate the underlying arrays (as of 1.16),
    // so reads will be fine without locking.
    public static synchronized void registerVillagerOffers(VillagerProfession profession, int level, Consumer<List<TradeOffers.Factory>> factory) {
        Objects.requireNonNull(profession, "VillagerProfession may not be null.");
        registerOffers(TradeOffers.PROFESSION_TO_LEVELED_TRADE.computeIfAbsent(profession, key -> new Int2ObjectOpenHashMap<>()), level, factory);
    }

    public static synchronized void registerWanderingTraderOffers(int level, Consumer<List<TradeOffers.Factory>> factory) {
        registerOffers(TradeOffers.WANDERING_TRADER_TRADES, level, factory);
    }

    // Shared code to register offers for both villagers and wandering traders.
    private static void registerOffers(Int2ObjectMap<TradeOffers.Factory[]> leveledTradeMap, int level, Consumer<List<TradeOffers.Factory>> factory) {
        final List<TradeOffers.Factory> list = new ArrayList<>();
        factory.accept(list);

        final TradeOffers.Factory[] originalEntries = leveledTradeMap.computeIfAbsent(level, key -> new TradeOffers.Factory[0]);
        final TradeOffers.Factory[] addedEntries = list.toArray(new TradeOffers.Factory[0]);

        final TradeOffers.Factory[] allEntries = ArrayUtils.addAll(originalEntries, addedEntries);
        leveledTradeMap.put(level, allEntries);
    }

    public static void printRefreshOffersWarning() {
        Throwable loggingThrowable = new Throwable();
        LOGGER.warn("TradeOfferHelper#refreshOffers does not do anything, yet it was called! Stack trace:", loggingThrowable);
    }
}