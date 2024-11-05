package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.world.structure.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public final class IafStructureTypes {
    public static final StructureType<GraveyardStructure> GRAVEYARD = registerType("graveyard", () -> GraveyardStructure.CODEC);
    public static final StructureType<MausoleumStructure> MAUSOLEUM = registerType("mausoleum", () -> MausoleumStructure.CODEC);
    public static final StructureType<GorgonTempleStructure> GORGON_TEMPLE = registerType("gorgon_temple", () -> GorgonTempleStructure.CODEC);
    public static final StructureType<FireDragonRoostStructure> FIRE_DRAGON_ROOST = registerType("fire_dragon_roost", () -> FireDragonRoostStructure.CODEC);
    public static final StructureType<IceDragonRoostStructure> ICE_DRAGON_ROOST = registerType("ice_dragon_roost", () -> IceDragonRoostStructure.CODEC);
    public static final StructureType<LightningDragonRoostStructure> LIGHTNING_DRAGON_ROOST = registerType("lightning_dragon_roost", () -> LightningDragonRoostStructure.CODEC);
    public static final StructureType<FireDragonCaveStructure> FIRE_DRAGON_CAVE = registerType("fire_dragon_cave", () -> FireDragonCaveStructure.CODEC);
    public static final StructureType<IceDragonCaveStructure> ICE_DRAGON_CAVE = registerType("ice_dragon_cave", () -> IceDragonCaveStructure.CODEC);
    public static final StructureType<LightningDragonCaveStructure> LIGHTNING_DRAGON_CAVE = registerType("lightning_dragon_cave", () -> LightningDragonCaveStructure.CODEC);
    public static final StructureType<MyrmexHiveStructure> MYRMEX_HIVE = registerType("myrmex_hive", () -> MyrmexHiveStructure.CODEC);

    private static <P extends Structure> StructureType<P> registerType(String name, StructureType<P> factory) {
        return Registry.register(Registries.STRUCTURE_TYPE, new Identifier(IceAndFire.MOD_ID, name), factory);
    }

    public static void init() {
    }
}
