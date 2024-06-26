package com.iafenvoy.iceandfire.registry;

import com.google.common.collect.ImmutableSet;
import com.iafenvoy.iceandfire.IceAndFire;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IafTrades {
    public static final PointOfInterestType SCRIBE_POI = new PointOfInterestType(ImmutableSet.copyOf(IafBlocks.LECTERN.getStateManager().getStates()), 1, 1);
    public static final VillagerProfession SCRIBE = new VillagerProfession("scribe", (entry) -> entry.value().equals(SCRIBE_POI), (entry) -> entry.value().equals(SCRIBE_POI), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.ENTITY_VILLAGER_WORK_LIBRARIAN);

    public static void init() {
        Registry.register(Registries.POINT_OF_INTEREST_TYPE, new Identifier(IceAndFire.MOD_ID, "scribe"), SCRIBE_POI);
        Registry.register(Registries.VILLAGER_PROFESSION, new Identifier(IceAndFire.MOD_ID, "scribe"), SCRIBE);
        addScribeTrades();
    }

    public static void addScribeTrades() {
        final float emeraldForItemsMultiplier = 0.05F; //Values taken from VillagerTrades.java
        final float itemForEmeraldMultiplier = 0.05F;
        final float rareItemForEmeraldMultiplier = 0.2F;
        TradeOfferHelper.registerVillagerOffers(SCRIBE, 1, factories -> factories.addAll(List.of(
                new BuyWithPrice(new ItemStack(Items.EMERALD, 1), new ItemStack(IafItems.MANUSCRIPT, 4), 25, 2, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.BOOKSHELF, 3), new ItemStack(Items.EMERALD, 1), 8, 3, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(Items.PAPER, 15), new ItemStack(Items.EMERALD, 2), 4, 4, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(IafBlocks.ASH, 10), new ItemStack(Items.EMERALD, 1), 8, 4, itemForEmeraldMultiplier))));
        TradeOfferHelper.registerVillagerOffers(SCRIBE, 2, factories -> factories.addAll(List.of(
                new BuyWithPrice(new ItemStack(IafItems.SILVER_INGOT, 5), new ItemStack(Items.EMERALD, 1), 3, 5, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(IafBlocks.FIRE_LILY, 8), new ItemStack(Items.EMERALD, 1), 3, 5, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(IafBlocks.LIGHTNING_LILY, 7), new ItemStack(Items.EMERALD, 3), 2, 5, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 3), new ItemStack(IafBlocks.FROST_LILY, 4), 3, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 2), new ItemStack(IafBlocks.DRAGON_ICE_SPIKES, 7), 2, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(IafItems.SAPPHIRE_GEM), new ItemStack(Items.EMERALD, 2), 30, 3, rareItemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 2), new ItemStack(IafBlocks.JAR_EMPTY, 1), 3, 4, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 2), new ItemStack(IafItems.MYRMEX_DESERT_RESIN, 1), 40, 2, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 2), new ItemStack(IafItems.MYRMEX_JUNGLE_RESIN, 1), 40, 2, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.AMETHYST_SHARD), new ItemStack(Items.EMERALD, 3), 20, 3, rareItemForEmeraldMultiplier))));
        TradeOfferHelper.registerVillagerOffers(SCRIBE, 3, factories -> factories.addAll(List.of(
                new BuyWithPrice(new ItemStack(IafItems.DRAGON_BONE, 6), new ItemStack(Items.EMERALD, 1), 7, 4, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(IafItems.CHAIN, 2), new ItemStack(Items.EMERALD, 3), 4, 2, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 6), new ItemStack(IafItems.PIXIE_DUST, 2), 8, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 6), new ItemStack(IafItems.FIRE_DRAGON_FLESH, 2), 8, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 7), new ItemStack(IafItems.ICE_DRAGON_FLESH, 1), 8, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 8), new ItemStack(IafItems.LIGHTNING_DRAGON_FLESH, 1), 8, 3, emeraldForItemsMultiplier))));
        TradeOfferHelper.registerVillagerOffers(SCRIBE, 4, factories -> factories.addAll(List.of(
                new BuyWithPrice(new ItemStack(Items.EMERALD, 10), new ItemStack(IafItems.DRAGON_BONE, 2), 20, 5, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 4), new ItemStack(IafItems.SHINY_SCALES, 1), 5, 2, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(IafItems.DREAD_SHARD, 5), new ItemStack(Items.EMERALD, 1), 10, 4, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 8), new ItemStack(IafItems.STYMPHALIAN_BIRD_FEATHER, 12), 3, 6, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 4), new ItemStack(IafItems.TROLL_TUSK, 12), 7, 3, emeraldForItemsMultiplier))));
        TradeOfferHelper.registerVillagerOffers(SCRIBE, 5, factories -> factories.addAll(List.of(
                new BuyWithPrice(new ItemStack(Items.EMERALD, 15), new ItemStack(IafItems.SERPENT_FANG, 3), 20, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 12), new ItemStack(IafItems.HYDRA_FANG, 1), 20, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(IafItems.ECTOPLASM, 6), new ItemStack(Items.EMERALD, 1), 7, 3, itemForEmeraldMultiplier))));
    }

    public static void addBuildingToPool(Registry<StructurePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, Identifier poolRL, String nbtPieceRL, int weight) {
        RegistryEntry<StructureProcessorList> villageHouseProcessorList = processorListRegistry.entryOf(IafProcessorLists.HOUSE_PROCESSOR);

        // Grab the pool we want to add to
        StructurePool pool = templatePoolRegistry.get(poolRL);
        if (pool == null) return;

        // Grabs the nbt piece and creates a SinglePoolElement of it that we can add to a structure's pool.
        // Use .legacy( for villages/outposts and .single( for everything else
        SinglePoolElement piece = SinglePoolElement.ofProcessedLegacySingle(nbtPieceRL, villageHouseProcessorList).apply(StructurePool.Projection.RIGID);

        // Use AccessTransformer or Accessor Mixin to make StructureTemplatePool's templates field public for us to see.
        // Weight is handled by how many times the entry appears in this list.
        // We do not need to worry about immutability as this field is created using Lists.newArrayList(); which makes a mutable list.
        for (int i = 0; i < weight; i++)
            pool.elements.add(piece);

        // Use AccessTransformer or Accessor Mixin to make StructureTemplatePool's rawTemplates field public for us to see.
        // This list of pairs of pieces and weights is not used by vanilla by default but another mod may need it for efficiency.
        // So let's add to this list for completeness. We need to make a copy of the array as it can be an immutable list.
        pool.elementCounts.add(new Pair<>(piece, weight));
    }

    private static class BuyWithPrice implements TradeOffers.Factory {
        private final ItemStack input1;
        private final ItemStack input2;
        private final ItemStack output;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public BuyWithPrice(ItemStack input, ItemStack output, int maxUses, int experience, float priceMultiplier) {
            this(input, null, output, maxUses, experience, priceMultiplier);
        }

        public BuyWithPrice(ItemStack input1, ItemStack input2, ItemStack output, int maxUses, int experience, float priceMultiplier) {
            this.input1 = input1;
            this.input2 = input2;
            this.output = output;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = priceMultiplier;
        }

        @Nullable
        @Override
        public TradeOffer create(Entity entity, Random random) {
            if (this.input2 == null)
                return new TradeOffer(this.input1, this.output, this.maxUses, this.experience, this.multiplier);
            return new TradeOffer(this.input1, this.input2, this.output, this.maxUses, this.experience, this.multiplier);
        }
    }
}
