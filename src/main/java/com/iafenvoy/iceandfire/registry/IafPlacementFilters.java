package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.world.CustomBiomeFilter;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

public class IafPlacementFilters {
    public static final PlacementModifierType<CustomBiomeFilter> CUSTOM_BIOME_FILTER = register("biome_extended", () -> CustomBiomeFilter.CODEC);

    public static <T extends PlacementModifier> PlacementModifierType<T> register(String name, PlacementModifierType<T> type) {
        return Registry.register(Registries.PLACEMENT_MODIFIER_TYPE, new Identifier(IceAndFire.MOD_ID, name), type);
    }

    public static void init() {
    }
}
