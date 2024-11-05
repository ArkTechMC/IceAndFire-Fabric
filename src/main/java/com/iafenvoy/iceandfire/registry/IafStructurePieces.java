package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.world.structure.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.StructurePieceType;

import java.util.Locale;

public final class IafStructurePieces {
    public static final StructurePieceType FIRE_DRAGON_ROOST = register(FireDragonRoostStructure.FireDragonRoostPiece::new, "fire_dragon_roost");
    public static final StructurePieceType ICE_DRAGON_ROOST = register(IceDragonRoostStructure.IceDragonRoostPiece::new, "ice_dragon_roost");
    public static final StructurePieceType LIGHTNING_DRAGON_ROOST = register(LightningDragonRoostStructure.LightningDragonRoostPiece::new, "lightning_dragon_roost");
    public static final StructurePieceType FIRE_DRAGON_CAVE = register(FireDragonCaveStructure.FireDragonCavePiece::new, "fire_dragon_cave");
    public static final StructurePieceType ICE_DRAGON_CAVE = register(IceDragonCaveStructure.IceDragonCavePiece::new, "ice_dragon_cave");
    public static final StructurePieceType LIGHTNING_DRAGON_CAVE = register(LightningDragonCaveStructure.LightningDragonCavePiece::new, "lightning_dragon_cave");
    public static final StructurePieceType MYRMEX_HIVE = register(MyrmexHiveStructure.MyrmexHivePiece::new, "myrmex_hive");

    private static StructurePieceType register(StructurePieceType type, String id) {
        return Registry.register(Registries.STRUCTURE_PIECE, id.toLowerCase(Locale.ROOT), type);
    }

    public static void init() {
    }
}
