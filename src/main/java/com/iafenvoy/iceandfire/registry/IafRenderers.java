package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.citadel.client.model.ITabulaModelAnimator;
import com.iafenvoy.citadel.client.model.TabulaModel;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.particle.*;
import com.iafenvoy.iceandfire.render.entity.*;
import com.iafenvoy.iceandfire.render.model.*;
import com.iafenvoy.iceandfire.render.model.animator.FireDragonTabulaModelAnimator;
import com.iafenvoy.iceandfire.render.model.animator.IceDragonTabulaModelAnimator;
import com.iafenvoy.iceandfire.render.model.animator.LightningTabulaDragonAnimator;
import com.iafenvoy.iceandfire.render.model.animator.SeaSerpentTabulaModelAnimator;
import com.iafenvoy.iceandfire.render.model.util.TabulaModelHandlerHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class IafRenderers {
    private static TabulaModel getOrNull(String modelPath, ITabulaModelAnimator<?> tabulaAnimator) {
        try {
            return new TabulaModel(TabulaModelHandlerHelper.loadTabulaModel(modelPath), tabulaAnimator);
        } catch (IOException e) {
            IceAndFire.LOGGER.error(e);
        }
        return null;
    }

    public static void registerRenderers() {
        final TabulaModel SEA_SERPENT_BASE_MODEL = getOrNull("/assets/iceandfire/models/tabula/seaserpent/seaserpent_base", new SeaSerpentTabulaModelAnimator());
        final TabulaModel FIRE_DRAGON_BASE_MODEL = getOrNull("/assets/iceandfire/models/tabula/firedragon/firedragon_ground", new FireDragonTabulaModelAnimator());
        final TabulaModel ICE_DRAGON_BASE_MODEL = getOrNull("/assets/iceandfire/models/tabula/icedragon/icedragon_ground", new IceDragonTabulaModelAnimator());
        final TabulaModel LIGHTNING_DRAGON_BASE_MODEL = getOrNull("/assets/iceandfire/models/tabula/lightningdragon/lightningdragon_ground", new LightningTabulaDragonAnimator());

        EntityRendererRegistry.register(IafEntities.FIRE_DRAGON, x -> new RenderDragonBase(x, FIRE_DRAGON_BASE_MODEL, 0));
        EntityRendererRegistry.register(IafEntities.ICE_DRAGON, manager -> new RenderDragonBase(manager, ICE_DRAGON_BASE_MODEL, 1));
        EntityRendererRegistry.register(IafEntities.LIGHTNING_DRAGON, manager -> new RenderLightningDragon(manager, LIGHTNING_DRAGON_BASE_MODEL, 2));
        EntityRendererRegistry.register(IafEntities.DRAGON_EGG, RenderDragonEgg::new);
        EntityRendererRegistry.register(IafEntities.DRAGON_ARROW, RenderDragonArrow::new);
        EntityRendererRegistry.register(IafEntities.DRAGON_SKULL, manager -> new RenderDragonSkull(manager, FIRE_DRAGON_BASE_MODEL, ICE_DRAGON_BASE_MODEL, LIGHTNING_DRAGON_BASE_MODEL));
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

    public static void registerParticleRenderers() {
        ParticleFactoryRegistry.getInstance().register(IafParticles.BLOOD, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleBlood(world, x, y, z));
        ParticleFactoryRegistry.getInstance().register(IafParticles.DRAGON_FLAME, ParticleDragonFlame::provider);
        ParticleFactoryRegistry.getInstance().register(IafParticles.DRAGON_FROST, ParticleDragonFrost::provider);
        ParticleFactoryRegistry.getInstance().register(IafParticles.DREAD_PORTAL, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleDreadPortal(world, x, y, z, velocityX, velocityY, velocityZ, 1));
        ParticleFactoryRegistry.getInstance().register(IafParticles.DREAD_TORCH, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleDreadTorch(world, x, y, z, velocityX, velocityY, velocityZ, 1));
        ParticleFactoryRegistry.getInstance().register(IafParticles.GHOST_APPEARANCE, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleGhostAppearance(world, x, y, z, 1));
        ParticleFactoryRegistry.getInstance().register(IafParticles.HYDRA_BREATH, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleHydraBreath(world, x, y, z, 1, 1, 1));
        ParticleFactoryRegistry.getInstance().register(IafParticles.PIXIE_DUST, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticlePixieDust(world, x, y, z, 1, 1, 1, 1));
        ParticleFactoryRegistry.getInstance().register(IafParticles.SERPENT_BUBBLE, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleSerpentBubble(world, x, y, z, velocityX, velocityY, velocityZ, 1));
        ParticleFactoryRegistry.getInstance().register(IafParticles.SIREN_APPEARANCE, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleSirenAppearance(world, x, y, z, 1));
        ParticleFactoryRegistry.getInstance().register(IafParticles.SIREN_MUSIC, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleSirenMusic(world, x, y, z, velocityX, velocityY, velocityZ));
    }
}
