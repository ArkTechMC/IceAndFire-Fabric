package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;

public final class IafStructures {
    public static final RegistryKey<Structure> GRAVEYARD = registerKey("graveyard");
    public static final RegistryKey<Structure> MAUSOLEUM = registerKey("mausoleum");
    public static final RegistryKey<Structure> GORGON_TEMPLE = registerKey("gorgon_temple");

    public static RegistryKey<Structure> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.STRUCTURE, new Identifier(IceAndFire.MOD_ID, name));
    }

    public static void init() {
    }
}
