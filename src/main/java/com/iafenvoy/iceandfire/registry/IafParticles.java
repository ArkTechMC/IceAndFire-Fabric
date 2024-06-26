package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
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
}
