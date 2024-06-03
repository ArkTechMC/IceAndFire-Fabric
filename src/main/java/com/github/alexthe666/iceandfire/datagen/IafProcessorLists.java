package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.gen.processor.DreadRuinProcessor;
import com.github.alexthe666.iceandfire.world.gen.processor.GorgonTempleProcessor;
import com.github.alexthe666.iceandfire.world.gen.processor.GraveyardProcessor;
import com.github.alexthe666.iceandfire.world.gen.processor.VillageHouseProcessor;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.processor.RuleStructureProcessor;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorRule;
import net.minecraft.structure.rule.AlwaysTrueRuleTest;
import net.minecraft.structure.rule.RandomBlockMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.List;

public class IafProcessorLists {
    public static final RegistryKey<StructureProcessorList> GRAVEYARD_PROCESSORS = createKey("graveyard_processors");
    public static final RegistryKey<StructureProcessorList> MAUSOLEUM_PROCESSORS = createKey("mausoleum_processors");
    public static final RegistryKey<StructureProcessorList> GORGON_TEMPLE_PROCESSORS = createKey("gorgon_temple_processors");
    public static final RegistryKey<StructureProcessorList> HOUSE_PROCESSOR = createKey("village_house_processor");

    private static RegistryKey<StructureProcessorList> createKey(String name) {
        return RegistryKey.of(RegistryKeys.PROCESSOR_LIST, new Identifier(IceAndFire.MOD_ID, name));
    }

    private static void register(Registerable<StructureProcessorList> pContext, RegistryKey<StructureProcessorList> pKey, List<StructureProcessor> pProcessors) {
        pContext.register(pKey, new StructureProcessorList(pProcessors));
    }

    public static void bootstrap(Registerable<StructureProcessorList> pContext) {
        register(pContext, GRAVEYARD_PROCESSORS, ImmutableList.of(GraveyardProcessor.INSTANCE));
        register(pContext, MAUSOLEUM_PROCESSORS, ImmutableList.of(DreadRuinProcessor.INSTANCE));
        register(pContext, GORGON_TEMPLE_PROCESSORS, ImmutableList.of(GorgonTempleProcessor.INSTANCE));

        RuleStructureProcessor mossify = new RuleStructureProcessor(ImmutableList.of(new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.COBBLESTONE, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.MOSSY_COBBLESTONE.getDefaultState())));
        register(pContext, HOUSE_PROCESSOR, ImmutableList.of(mossify, VillageHouseProcessor.INSTANCE));
    }
}
