package com.github.alexthe666.citadel;

import com.github.alexthe666.citadel.config.ConfigHolder;
import com.github.alexthe666.citadel.config.ServerConfig;
import com.github.alexthe666.citadel.item.ItemCitadelBook;
import com.github.alexthe666.citadel.item.ItemCitadelDebug;
import com.github.alexthe666.citadel.item.ItemCustomRender;
import com.github.alexthe666.citadel.server.CitadelEvents;
import com.github.alexthe666.citadel.server.block.CitadelLecternBlock;
import com.github.alexthe666.citadel.server.block.CitadelLecternBlockEntity;
import com.github.alexthe666.citadel.server.block.LecternBooks;
import com.github.alexthe666.citadel.server.generation.SpawnProbabilityModifier;
import com.github.alexthe666.citadel.server.generation.VillageHouseManager;
import com.github.alexthe666.citadel.server.world.ExpandedBiomeSource;
import com.github.alexthe666.citadel.server.world.ExpandedBiomes;
import com.mojang.serialization.Codec;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@Mod(Citadel.MOD_ID)
public class Citadel {
    public static final String MOD_ID="citadel";
    public static final Logger LOGGER = LogManager.getLogger("citadel");
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    private static final Identifier PACKET_NETWORK_NAME = new Identifier("citadel:main_channel");
    public static final SimpleChannel NETWORK_WRAPPER = NetworkRegistry.ChannelBuilder
            .named(PACKET_NETWORK_NAME)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
    public static ServerProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public static List<String> PATREONS = new ArrayList<>();
    public static final LazyRegistrar<Item> ITEMS = LazyRegistrar.create(Registries.ITEM, "citadel");
    public static final LazyRegistrar<Block> BLOCKS = LazyRegistrar.create(Registries.BLOCK, "citadel");
    public static final LazyRegistrar<BlockEntityType<?>> BLOCK_ENTITIES = LazyRegistrar.create(Registries.BLOCK_ENTITY_TYPE, "citadel");

    public static final RegistryObject<Item> DEBUG_ITEM = ITEMS.register("debug", () -> new ItemCitadelDebug(new Item.Settings()));
    public static final RegistryObject<Item> CITADEL_BOOK = ITEMS.register("citadel_book", () -> new ItemCitadelBook(new Item.Settings().maxCount(1)));
    public static final RegistryObject<Item> EFFECT_ITEM = ITEMS.register("effect_item", () -> new ItemCustomRender(new Item.Settings().maxCount(1)));
    public static final RegistryObject<Item> FANCY_ITEM = ITEMS.register("fancy_item", () -> new ItemCustomRender(new Item.Settings().maxCount(1)));
    public static final RegistryObject<Item> ICON_ITEM = ITEMS.register("icon_item", () -> new ItemCustomRender(new Item.Settings().maxCount(1)));

    public static final RegistryObject<Block> LECTERN = BLOCKS.register("lectern", () -> new CitadelLecternBlock(AbstractBlock.Settings.copy(Blocks.LECTERN)));

    public static final RegistryObject<BlockEntityType<CitadelLecternBlockEntity>> LECTERN_BE = BLOCK_ENTITIES.register("lectern", () -> BlockEntityType.Builder.create(CitadelLecternBlockEntity::new, LECTERN.get()).build(null));


    public Citadel() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(this::enqueueIMC);
        bus.addListener(this::processIMC);
        bus.addListener(this::doClientStuff);
        bus.addListener(this::onModConfigEvent);
        ITEMS.register(bus);
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
        final DeferredRegister<Codec<? extends BiomeModifier>> serializers = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, "citadel");
        serializers.register(bus);
        serializers.register("mob_spawn_probability", SpawnProbabilityModifier::makeCodec);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(PROXY);
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHolder.SERVER_SPEC);
        MinecraftForge.EVENT_BUS.register(new CitadelEvents());
    }

    public static <MSG> void sendMSGToServer(MSG message) {
        NETWORK_WRAPPER.sendToServer(message);
    }

    public static <MSG> void sendMSGToAll(MSG message) {
        for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            sendNonLocal(message, player);
        }
    }

    public static <MSG> void sendNonLocal(MSG msg, ServerPlayerEntity player) {
        NETWORK_WRAPPER.sendTo(msg, player.networkHandler.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PROXY.onPreInit();
        LecternBooks.init();
    }

    @SubscribeEvent
    public void onModConfigEvent(final ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        // Rebake the configs when they change
        ServerConfig.skipWarnings = ConfigHolder.SERVER.skipDatapackWarnings.get();
        if (config.getSpec() == ConfigHolder.SERVER_SPEC) {
            ServerConfig.citadelEntityTrack = ConfigHolder.SERVER.citadelEntityTracker.get();
            ServerConfig.chunkGenSpawnModifierVal = ConfigHolder.SERVER.chunkGenSpawnModifier.get();
            ServerConfig.aprilFools = ConfigHolder.SERVER.aprilFoolsContent.get();
            //citadelTestBiomeData = SpawnBiomeConfig.create(new ResourceLocation("citadel:config_biome"), CitadelBiomeDefinitions.TERRALITH_TEST);
        }
    }


    private void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> PROXY.onClientInit());
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {

    }

    private void processIMC(final InterModProcessEvent event) {

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onServerAboutToStart(ServerAboutToStartEvent event) {
        DynamicRegistryManager registryAccess = event.getServer().registryAccess();
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
