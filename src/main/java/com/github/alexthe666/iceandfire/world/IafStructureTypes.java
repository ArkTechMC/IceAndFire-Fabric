package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.structure.GorgonTempleStructure;
import com.github.alexthe666.iceandfire.world.structure.GraveyardStructure;
import com.github.alexthe666.iceandfire.world.structure.MausoleumStructure;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.function.Supplier;

public class IafStructureTypes {
    public static final LazyRegistrar<StructureType<?>> STRUCTURE_TYPES = LazyRegistrar.create(RegistryKeys.STRUCTURE_TYPE, IceAndFire.MOD_ID);

    public static final RegistryObject<StructureType<GraveyardStructure>> GRAVEYARD = registerType("graveyard", () -> () -> GraveyardStructure.ENTRY_CODEC);
    public static final RegistryObject<StructureType<MausoleumStructure>> MAUSOLEUM = registerType("mausoleum", () -> () -> MausoleumStructure.ENTRY_CODEC);
    public static final RegistryObject<StructureType<GorgonTempleStructure>> GORGON_TEMPLE = registerType("gorgon_temple", () -> () -> GorgonTempleStructure.ENTRY_CODEC);

    private static <P extends Structure> RegistryObject<StructureType<P>> registerType(String name, Supplier<StructureType<P>> factory) {
        return STRUCTURE_TYPES.register(name, factory);
    }
}
