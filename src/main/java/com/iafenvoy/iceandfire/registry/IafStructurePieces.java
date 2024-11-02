package com.iafenvoy.iceandfire.registry;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.world.structure.DragonCaveStructure;
import com.iafenvoy.uranus.object.IdUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;

import java.util.Locale;

public final class IafStructurePieces {
    public static final StructurePieceType DRAGON_CAVE = register(DragonCaveStructure.DragonCavePiece::new, "dragon_cave");

    private static StructurePieceType register(StructurePieceType type, String id) {
        return Registry.register(Registries.STRUCTURE_PIECE, id.toLowerCase(Locale.ROOT), type);
    }

    public static void init() {
    }
}
