package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.fabric.trade.villager.offer.TradeOfferHelper;
import com.iafenvoy.fabric.trade.villager.offer.factory.BuyWithPrice;
import com.iafenvoy.fabric.trade.villager.profession.ProfessionHolder;
import com.iafenvoy.fabric.trade.villager.profession.ProfessionRegistry;
import com.iafenvoy.iceandfire.IceAndFire;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;

public final class IafTrades {
    public static final ProfessionHolder SCRIBE = ProfessionRegistry.register(new Identifier(IceAndFire.MOD_ID, "scribe"), SoundEvents.ENTITY_VILLAGER_WORK_LIBRARIAN, IafBlocks.LECTERN);

    public static void init() {
        addScribeTrades();
    }

    public static void addScribeTrades() {
        assert SCRIBE != null;
        VillagerProfession profession = SCRIBE.profession();
        final float emeraldForItemsMultiplier = 0.05F; //Values taken from VillagerTrades.java
        final float itemForEmeraldMultiplier = 0.05F;
        final float rareItemForEmeraldMultiplier = 0.2F;
        TradeOfferHelper.registerVillagerOffers(profession, 1,
                new BuyWithPrice(new ItemStack(Items.EMERALD, 1), new ItemStack(IafItems.MANUSCRIPT, 4), 25, 2, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.BOOKSHELF, 3), new ItemStack(Items.EMERALD, 1), 8, 3, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(Items.PAPER, 15), new ItemStack(Items.EMERALD, 2), 4, 4, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(IafBlocks.ASH, 10), new ItemStack(Items.EMERALD, 1), 8, 4, itemForEmeraldMultiplier));
        TradeOfferHelper.registerVillagerOffers(profession, 2,
                new BuyWithPrice(new ItemStack(IafItems.SILVER_INGOT, 5), new ItemStack(Items.EMERALD, 1), 3, 5, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(IafBlocks.FIRE_LILY, 8), new ItemStack(Items.EMERALD, 1), 3, 5, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(IafBlocks.LIGHTNING_LILY, 7), new ItemStack(Items.EMERALD, 3), 2, 5, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 3), new ItemStack(IafBlocks.FROST_LILY, 4), 3, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 2), new ItemStack(IafBlocks.DRAGON_ICE_SPIKES, 7), 2, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(IafItems.SAPPHIRE_GEM), new ItemStack(Items.EMERALD, 2), 30, 3, rareItemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 2), new ItemStack(IafBlocks.JAR_EMPTY, 1), 3, 4, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 2), new ItemStack(IafItems.MYRMEX_DESERT_RESIN, 1), 40, 2, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 2), new ItemStack(IafItems.MYRMEX_JUNGLE_RESIN, 1), 40, 2, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.AMETHYST_SHARD), new ItemStack(Items.EMERALD, 3), 20, 3, rareItemForEmeraldMultiplier));
        TradeOfferHelper.registerVillagerOffers(profession, 3,
                new BuyWithPrice(new ItemStack(IafItems.DRAGON_BONE, 6), new ItemStack(Items.EMERALD, 1), 7, 4, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(IafItems.CHAIN, 2), new ItemStack(Items.EMERALD, 3), 4, 2, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 6), new ItemStack(IafItems.PIXIE_DUST, 2), 8, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 6), new ItemStack(IafItems.FIRE_DRAGON_FLESH, 2), 8, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 7), new ItemStack(IafItems.ICE_DRAGON_FLESH, 1), 8, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 8), new ItemStack(IafItems.LIGHTNING_DRAGON_FLESH, 1), 8, 3, emeraldForItemsMultiplier));
        TradeOfferHelper.registerVillagerOffers(profession, 4,
                new BuyWithPrice(new ItemStack(Items.EMERALD, 10), new ItemStack(IafItems.DRAGON_BONE, 2), 20, 5, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 4), new ItemStack(IafItems.SHINY_SCALES, 1), 5, 2, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(IafItems.DREAD_SHARD, 5), new ItemStack(Items.EMERALD, 1), 10, 4, itemForEmeraldMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 8), new ItemStack(IafItems.STYMPHALIAN_BIRD_FEATHER, 12), 3, 6, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 4), new ItemStack(IafItems.TROLL_TUSK, 12), 7, 3, emeraldForItemsMultiplier));
        TradeOfferHelper.registerVillagerOffers(profession, 5,
                new BuyWithPrice(new ItemStack(Items.EMERALD, 15), new ItemStack(IafItems.SERPENT_FANG, 3), 20, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(Items.EMERALD, 12), new ItemStack(IafItems.HYDRA_FANG, 1), 20, 3, emeraldForItemsMultiplier),
                new BuyWithPrice(new ItemStack(IafItems.ECTOPLASM, 6), new ItemStack(Items.EMERALD, 1), 7, 3, itemForEmeraldMultiplier));
    }
}
