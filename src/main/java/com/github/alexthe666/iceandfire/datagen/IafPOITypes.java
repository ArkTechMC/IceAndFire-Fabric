package com.github.alexthe666.iceandfire.datagen;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;

public class IafPOITypes {

    public static final RegistryKey<PointOfInterestType> SCRIBE_POI = registerKey("scribe");

    public static RegistryKey<PointOfInterestType> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, new Identifier(IceAndFire.MOD_ID, name));
    }
}
