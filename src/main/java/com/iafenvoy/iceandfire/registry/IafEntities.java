package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.config.IafConfig;
import com.iafenvoy.iceandfire.entity.*;
import com.iafenvoy.iceandfire.registry.tag.IafBiomeTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;

public class IafEntities {
    public static final EntityType<EntityDragonPart> DRAGON_MULTIPART = register("dragon_multipart", build(EntityDragonPart::new, SpawnGroup.MISC, true, 0.5F, 0.5F));
    public static final EntityType<EntitySlowPart> SLOW_MULTIPART = register("multipart", build(EntitySlowPart::new, SpawnGroup.MISC, true, 0.5F, 0.5F));
    public static final EntityType<EntityHydraHead> HYDRA_MULTIPART = register("hydra_multipart", build(EntityHydraHead::new, SpawnGroup.MISC, true, 0.5F, 0.5F));
    public static final EntityType<EntityCyclopsEye> CYCLOPS_MULTIPART = register("cylcops_multipart", build(EntityCyclopsEye::new, SpawnGroup.MISC, true, 0.5F, 0.5F));
    public static final EntityType<EntityDragonEgg> DRAGON_EGG = register("dragon_egg", build(EntityDragonEgg::new, SpawnGroup.MISC, true, 0.45F, 0.55F));
    public static final EntityType<EntityDragonArrow> DRAGON_ARROW = register("dragon_arrow", build(EntityDragonArrow::new, SpawnGroup.MISC, false, 0.5F, 0.5F));
    public static final EntityType<EntityDragonSkull> DRAGON_SKULL = register("dragon_skull", build(EntityDragonSkull::new, SpawnGroup.MISC, false, 0.9F, 0.65F));
    public static final EntityType<EntityFireDragon> FIRE_DRAGON = register("fire_dragon", build(EntityFireDragon::new, SpawnGroup.CREATURE, true, 0.78F, 1.2F, 256));
    public static final EntityType<EntityIceDragon> ICE_DRAGON = register("ice_dragon", build(EntityIceDragon::new, SpawnGroup.CREATURE, false, 0.78F, 1.2F, 256));
    public static final EntityType<EntityLightningDragon> LIGHTNING_DRAGON = register("lightning_dragon", build(EntityLightningDragon::new, SpawnGroup.CREATURE, false, 0.78F, 1.2F, 256));
    public static final EntityType<EntityDragonFireCharge> FIRE_DRAGON_CHARGE = register("fire_dragon_charge", build(EntityDragonFireCharge::new, SpawnGroup.MISC, false, 0.9F, 0.9F));
    public static final EntityType<EntityDragonIceCharge> ICE_DRAGON_CHARGE = register("ice_dragon_charge", build(EntityDragonIceCharge::new, SpawnGroup.MISC, false, 0.9F, 0.9F));
    public static final EntityType<EntityDragonLightningCharge> LIGHTNING_DRAGON_CHARGE = register("lightning_dragon_charge", build(EntityDragonLightningCharge::new, SpawnGroup.MISC, false, 0.9F, 0.9F));
    public static final EntityType<EntityHippogryphEgg> HIPPOGRYPH_EGG = register("hippogryph_egg", build(EntityHippogryphEgg::new, SpawnGroup.MISC, false, 0.5F, 0.5F));
    public static final EntityType<EntityHippogryph> HIPPOGRYPH = register("hippogryph", build(EntityHippogryph::new, SpawnGroup.CREATURE, false, 1.7F, 1.6F, 128));
    public static final EntityType<EntityStoneStatue> STONE_STATUE = register("stone_statue", build(EntityStoneStatue::new, SpawnGroup.CREATURE, false, 0.5F, 0.5F));
    public static final EntityType<EntityGorgon> GORGON = register("gorgon", build(EntityGorgon::new, SpawnGroup.CREATURE, false, 0.8F, 1.99F));
    public static final EntityType<EntityPixie> PIXIE = register("pixie", build(EntityPixie::new, SpawnGroup.CREATURE, false, 0.4F, 0.8F));
    public static final EntityType<EntityCyclops> CYCLOPS = register("cyclops", build(EntityCyclops::new, SpawnGroup.CREATURE, false, 1.95F, 7.4F, 8));
    public static final EntityType<EntitySiren> SIREN = register("siren", build(EntitySiren::new, SpawnGroup.CREATURE, false, 1.6F, 0.9F));
    public static final EntityType<EntityHippocampus> HIPPOCAMPUS = register("hippocampus", build(EntityHippocampus::new, SpawnGroup.WATER_CREATURE, false, 1.95F, 0.95F));
    public static final EntityType<EntityDeathWorm> DEATH_WORM = register("deathworm", build(EntityDeathWorm::new, SpawnGroup.CREATURE, false, 0.8F, 0.8F, 128));
    public static final EntityType<EntityDeathWormEgg> DEATH_WORM_EGG = register("deathworm_egg", build(EntityDeathWormEgg::new, SpawnGroup.MISC, false, 0.5F, 0.5F));
    public static final EntityType<EntityCockatrice> COCKATRICE = register("cockatrice", build(EntityCockatrice::new, SpawnGroup.CREATURE, false, 1.1F, 1F));
    public static final EntityType<EntityCockatriceEgg> COCKATRICE_EGG = register("cockatrice_egg", build(EntityCockatriceEgg::new, SpawnGroup.MISC, false, 0.5F, 0.5F));
    public static final EntityType<EntityStymphalianBird> STYMPHALIAN_BIRD = register("stymphalian_bird", build(EntityStymphalianBird::new, SpawnGroup.CREATURE, false, 1.3F, 1.2F, 128));
    public static final EntityType<EntityStymphalianFeather> STYMPHALIAN_FEATHER = register("stymphalian_feather", build(EntityStymphalianFeather::new, SpawnGroup.MISC, false, 0.5F, 0.5F));
    public static final EntityType<EntityStymphalianArrow> STYMPHALIAN_ARROW = register("stymphalian_arrow", build(EntityStymphalianArrow::new, SpawnGroup.MISC, false, 0.5F, 0.5F));
    public static final EntityType<EntityTroll> TROLL = register("troll", build(EntityTroll::new, SpawnGroup.MONSTER, false, 1.2F, 3.5F));
    public static final EntityType<EntityMyrmexWorker> MYRMEX_WORKER = register("myrmex_worker", build(EntityMyrmexWorker::new, SpawnGroup.CREATURE, false, 0.9F, 0.9F));
    public static final EntityType<EntityMyrmexSoldier> MYRMEX_SOLDIER = register("myrmex_soldier", build(EntityMyrmexSoldier::new, SpawnGroup.CREATURE, false, 1.2F, 0.95F));
    public static final EntityType<EntityMyrmexSentinel> MYRMEX_SENTINEL = register("myrmex_sentinel", build(EntityMyrmexSentinel::new, SpawnGroup.CREATURE, false, 1.3F, 1.95F));
    public static final EntityType<EntityMyrmexRoyal> MYRMEX_ROYAL = register("myrmex_royal", build(EntityMyrmexRoyal::new, SpawnGroup.CREATURE, false, 1.9F, 1.86F));
    public static final EntityType<EntityMyrmexQueen> MYRMEX_QUEEN = register("myrmex_queen", build(EntityMyrmexQueen::new, SpawnGroup.CREATURE, false, 2.9F, 1.86F));
    public static final EntityType<EntityMyrmexEgg> MYRMEX_EGG = register("myrmex_egg", build(EntityMyrmexEgg::new, SpawnGroup.MISC, false, 0.45F, 0.55F));
    public static final EntityType<EntityAmphithere> AMPHITHERE = register("amphithere", build(EntityAmphithere::new, SpawnGroup.CREATURE, false, 2.5F, 1.25F, 128));
    public static final EntityType<EntityAmphithereArrow> AMPHITHERE_ARROW = register("amphithere_arrow", build(EntityAmphithereArrow::new, SpawnGroup.MISC, false, 0.5F, 0.5F));
    public static final EntityType<EntitySeaSerpent> SEA_SERPENT = register("sea_serpent", build(EntitySeaSerpent::new, SpawnGroup.CREATURE, false, 0.5F, 0.5F, 256));
    public static final EntityType<EntitySeaSerpentBubbles> SEA_SERPENT_BUBBLES = register("sea_serpent_bubbles", build(EntitySeaSerpentBubbles::new, SpawnGroup.MISC, false, 0.9F, 0.9F));
    public static final EntityType<EntitySeaSerpentArrow> SEA_SERPENT_ARROW = register("sea_serpent_arrow", build(EntitySeaSerpentArrow::new, SpawnGroup.MISC, false, 0.5F, 0.5F));
    public static final EntityType<EntityChainTie> CHAIN_TIE = register("chain_tie", build(EntityChainTie::new, SpawnGroup.MISC, false, 0.8F, 0.9F));
    public static final EntityType<EntityPixieCharge> PIXIE_CHARGE = register("pixie_charge", build(EntityPixieCharge::new, SpawnGroup.MISC, false, 0.5F, 0.5F));
    public static final EntityType<EntityMyrmexSwarmer> MYRMEX_SWARMER = register("myrmex_swarmer", build(EntityMyrmexSwarmer::new, SpawnGroup.CREATURE, false, 0.5F, 0.5F));
    public static final EntityType<EntityTideTrident> TIDE_TRIDENT = register("tide_trident", build(EntityTideTrident::new, SpawnGroup.MISC, false, 0.85F, 0.5F));
    public static final EntityType<EntityMobSkull> MOB_SKULL = register("mob_skull", build(EntityMobSkull::new, SpawnGroup.MISC, false, 0.85F, 0.85F));
    public static final EntityType<EntityDreadThrall> DREAD_THRALL = register("dread_thrall", build(EntityDreadThrall::new, SpawnGroup.MONSTER, false, 0.6F, 1.8F));
    public static final EntityType<EntityDreadGhoul> DREAD_GHOUL = register("dread_ghoul", build(EntityDreadGhoul::new, SpawnGroup.MONSTER, false, 0.6F, 1.8F));
    public static final EntityType<EntityDreadBeast> DREAD_BEAST = register("dread_beast", build(EntityDreadBeast::new, SpawnGroup.MONSTER, false, 1.2F, 0.9F));
    public static final EntityType<EntityDreadScuttler> DREAD_SCUTTLER = register("dread_scuttler", build(EntityDreadScuttler::new, SpawnGroup.MONSTER, false, 1.5F, 1.3F));
    public static final EntityType<EntityDreadLich> DREAD_LICH = register("dread_lich", build(EntityDreadLich::new, SpawnGroup.MONSTER, false, 0.6F, 1.8F));
    public static final EntityType<EntityDreadLichSkull> DREAD_LICH_SKULL = register("dread_lich_skull", build(EntityDreadLichSkull::new, SpawnGroup.MISC, false, 0.5F, 0.5F));
    public static final EntityType<EntityDreadKnight> DREAD_KNIGHT = register("dread_knight", build(EntityDreadKnight::new, SpawnGroup.MONSTER, false, 0.6F, 1.8F));
    public static final EntityType<EntityDreadHorse> DREAD_HORSE = register("dread_horse", build(EntityDreadHorse::new, SpawnGroup.MONSTER, false, 1.3964844F, 1.6F));
    public static final EntityType<EntityHydra> HYDRA = register("hydra", build(EntityHydra::new, SpawnGroup.CREATURE, false, 2.8F, 1.39F));
    public static final EntityType<EntityHydraBreath> HYDRA_BREATH = register("hydra_breath", build(EntityHydraBreath::new, SpawnGroup.MISC, false, 0.9F, 0.9F));
    public static final EntityType<EntityHydraArrow> HYDRA_ARROW = register("hydra_arrow", build(EntityHydraArrow::new, SpawnGroup.MISC, false, 0.5F, 0.5F));
    public static final EntityType<EntityGhost> GHOST = register("ghost", build(EntityGhost::new, SpawnGroup.MONSTER, true, 0.8F, 1.9F));
    public static final EntityType<EntityGhostSword> GHOST_SWORD = register("ghost_sword", build(EntityGhostSword::new, SpawnGroup.MISC, false, 0.5F, 0.5F));

    private static <T extends Entity> EntityType<T> build(EntityType.EntityFactory<T> constructor, SpawnGroup category, boolean fireImmune, float sizeX, float sizeY) {
        FabricEntityTypeBuilder<T> builder = FabricEntityTypeBuilder.create(category, constructor).dimensions(EntityDimensions.changing(sizeX, sizeY));
        if (fireImmune) builder.fireImmune();
        return builder.build();
    }

    private static <T extends Entity> EntityType<T> build(EntityType.EntityFactory<T> constructor, SpawnGroup category, boolean fireImmune, float sizeX, float sizeY, int trackingRange) {
        FabricEntityTypeBuilder<T> builder = FabricEntityTypeBuilder.create(category, constructor).dimensions(EntityDimensions.changing(sizeX, sizeY)).trackRangeBlocks(trackingRange);
        if (fireImmune) builder.fireImmune();
        return builder.build();
    }


    private static <T extends Entity> EntityType<T> register(String entityName, EntityType<T> builder) {
        return Registry.register(Registries.ENTITY_TYPE, new Identifier(IceAndFire.MOD_ID, entityName), builder);
    }

    public static void init() {
        bakeAttributes();
        addSpawners();
        commonSetup();
    }

    public static void bakeAttributes() {
        FabricDefaultAttributeRegistry.register(DRAGON_EGG, EntityDragonEgg.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(DRAGON_SKULL, EntityDragonSkull.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(FIRE_DRAGON, EntityFireDragon.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(ICE_DRAGON, EntityIceDragon.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(LIGHTNING_DRAGON, EntityLightningDragon.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(HIPPOGRYPH, EntityHippogryph.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(GORGON, EntityGorgon.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(STONE_STATUE, EntityStoneStatue.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(PIXIE, EntityPixie.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(CYCLOPS, EntityCyclops.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(SIREN, EntitySiren.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(HIPPOCAMPUS, EntityHippocampus.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(DEATH_WORM, EntityDeathWorm.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(COCKATRICE, EntityCockatrice.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(STYMPHALIAN_BIRD, EntityStymphalianBird.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(TROLL, EntityTroll.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(MYRMEX_WORKER, EntityMyrmexWorker.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(MYRMEX_SOLDIER, EntityMyrmexSoldier.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(MYRMEX_SENTINEL, EntityMyrmexSentinel.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(MYRMEX_ROYAL, EntityMyrmexRoyal.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(MYRMEX_QUEEN, EntityMyrmexQueen.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(MYRMEX_EGG, EntityMyrmexEgg.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(MYRMEX_SWARMER, EntityMyrmexSwarmer.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(AMPHITHERE, EntityAmphithere.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(SEA_SERPENT, EntitySeaSerpent.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(MOB_SKULL, EntityMobSkull.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(DREAD_THRALL, EntityDreadThrall.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(DREAD_LICH, EntityDreadLich.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(DREAD_BEAST, EntityDreadBeast.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(DREAD_HORSE, EntityDreadHorse.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(DREAD_GHOUL, EntityDreadGhoul.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(DREAD_KNIGHT, EntityDreadKnight.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(DREAD_SCUTTLER, EntityDreadScuttler.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(HYDRA, EntityHydra.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(GHOST, EntityGhost.bakeAttributes().build());
    }

    public static void commonSetup() {
        SpawnRestriction.register(HIPPOGRYPH, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityHippogryph::canMobSpawn);
        SpawnRestriction.register(TROLL, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTroll::canTrollSpawnOn);
        SpawnRestriction.register(DREAD_LICH, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityDreadLich::canLichSpawnOn);
        SpawnRestriction.register(COCKATRICE, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityCockatrice::canMobSpawn);
        SpawnRestriction.register(AMPHITHERE, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING, EntityAmphithere::canAmphithereSpawnOn);
    }

    public static void addSpawners() {
        if (IafConfig.getInstance().hippogryphs.spawn)
            BiomeModifications.addSpawn(context -> context.hasTag(IafBiomeTags.HIPPOGRYPH), SpawnGroup.CREATURE, IafEntities.HIPPOGRYPH, IafConfig.getInstance().hippogryphs.spawnWeight, 1, 1);
        if (IafConfig.getInstance().lich.spawn)
            BiomeModifications.addSpawn(context -> context.hasTag(IafBiomeTags.MAUSOLEUM), SpawnGroup.MONSTER, IafEntities.DREAD_LICH, IafConfig.getInstance().lich.spawnWeight, 1, 1);
        if (IafConfig.getInstance().cockatrice.spawn)
            BiomeModifications.addSpawn(context -> context.hasTag(IafBiomeTags.COCKATRICE), SpawnGroup.CREATURE, IafEntities.COCKATRICE, IafConfig.getInstance().cockatrice.spawnWeight, 1, 2);
        if (IafConfig.getInstance().amphithere.spawn)
            BiomeModifications.addSpawn(context -> context.hasTag(IafBiomeTags.AMPHITHERE), SpawnGroup.CREATURE, IafEntities.AMPHITHERE, IafConfig.getInstance().amphithere.spawnWeight, 1, 3);
        if (IafConfig.getInstance().troll.spawn)
            BiomeModifications.addSpawn(context -> context.hasTag(IafBiomeTags.TROLL), SpawnGroup.MONSTER, IafEntities.TROLL, IafConfig.getInstance().troll.spawnWeight, 1, 3);
    }
}
