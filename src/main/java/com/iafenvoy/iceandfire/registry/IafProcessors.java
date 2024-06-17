package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.world.gen.processor.DreadRuinProcessor;
import com.iafenvoy.iceandfire.world.gen.processor.GorgonTempleProcessor;
import com.iafenvoy.iceandfire.world.gen.processor.GraveyardProcessor;
import com.iafenvoy.iceandfire.world.gen.processor.VillageHouseProcessor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Identifier;

public class IafProcessors {
    public static final StructureProcessorType<DreadRuinProcessor> DREADRUINPROCESSOR = registerProcessor("dread_mausoleum_processor", () -> DreadRuinProcessor.CODEC);
    public static final StructureProcessorType<GorgonTempleProcessor> GORGONTEMPLEPROCESSOR = registerProcessor("gorgon_temple_processor", () -> GorgonTempleProcessor.CODEC);
    public static final StructureProcessorType<GraveyardProcessor> GRAVEYARDPROCESSOR = registerProcessor("graveyard_processor", () -> GraveyardProcessor.CODEC);
    public static final StructureProcessorType<VillageHouseProcessor> VILLAGEHOUSEPROCESSOR = registerProcessor("village_house_processor", () -> VillageHouseProcessor.CODEC);

    private static <P extends StructureProcessor> StructureProcessorType<P> registerProcessor(String name, StructureProcessorType<P> processor) {
        return Registry.register(Registries.STRUCTURE_PROCESSOR, new Identifier(IceAndFire.MOD_ID, name), processor);
    }

    public static void init() {
    }
}
