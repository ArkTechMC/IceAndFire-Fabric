package com.iafenvoy.iceandfire.util.trade.profession;

import com.google.common.collect.ImmutableSet;
import com.iafenvoy.iceandfire.mixin.PointOfInterestTypesAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Utilities to help with registration of villager profession.
 */
public class ProfessionRegistry {
    private static final TagKey<PointOfInterestType> ACTIVATE_TAG = TagKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, new Identifier("acquirable_job_site"));

    /**
     * Registers village profession.
     *
     * @param id             the registration id
     * @param ticketCount    count for the max village of a workstation
     * @param searchDistance villager search distance
     * @param soundEvent     villager work sound
     * @param workStations   workstation block(s)
     * @return The registered village profession
     */
    public static ProfessionHolder register(Identifier id, int ticketCount, int searchDistance, SoundEvent soundEvent, Block... workStations) {
        List<BlockState> states = Arrays.stream(workStations).flatMap(x -> x.getStateManager().getStates().stream()).toList();
        PointOfInterestType type = Registry.register(Registries.POINT_OF_INTEREST_TYPE, id, new PointOfInterestType(ImmutableSet.copyOf(states), ticketCount, searchDistance));
        RegistryEntry<PointOfInterestType> entry = Registries.POINT_OF_INTEREST_TYPE.getEntry(type);
        for (BlockState state : states) PointOfInterestTypesAccessor.getPoiToStatesMap().put(state, entry);
        Optional<RegistryKey<PointOfInterestType>> optional = entry.getKey();
        if (optional.isEmpty()) return null;// Should not happen
        RegistryKey<PointOfInterestType> key = optional.get();
        VillagerProfession profession = Registry.register(Registries.VILLAGER_PROFESSION, id, new VillagerProfession(id.getPath(), e -> e.matchesKey(key), e -> e.matchesKey(key), ImmutableSet.of(), ImmutableSet.of(), soundEvent));
        return new ProfessionHolder(type, profession);
    }

    /**
     * Registers village profession.
     *
     * @param id           the registration id
     * @param soundEvent   villager work sound
     * @param workStations workstation block(s)
     * @return The registered village profession
     */
    public static ProfessionHolder register(Identifier id, SoundEvent soundEvent, Block... workStations) {
        return register(id, 1, 1, soundEvent, workStations);
    }
}
