package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.world.structure.DragonCaveStructure;
import com.iafenvoy.iceandfire.world.structure.GorgonTempleStructure;
import com.iafenvoy.iceandfire.world.structure.GraveyardStructure;
import com.iafenvoy.iceandfire.world.structure.MausoleumStructure;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public final class IafStructureTypes {
    public static final StructureType<GraveyardStructure> GRAVEYARD = registerType("graveyard", () -> GraveyardStructure.ENTRY_CODEC);
    public static final StructureType<MausoleumStructure> MAUSOLEUM = registerType("mausoleum", () -> MausoleumStructure.ENTRY_CODEC);
    public static final StructureType<GorgonTempleStructure> GORGON_TEMPLE = registerType("gorgon_temple", () -> GorgonTempleStructure.ENTRY_CODEC);
    public static final StructureType<DragonCaveStructure> DRAGON_CAVE = registerType("dragon_cave", () -> DragonCaveStructure.ENTRY_CODEC);

    private static <P extends Structure> StructureType<P> registerType(String name, StructureType<P> factory) {
        return Registry.register(Registries.STRUCTURE_TYPE, new Identifier(IceAndFire.MOD_ID, name), factory);
    }

    public static void init() {
    }
}
