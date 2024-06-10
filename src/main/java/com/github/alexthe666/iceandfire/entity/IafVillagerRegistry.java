package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.datagen.IafProcessorLists;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
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

public class IafVillagerRegistry {
    public static final LazyRegistrar<PointOfInterestType> POI_TYPES = LazyRegistrar.create(Registries.POINT_OF_INTEREST_TYPE, IceAndFire.MOD_ID);
    public static final RegistryObject<PointOfInterestType> SCRIBE_POI = POI_TYPES.register("scribe", () -> new PointOfInterestType(ImmutableSet.copyOf(IafBlockRegistry.LECTERN.get().getStateManager().getStates()), 1, 1));
    public static final LazyRegistrar<VillagerProfession> PROFESSIONS = LazyRegistrar.create(Registries.VILLAGER_PROFESSION, IceAndFire.MOD_ID);
    public static final RegistryObject<VillagerProfession> SCRIBE = PROFESSIONS.register("scribe", () -> new VillagerProfession("scribe", (entry) -> entry.value().equals(SCRIBE_POI.get()), (entry) -> entry.value().equals(SCRIBE_POI.get()), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.ENTITY_VILLAGER_WORK_LIBRARIAN));

    public static void addScribeTrades() {

        final float emeraldForItemsMultiplier = 0.05F; //Values taken from VillagerTrades.java
        final float itemForEmeraldMultiplier = 0.05F;
        final float rareItemForEmeraldMultiplier = 0.2F;
        TradeOfferHelper.registerVillagerOffers(SCRIBE.get(), 1, factories -> factories.addAll(List.of(
                new BuyWithPrice(new ItemStack(Items.EMERALD, 1), new ItemStack(IafItemRegistry.MANUSCRIPT.get(), 4), 25, 2, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.BOOKSHELF, 3), new ItemStack(Items.EMERALD, 1), 8, 3, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(Items.PAPER, 15), new ItemStack(Items.EMERALD, 2), 4, 4, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(IafBlockRegistry.ASH.get(), 10), new ItemStack(Items.EMERALD, 1), 8, 4, itemForEmeraldMultiplier)
        )));
        TradeOfferHelper.registerVillagerOffers(SCRIBE.get(), 2, factories -> factories.addAll(List.of(
                new BuyWithPrice(new ItemStack(IafItemRegistry.SILVER_INGOT.get(), 5), new ItemStack(Items.EMERALD, 1), 3, 5, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(IafBlockRegistry.FIRE_LILY.get(), 8), new ItemStack(Items.EMERALD, 1), 3, 5, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(IafBlockRegistry.LIGHTNING_LILY.get(), 7), new ItemStack(Items.EMERALD, 3), 2, 5, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 3), new ItemStack(IafBlockRegistry.FROST_LILY.get(), 4), 3, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 2), new ItemStack(IafBlockRegistry.DRAGON_ICE_SPIKES.get(), 7), 2, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(IafItemRegistry.SAPPHIRE_GEM.get()), new ItemStack(Items.EMERALD, 2), 30, 3, rareItemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 2), new ItemStack(IafBlockRegistry.JAR_EMPTY.get(), 1), 3, 4, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 2), new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN.get(), 1), 40, 2, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 2), new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN.get(), 1), 40, 2, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.AMETHYST_SHARD), new ItemStack(Items.EMERALD, 3), 20, 3, rareItemForEmeraldMultiplier)
        )));
        TradeOfferHelper.registerVillagerOffers(SCRIBE.get(), 3, factories -> factories.addAll(List.of(
                new BuyWithPrice(new ItemStack(IafItemRegistry.DRAGON_BONE.get(), 6), new ItemStack(Items.EMERALD, 1), 7, 4, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(IafItemRegistry.CHAIN.get(), 2), new ItemStack(Items.EMERALD, 3), 4, 2, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 6), new ItemStack(IafItemRegistry.PIXIE_DUST.get(), 2), 8, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 6), new ItemStack(IafItemRegistry.FIRE_DRAGON_FLESH.get(), 2), 8, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 7), new ItemStack(IafItemRegistry.ICE_DRAGON_FLESH.get(), 1), 8, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 8), new ItemStack(IafItemRegistry.LIGHTNING_DRAGON_FLESH.get(), 1), 8, 3, emeraldForItemsMultiplier)
        )));
        TradeOfferHelper.registerVillagerOffers(SCRIBE.get(), 4, factories -> factories.addAll(List.of(
                new BuyWithPrice(new ItemStack(Items.EMERALD, 10), new ItemStack(IafItemRegistry.DRAGON_BONE.get(), 2), 20, 5, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 4), new ItemStack(IafItemRegistry.SHINY_SCALES.get(), 1), 5, 2, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(IafItemRegistry.DREAD_SHARD.get(), 5), new ItemStack(Items.EMERALD, 1), 10, 4, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 8), new ItemStack(IafItemRegistry.STYMPHALIAN_BIRD_FEATHER.get(), 12), 3, 6, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 4), new ItemStack(IafItemRegistry.TROLL_TUSK.get(), 12), 7, 3, emeraldForItemsMultiplier)
        )));
        TradeOfferHelper.registerVillagerOffers(SCRIBE.get(), 5, factories -> factories.addAll(List.of(
                new BuyWithPrice(new ItemStack(Items.EMERALD, 15), new ItemStack(IafItemRegistry.SERPENT_FANG.get(), 3), 20, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 12), new ItemStack(IafItemRegistry.HYDRA_FANG.get(), 1), 20, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(IafItemRegistry.ECTOPLASM.get(), 6), new ItemStack(Items.EMERALD, 1), 7, 3, itemForEmeraldMultiplier)
        )));

    }

    public static void addBuildingToPool(Registry<StructurePool> templatePoolRegistry,
                                         Registry<StructureProcessorList> processorListRegistry,
                                         Identifier poolRL,
                                         String nbtPieceRL,
                                         int weight) {

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
        for (int i = 0; i < weight; i++) {
            pool.elements.add(piece);
        }

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
