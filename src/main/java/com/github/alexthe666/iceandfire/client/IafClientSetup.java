package com.github.alexthe666.iceandfire.client;


import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.client.gui.IafGuiRegistry;
import com.github.alexthe666.iceandfire.client.model.*;
import com.github.alexthe666.iceandfire.client.model.animator.FireDragonTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.animator.IceDragonTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.animator.LightningTabulaDragonAnimator;
import com.github.alexthe666.iceandfire.client.model.animator.SeaSerpentTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.*;
import com.github.alexthe666.iceandfire.client.render.entity.*;
import com.github.alexthe666.iceandfire.client.render.tile.*;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemDragonHorn;
import com.github.alexthe666.iceandfire.item.ItemSummoningCrystal;
import dev.arktechmc.iafextra.resource.TabulaResourceManager;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.IOException;

public class IafClientSetup {
    public static final Identifier GHOST_CHEST_LOCATION = new Identifier(IceAndFire.MOD_ID, "models/ghost/ghost_chest");
    public static final Identifier GHOST_CHEST_LEFT_LOCATION = new Identifier(IceAndFire.MOD_ID, "models/ghost/ghost_chest_left");
    public static final Identifier GHOST_CHEST_RIGHT_LOCATION = new Identifier(IceAndFire.MOD_ID, "models/ghost/ghost_chest_right");
    public static TabulaModel FIRE_DRAGON_BASE_MODEL;
    public static TabulaModel ICE_DRAGON_BASE_MODEL;
    public static TabulaModel SEA_SERPENT_BASE_MODEL;
    public static TabulaModel LIGHTNING_DRAGON_BASE_MODEL;

    public static void clientInit() {
        EntityRendererRegistry.register(IafEntityRegistry.FIRE_DRAGON.get(), x -> new RenderDragonBase(x, FIRE_DRAGON_BASE_MODEL, 0));
        EntityRendererRegistry.register(IafEntityRegistry.ICE_DRAGON.get(), manager -> new RenderDragonBase(manager, ICE_DRAGON_BASE_MODEL, 1));
        EntityRendererRegistry.register(IafEntityRegistry.LIGHTNING_DRAGON.get(), manager -> new RenderLightningDragon(manager, LIGHTNING_DRAGON_BASE_MODEL, 2));
        EntityRendererRegistry.register(IafEntityRegistry.DRAGON_EGG.get(), RenderDragonEgg::new);
        EntityRendererRegistry.register(IafEntityRegistry.DRAGON_ARROW.get(), RenderDragonArrow::new);
        EntityRendererRegistry.register(IafEntityRegistry.DRAGON_SKULL.get(), manager -> new RenderDragonSkull(manager, FIRE_DRAGON_BASE_MODEL, ICE_DRAGON_BASE_MODEL, LIGHTNING_DRAGON_BASE_MODEL));
        EntityRendererRegistry.register(IafEntityRegistry.FIRE_DRAGON_CHARGE.get(), manager -> new RenderDragonFireCharge(manager, true));
        EntityRendererRegistry.register(IafEntityRegistry.ICE_DRAGON_CHARGE.get(), manager -> new RenderDragonFireCharge(manager, false));
        EntityRendererRegistry.register(IafEntityRegistry.LIGHTNING_DRAGON_CHARGE.get(), RenderDragonLightningCharge::new);
        EntityRendererRegistry.register(IafEntityRegistry.HIPPOGRYPH_EGG.get(), FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(IafEntityRegistry.HIPPOGRYPH.get(), RenderHippogryph::new);
        EntityRendererRegistry.register(IafEntityRegistry.STONE_STATUE.get(), RenderStoneStatue::new);
        EntityRendererRegistry.register(IafEntityRegistry.GORGON.get(), RenderGorgon::new);
        EntityRendererRegistry.register(IafEntityRegistry.PIXIE.get(), RenderPixie::new);
        EntityRendererRegistry.register(IafEntityRegistry.CYCLOPS.get(), RenderCyclops::new);
        EntityRendererRegistry.register(IafEntityRegistry.SIREN.get(), RenderSiren::new);
        EntityRendererRegistry.register(IafEntityRegistry.HIPPOCAMPUS.get(), RenderHippocampus::new);
        EntityRendererRegistry.register(IafEntityRegistry.DEATH_WORM.get(), RenderDeathWorm::new);
        EntityRendererRegistry.register(IafEntityRegistry.DEATH_WORM_EGG.get(), FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(IafEntityRegistry.COCKATRICE.get(), RenderCockatrice::new);
        EntityRendererRegistry.register(IafEntityRegistry.COCKATRICE_EGG.get(), FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(IafEntityRegistry.STYMPHALIAN_BIRD.get(), RenderStymphalianBird::new);
        EntityRendererRegistry.register(IafEntityRegistry.STYMPHALIAN_FEATHER.get(), RenderStymphalianFeather::new);
        EntityRendererRegistry.register(IafEntityRegistry.STYMPHALIAN_ARROW.get(), RenderStymphalianArrow::new);
        EntityRendererRegistry.register(IafEntityRegistry.TROLL.get(), RenderTroll::new);
        EntityRendererRegistry.register(IafEntityRegistry.MYRMEX_WORKER.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexWorker(), 0.5F));
        EntityRendererRegistry.register(IafEntityRegistry.MYRMEX_SOLDIER.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexSoldier(), 0.75F));
        EntityRendererRegistry.register(IafEntityRegistry.MYRMEX_QUEEN.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexQueen(), 1.25F));
        EntityRendererRegistry.register(IafEntityRegistry.MYRMEX_EGG.get(), RenderMyrmexEgg::new);
        EntityRendererRegistry.register(IafEntityRegistry.MYRMEX_SENTINEL.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexSentinel(), 0.85F));
        EntityRendererRegistry.register(IafEntityRegistry.MYRMEX_ROYAL.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexRoyal(), 0.75F));
        EntityRendererRegistry.register(IafEntityRegistry.MYRMEX_SWARMER.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexRoyal(), 0.25F));
        EntityRendererRegistry.register(IafEntityRegistry.AMPHITHERE.get(), RenderAmphithere::new);
        EntityRendererRegistry.register(IafEntityRegistry.AMPHITHERE_ARROW.get(), RenderAmphithereArrow::new);
        EntityRendererRegistry.register(IafEntityRegistry.SEA_SERPENT.get(), manager -> new RenderSeaSerpent(manager, SEA_SERPENT_BASE_MODEL));
        EntityRendererRegistry.register(IafEntityRegistry.SEA_SERPENT_BUBBLES.get(), RenderNothing::new);
        EntityRendererRegistry.register(IafEntityRegistry.SEA_SERPENT_ARROW.get(), RenderSeaSerpentArrow::new);
        EntityRendererRegistry.register(IafEntityRegistry.CHAIN_TIE.get(), RenderChainTie::new);
        EntityRendererRegistry.register(IafEntityRegistry.PIXIE_CHARGE.get(), RenderNothing::new);
        EntityRendererRegistry.register(IafEntityRegistry.TIDE_TRIDENT.get(), RenderTideTrident::new);
        EntityRendererRegistry.register(IafEntityRegistry.MOB_SKULL.get(), manager -> new RenderMobSkull(manager, SEA_SERPENT_BASE_MODEL));
        EntityRendererRegistry.register(IafEntityRegistry.DREAD_SCUTTLER.get(), RenderDreadScuttler::new);
        EntityRendererRegistry.register(IafEntityRegistry.DREAD_GHOUL.get(), RenderDreadGhoul::new);
        EntityRendererRegistry.register(IafEntityRegistry.DREAD_BEAST.get(), RenderDreadBeast::new);
        EntityRendererRegistry.register(IafEntityRegistry.DREAD_SCUTTLER.get(), RenderDreadScuttler::new);
        EntityRendererRegistry.register(IafEntityRegistry.DREAD_THRALL.get(), RenderDreadThrall::new);
        EntityRendererRegistry.register(IafEntityRegistry.DREAD_LICH.get(), RenderDreadLich::new);
        EntityRendererRegistry.register(IafEntityRegistry.DREAD_LICH_SKULL.get(), RenderDreadLichSkull::new);
        EntityRendererRegistry.register(IafEntityRegistry.DREAD_KNIGHT.get(), RenderDreadKnight::new);
        EntityRendererRegistry.register(IafEntityRegistry.DREAD_HORSE.get(), RenderDreadHorse::new);
        EntityRendererRegistry.register(IafEntityRegistry.HYDRA.get(), RenderHydra::new);
        EntityRendererRegistry.register(IafEntityRegistry.HYDRA_BREATH.get(), RenderNothing::new);
        EntityRendererRegistry.register(IafEntityRegistry.HYDRA_ARROW.get(), RenderHydraArrow::new);
        EntityRendererRegistry.register(IafEntityRegistry.SLOW_MULTIPART.get(), RenderNothing::new);
        EntityRendererRegistry.register(IafEntityRegistry.DRAGON_MULTIPART.get(), RenderNothing::new);
        EntityRendererRegistry.register(IafEntityRegistry.CYCLOPS_MULTIPART.get(), RenderNothing::new);
        EntityRendererRegistry.register(IafEntityRegistry.HYDRA_MULTIPART.get(), RenderNothing::new);
        EntityRendererRegistry.register(IafEntityRegistry.GHOST.get(), RenderGhost::new);
        EntityRendererRegistry.register(IafEntityRegistry.GHOST_SWORD.get(), RenderGhostSword::new);

        BlockEntityRendererFactories.register(IafTileEntityRegistry.PODIUM.get(), RenderPodium::new);
        BlockEntityRendererFactories.register(IafTileEntityRegistry.IAF_LECTERN.get(), RenderLectern::new);
        BlockEntityRendererFactories.register(IafTileEntityRegistry.EGG_IN_ICE.get(), RenderEggInIce::new);
        BlockEntityRendererFactories.register(IafTileEntityRegistry.PIXIE_HOUSE.get(), RenderPixieHouse::new);
        BlockEntityRendererFactories.register(IafTileEntityRegistry.PIXIE_JAR.get(), RenderJar::new);
        BlockEntityRendererFactories.register(IafTileEntityRegistry.DREAD_PORTAL.get(), RenderDreadPortal::new);
        BlockEntityRendererFactories.register(IafTileEntityRegistry.DREAD_SPAWNER.get(), RenderDreadSpawner::new);
        BlockEntityRendererFactories.register(IafTileEntityRegistry.GHOST_CHEST.get(), RenderGhostChest::new);

    }

    public static void setupClient() {
        IafGuiRegistry.register();
        EnumSeaSerpentAnimations.initializeSerpentModels();
        DragonAnimationsLibrary.register(EnumDragonPoses.values(), EnumDragonModelTypes.values());

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new TabulaResourceManager());

        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.GOLD_PILE.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.SILVER_PILE.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.LECTERN.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.PODIUM_OAK.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.PODIUM_BIRCH.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.PODIUM_SPRUCE.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.PODIUM_JUNGLE.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.PODIUM_ACACIA.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.PODIUM_DARK_OAK.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.FIRE_LILY.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.FROST_LILY.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.LIGHTNING_LILY.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.DRAGON_ICE_SPIKES.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.MYRMEX_DESERT_RESIN_BLOCK.get(), RenderLayer.getTranslucent());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.MYRMEX_DESERT_RESIN_GLASS.get(), RenderLayer.getTranslucent());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.MYRMEX_JUNGLE_RESIN_BLOCK.get(), RenderLayer.getTranslucent());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.MYRMEX_JUNGLE_RESIN_GLASS.get(), RenderLayer.getTranslucent());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.MYRMEX_DESERT_BIOLIGHT.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.MYRMEX_JUNGLE_BIOLIGHT.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.DREAD_STONE_FACE.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.DREAD_TORCH.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.BURNT_TORCH.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.EGG_IN_ICE.get(), RenderLayer.getTranslucent());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.JAR_EMPTY.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.JAR_PIXIE_0.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.JAR_PIXIE_1.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.JAR_PIXIE_2.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.JAR_PIXIE_3.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.JAR_PIXIE_4.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_BROWN.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_RED.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.PIXIE_HOUSE_OAK.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.PIXIE_HOUSE_BIRCH.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.PIXIE_HOUSE_SPRUCE.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.PIXIE_HOUSE_DARK_OAK.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.DREAD_SPAWNER.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.DREAD_TORCH_WALL.get(), RenderLayer.getCutout());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(IafBlockRegistry.BURNT_TORCH_WALL.get(), RenderLayer.getCutout());

        ModelPredicateProviderRegistry.register(IafItemRegistry.DRAGON_BOW.get(), new Identifier("pulling"), (itemStack, clientWorld, livingEntity, seed) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1 : 0);
        ModelPredicateProviderRegistry.register(IafItemRegistry.DRAGON_BOW.get(), new Identifier("pull"), (itemStack, clientWorld, livingEntity, seed) -> livingEntity == null ? 0 : livingEntity.getActiveItem() != itemStack ? 0 : (float) (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / 20);

        ModelPredicateProviderRegistry.register(IafItemRegistry.DRAGON_HORN.get(), new Identifier("iceorfire"), (stack, level, entity, p) -> ItemDragonHorn.getDragonType(stack) * 0.25F);
        ModelPredicateProviderRegistry.register(IafItemRegistry.SUMMONING_CRYSTAL_FIRE.get(), new Identifier("has_dragon"), (stack, level, entity, p) -> ItemSummoningCrystal.hasDragon(stack) ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(IafItemRegistry.SUMMONING_CRYSTAL_ICE.get(), new Identifier("has_dragon"), (stack, level, entity, p) -> ItemSummoningCrystal.hasDragon(stack) ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(IafItemRegistry.SUMMONING_CRYSTAL_LIGHTNING.get(), new Identifier("has_dragon"), (stack, level, entity, p) -> ItemSummoningCrystal.hasDragon(stack) ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(IafItemRegistry.TIDE_TRIDENT.get(), new Identifier("throwing"), (stack, level, entity, p) -> entity != null && entity.isUsingItem() && entity.getMainHandStack() == stack ? 1.0F : 0.0F);
    }
}
