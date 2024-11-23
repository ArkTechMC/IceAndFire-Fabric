package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.data.DragonArmor;
import com.iafenvoy.iceandfire.data.SeaSerpent;
import com.iafenvoy.iceandfire.data.TrollType;
import com.iafenvoy.iceandfire.item.ItemDragonHorn;
import com.iafenvoy.iceandfire.item.ItemSummoningCrystal;
import com.iafenvoy.iceandfire.particle.*;
import com.iafenvoy.iceandfire.render.TEISRItemRenderer;
import com.iafenvoy.iceandfire.render.TideTridentRenderer;
import com.iafenvoy.iceandfire.render.TrollWeaponRenderer;
import com.iafenvoy.iceandfire.render.armor.*;
import com.iafenvoy.iceandfire.render.block.*;
import com.iafenvoy.iceandfire.render.entity.*;
import com.iafenvoy.iceandfire.render.item.DeathwormGauntletRenderer;
import com.iafenvoy.iceandfire.render.item.GorgonHeadRenderer;
import com.iafenvoy.iceandfire.render.model.*;
import com.iafenvoy.iceandfire.render.model.animator.FireDragonTabulaModelAnimator;
import com.iafenvoy.iceandfire.render.model.animator.IceDragonTabulaModelAnimator;
import com.iafenvoy.iceandfire.render.model.animator.LightningTabulaDragonAnimator;
import com.iafenvoy.iceandfire.render.model.animator.SeaSerpentTabulaModelAnimator;
import com.iafenvoy.uranus.client.model.ITabulaModelAnimator;
import com.iafenvoy.uranus.client.model.TabulaModel;
import com.iafenvoy.uranus.client.model.util.TabulaModelHandlerHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.util.Identifier;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public final class IafRenderers {
    public static final TabulaModel SEA_SERPENT_BASE_MODEL = getOrNull("/assets/iceandfire/models/tabula/seaserpent/seaserpent_base", new SeaSerpentTabulaModelAnimator());
    public static final TabulaModel FIRE_DRAGON_BASE_MODEL = getOrNull("/assets/iceandfire/models/tabula/firedragon/firedragon_ground", new FireDragonTabulaModelAnimator());
    public static final TabulaModel ICE_DRAGON_BASE_MODEL = getOrNull("/assets/iceandfire/models/tabula/icedragon/icedragon_ground", new IceDragonTabulaModelAnimator());
    public static final TabulaModel LIGHTNING_DRAGON_BASE_MODEL = getOrNull("/assets/iceandfire/models/tabula/lightningdragon/lightningdragon_ground", new LightningTabulaDragonAnimator());

    public static TabulaModel getOrNull(String modelPath, ITabulaModelAnimator<?> tabulaAnimator) {
        try {
            return new TabulaModel(TabulaModelHandlerHelper.loadTabulaModel(modelPath), tabulaAnimator);
        } catch (IOException e) {
            IceAndFire.LOGGER.error(e);
        }
        return null;
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(IafEntities.FIRE_DRAGON, x -> new RenderDragonBase(x, FIRE_DRAGON_BASE_MODEL, 0));
        EntityRendererRegistry.register(IafEntities.ICE_DRAGON, manager -> new RenderDragonBase(manager, ICE_DRAGON_BASE_MODEL, 1));
        EntityRendererRegistry.register(IafEntities.LIGHTNING_DRAGON, manager -> new RenderLightningDragon(manager, LIGHTNING_DRAGON_BASE_MODEL, 2));
        EntityRendererRegistry.register(IafEntities.DRAGON_EGG, RenderDragonEgg::new);
        EntityRendererRegistry.register(IafEntities.DRAGON_ARROW, RenderDragonArrow::new);
        EntityRendererRegistry.register(IafEntities.DRAGON_SKULL, RenderDragonSkull::new);
        EntityRendererRegistry.register(IafEntities.FIRE_DRAGON_CHARGE, manager -> new RenderDragonFireCharge(manager, true));
        EntityRendererRegistry.register(IafEntities.ICE_DRAGON_CHARGE, manager -> new RenderDragonFireCharge(manager, false));
        EntityRendererRegistry.register(IafEntities.LIGHTNING_DRAGON_CHARGE, RenderDragonLightningCharge::new);
        EntityRendererRegistry.register(IafEntities.HIPPOGRYPH_EGG, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(IafEntities.HIPPOGRYPH, RenderHippogryph::new);
        EntityRendererRegistry.register(IafEntities.STONE_STATUE, RenderStoneStatue::new);
        EntityRendererRegistry.register(IafEntities.GORGON, RenderGorgon::new);
        EntityRendererRegistry.register(IafEntities.PIXIE, RenderPixie::new);
        EntityRendererRegistry.register(IafEntities.CYCLOPS, RenderCyclops::new);
        EntityRendererRegistry.register(IafEntities.SIREN, RenderSiren::new);
        EntityRendererRegistry.register(IafEntities.HIPPOCAMPUS, RenderHippocampus::new);
        EntityRendererRegistry.register(IafEntities.DEATH_WORM, RenderDeathWorm::new);
        EntityRendererRegistry.register(IafEntities.DEATH_WORM_EGG, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(IafEntities.COCKATRICE, RenderCockatrice::new);
        EntityRendererRegistry.register(IafEntities.COCKATRICE_EGG, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(IafEntities.STYMPHALIAN_BIRD, RenderStymphalianBird::new);
        EntityRendererRegistry.register(IafEntities.STYMPHALIAN_FEATHER, RenderStymphalianFeather::new);
        EntityRendererRegistry.register(IafEntities.STYMPHALIAN_ARROW, RenderStymphalianArrow::new);
        EntityRendererRegistry.register(IafEntities.TROLL, RenderTroll::new);
        EntityRendererRegistry.register(IafEntities.MYRMEX_WORKER, manager -> new RenderMyrmexBase(manager, new ModelMyrmexWorker(), 0.5F));
        EntityRendererRegistry.register(IafEntities.MYRMEX_SOLDIER, manager -> new RenderMyrmexBase(manager, new ModelMyrmexSoldier(), 0.75F));
        EntityRendererRegistry.register(IafEntities.MYRMEX_QUEEN, manager -> new RenderMyrmexBase(manager, new ModelMyrmexQueen(), 1.25F));
        EntityRendererRegistry.register(IafEntities.MYRMEX_EGG, RenderMyrmexEgg::new);
        EntityRendererRegistry.register(IafEntities.MYRMEX_SENTINEL, manager -> new RenderMyrmexBase(manager, new ModelMyrmexSentinel(), 0.85F));
        EntityRendererRegistry.register(IafEntities.MYRMEX_ROYAL, manager -> new RenderMyrmexBase(manager, new ModelMyrmexRoyal(), 0.75F));
        EntityRendererRegistry.register(IafEntities.MYRMEX_SWARMER, manager -> new RenderMyrmexBase(manager, new ModelMyrmexRoyal(), 0.25F));
        EntityRendererRegistry.register(IafEntities.AMPHITHERE, RenderAmphithere::new);
        EntityRendererRegistry.register(IafEntities.AMPHITHERE_ARROW, RenderAmphithereArrow::new);
        EntityRendererRegistry.register(IafEntities.SEA_SERPENT, manager -> new RenderSeaSerpent(manager, SEA_SERPENT_BASE_MODEL));
        EntityRendererRegistry.register(IafEntities.SEA_SERPENT_BUBBLES, RenderNothing::new);
        EntityRendererRegistry.register(IafEntities.SEA_SERPENT_ARROW, RenderSeaSerpentArrow::new);
        EntityRendererRegistry.register(IafEntities.CHAIN_TIE, RenderChainTie::new);
        EntityRendererRegistry.register(IafEntities.PIXIE_CHARGE, RenderNothing::new);
        EntityRendererRegistry.register(IafEntities.TIDE_TRIDENT, RenderTideTrident::new);
        EntityRendererRegistry.register(IafEntities.MOB_SKULL, manager -> new RenderMobSkull(manager, SEA_SERPENT_BASE_MODEL));
        EntityRendererRegistry.register(IafEntities.DREAD_SCUTTLER, RenderDreadScuttler::new);
        EntityRendererRegistry.register(IafEntities.DREAD_GHOUL, RenderDreadGhoul::new);
        EntityRendererRegistry.register(IafEntities.DREAD_BEAST, RenderDreadBeast::new);
        EntityRendererRegistry.register(IafEntities.DREAD_SCUTTLER, RenderDreadScuttler::new);
        EntityRendererRegistry.register(IafEntities.DREAD_THRALL, RenderDreadThrall::new);
        EntityRendererRegistry.register(IafEntities.DREAD_LICH, RenderDreadLich::new);
        EntityRendererRegistry.register(IafEntities.DREAD_LICH_SKULL, RenderDreadLichSkull::new);
        EntityRendererRegistry.register(IafEntities.DREAD_KNIGHT, RenderDreadKnight::new);
        EntityRendererRegistry.register(IafEntities.DREAD_HORSE, RenderDreadHorse::new);
        EntityRendererRegistry.register(IafEntities.HYDRA, RenderHydra::new);
        EntityRendererRegistry.register(IafEntities.HYDRA_BREATH, RenderNothing::new);
        EntityRendererRegistry.register(IafEntities.HYDRA_ARROW, RenderHydraArrow::new);
        EntityRendererRegistry.register(IafEntities.SLOW_MULTIPART, RenderNothing::new);
        EntityRendererRegistry.register(IafEntities.DRAGON_MULTIPART, RenderNothing::new);
        EntityRendererRegistry.register(IafEntities.CYCLOPS_MULTIPART, RenderNothing::new);
        EntityRendererRegistry.register(IafEntities.HYDRA_MULTIPART, RenderNothing::new);
        EntityRendererRegistry.register(IafEntities.GHOST, RenderGhost::new);
        EntityRendererRegistry.register(IafEntities.GHOST_SWORD, RenderGhostSword::new);
    }

    public static void registerBlockEntityRenderers() {
        BlockEntityRendererFactories.register(IafBlockEntities.PODIUM, RenderPodium::new);
        BlockEntityRendererFactories.register(IafBlockEntities.IAF_LECTERN, RenderLectern::new);
        BlockEntityRendererFactories.register(IafBlockEntities.EGG_IN_ICE, RenderEggInIce::new);
        BlockEntityRendererFactories.register(IafBlockEntities.PIXIE_HOUSE, RenderPixieHouse::new);
        BlockEntityRendererFactories.register(IafBlockEntities.PIXIE_JAR, RenderJar::new);
        BlockEntityRendererFactories.register(IafBlockEntities.DREAD_PORTAL, RenderDreadPortal::new);
        BlockEntityRendererFactories.register(IafBlockEntities.DREAD_SPAWNER, RenderDreadSpawner::new);
        BlockEntityRendererFactories.register(IafBlockEntities.GHOST_CHEST, RenderGhostChest::new);
    }

    public static void registerParticleRenderers() {
        //Fire
        ParticleFactoryRegistry.getInstance().register(IafParticles.DRAGON_FLAME_1, spirit -> ParticleDragonFlame.provider(spirit, 1));
        ParticleFactoryRegistry.getInstance().register(IafParticles.DRAGON_FLAME_2, spirit -> ParticleDragonFlame.provider(spirit, 2));
        ParticleFactoryRegistry.getInstance().register(IafParticles.DRAGON_FLAME_3, spirit -> ParticleDragonFlame.provider(spirit, 3));
        ParticleFactoryRegistry.getInstance().register(IafParticles.DRAGON_FLAME_4, spirit -> ParticleDragonFlame.provider(spirit, 4));
        ParticleFactoryRegistry.getInstance().register(IafParticles.DRAGON_FLAME_5, spirit -> ParticleDragonFlame.provider(spirit, 5));
        //Ice
        ParticleFactoryRegistry.getInstance().register(IafParticles.DRAGON_FROST_1, spirit -> ParticleDragonFrost.provider(spirit, 1));
        ParticleFactoryRegistry.getInstance().register(IafParticles.DRAGON_FROST_2, spirit -> ParticleDragonFrost.provider(spirit, 2));
        ParticleFactoryRegistry.getInstance().register(IafParticles.DRAGON_FROST_3, spirit -> ParticleDragonFrost.provider(spirit, 3));
        ParticleFactoryRegistry.getInstance().register(IafParticles.DRAGON_FROST_4, spirit -> ParticleDragonFrost.provider(spirit, 4));
        ParticleFactoryRegistry.getInstance().register(IafParticles.DRAGON_FROST_5, spirit -> ParticleDragonFrost.provider(spirit, 5));
        //Others
        ParticleFactoryRegistry.getInstance().register(IafParticles.BLOOD, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleBlood(world, x, y, z));
        ParticleFactoryRegistry.getInstance().register(IafParticles.DREAD_PORTAL, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleDreadPortal(world, x, y, z, velocityX, velocityY, velocityZ, 1));
        ParticleFactoryRegistry.getInstance().register(IafParticles.DREAD_TORCH, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleDreadTorch(world, x, y, z, velocityX, velocityY, velocityZ, 1));
        ParticleFactoryRegistry.getInstance().register(IafParticles.GHOST_APPEARANCE, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleGhostAppearance(world, x, y, z, 1));
        ParticleFactoryRegistry.getInstance().register(IafParticles.HYDRA_BREATH, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleHydraBreath(world, x, y, z, 1, 1, 1));
        ParticleFactoryRegistry.getInstance().register(IafParticles.PIXIE_DUST, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticlePixieDust(world, x, y, z, 1, 1, 1, 1));
        ParticleFactoryRegistry.getInstance().register(IafParticles.SERPENT_BUBBLE, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleSerpentBubble(world, x, y, z, velocityX, velocityY, velocityZ, 1));
        ParticleFactoryRegistry.getInstance().register(IafParticles.SIREN_APPEARANCE, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleSirenAppearance(world, x, y, z, 1));
        ParticleFactoryRegistry.getInstance().register(IafParticles.SIREN_MUSIC, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleSirenMusic(world, x, y, z, velocityX, velocityY, velocityZ));
    }

    public static void registerArmorRenderers(){
        ArmorRenderer.register(new CopperArmorRenderer(), IafItems.COPPER_HELMET, IafItems.COPPER_CHESTPLATE, IafItems.COPPER_LEGGINGS, IafItems.COPPER_BOOTS);
        ArmorRenderer.register(new DeathWormArmorRenderer(), IafItems.DEATHWORM_WHITE_HELMET, IafItems.DEATHWORM_WHITE_CHESTPLATE, IafItems.DEATHWORM_WHITE_LEGGINGS, IafItems.DEATHWORM_WHITE_BOOTS);
        ArmorRenderer.register(new DeathWormArmorRenderer(), IafItems.DEATHWORM_YELLOW_HELMET, IafItems.DEATHWORM_YELLOW_CHESTPLATE, IafItems.DEATHWORM_YELLOW_LEGGINGS, IafItems.DEATHWORM_YELLOW_BOOTS);
        ArmorRenderer.register(new DeathWormArmorRenderer(), IafItems.DEATHWORM_RED_HELMET, IafItems.DEATHWORM_RED_CHESTPLATE, IafItems.DEATHWORM_RED_LEGGINGS, IafItems.DEATHWORM_RED_BOOTS);
        ArmorRenderer.register(new DragonSteelArmorRenderer(), IafItems.DRAGONSTEEL_FIRE_HELMET, IafItems.DRAGONSTEEL_FIRE_CHESTPLATE, IafItems.DRAGONSTEEL_FIRE_LEGGINGS, IafItems.DRAGONSTEEL_FIRE_BOOTS);
        ArmorRenderer.register(new DragonSteelArmorRenderer(), IafItems.DRAGONSTEEL_ICE_HELMET, IafItems.DRAGONSTEEL_ICE_CHESTPLATE, IafItems.DRAGONSTEEL_ICE_LEGGINGS, IafItems.DRAGONSTEEL_ICE_BOOTS);
        ArmorRenderer.register(new DragonSteelArmorRenderer(), IafItems.DRAGONSTEEL_LIGHTNING_HELMET, IafItems.DRAGONSTEEL_LIGHTNING_CHESTPLATE, IafItems.DRAGONSTEEL_LIGHTNING_LEGGINGS, IafItems.DRAGONSTEEL_LIGHTNING_BOOTS);
        ArmorRenderer.register(new SilverArmorRenderer(), IafItems.SILVER_HELMET, IafItems.SILVER_CHESTPLATE, IafItems.SILVER_LEGGINGS, IafItems.SILVER_BOOTS);
        for (DragonArmor armor : DragonArmor.values())
            ArmorRenderer.register(new ScaleArmorRenderer(), armor.helmet, armor.chestplate, armor.leggings, armor.boots);
        for (SeaSerpent seaSerpent : SeaSerpent.values())
            ArmorRenderer.register(new SeaSerpentArmorRenderer(), seaSerpent.helmet, seaSerpent.chestplate, seaSerpent.leggings, seaSerpent.boots);
        for (TrollType troll : TrollType.values())
            ArmorRenderer.register(new TrollArmorRenderer(), troll.helmet, troll.chestplate, troll.leggings, troll.boots);
    }

    public static void registerItemRenderers(){
        BuiltinItemRendererRegistry.INSTANCE.register(IafItems.DEATHWORM_GAUNTLET_RED, new DeathwormGauntletRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafItems.DEATHWORM_GAUNTLET_YELLOW, new DeathwormGauntletRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafItems.DEATHWORM_GAUNTLET_WHITE, new DeathwormGauntletRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafItems.GORGON_HEAD, new GorgonHeadRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafItems.TIDE_TRIDENT, new TideTridentRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlocks.PIXIE_HOUSE_BIRCH, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlocks.PIXIE_HOUSE_OAK, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlocks.PIXIE_HOUSE_DARK_OAK, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlocks.PIXIE_HOUSE_SPRUCE, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlocks.PIXIE_HOUSE_MUSHROOM_RED, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlocks.PIXIE_HOUSE_MUSHROOM_BROWN, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlocks.DREAD_PORTAL, new TEISRItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(IafBlocks.GHOST_CHEST, new TEISRItemRenderer());
        for (TrollType.BuiltinWeapon weapon : TrollType.BuiltinWeapon.values())
            BuiltinItemRendererRegistry.INSTANCE.register(weapon.getItem(), new TrollWeaponRenderer());
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
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.DREADWOOD_LEAVES, RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlocks.DREADWOOD_SAPLING, RenderLayer.getCutout());
    }

    public static void registerModelPredicates() {
        ModelPredicateProviderRegistry.register(IafItems.DRAGON_BOW, new Identifier("pulling"), (itemStack, clientWorld, livingEntity, seed) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1 : 0);
        ModelPredicateProviderRegistry.register(IafItems.DRAGON_BOW, new Identifier("pull"), (itemStack, clientWorld, livingEntity, seed) -> livingEntity == null ? 0 : livingEntity.getActiveItem() != itemStack ? 0 : (float) (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / 20);

        ModelPredicateProviderRegistry.register(IafItems.DRAGON_HORN, new Identifier("iceorfire"), (stack, level, entity, p) -> ItemDragonHorn.getDragonType(stack) * 0.25F);
        ModelPredicateProviderRegistry.register(IafItems.SUMMONING_CRYSTAL_FIRE, new Identifier("has_dragon"), (stack, level, entity, p) -> ItemSummoningCrystal.hasDragon(stack) ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(IafItems.SUMMONING_CRYSTAL_ICE, new Identifier("has_dragon"), (stack, level, entity, p) -> ItemSummoningCrystal.hasDragon(stack) ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(IafItems.SUMMONING_CRYSTAL_LIGHTNING, new Identifier("has_dragon"), (stack, level, entity, p) -> ItemSummoningCrystal.hasDragon(stack) ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(IafItems.TIDE_TRIDENT, new Identifier("throwing"), (stack, level, entity, p) -> entity != null && entity.isUsingItem() && entity.getMainHandStack() == stack ? 1.0F : 0.0F);
    }
}
