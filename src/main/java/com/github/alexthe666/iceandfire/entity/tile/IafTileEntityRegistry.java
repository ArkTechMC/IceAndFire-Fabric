package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.client.render.tile.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class IafTileEntityRegistry {
    public static final BlockEntityType<TileEntityLectern> IAF_LECTERN = registerTileEntity(BlockEntityType.Builder.create(TileEntityLectern::new, IafBlockRegistry.LECTERN), "lectern");
    public static final BlockEntityType<TileEntityPodium> PODIUM = registerTileEntity(BlockEntityType.Builder.create(TileEntityPodium::new, IafBlockRegistry.PODIUM_OAK, IafBlockRegistry.PODIUM_BIRCH, IafBlockRegistry.PODIUM_SPRUCE, IafBlockRegistry.PODIUM_JUNGLE, IafBlockRegistry.PODIUM_DARK_OAK, IafBlockRegistry.PODIUM_ACACIA), "podium");
    public static final BlockEntityType<TileEntityEggInIce> EGG_IN_ICE = registerTileEntity(BlockEntityType.Builder.create(TileEntityEggInIce::new, IafBlockRegistry.EGG_IN_ICE), "egginice");
    public static final BlockEntityType<TileEntityPixieHouse> PIXIE_HOUSE = registerTileEntity(BlockEntityType.Builder.create(TileEntityPixieHouse::new, IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_RED, IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_BROWN, IafBlockRegistry.PIXIE_HOUSE_OAK, IafBlockRegistry.PIXIE_HOUSE_BIRCH, IafBlockRegistry.PIXIE_HOUSE_BIRCH, IafBlockRegistry.PIXIE_HOUSE_SPRUCE, IafBlockRegistry.PIXIE_HOUSE_DARK_OAK), "pixie_house");
    public static final BlockEntityType<TileEntityJar> PIXIE_JAR = registerTileEntity(BlockEntityType.Builder.create(TileEntityJar::new, IafBlockRegistry.JAR_EMPTY, IafBlockRegistry.JAR_PIXIE_0, IafBlockRegistry.JAR_PIXIE_1, IafBlockRegistry.JAR_PIXIE_2, IafBlockRegistry.JAR_PIXIE_3, IafBlockRegistry.JAR_PIXIE_4), "pixie_jar");
    public static final BlockEntityType<TileEntityMyrmexCocoon> MYRMEX_COCOON = registerTileEntity(BlockEntityType.Builder.create(TileEntityMyrmexCocoon::new, IafBlockRegistry.DESERT_MYRMEX_COCOON, IafBlockRegistry.JUNGLE_MYRMEX_COCOON), "myrmex_cocoon");
    public static final BlockEntityType<TileEntityDragonforge> DRAGONFORGE_CORE = registerTileEntity(BlockEntityType.Builder.create(TileEntityDragonforge::new, IafBlockRegistry.DRAGONFORGE_FIRE_CORE, IafBlockRegistry.DRAGONFORGE_ICE_CORE, IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED, IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED), "dragonforge_core");
    public static final BlockEntityType<TileEntityDragonforgeBrick> DRAGONFORGE_BRICK = registerTileEntity(BlockEntityType.Builder.create(TileEntityDragonforgeBrick::new, IafBlockRegistry.DRAGONFORGE_FIRE_BRICK, IafBlockRegistry.DRAGONFORGE_ICE_BRICK, IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK), "dragonforge_brick");
    public static final BlockEntityType<TileEntityDragonforgeInput> DRAGONFORGE_INPUT = registerTileEntity(BlockEntityType.Builder.create(TileEntityDragonforgeInput::new, IafBlockRegistry.DRAGONFORGE_FIRE_INPUT, IafBlockRegistry.DRAGONFORGE_ICE_INPUT, IafBlockRegistry.DRAGONFORGE_LIGHTNING_INPUT), "dragonforge_input");
    public static final BlockEntityType<TileEntityDreadPortal> DREAD_PORTAL = registerTileEntity(BlockEntityType.Builder.create(TileEntityDreadPortal::new, IafBlockRegistry.DREAD_PORTAL), "dread_portal");
    public static final BlockEntityType<TileEntityDreadSpawner> DREAD_SPAWNER = registerTileEntity(BlockEntityType.Builder.create(TileEntityDreadSpawner::new, IafBlockRegistry.DREAD_SPAWNER), "dread_spawner");
    public static final BlockEntityType<TileEntityGhostChest> GHOST_CHEST = registerTileEntity(BlockEntityType.Builder.create(TileEntityGhostChest::new, IafBlockRegistry.GHOST_CHEST), "ghost_chest");

    public static <T extends BlockEntity> BlockEntityType<T> registerTileEntity(BlockEntityType.Builder<T> builder, String entityName) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(IceAndFire.MOD_ID, entityName), builder.build(null));
    }

    public static void init() {
    }

    @Environment(EnvType.CLIENT)
    public static void registerRenderers(){
        BlockEntityRendererFactories.register(IafTileEntityRegistry.PODIUM, RenderPodium::new);
        BlockEntityRendererFactories.register(IafTileEntityRegistry.IAF_LECTERN, RenderLectern::new);
        BlockEntityRendererFactories.register(IafTileEntityRegistry.EGG_IN_ICE, RenderEggInIce::new);
        BlockEntityRendererFactories.register(IafTileEntityRegistry.PIXIE_HOUSE, RenderPixieHouse::new);
        BlockEntityRendererFactories.register(IafTileEntityRegistry.PIXIE_JAR, RenderJar::new);
        BlockEntityRendererFactories.register(IafTileEntityRegistry.DREAD_PORTAL, RenderDreadPortal::new);
        BlockEntityRendererFactories.register(IafTileEntityRegistry.DREAD_SPAWNER, RenderDreadSpawner::new);
        BlockEntityRendererFactories.register(IafTileEntityRegistry.GHOST_CHEST, RenderGhostChest::new);
    }
}
