package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.datagen.tags.*;
import net.minecraft.MinecraftVersion;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataOutput;
import net.minecraft.data.MetadataProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.text.Text;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;


@Mod.EventBusSubscriber(modid = IceAndFire.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        DataOutput output = event.getGenerator().getPackOutput();
        CompletableFuture<RegistryWrapper.WrapperLookup> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        DatapackBuiltinEntriesProvider datapackProvider = new RegistryDataGenerator(output, provider);
        CompletableFuture<RegistryWrapper.WrapperLookup> lookupProvider = datapackProvider.getRegistryProvider();
        generator.addProvider(event.includeServer(), datapackProvider);
        generator.addProvider(event.includeServer(), new BannerPatternTagGenerator(output, provider));
        generator.addProvider(event.includeServer(), new POITagGenerator(output, provider));
        generator.addProvider(true, new MetadataProvider(output).add(PackResourceMetadata.SERIALIZER, new PackResourceMetadata(
                Text.literal("Resources for Ice and Fire"),
                MinecraftVersion.CURRENT.getResourceVersion(ResourceType.CLIENT_RESOURCES),
                Arrays.stream(ResourceType.values()).collect(Collectors.toMap(Function.identity(), MinecraftVersion.CURRENT::getResourceVersion)))));
        generator.addProvider(event.includeServer(), new IafBiomeTagGenerator(output, lookupProvider));
        generator.addProvider(event.includeClient(), new AtlasGenerator(output, helper));
        BlockTagsProvider blocktags  = new IafBlockTags(output, provider, helper);
        generator.addProvider(event.includeServer(), blocktags);
        generator.addProvider(event.includeServer(), new IafItemTags(output, provider, blocktags.contentsGetter(), helper));
        generator.addProvider(event.includeServer(), new IafEntityTags(output, provider));
        generator.addProvider(event.includeServer(), new IafRecipes(output));

    }
}
