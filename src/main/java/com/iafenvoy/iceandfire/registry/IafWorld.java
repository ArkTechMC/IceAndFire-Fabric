package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public final class IafWorld {
    public static final RegistryKey<World> DREAD_LAND = RegistryKey.of(RegistryKeys.WORLD, new Identifier(IceAndFire.MOD_ID, "dread_land"));
}
