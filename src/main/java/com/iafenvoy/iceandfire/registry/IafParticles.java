package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;

public final class IafParticles {
    //Fire
    public static final DefaultParticleType DRAGON_FLAME_1 = register("dragon_flame_1", FabricParticleTypes.simple(true));
    public static final DefaultParticleType DRAGON_FLAME_2 = register("dragon_flame_2", FabricParticleTypes.simple(true));
    public static final DefaultParticleType DRAGON_FLAME_3 = register("dragon_flame_3", FabricParticleTypes.simple(true));
    public static final DefaultParticleType DRAGON_FLAME_4 = register("dragon_flame_4", FabricParticleTypes.simple(true));
    public static final DefaultParticleType DRAGON_FLAME_5 = register("dragon_flame_5", FabricParticleTypes.simple(true));
    public static final List<DefaultParticleType> ALL_DRAGON_FLAME = List.of(ParticleTypes.FLAME, DRAGON_FLAME_1, DRAGON_FLAME_2, DRAGON_FLAME_3, DRAGON_FLAME_4, DRAGON_FLAME_5);
    //Ice
    public static final DefaultParticleType DRAGON_FROST_1 = register("dragon_frost_1", FabricParticleTypes.simple(true));
    public static final DefaultParticleType DRAGON_FROST_2 = register("dragon_frost_2", FabricParticleTypes.simple(true));
    public static final DefaultParticleType DRAGON_FROST_3 = register("dragon_frost_3", FabricParticleTypes.simple(true));
    public static final DefaultParticleType DRAGON_FROST_4 = register("dragon_frost_4", FabricParticleTypes.simple(true));
    public static final DefaultParticleType DRAGON_FROST_5 = register("dragon_frost_5", FabricParticleTypes.simple(true));
    public static final List<DefaultParticleType> ALL_DRAGON_FROST = List.of(ParticleTypes.SNOWFLAKE, DRAGON_FROST_1, DRAGON_FROST_2, DRAGON_FROST_3, DRAGON_FROST_4, DRAGON_FROST_5);
    //Others
    public static final DefaultParticleType BLOOD = register("blood", FabricParticleTypes.simple(true));
    public static final DefaultParticleType DREAD_PORTAL = register("dread_portal", FabricParticleTypes.simple(true));
    public static final DefaultParticleType DREAD_TORCH = register("dread_torch", FabricParticleTypes.simple(true));
    public static final DefaultParticleType GHOST_APPEARANCE = register("ghost_appearance", FabricParticleTypes.simple(true));
    public static final DefaultParticleType HYDRA_BREATH = register("hydra_breath", FabricParticleTypes.simple(true));
    public static final DefaultParticleType PIXIE_DUST = register("pixie_dust", FabricParticleTypes.simple(true));
    public static final DefaultParticleType SERPENT_BUBBLE = register("serpent_bubble", FabricParticleTypes.simple(true));
    public static final DefaultParticleType SIREN_APPEARANCE = register("siren_appearance", FabricParticleTypes.simple(true));
    public static final DefaultParticleType SIREN_MUSIC = register("siren_music", FabricParticleTypes.simple(true));

    private static DefaultParticleType register(String name, DefaultParticleType obj) {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(IceAndFire.MOD_ID, name), obj);
        return obj;
    }

    public static void init() {
    }
}
