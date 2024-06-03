package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.SpreadType;
import net.minecraft.world.gen.structure.Structure;

public class IafStructureSets {

    public static final RegistryKey<StructureSet> GRAVEYARD = registerKey("graveyard");
    public static final RegistryKey<StructureSet> MAUSOLEUM = registerKey("mausoleum");
    public static final RegistryKey<StructureSet> GORGON_TEMPLE = registerKey("gorgon_temple");


    private static RegistryKey<StructureSet> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.STRUCTURE_SET, new Identifier(IceAndFire.MOD_ID, name));
    }

    public static void bootstrap(Registerable<StructureSet> context) {
        RegistryEntryLookup<Structure> structures = context.getRegistryLookup(RegistryKeys.STRUCTURE);
        context.register(GRAVEYARD, new StructureSet(structures.getOrThrow(IafStructures.GRAVEYARD), new RandomSpreadStructurePlacement(28, 8, SpreadType.LINEAR, 44712661)));
        context.register(MAUSOLEUM, new StructureSet(structures.getOrThrow(IafStructures.MAUSOLEUM), new RandomSpreadStructurePlacement(32, 12, SpreadType.LINEAR, 14200531)));
        context.register(GORGON_TEMPLE, new StructureSet(structures.getOrThrow(IafStructures.GORGON_TEMPLE), new RandomSpreadStructurePlacement(32, 12, SpreadType.LINEAR, 76489509)));
    }
}
