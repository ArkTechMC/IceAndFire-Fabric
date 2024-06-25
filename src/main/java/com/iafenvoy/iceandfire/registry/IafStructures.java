package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.world.structure.GorgonTempleStructure;
import com.iafenvoy.iceandfire.world.structure.GraveyardStructure;
import com.iafenvoy.iceandfire.world.structure.MausoleumStructure;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;

public class IafStructures {
    public static final RegistryKey<Structure> GRAVEYARD = registerKey("graveyard");
    public static final RegistryKey<Structure> MAUSOLEUM = registerKey("mausoleum");
    public static final RegistryKey<Structure> GORGON_TEMPLE = registerKey("gorgon_temple");

    public static RegistryKey<Structure> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.STRUCTURE, new Identifier(IceAndFire.MOD_ID, name));
    }

    public static void bootstrap(Registerable<Structure> context) {
        context.register(GRAVEYARD, GraveyardStructure.buildStructureConfig(context));
        context.register(MAUSOLEUM, MausoleumStructure.buildStructureConfig(context));
        context.register(GORGON_TEMPLE, GorgonTempleStructure.buildStructureConfig(context));
    }
}
