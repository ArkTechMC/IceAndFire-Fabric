package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.enums.EnumDragonColor;
import com.iafenvoy.iceandfire.item.block.*;
import com.iafenvoy.iceandfire.item.block.util.IWallBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.UniformIntProvider;

@SuppressWarnings("unused")
public class IafBlocks {
    public static final BlockSoundGroup SOUND_TYPE_GOLD = new BlockSoundGroup(1.0F, 1.0F, IafSounds.GOLD_PILE_BREAK, IafSounds.GOLD_PILE_STEP, IafSounds.GOLD_PILE_BREAK, IafSounds.GOLD_PILE_STEP, IafSounds.GOLD_PILE_STEP);
    public static final Block LECTERN = register("lectern", new BlockLectern());
    public static final Block PODIUM_OAK = register("podium_oak", new BlockPodium());
    public static final Block PODIUM_BIRCH = register("podium_birch", new BlockPodium());
    public static final Block PODIUM_SPRUCE = register("podium_spruce", new BlockPodium());
    public static final Block PODIUM_JUNGLE = register("podium_jungle", new BlockPodium());
    public static final Block PODIUM_DARK_OAK = register("podium_dark_oak", new BlockPodium());
    public static final Block PODIUM_ACACIA = register("podium_acacia", new BlockPodium());
    public static final Block FIRE_LILY = register("fire_lily", new BlockElementalFlower());
    public static final Block FROST_LILY = register("frost_lily", new BlockElementalFlower());
    public static final Block LIGHTNING_LILY = register("lightning_lily", new BlockElementalFlower());
    public static final Block GOLD_PILE = register("gold_pile", new BlockGoldPile());
    public static final Block SILVER_PILE = register("silver_pile", new BlockGoldPile());
    public static final Block COPPER_PILE = register("copper_pile", new BlockGoldPile());
    public static final Block SILVER_ORE = register("silver_ore", new ExperienceDroppingBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).strength(3, 3).requiresTool()));
    public static final Block DEEPSLATE_SILVER_ORE = register("deepslate_silver_ore", new ExperienceDroppingBlock(AbstractBlock.Settings.create().mapColor(MapColor.DEEPSLATE_GRAY).strength(3, 3).requiresTool()));
    public static final Block SILVER_BLOCK = register("silver_block", BlockGeneric.builder(3.0F, 5.0F, BlockSoundGroup.METAL, MapColor.IRON_GRAY, null, null, false));
    public static final Block SAPPHIRE_ORE = register("sapphire_ore", new ExperienceDroppingBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).strength(4, 3).requiresTool(), UniformIntProvider.create(3, 7)));
    public static final Block SAPPHIRE_BLOCK = register("sapphire_block", BlockGeneric.builder(3.0F, 6.0F, BlockSoundGroup.METAL, MapColor.IRON_GRAY, null, null, false));
    public static final Block RAW_SILVER_BLOCK = register("raw_silver_block", BlockGeneric.builder(3.0F, 5.0F, BlockSoundGroup.STONE, MapColor.IRON_GRAY, Instrument.BASEDRUM, null, false));
    public static final Block CHARRED_DIRT = register("chared_dirt", BlockReturningState.builder(0.5F, 0.0F, BlockSoundGroup.GRAVEL, MapColor.DIRT_BROWN, null, null, false, Blocks.DIRT.getDefaultState()));
    public static final Block CHARRED_GRASS = register("chared_grass", BlockReturningState.builder(0.6F, 0.0F, BlockSoundGroup.GRAVEL, MapColor.PALE_GREEN, null, null, false, Blocks.GRASS_BLOCK.getDefaultState()));
    public static final Block CHARRED_STONE = register("chared_stone", BlockReturningState.builder(1.5F, 10.0F, BlockSoundGroup.STONE, MapColor.STONE_GRAY, Instrument.BASEDRUM, null, false, Blocks.STONE.getDefaultState()));
    public static final Block CHARRED_COBBLESTONE = register("chared_cobblestone", BlockReturningState.builder(2F, 10.0F, BlockSoundGroup.STONE, MapColor.STONE_GRAY, Instrument.BASEDRUM, null, false, Blocks.COBBLESTONE.getDefaultState()));
    public static final Block CHARRED_GRAVEL = register("chared_gravel", new BlockFallingReturningState(0.6F, 0F, BlockSoundGroup.GRAVEL, MapColor.DIRT_BROWN, Blocks.GRAVEL.getDefaultState()));
    public static final Block CHARRED_DIRT_PATH = register(BlockCharedPath.getNameFromType(0), new BlockCharedPath(0));
    public static final Block ASH = register("ash", BlockFallingGeneric.builder(0.5F, 0F, BlockSoundGroup.SAND, MapColor.PALE_YELLOW, Instrument.SNARE));
    public static final Block FROZEN_DIRT = register("frozen_dirt", BlockReturningState.builder(0.5F, 0.0F, BlockSoundGroup.GLASS, true, MapColor.DIRT_BROWN, null, null, false, Blocks.DIRT.getDefaultState()));
    public static final Block FROZEN_GRASS = register("frozen_grass", BlockReturningState.builder(0.6F, 0.0F, BlockSoundGroup.GLASS, true, MapColor.PALE_GREEN, null, null, false, Blocks.GRASS_BLOCK.getDefaultState()));
    public static final Block FROZEN_STONE = register("frozen_stone", BlockReturningState.builder(1.5F, 1.0F, BlockSoundGroup.GLASS, true, MapColor.STONE_GRAY, Instrument.BASEDRUM, null, false, Blocks.STONE.getDefaultState()));
    public static final Block FROZEN_COBBLESTONE = register("frozen_cobblestone", BlockReturningState.builder(2F, 2.0F, BlockSoundGroup.GLASS, true, MapColor.STONE_GRAY, Instrument.BASEDRUM, null, false, Blocks.COBBLESTONE.getDefaultState()));
    public static final Block FROZEN_GRAVEL = register("frozen_gravel", new BlockFallingReturningState(0.6F, 0F, BlockSoundGroup.GLASS, true, MapColor.DIRT_BROWN, Blocks.GRAVEL.getDefaultState()));
    public static final Block FROZEN_DIRT_PATH = register(BlockCharedPath.getNameFromType(1), new BlockCharedPath(1));
    public static final Block FROZEN_SPLINTERS = register("frozen_splinters", BlockGeneric.builder(2.0F, 1.0F, BlockSoundGroup.GLASS, true, MapColor.OAK_TAN, Instrument.BASS, null, true));
    public static final Block DRAGON_ICE = register("dragon_ice", BlockGeneric.builder(0.5F, 0F, BlockSoundGroup.GLASS, true, MapColor.PALE_PURPLE, null, null, false));
    public static final Block DRAGON_ICE_SPIKES = register("dragon_ice_spikes", new BlockIceSpikes());
    public static final Block CRACKLED_DIRT = register("crackled_dirt", BlockReturningState.builder(0.5F, 0.0F, BlockSoundGroup.GRAVEL, MapColor.DIRT_BROWN, null, null, false, Blocks.DIRT.getDefaultState()));
    public static final Block CRACKLED_GRASS = register("crackled_grass", BlockReturningState.builder(0.6F, 0.0F, BlockSoundGroup.GRAVEL, MapColor.PALE_GREEN, null, null, false, Blocks.GRASS_BLOCK.getDefaultState()));
    public static final Block CRACKLED_STONE = register("crackled_stone", BlockReturningState.builder(1.5F, 1.0F, BlockSoundGroup.STONE, MapColor.STONE_GRAY, Instrument.BASEDRUM, null, false, Blocks.STONE.getDefaultState()));
    public static final Block CRACKLED_COBBLESTONE = register("crackled_cobblestone", BlockReturningState.builder(2F, 2F, BlockSoundGroup.STONE, MapColor.STONE_GRAY, Instrument.BASEDRUM, null, false, Blocks.COBBLESTONE.getDefaultState()));
    public static final Block CRACKLED_GRAVEL = register("crackled_gravel", new BlockFallingReturningState(0.6F, 0F, BlockSoundGroup.GRAVEL, MapColor.DIRT_BROWN, Blocks.GRAVEL.getDefaultState()));
    public static final Block CRACKLED_DIRT_PATH = register(BlockCharedPath.getNameFromType(2), new BlockCharedPath(2));

    public static final Block NEST = register("nest", BlockGeneric.builder(0.5F, 0F, BlockSoundGroup.GRAVEL, false, MapColor.DARK_GREEN, null, PistonBehavior.DESTROY, false));

    public static final Block DRAGON_SCALE_RED = register("dragonscale_red", new BlockDragonScales(EnumDragonColor.RED));
    public static final Block DRAGON_SCALE_GREEN = register("dragonscale_green", new BlockDragonScales(EnumDragonColor.GREEN));
    public static final Block DRAGON_SCALE_BRONZE = register("dragonscale_bronze", new BlockDragonScales(EnumDragonColor.BRONZE));
    public static final Block DRAGON_SCALE_GRAY = register("dragonscale_gray", new BlockDragonScales(EnumDragonColor.GRAY));
    public static final Block DRAGON_SCALE_BLUE = register("dragonscale_blue", new BlockDragonScales(EnumDragonColor.BLUE));
    public static final Block DRAGON_SCALE_WHITE = register("dragonscale_white", new BlockDragonScales(EnumDragonColor.WHITE));
    public static final Block DRAGON_SCALE_SAPPHIRE = register("dragonscale_sapphire", new BlockDragonScales(EnumDragonColor.SAPPHIRE));
    public static final Block DRAGON_SCALE_SILVER = register("dragonscale_silver", new BlockDragonScales(EnumDragonColor.SILVER));
    public static final Block DRAGON_SCALE_ELECTRIC = register("dragonscale_electric", new BlockDragonScales(EnumDragonColor.ELECTRIC));
    public static final Block DRAGON_SCALE_AMYTHEST = register("dragonscale_amythest", new BlockDragonScales(EnumDragonColor.AMETHYST));
    public static final Block DRAGON_SCALE_COPPER = register("dragonscale_copper", new BlockDragonScales(EnumDragonColor.COPPER));
    public static final Block DRAGON_SCALE_BLACK = register("dragonscale_black", new BlockDragonScales(EnumDragonColor.BLACK));

    public static final Block DRAGON_BONE_BLOCK = register("dragon_bone_block", new BlockDragonBone());
    public static final Block DRAGON_BONE_BLOCK_WALL = register("dragon_bone_wall", new BlockDragonBoneWall(AbstractBlock.Settings.copy(IafBlocks.DRAGON_BONE_BLOCK)));
    public static final Block DRAGONFORGE_FIRE_BRICK = register(BlockDragonForgeBricks.name(0), new BlockDragonForgeBricks(0));
    public static final Block DRAGONFORGE_ICE_BRICK = register(BlockDragonForgeBricks.name(1), new BlockDragonForgeBricks(1));
    public static final Block DRAGONFORGE_LIGHTNING_BRICK = register(BlockDragonForgeBricks.name(2), new BlockDragonForgeBricks(2));
    public static final Block DRAGONFORGE_FIRE_INPUT = register(BlockDragonForgeInput.name(0), new BlockDragonForgeInput(0));
    public static final Block DRAGONFORGE_ICE_INPUT = register(BlockDragonForgeInput.name(1), new BlockDragonForgeInput(1));
    public static final Block DRAGONFORGE_LIGHTNING_INPUT = register(BlockDragonForgeInput.name(2), new BlockDragonForgeInput(2));
    public static final Block DRAGONFORGE_FIRE_CORE = register(BlockDragonForgeCore.name(0, true), new BlockDragonForgeCore(0, true));
    public static final Block DRAGONFORGE_ICE_CORE = register(BlockDragonForgeCore.name(1, true), new BlockDragonForgeCore(1, true));
    public static final Block DRAGONFORGE_LIGHTNING_CORE = register(BlockDragonForgeCore.name(2, true), new BlockDragonForgeCore(2, true));
    public static final Block DRAGONFORGE_FIRE_CORE_DISABLED = register(BlockDragonForgeCore.name(0, false), new BlockDragonForgeCore(0, false));
    public static final Block DRAGONFORGE_ICE_CORE_DISABLED = register(BlockDragonForgeCore.name(1, false), new BlockDragonForgeCore(1, false));
    public static final Block DRAGONFORGE_LIGHTNING_CORE_DISABLED = register(BlockDragonForgeCore.name(2, false), new BlockDragonForgeCore(2, false));
    public static final Block EGG_IN_ICE = register("egginice", new BlockEggInIce());
    public static final Block PIXIE_HOUSE_MUSHROOM_RED = register(BlockPixieHouse.name("mushroom_red"), new BlockPixieHouse());
    public static final Block PIXIE_HOUSE_MUSHROOM_BROWN = register(BlockPixieHouse.name("mushroom_brown"), new BlockPixieHouse());
    public static final Block PIXIE_HOUSE_OAK = register(BlockPixieHouse.name("oak"), new BlockPixieHouse());
    public static final Block PIXIE_HOUSE_BIRCH = register(BlockPixieHouse.name("birch"), new BlockPixieHouse());
    public static final Block PIXIE_HOUSE_SPRUCE = register(BlockPixieHouse.name("spruce"), new BlockPixieHouse());
    public static final Block PIXIE_HOUSE_DARK_OAK = register(BlockPixieHouse.name("dark_oak"), new BlockPixieHouse());
    public static final Block JAR_EMPTY = register(BlockJar.name(-1), new BlockJar(-1));
    public static final Block JAR_PIXIE_0 = register(BlockJar.name(0), new BlockJar(0));
    public static final Block JAR_PIXIE_1 = register(BlockJar.name(1), new BlockJar(1));
    public static final Block JAR_PIXIE_2 = register(BlockJar.name(2), new BlockJar(2));
    public static final Block JAR_PIXIE_3 = register(BlockJar.name(3), new BlockJar(3));
    public static final Block JAR_PIXIE_4 = register(BlockJar.name(4), new BlockJar(4));
    public static final Block MYRMEX_DESERT_RESIN = register(BlockMyrmexResin.name(false, "desert"), new BlockMyrmexResin(false));
    public static final Block MYRMEX_DESERT_RESIN_STICKY = register(BlockMyrmexResin.name(true, "desert"), new BlockMyrmexResin(true));
    public static final Block MYRMEX_JUNGLE_RESIN = register(BlockMyrmexResin.name(false, "jungle"), new BlockMyrmexResin(false));
    public static final Block MYRMEX_JUNGLE_RESIN_STICKY = register(BlockMyrmexResin.name(true, "jungle"), new BlockMyrmexResin(true));
    public static final Block DESERT_MYRMEX_COCOON = register("desert_myrmex_cocoon", new BlockMyrmexCocoon());
    public static final Block JUNGLE_MYRMEX_COCOON = register("jungle_myrmex_cocoon", new BlockMyrmexCocoon());
    public static final Block MYRMEX_DESERT_BIOLIGHT = register("myrmex_desert_biolight", new BlockMyrmexBiolight());
    public static final Block MYRMEX_JUNGLE_BIOLIGHT = register("myrmex_jungle_biolight", new BlockMyrmexBiolight());
    public static final Block MYRMEX_DESERT_RESIN_BLOCK = register(BlockMyrmexConnectedResin.name(false, false), new BlockMyrmexConnectedResin(false, false));
    public static final Block MYRMEX_JUNGLE_RESIN_BLOCK = register(BlockMyrmexConnectedResin.name(true, false), new BlockMyrmexConnectedResin(true, false));
    public static final Block MYRMEX_DESERT_RESIN_GLASS = register(BlockMyrmexConnectedResin.name(false, true), new BlockMyrmexConnectedResin(false, true));
    public static final Block MYRMEX_JUNGLE_RESIN_GLASS = register(BlockMyrmexConnectedResin.name(true, true), new BlockMyrmexConnectedResin(true, true));
    public static final Block DRAGONSTEEL_FIRE_BLOCK = register("dragonsteel_fire_block", BlockGeneric.builder(10.0F, 1000.0F, BlockSoundGroup.METAL, MapColor.IRON_GRAY, null, null, false));
    public static final Block DRAGONSTEEL_ICE_BLOCK = register("dragonsteel_ice_block", BlockGeneric.builder(10.0F, 1000.0F, BlockSoundGroup.METAL, MapColor.IRON_GRAY, null, null, false));
    public static final Block DRAGONSTEEL_LIGHTNING_BLOCK = register("dragonsteel_lightning_block", BlockGeneric.builder(10.0F, 1000.0F, BlockSoundGroup.METAL, MapColor.IRON_GRAY, null, null, false));
    public static final BlockDreadBase DREAD_STONE = register("dread_stone", BlockDreadBase.builder(-1.0F, 100000.0F, BlockSoundGroup.STONE, MapColor.STONE_GRAY, null, false));
    public static final BlockDreadBase DREAD_STONE_BRICKS = register("dread_stone_bricks", BlockDreadBase.builder(-1.0F, 100000.0F, BlockSoundGroup.STONE, MapColor.STONE_GRAY, null, false));
    public static final Block DREAD_STONE_BRICKS_STAIRS = register("dread_stone_stairs", new StairsBlock(DREAD_STONE_BRICKS.getDefaultState(), AbstractBlock.Settings.create().strength(20F)));
    public static final BlockDreadBase DREAD_STONE_BRICKS_CHISELED = register("dread_stone_bricks_chiseled", BlockDreadBase.builder(-1.0F, 100000.0F, BlockSoundGroup.STONE, MapColor.STONE_GRAY, null, false));
    public static final BlockDreadBase DREAD_STONE_BRICKS_CRACKED = register("dread_stone_bricks_cracked", BlockDreadBase.builder(-1.0F, 100000.0F, BlockSoundGroup.STONE, MapColor.STONE_GRAY, null, false));
    public static final BlockDreadBase DREAD_STONE_BRICKS_MOSSY = register("dread_stone_bricks_mossy", BlockDreadBase.builder(-1.0F, 100000.0F, BlockSoundGroup.STONE, MapColor.STONE_GRAY, null, false));
    public static final BlockDreadBase DREAD_STONE_TILE = register("dread_stone_tile", BlockDreadBase.builder(-1.0F, 100000.0F, BlockSoundGroup.STONE, MapColor.STONE_GRAY, null, false));
    public static final Block DREAD_STONE_FACE = register("dread_stone_face", new BlockDreadStoneFace());
    public static final TorchBlock DREAD_TORCH = registerWallBlock("dread_torch", new BlockDreadTorch());
    public static final BlockDreadTorchWall DREAD_TORCH_WALL = registerWallTorch("dread_torch_wall", new BlockDreadTorchWall());
    public static final Block DREAD_STONE_BRICKS_SLAB = register("dread_stone_slab", new SlabBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).strength(10F, 10000F)));
    public static final Block DREADWOOD_LOG = register("dreadwood_log", new BlockDreadWoodLog());
    public static final BlockDreadBase DREADWOOD_PLANKS = register("dreadwood_planks", BlockDreadBase.builder(-1.0F, 100000.0F, BlockSoundGroup.WOOD, MapColor.OAK_TAN, Instrument.BASS, true));
    public static final Block DREADWOOD_PLANKS_LOCK = register("dreadwood_planks_lock", new BlockDreadWoodLock());
    public static final Block DREAD_PORTAL = register("dread_portal", new BlockDreadPortal());
    public static final Block DREAD_SPAWNER = register("dread_spawner", new BlockDreadSpawner());
    public static final TorchBlock BURNT_TORCH = registerWallBlock("burnt_torch", new BlockBurntTorch());
    public static final BlockBurntTorchWall BURNT_TORCH_WALL = registerWallTorch("burnt_torch_wall", new BlockBurntTorchWall());
    public static final Block GHOST_CHEST = register("ghost_chest", new BlockGhostChest());
    public static final Block GRAVEYARD_SOIL = register("graveyard_soil", new BlockGraveyardSoil());

    public static <T extends Block> T register(String name, T block) {
        IafItems.register(name, new BlockItem(block, new FabricItemSettings()), false);
        IafItemGroups.TAB_BLOCKS_LIST.add(block);
        return Registry.register(Registries.BLOCK, new Identifier(IceAndFire.MOD_ID, name), block);
    }

    private static <T extends TorchBlock> T registerWallBlock(String name, T block) {
        IafItems.register(name, new VerticallyAttachableBlockItem(block, ((IWallBlock) block).wallBlock(), new Item.Settings(), Direction.DOWN), false);
        IafItemGroups.TAB_BLOCKS_LIST.add(block);
        return Registry.register(Registries.BLOCK, new Identifier(IceAndFire.MOD_ID, name), block);
    }

    private static <T extends WallTorchBlock> T registerWallTorch(String name, T block) {
        return Registry.register(Registries.BLOCK, new Identifier(IceAndFire.MOD_ID, name), block);
    }

    public static void init() {
    }

    public static void registerRenderLayers() {
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.GOLD_PILE, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.SILVER_PILE, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.LECTERN, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.PODIUM_OAK, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.PODIUM_BIRCH, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.PODIUM_SPRUCE, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.PODIUM_JUNGLE, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.PODIUM_ACACIA, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.PODIUM_DARK_OAK, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.FIRE_LILY, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.FROST_LILY, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.LIGHTNING_LILY, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.DRAGON_ICE_SPIKES, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.MYRMEX_DESERT_RESIN_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.MYRMEX_DESERT_RESIN_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.MYRMEX_JUNGLE_RESIN_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.MYRMEX_JUNGLE_RESIN_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.MYRMEX_DESERT_BIOLIGHT, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.MYRMEX_JUNGLE_BIOLIGHT, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.DREAD_STONE_FACE, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.DREAD_TORCH, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.BURNT_TORCH, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.EGG_IN_ICE, RenderLayer.getTranslucent());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.JAR_EMPTY, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.JAR_PIXIE_0, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.JAR_PIXIE_1, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.JAR_PIXIE_2, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.JAR_PIXIE_3, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.JAR_PIXIE_4, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.PIXIE_HOUSE_MUSHROOM_BROWN, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.PIXIE_HOUSE_MUSHROOM_RED, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.PIXIE_HOUSE_OAK, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.PIXIE_HOUSE_BIRCH, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.PIXIE_HOUSE_SPRUCE, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.PIXIE_HOUSE_DARK_OAK, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.DREAD_SPAWNER, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.DREAD_TORCH_WALL, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.BURNT_TORCH_WALL, RenderLayer.getCutout());
    }
}
