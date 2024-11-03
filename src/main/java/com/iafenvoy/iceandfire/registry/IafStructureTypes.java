package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.world.structure.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public final class IafStructureTypes {
    public static final StructureType<GraveyardStructure> GRAVEYARD = registerType("graveyard", () -> GraveyardStructure.ENTRY_CODEC);
    public static final StructureType<MausoleumStructure> MAUSOLEUM = registerType("mausoleum", () -> MausoleumStructure.ENTRY_CODEC);
    public static final StructureType<GorgonTempleStructure> GORGON_TEMPLE = registerType("gorgon_temple", () -> GorgonTempleStructure.ENTRY_CODEC);
    public static final StructureType<FireDragonCaveStructure> FIRE_DRAGON_CAVE = registerType("fire_dragon_cave", () -> FireDragonCaveStructure.ENTRY_CODEC);
    public static final StructureType<IceDragonCaveStructure> ICE_DRAGON_CAVE = registerType("ice_dragon_cave", () -> IceDragonCaveStructure.ENTRY_CODEC);
    public static final StructureType<LightningDragonCaveStructure> LIGHTNING_DRAGON_CAVE = registerType("lightning_dragon_cave", () -> LightningDragonCaveStructure.ENTRY_CODEC);

    private static <P extends Structure> StructureType<P> registerType(String name, StructureType<P> factory) {
        return Registry.register(Registries.STRUCTURE_TYPE, new Identifier(IceAndFire.MOD_ID, name), factory);
    }

    public static void init() {
    }
}
