package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.entity.block.*;
import com.iafenvoy.iceandfire.render.block.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class IafBlockEntities {
    public static final BlockEntityType<BlockEntityEggInIce> EGG_IN_ICE = register(BlockEntityType.Builder.create(BlockEntityEggInIce::new, IafBlocks.EGG_IN_ICE), "egginice");
    public static final BlockEntityType<BlockEntityPixieHouse> PIXIE_HOUSE = register(BlockEntityType.Builder.create(BlockEntityPixieHouse::new, IafBlocks.PIXIE_HOUSE_MUSHROOM_RED, IafBlocks.PIXIE_HOUSE_MUSHROOM_BROWN, IafBlocks.PIXIE_HOUSE_OAK, IafBlocks.PIXIE_HOUSE_BIRCH, IafBlocks.PIXIE_HOUSE_BIRCH, IafBlocks.PIXIE_HOUSE_SPRUCE, IafBlocks.PIXIE_HOUSE_DARK_OAK), "pixie_house");
    public static final BlockEntityType<BlockEntityJar> PIXIE_JAR = register(BlockEntityType.Builder.create(BlockEntityJar::new, IafBlocks.JAR_EMPTY, IafBlocks.JAR_PIXIE_0, IafBlocks.JAR_PIXIE_1, IafBlocks.JAR_PIXIE_2, IafBlocks.JAR_PIXIE_3, IafBlocks.JAR_PIXIE_4), "pixie_jar");
    public static final BlockEntityType<BlockEntityMyrmexCocoon> MYRMEX_COCOON = register(BlockEntityType.Builder.create(BlockEntityMyrmexCocoon::new, IafBlocks.DESERT_MYRMEX_COCOON, IafBlocks.JUNGLE_MYRMEX_COCOON), "myrmex_cocoon");
    public static final BlockEntityType<BlockEntityDragonForge> DRAGONFORGE_CORE = register(BlockEntityType.Builder.create(BlockEntityDragonForge::new, IafBlocks.DRAGONFORGE_FIRE_CORE, IafBlocks.DRAGONFORGE_ICE_CORE, IafBlocks.DRAGONFORGE_FIRE_CORE_DISABLED, IafBlocks.DRAGONFORGE_ICE_CORE_DISABLED, IafBlocks.DRAGONFORGE_LIGHTNING_CORE, IafBlocks.DRAGONFORGE_LIGHTNING_CORE_DISABLED), "dragonforge_core");
    public static final BlockEntityType<BlockEntityDragonForgeBrick> DRAGONFORGE_BRICK = register(BlockEntityType.Builder.create(BlockEntityDragonForgeBrick::new, IafBlocks.DRAGONFORGE_FIRE_BRICK, IafBlocks.DRAGONFORGE_ICE_BRICK, IafBlocks.DRAGONFORGE_LIGHTNING_BRICK), "dragonforge_brick");
    public static final BlockEntityType<BlockEntityDragonForgeInput> DRAGONFORGE_INPUT = register(BlockEntityType.Builder.create(BlockEntityDragonForgeInput::new, IafBlocks.DRAGONFORGE_FIRE_INPUT, IafBlocks.DRAGONFORGE_ICE_INPUT, IafBlocks.DRAGONFORGE_LIGHTNING_INPUT), "dragonforge_input");
    public static final BlockEntityType<BlockEntityDreadPortal> DREAD_PORTAL = register(BlockEntityType.Builder.create(BlockEntityDreadPortal::new, IafBlocks.DREAD_PORTAL), "dread_portal");
    public static final BlockEntityType<BlockEntityDreadSpawner> DREAD_SPAWNER = register(BlockEntityType.Builder.create(BlockEntityDreadSpawner::new, IafBlocks.DREAD_SPAWNER), "dread_spawner");
    public static final BlockEntityType<BlockEntityGhostChest> GHOST_CHEST = register(BlockEntityType.Builder.create(BlockEntityGhostChest::new, IafBlocks.GHOST_CHEST), "ghost_chest");
    public static final BlockEntityType<BlockEntityLectern> IAF_LECTERN = register(BlockEntityType.Builder.create(BlockEntityLectern::new, IafBlocks.LECTERN), "lectern");
    public static final BlockEntityType<BlockEntityPodium> PODIUM = register(BlockEntityType.Builder.create(BlockEntityPodium::new, IafBlocks.PODIUM_OAK, IafBlocks.PODIUM_BIRCH, IafBlocks.PODIUM_SPRUCE, IafBlocks.PODIUM_JUNGLE, IafBlocks.PODIUM_DARK_OAK, IafBlocks.PODIUM_ACACIA), "podium");

    private static <T extends BlockEntity> BlockEntityType<T> register(BlockEntityType.Builder<T> builder, String entityName) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(IceAndFire.MOD_ID, entityName), builder.build(null));
    }

    public static void init() {
    }
}
