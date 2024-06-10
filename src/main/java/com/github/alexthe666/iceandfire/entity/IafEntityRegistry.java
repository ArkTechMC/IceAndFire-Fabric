package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.registry.Registries;
import net.minecraft.world.Heightmap;

import java.util.HashMap;

public class IafEntityRegistry {

    public static final LazyRegistrar<EntityType<?>> ENTITIES = LazyRegistrar.create(Registries.ENTITY_TYPE, IceAndFire.MOD_ID);

    public static final RegistryObject<EntityType<EntityDragonPart>> DRAGON_MULTIPART = registerEntity(EntityType.Builder.<EntityDragonPart>create(EntityDragonPart::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F).makeFireImmune(), "dragon_multipart");
    public static final RegistryObject<EntityType<EntitySlowPart>> SLOW_MULTIPART = registerEntity(EntityType.Builder.<EntitySlowPart>create(EntitySlowPart::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F).makeFireImmune(), "multipart");
    public static final RegistryObject<EntityType<EntityHydraHead>> HYDRA_MULTIPART = registerEntity(EntityType.Builder.<EntityHydraHead>create(EntityHydraHead::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F).makeFireImmune(), "hydra_multipart");
    public static final RegistryObject<EntityType<EntityCyclopsEye>> CYCLOPS_MULTIPART = registerEntity(EntityType.Builder.<EntityCyclopsEye>create(EntityCyclopsEye::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F).makeFireImmune(), "cylcops_multipart");
    public static final RegistryObject<EntityType<EntityDragonEgg>> DRAGON_EGG = registerEntity(EntityType.Builder.create(EntityDragonEgg::new, SpawnGroup.MISC).setDimensions(0.45F, 0.55F).makeFireImmune(), "dragon_egg");
    public static final RegistryObject<EntityType<EntityDragonArrow>> DRAGON_ARROW = registerEntity(EntityType.Builder.<EntityDragonArrow>create(EntityDragonArrow::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F), "dragon_arrow");
    public static final RegistryObject<EntityType<EntityDragonSkull>> DRAGON_SKULL = registerEntity(EntityType.Builder.create(EntityDragonSkull::new, SpawnGroup.MISC).setDimensions(0.9F, 0.65F), "dragon_skull");
    public static final RegistryObject<EntityType<EntityFireDragon>> FIRE_DRAGON = registerEntity(EntityType.Builder.<EntityFireDragon>create(EntityFireDragon::new, SpawnGroup.CREATURE).setDimensions(0.78F, 1.2F).makeFireImmune().maxTrackingRange(256), "fire_dragon");
    public static final RegistryObject<EntityType<EntityIceDragon>> ICE_DRAGON = registerEntity(EntityType.Builder.<EntityIceDragon>create(EntityIceDragon::new, SpawnGroup.CREATURE).setDimensions(0.78F, 1.2F).maxTrackingRange(256), "ice_dragon");
    public static final RegistryObject<EntityType<EntityLightningDragon>> LIGHTNING_DRAGON = registerEntity(EntityType.Builder.<EntityLightningDragon>create(EntityLightningDragon::new, SpawnGroup.CREATURE).setDimensions(0.78F, 1.2F).maxTrackingRange(256), "lightning_dragon");
    public static final RegistryObject<EntityType<EntityDragonFireCharge>> FIRE_DRAGON_CHARGE = registerEntity(EntityType.Builder.<EntityDragonFireCharge>create(EntityDragonFireCharge::new, SpawnGroup.MISC).setDimensions(0.9F, 0.9F), "fire_dragon_charge");
    public static final RegistryObject<EntityType<EntityDragonIceCharge>> ICE_DRAGON_CHARGE = registerEntity(EntityType.Builder.<EntityDragonIceCharge>create(EntityDragonIceCharge::new, SpawnGroup.MISC).setDimensions(0.9F, 0.9F), "ice_dragon_charge");
    public static final RegistryObject<EntityType<EntityDragonLightningCharge>> LIGHTNING_DRAGON_CHARGE = registerEntity(EntityType.Builder.<EntityDragonLightningCharge>create(EntityDragonLightningCharge::new, SpawnGroup.MISC).setDimensions(0.9F, 0.9F), "lightning_dragon_charge");
    public static final RegistryObject<EntityType<EntityHippogryphEgg>> HIPPOGRYPH_EGG = registerEntity(EntityType.Builder.<EntityHippogryphEgg>create(EntityHippogryphEgg::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F), "hippogryph_egg");
    public static final RegistryObject<EntityType<EntityHippogryph>> HIPPOGRYPH = registerEntity(EntityType.Builder.create(EntityHippogryph::new, SpawnGroup.CREATURE).setDimensions(1.7F, 1.6F).maxTrackingRange(128), "hippogryph");
    public static final RegistryObject<EntityType<EntityStoneStatue>> STONE_STATUE = registerEntity(EntityType.Builder.create(EntityStoneStatue::new, SpawnGroup.CREATURE).setDimensions(0.5F, 0.5F), "stone_statue");
    public static final RegistryObject<EntityType<EntityGorgon>> GORGON = registerEntity(EntityType.Builder.create(EntityGorgon::new, SpawnGroup.CREATURE).setDimensions(0.8F, 1.99F), "gorgon");
    public static final RegistryObject<EntityType<EntityPixie>> PIXIE = registerEntity(EntityType.Builder.create(EntityPixie::new, SpawnGroup.CREATURE).setDimensions(0.4F, 0.8F), "pixie");
    public static final RegistryObject<EntityType<EntityCyclops>> CYCLOPS = registerEntity(EntityType.Builder.create(EntityCyclops::new, SpawnGroup.CREATURE).setDimensions(1.95F, 7.4F).maxTrackingRange(8), "cyclops");
    public static final RegistryObject<EntityType<EntitySiren>> SIREN = registerEntity(EntityType.Builder.create(EntitySiren::new, SpawnGroup.CREATURE).setDimensions(1.6F, 0.9F), "siren");
    public static final RegistryObject<EntityType<EntityHippocampus>> HIPPOCAMPUS = registerEntity(EntityType.Builder.create(EntityHippocampus::new, SpawnGroup.CREATURE).setDimensions(1.95F, 0.95F), "hippocampus");
    public static final RegistryObject<EntityType<EntityDeathWorm>> DEATH_WORM = registerEntity(EntityType.Builder.create(EntityDeathWorm::new, SpawnGroup.CREATURE).setDimensions(0.8F, 0.8F).maxTrackingRange(128), "deathworm");
    public static final RegistryObject<EntityType<EntityDeathWormEgg>> DEATH_WORM_EGG = registerEntity(EntityType.Builder.<EntityDeathWormEgg>create(EntityDeathWormEgg::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F), "deathworm_egg");
    public static final RegistryObject<EntityType<EntityCockatrice>> COCKATRICE = registerEntity(EntityType.Builder.create(EntityCockatrice::new, SpawnGroup.CREATURE).setDimensions(1.1F, 1F), "cockatrice");
    public static final RegistryObject<EntityType<EntityCockatriceEgg>> COCKATRICE_EGG = registerEntity(EntityType.Builder.<EntityCockatriceEgg>create(EntityCockatriceEgg::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F), "cockatrice_egg");
    public static final RegistryObject<EntityType<EntityStymphalianBird>> STYMPHALIAN_BIRD = registerEntity(EntityType.Builder.create(EntityStymphalianBird::new, SpawnGroup.CREATURE).setDimensions(1.3F, 1.2F).maxTrackingRange(128), "stymphalian_bird");
    public static final RegistryObject<EntityType<EntityStymphalianFeather>> STYMPHALIAN_FEATHER = registerEntity(EntityType.Builder.<EntityStymphalianFeather>create(EntityStymphalianFeather::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F), "stymphalian_feather");
    public static final RegistryObject<EntityType<EntityStymphalianArrow>> STYMPHALIAN_ARROW = registerEntity(EntityType.Builder.<EntityStymphalianArrow>create(EntityStymphalianArrow::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F), "stymphalian_arrow");
    public static final RegistryObject<EntityType<EntityTroll>> TROLL = registerEntity(EntityType.Builder.create(EntityTroll::new, SpawnGroup.MONSTER).setDimensions(1.2F, 3.5F), "troll");
    public static final RegistryObject<EntityType<EntityMyrmexWorker>> MYRMEX_WORKER = registerEntity(EntityType.Builder.create(EntityMyrmexWorker::new, SpawnGroup.CREATURE).setDimensions(0.9F, 0.9F), "myrmex_worker");
    public static final RegistryObject<EntityType<EntityMyrmexSoldier>> MYRMEX_SOLDIER = registerEntity(EntityType.Builder.create(EntityMyrmexSoldier::new, SpawnGroup.CREATURE).setDimensions(1.2F, 0.95F), "myrmex_soldier");
    public static final RegistryObject<EntityType<EntityMyrmexSentinel>> MYRMEX_SENTINEL = registerEntity(EntityType.Builder.create(EntityMyrmexSentinel::new, SpawnGroup.CREATURE).setDimensions(1.3F, 1.95F), "myrmex_sentinel");
    public static final RegistryObject<EntityType<EntityMyrmexRoyal>> MYRMEX_ROYAL = registerEntity(EntityType.Builder.create(EntityMyrmexRoyal::new, SpawnGroup.CREATURE).setDimensions(1.9F, 1.86F), "myrmex_royal");
    public static final RegistryObject<EntityType<EntityMyrmexQueen>> MYRMEX_QUEEN = registerEntity(EntityType.Builder.create(EntityMyrmexQueen::new, SpawnGroup.CREATURE).setDimensions(2.9F, 1.86F), "myrmex_queen");
    public static final RegistryObject<EntityType<EntityMyrmexEgg>> MYRMEX_EGG = registerEntity(EntityType.Builder.create(EntityMyrmexEgg::new, SpawnGroup.MISC).setDimensions(0.45F, 0.55F), "myrmex_egg");
    public static final RegistryObject<EntityType<EntityAmphithere>> AMPHITHERE = registerEntity(EntityType.Builder.create(EntityAmphithere::new, SpawnGroup.CREATURE).setDimensions(2.5F, 1.25F).maxTrackingRange(128), "amphithere");
    public static final RegistryObject<EntityType<EntityAmphithereArrow>> AMPHITHERE_ARROW = registerEntity(EntityType.Builder.<EntityAmphithereArrow>create(EntityAmphithereArrow::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F), "amphithere_arrow");
    public static final RegistryObject<EntityType<EntitySeaSerpent>> SEA_SERPENT = registerEntity(EntityType.Builder.create(EntitySeaSerpent::new, SpawnGroup.CREATURE).setDimensions(0.5F, 0.5F).maxTrackingRange(256), "sea_serpent");
    public static final RegistryObject<EntityType<EntitySeaSerpentBubbles>> SEA_SERPENT_BUBBLES = registerEntity(EntityType.Builder.<EntitySeaSerpentBubbles>create(EntitySeaSerpentBubbles::new, SpawnGroup.MISC).setDimensions(0.9F, 0.9F), "sea_serpent_bubbles");
    public static final RegistryObject<EntityType<EntitySeaSerpentArrow>> SEA_SERPENT_ARROW = registerEntity(EntityType.Builder.<EntitySeaSerpentArrow>create(EntitySeaSerpentArrow::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F), "sea_serpent_arrow");
    public static final RegistryObject<EntityType<EntityChainTie>> CHAIN_TIE = registerEntity(EntityType.Builder.<EntityChainTie>create(EntityChainTie::new, SpawnGroup.MISC).setDimensions(0.8F, 0.9F), "chain_tie");
    public static final RegistryObject<EntityType<EntityPixieCharge>> PIXIE_CHARGE = registerEntity(EntityType.Builder.<EntityPixieCharge>create(EntityPixieCharge::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F), "pixie_charge");
    public static final RegistryObject<EntityType<EntityMyrmexSwarmer>> MYRMEX_SWARMER = registerEntity(EntityType.Builder.create(EntityMyrmexSwarmer::new, SpawnGroup.CREATURE).setDimensions(0.5F, 0.5F), "myrmex_swarmer");
    public static final RegistryObject<EntityType<EntityTideTrident>> TIDE_TRIDENT = registerEntity(EntityType.Builder.<EntityTideTrident>create(EntityTideTrident::new, SpawnGroup.MISC).setDimensions(0.85F, 0.5F), "tide_trident");
    public static final RegistryObject<EntityType<EntityMobSkull>> MOB_SKULL = registerEntity(EntityType.Builder.create(EntityMobSkull::new, SpawnGroup.MISC).setDimensions(0.85F, 0.85F), "mob_skull");
    public static final RegistryObject<EntityType<EntityDreadThrall>> DREAD_THRALL = registerEntity(EntityType.Builder.create(EntityDreadThrall::new, SpawnGroup.MONSTER).setDimensions(0.6F, 1.8F), "dread_thrall");
    public static final RegistryObject<EntityType<EntityDreadGhoul>> DREAD_GHOUL = registerEntity(EntityType.Builder.create(EntityDreadGhoul::new, SpawnGroup.MONSTER).setDimensions(0.6F, 1.8F), "dread_ghoul");
    public static final RegistryObject<EntityType<EntityDreadBeast>> DREAD_BEAST = registerEntity(EntityType.Builder.create(EntityDreadBeast::new, SpawnGroup.MONSTER).setDimensions(1.2F, 0.9F), "dread_beast");
    public static final RegistryObject<EntityType<EntityDreadScuttler>> DREAD_SCUTTLER = registerEntity(EntityType.Builder.create(EntityDreadScuttler::new, SpawnGroup.MONSTER).setDimensions(1.5F, 1.3F), "dread_scuttler");
    public static final RegistryObject<EntityType<EntityDreadLich>> DREAD_LICH = registerEntity(EntityType.Builder.create(EntityDreadLich::new, SpawnGroup.MONSTER).setDimensions(0.6F, 1.8F), "dread_lich");
    public static final RegistryObject<EntityType<EntityDreadLichSkull>> DREAD_LICH_SKULL = registerEntity(EntityType.Builder.<EntityDreadLichSkull>create(EntityDreadLichSkull::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F), "dread_lich_skull");
    public static final RegistryObject<EntityType<EntityDreadKnight>> DREAD_KNIGHT = registerEntity(EntityType.Builder.create(EntityDreadKnight::new, SpawnGroup.MONSTER).setDimensions(0.6F, 1.8F), "dread_knight");
    public static final RegistryObject<EntityType<EntityDreadHorse>> DREAD_HORSE = registerEntity(EntityType.Builder.create(EntityDreadHorse::new, SpawnGroup.MONSTER).setDimensions(1.3964844F, 1.6F), "dread_horse");
    public static final RegistryObject<EntityType<EntityHydra>> HYDRA = registerEntity(EntityType.Builder.create(EntityHydra::new, SpawnGroup.CREATURE).setDimensions(2.8F, 1.39F), "hydra");
    public static final RegistryObject<EntityType<EntityHydraBreath>> HYDRA_BREATH = registerEntity(EntityType.Builder.<EntityHydraBreath>create(EntityHydraBreath::new, SpawnGroup.MISC).setDimensions(0.9F, 0.9F), "hydra_breath");
    public static final RegistryObject<EntityType<EntityHydraArrow>> HYDRA_ARROW = registerEntity(EntityType.Builder.<EntityHydraArrow>create(EntityHydraArrow::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F), "hydra_arrow");
    public static final RegistryObject<EntityType<EntityGhost>> GHOST = registerEntity(EntityType.Builder.create(EntityGhost::new, SpawnGroup.MONSTER).setDimensions(0.8F, 1.9F).makeFireImmune(), "ghost");
    public static final RegistryObject<EntityType<EntityGhostSword>> GHOST_SWORD = registerEntity(EntityType.Builder.<EntityGhostSword>create(EntityGhostSword::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F), "ghost_sword");
    public static HashMap<String, Boolean> LOADED_ENTITIES;

    static {
        LOADED_ENTITIES = new HashMap<>();
        LOADED_ENTITIES.put("HIPPOGRYPH", false);
        LOADED_ENTITIES.put("DREAD_LICH", false);
        LOADED_ENTITIES.put("COCKATRICE", false);
        LOADED_ENTITIES.put("AMPHITHERE", false);
        LOADED_ENTITIES.put("TROLL_F", false);
        LOADED_ENTITIES.put("TROLL_S", false);
        LOADED_ENTITIES.put("TROLL_M", false);
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(EntityType.Builder<T> builder, String entityName) {
        return ENTITIES.register(entityName, () -> builder.build(entityName));
    }

    public static void bakeAttributes() {
        FabricDefaultAttributeRegistry.register(DRAGON_EGG.get(), EntityDragonEgg.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(DRAGON_SKULL.get(), EntityDragonSkull.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(FIRE_DRAGON.get(), EntityFireDragon.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(ICE_DRAGON.get(), EntityIceDragon.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(LIGHTNING_DRAGON.get(), EntityLightningDragon.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(HIPPOGRYPH.get(), EntityHippogryph.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(GORGON.get(), EntityGorgon.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(STONE_STATUE.get(), EntityStoneStatue.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(PIXIE.get(), EntityPixie.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(CYCLOPS.get(), EntityCyclops.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(SIREN.get(), EntitySiren.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(HIPPOCAMPUS.get(), EntityHippocampus.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(DEATH_WORM.get(), EntityDeathWorm.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(COCKATRICE.get(), EntityCockatrice.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(STYMPHALIAN_BIRD.get(), EntityStymphalianBird.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(TROLL.get(), EntityTroll.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(MYRMEX_WORKER.get(), EntityMyrmexWorker.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(MYRMEX_SOLDIER.get(), EntityMyrmexSoldier.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(MYRMEX_SENTINEL.get(), EntityMyrmexSentinel.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(MYRMEX_ROYAL.get(), EntityMyrmexRoyal.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(MYRMEX_QUEEN.get(), EntityMyrmexQueen.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(MYRMEX_EGG.get(), EntityMyrmexEgg.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(MYRMEX_SWARMER.get(), EntityMyrmexSwarmer.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(AMPHITHERE.get(), EntityAmphithere.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(SEA_SERPENT.get(), EntitySeaSerpent.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(MOB_SKULL.get(), EntityMobSkull.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(DREAD_THRALL.get(), EntityDreadThrall.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(DREAD_LICH.get(), EntityDreadLich.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(DREAD_BEAST.get(), EntityDreadBeast.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(DREAD_HORSE.get(), EntityDreadHorse.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(DREAD_GHOUL.get(), EntityDreadGhoul.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(DREAD_KNIGHT.get(), EntityDreadKnight.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(DREAD_SCUTTLER.get(), EntityDreadScuttler.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(HYDRA.get(), EntityHydra.bakeAttributes().build());
        FabricDefaultAttributeRegistry.register(GHOST.get(), EntityGhost.bakeAttributes().build());
    }

    public static void commonSetup() {
        SpawnRestriction.register(HIPPOGRYPH.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityHippogryph::canMobSpawn);
        SpawnRestriction.register(TROLL.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTroll::canTrollSpawnOn);
        SpawnRestriction.register(DREAD_LICH.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityDreadLich::canLichSpawnOn);
        SpawnRestriction.register(COCKATRICE.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityCockatrice::canMobSpawn);
        SpawnRestriction.register(AMPHITHERE.get(), SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING, EntityAmphithere::canAmphithereSpawnOn);
    }

    public static void addSpawners() {
        if (IafConfig.spawnHippogryphs) {
            BiomeModifications.addSpawn(context -> BiomeConfig.test(BiomeConfig.hippogryphBiomes, context.getBiomeRegistryEntry()), SpawnGroup.CREATURE, IafEntityRegistry.HIPPOGRYPH.get(), IafConfig.hippogryphSpawnRate, 1, 1);
            LOADED_ENTITIES.put("HIPPOGRYPH", true);
        }
        if (IafConfig.spawnLiches) {
            BiomeModifications.addSpawn(context -> BiomeConfig.test(BiomeConfig.mausoleumBiomes, context.getBiomeRegistryEntry()), SpawnGroup.MONSTER, IafEntityRegistry.DREAD_LICH.get(), IafConfig.lichSpawnRate, 1, 1);
            LOADED_ENTITIES.put("DREAD_LICH", true);
        }
        if (IafConfig.spawnCockatrices) {
            BiomeModifications.addSpawn(context -> BiomeConfig.test(BiomeConfig.cockatriceBiomes, context.getBiomeRegistryEntry()), SpawnGroup.CREATURE, IafEntityRegistry.COCKATRICE.get(), IafConfig.cockatriceSpawnRate, 1, 2);
            LOADED_ENTITIES.put("COCKATRICE", true);
        }
        if (IafConfig.spawnAmphitheres) {
            BiomeModifications.addSpawn(context -> BiomeConfig.test(BiomeConfig.amphithereBiomes, context.getBiomeRegistryEntry()), SpawnGroup.CREATURE, IafEntityRegistry.AMPHITHERE.get(), IafConfig.amphithereSpawnRate, 1, 3);
            LOADED_ENTITIES.put("AMPHITHERE", true);
        }
        if (IafConfig.spawnTrolls) {
            BiomeModifications.addSpawn(context -> BiomeConfig.test(BiomeConfig.forestTrollBiomes, context.getBiomeRegistryEntry()) ||
                    BiomeConfig.test(BiomeConfig.snowyTrollBiomes, context.getBiomeRegistryEntry()) ||
                    BiomeConfig.test(BiomeConfig.mountainTrollBiomes, context.getBiomeRegistryEntry()), SpawnGroup.MONSTER, IafEntityRegistry.TROLL.get(), IafConfig.trollSpawnRate, 1, 3);
            LOADED_ENTITIES.put("TROLL_F", true);
            LOADED_ENTITIES.put("TROLL_S", true);
            LOADED_ENTITIES.put("TROLL_M", true);
        }

    }
}
