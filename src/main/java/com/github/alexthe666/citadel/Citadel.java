package com.github.alexthe666.citadel;

import com.github.alexthe666.citadel.config.ConfigHolder;
import com.github.alexthe666.citadel.config.ServerConfig;
import com.github.alexthe666.citadel.item.ItemCitadelBook;
import com.github.alexthe666.citadel.item.ItemCitadelDebug;
import com.github.alexthe666.citadel.item.ItemCustomRender;
import com.github.alexthe666.citadel.server.block.CitadelLecternBlock;
import com.github.alexthe666.citadel.server.block.CitadelLecternBlockEntity;
import com.github.alexthe666.citadel.server.generation.VillageHouseManager;
import com.github.alexthe666.citadel.server.world.ExpandedBiomeSource;
import com.github.alexthe666.citadel.server.world.ExpandedBiomes;
import dev.arktechmc.iafextra.StaticVariables;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Citadel {
    public static final String MOD_ID = "citadel";
    public static final Logger LOGGER = LogManager.getLogger("citadel");
    public static final LazyRegistrar<Item> ITEMS = LazyRegistrar.create(Registries.ITEM, "citadel");
    public static final LazyRegistrar<Block> BLOCKS = LazyRegistrar.create(Registries.BLOCK, "citadel");
    public static final LazyRegistrar<BlockEntityType<?>> BLOCK_ENTITIES = LazyRegistrar.create(Registries.BLOCK_ENTITY_TYPE, "citadel");
    public static final RegistryObject<Item> DEBUG_ITEM = ITEMS.register("debug", () -> new ItemCitadelDebug(new Item.Settings()));
    public static final RegistryObject<Item> CITADEL_BOOK = ITEMS.register("citadel_book", () -> new ItemCitadelBook(new Item.Settings().maxCount(1)));
    public static final RegistryObject<Item> EFFECT_ITEM = ITEMS.register("effect_item", () -> new ItemCustomRender(new Item.Settings().maxCount(1)));
    public static final RegistryObject<Item> FANCY_ITEM = ITEMS.register("fancy_item", () -> new ItemCustomRender(new Item.Settings().maxCount(1)));
    public static final RegistryObject<Item> ICON_ITEM = ITEMS.register("icon_item", () -> new ItemCustomRender(new Item.Settings().maxCount(1)));
    public static final RegistryObject<Block> LECTERN = BLOCKS.register("lectern", () -> new CitadelLecternBlock(AbstractBlock.Settings.copy(Blocks.LECTERN)));

    public Citadel() {
        this.onModConfigEvent();
    }

    public static final RegistryObject<BlockEntityType<CitadelLecternBlockEntity>> LECTERN_BE = BLOCK_ENTITIES.register("lectern", () -> BlockEntityType.Builder.create(CitadelLecternBlockEntity::new, LECTERN.get()).build(null));

    public void onModConfigEvent() {
        // Rebake the configs when they change
        ServerConfig.skipWarnings = ConfigHolder.SERVER.skipDatapackWarnings.get();
        ServerConfig.citadelEntityTrack = ConfigHolder.SERVER.citadelEntityTracker.get();
        ServerConfig.chunkGenSpawnModifierVal = ConfigHolder.SERVER.chunkGenSpawnModifier.get();
        //citadelTestBiomeData = SpawnBiomeConfig.create(new ResourceLocation("citadel:config_biome"), CitadelBiomeDefinitions.TERRALITH_TEST);
    }

    public static void onServerAboutToStart() {
        DynamicRegistryManager registryAccess = StaticVariables.server.getRegistryManager();
        VillageHouseManager.addAllHouses(registryAccess);
        Registry<Biome> allBiomes = registryAccess.get(RegistryKeys.BIOME);
        Registry<DimensionOptions> levelStems = registryAccess.get(RegistryKeys.DIMENSION);
        Map<RegistryKey<Biome>, RegistryEntry<Biome>> biomeMap = new HashMap<>();
        for (RegistryKey<Biome> biomeResourceKey : allBiomes.getKeys()) {
            Optional<RegistryEntry.Reference<Biome>> holderOptional = allBiomes.getEntry(biomeResourceKey);
            holderOptional.ifPresent(biomeHolder -> biomeMap.put(biomeResourceKey, biomeHolder));
        }
        for (RegistryKey<DimensionOptions> levelStemResourceKey : levelStems.getKeys()) {
            Optional<RegistryEntry.Reference<DimensionOptions>> holderOptional = levelStems.getEntry(levelStemResourceKey);
            if (holderOptional.isPresent() && holderOptional.get().value().chunkGenerator().getBiomeSource() instanceof ExpandedBiomeSource expandedBiomeSource) {
                expandedBiomeSource.setResourceKeyMap(biomeMap);
                Set<RegistryEntry<Biome>> biomeHolders = ExpandedBiomes.buildBiomeList(registryAccess, levelStemResourceKey);
                expandedBiomeSource.expandBiomesWith(biomeHolders);
            }
        }
    }


}
