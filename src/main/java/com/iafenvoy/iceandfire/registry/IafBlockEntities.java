package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.client.render.block.*;
import com.iafenvoy.iceandfire.entity.block.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class IafBlockEntities {
    public static <T extends BlockEntity> BlockEntityType<T> registerTileEntity(BlockEntityType.Builder<T> builder, String entityName) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(IceAndFire.MOD_ID, entityName), builder.build(null));
    }    public static final BlockEntityType<BlockEntityLectern> IAF_LECTERN = registerTileEntity(BlockEntityType.Builder.create(BlockEntityLectern::new, IafBlocks.LECTERN), "lectern");

    public static void init() {
    }    public static final BlockEntityType<BlockEntityPodium> PODIUM = registerTileEntity(BlockEntityType.Builder.create(BlockEntityPodium::new, IafBlocks.PODIUM_OAK, IafBlocks.PODIUM_BIRCH, IafBlocks.PODIUM_SPRUCE, IafBlocks.PODIUM_JUNGLE, IafBlocks.PODIUM_DARK_OAK, IafBlocks.PODIUM_ACACIA), "podium");

    @Environment(EnvType.CLIENT)
    public static void registerRenderers() {
        BlockEntityRendererFactories.register(IafBlockEntities.PODIUM, RenderPodium::new);
        BlockEntityRendererFactories.register(IafBlockEntities.IAF_LECTERN, RenderLectern::new);
        BlockEntityRendererFactories.register(IafBlockEntities.EGG_IN_ICE, RenderEggInIce::new);
        BlockEntityRendererFactories.register(IafBlockEntities.PIXIE_HOUSE, RenderPixieHouse::new);
        BlockEntityRendererFactories.register(IafBlockEntities.PIXIE_JAR, RenderJar::new);
        BlockEntityRendererFactories.register(IafBlockEntities.DREAD_PORTAL, RenderDreadPortal::new);
        BlockEntityRendererFactories.register(IafBlockEntities.DREAD_SPAWNER, RenderDreadSpawner::new);
        BlockEntityRendererFactories.register(IafBlockEntities.GHOST_CHEST, RenderGhostChest::new);
    }    public static final BlockEntityType<BlockEntityEggInIce> EGG_IN_ICE = registerTileEntity(BlockEntityType.Builder.create(BlockEntityEggInIce::new, IafBlocks.EGG_IN_ICE), "egginice");
    public static final BlockEntityType<BlockEntityPixieHouse> PIXIE_HOUSE = registerTileEntity(BlockEntityType.Builder.create(BlockEntityPixieHouse::new, IafBlocks.PIXIE_HOUSE_MUSHROOM_RED, IafBlocks.PIXIE_HOUSE_MUSHROOM_BROWN, IafBlocks.PIXIE_HOUSE_OAK, IafBlocks.PIXIE_HOUSE_BIRCH, IafBlocks.PIXIE_HOUSE_BIRCH, IafBlocks.PIXIE_HOUSE_SPRUCE, IafBlocks.PIXIE_HOUSE_DARK_OAK), "pixie_house");
    public static final BlockEntityType<BlockEntityJar> PIXIE_JAR = registerTileEntity(BlockEntityType.Builder.create(BlockEntityJar::new, IafBlocks.JAR_EMPTY, IafBlocks.JAR_PIXIE_0, IafBlocks.JAR_PIXIE_1, IafBlocks.JAR_PIXIE_2, IafBlocks.JAR_PIXIE_3, IafBlocks.JAR_PIXIE_4), "pixie_jar");
    public static final BlockEntityType<BlockEntityMyrmexCocoon> MYRMEX_COCOON = registerTileEntity(BlockEntityType.Builder.create(BlockEntityMyrmexCocoon::new, IafBlocks.DESERT_MYRMEX_COCOON, IafBlocks.JUNGLE_MYRMEX_COCOON), "myrmex_cocoon");
    public static final BlockEntityType<BlockEntityDragonForge> DRAGONFORGE_CORE = registerTileEntity(BlockEntityType.Builder.create(BlockEntityDragonForge::new, IafBlocks.DRAGONFORGE_FIRE_CORE, IafBlocks.DRAGONFORGE_ICE_CORE, IafBlocks.DRAGONFORGE_FIRE_CORE_DISABLED, IafBlocks.DRAGONFORGE_ICE_CORE_DISABLED, IafBlocks.DRAGONFORGE_LIGHTNING_CORE, IafBlocks.DRAGONFORGE_LIGHTNING_CORE_DISABLED), "dragonforge_core");
    public static final BlockEntityType<BlockEntityDragonForgeBrick> DRAGONFORGE_BRICK = registerTileEntity(BlockEntityType.Builder.create(BlockEntityDragonForgeBrick::new, IafBlocks.DRAGONFORGE_FIRE_BRICK, IafBlocks.DRAGONFORGE_ICE_BRICK, IafBlocks.DRAGONFORGE_LIGHTNING_BRICK), "dragonforge_brick");
    public static final BlockEntityType<BlockEntityDragonForgeInput> DRAGONFORGE_INPUT = registerTileEntity(BlockEntityType.Builder.create(BlockEntityDragonForgeInput::new, IafBlocks.DRAGONFORGE_FIRE_INPUT, IafBlocks.DRAGONFORGE_ICE_INPUT, IafBlocks.DRAGONFORGE_LIGHTNING_INPUT), "dragonforge_input");
    public static final BlockEntityType<BlockEntityDreadPortal> DREAD_PORTAL = registerTileEntity(BlockEntityType.Builder.create(BlockEntityDreadPortal::new, IafBlocks.DREAD_PORTAL), "dread_portal");
    public static final BlockEntityType<BlockEntityDreadSpawner> DREAD_SPAWNER = registerTileEntity(BlockEntityType.Builder.create(BlockEntityDreadSpawner::new, IafBlocks.DREAD_SPAWNER), "dread_spawner");
    public static final BlockEntityType<BlockEntityGhostChest> GHOST_CHEST = registerTileEntity(BlockEntityType.Builder.create(BlockEntityGhostChest::new, IafBlocks.GHOST_CHEST), "ghost_chest");






}
