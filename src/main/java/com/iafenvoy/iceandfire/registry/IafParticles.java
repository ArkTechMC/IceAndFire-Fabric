package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.client.particle.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class IafParticles {
    public static final DefaultParticleType BLOOD = register("blood", FabricParticleTypes.simple());
    public static final DefaultParticleType DRAGON_FLAME = register("dragon_flame", FabricParticleTypes.simple());
    public static final DefaultParticleType DRAGON_FROST = register("dragon_frost", FabricParticleTypes.simple());
    public static final DefaultParticleType DREAD_PORTAL = register("dread_portal", FabricParticleTypes.simple());
    public static final DefaultParticleType DREAD_TORCH = register("dread_torch", FabricParticleTypes.simple());
    public static final DefaultParticleType GHOST_APPEARANCE = register("ghost_appearance", FabricParticleTypes.simple());
    public static final DefaultParticleType HYDRA_BREATH = register("hydra_breath", FabricParticleTypes.simple());
    public static final DefaultParticleType PIXIE_DUST = register("pixie_dust", FabricParticleTypes.simple());
    public static final DefaultParticleType SERPENT_BUBBLE = register("serpent_bubble", FabricParticleTypes.simple());
    public static final DefaultParticleType SIREN_APPEARANCE = register("siren_appearance", FabricParticleTypes.simple());
    public static final DefaultParticleType SIREN_MUSIC = register("siren_music", FabricParticleTypes.simple());

    private static DefaultParticleType register(String name, DefaultParticleType obj) {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IceAndFire.MOD_ID, name), obj);
        return obj;
    }

    public static void init() {
    }

    @Environment(EnvType.CLIENT)
    public static void registerParticleRenderers() {
        ParticleFactoryRegistry.getInstance().register(BLOOD, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleBlood(world, x, y, z));
        ParticleFactoryRegistry.getInstance().register(DRAGON_FLAME, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleDragonFlame(world, x, y, z, velocityX, velocityY, velocityZ, 3));
        ParticleFactoryRegistry.getInstance().register(DRAGON_FROST, ParticleDragonFrost.Provider::new);
        ParticleFactoryRegistry.getInstance().register(DREAD_PORTAL, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleDreadPortal(world, x, y, z, velocityX, velocityY, velocityZ, 1));
        ParticleFactoryRegistry.getInstance().register(DREAD_TORCH, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleDreadTorch(world, x, y, z, velocityX, velocityY, velocityZ, 1));
        ParticleFactoryRegistry.getInstance().register(GHOST_APPEARANCE, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleGhostAppearance(world, x, y, z, 1));
        ParticleFactoryRegistry.getInstance().register(HYDRA_BREATH, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleHydraBreath(world, x, y, z, 1, 1, 1));
        ParticleFactoryRegistry.getInstance().register(PIXIE_DUST, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticlePixieDust(world, x, y, z, 1, 1, 1, 1));
        ParticleFactoryRegistry.getInstance().register(SERPENT_BUBBLE, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleSerpentBubble(world, x, y, z, velocityX, velocityY, velocityZ, 1));
        ParticleFactoryRegistry.getInstance().register(SIREN_APPEARANCE, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleSirenAppearance(world, x, y, z, 1));
        ParticleFactoryRegistry.getInstance().register(SIREN_MUSIC, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new ParticleSirenMusic(world, x, y, z, velocityX, velocityY, velocityZ, 1));
    }
}
