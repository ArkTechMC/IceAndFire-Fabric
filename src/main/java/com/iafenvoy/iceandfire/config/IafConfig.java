package com.iafenvoy.iceandfire.config;

import com.iafenvoy.iceandfire.IceAndFire;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IafConfig {
    private static IafConfig INSTANCE = null;
    private static final int CURRENT_VERSION = 0;
    private static final String configPath = "./config/iceandfire/common.json";
    private static final String backupPath = "./config/iceandfire/backup/";
    // Version key for identify
    public int version = CURRENT_VERSION;
    public final DragonConfig dragon = new DragonConfig();
    public final HippogryphsConfig hippogryphs = new HippogryphsConfig();
    public final PixieConfig pixie = new PixieConfig();
    public final CyclopsConfig cyclops = new CyclopsConfig();
    public final SirenConfig siren = new SirenConfig();
    public final GorgonConfig gorgon = new GorgonConfig();
    public final DeathwormConfig deathworm = new DeathwormConfig();
    public final CockatriceConfig cockatrice = new CockatriceConfig();
    public final StymphalianBirdConfig stymphalianBird = new StymphalianBirdConfig();
    public final TrollConfig troll = new TrollConfig();
    public final MyrmexConfig myrmex = new MyrmexConfig();
    public final AmphithereConfig amphithere = new AmphithereConfig();
    public final SeaSerpentConfig seaSerpent = new SeaSerpentConfig();
    public final LichConfig lich = new LichConfig();
    public final Hydra hydra = new Hydra();
    public final GhostConfig ghost = new GhostConfig();
    public final Armors armors = new Armors();
    public final WorldGenConfig worldGen = new WorldGenConfig();
    public final double dreadQueenMaxHealth = 750;
    public final int hippocampusSpawnChance = 40;
    public final double hippocampusSwimSpeedMod = 1F;
    public final boolean allowAttributeOverriding = true;

    public static final class DragonConfig {
        public final Generate generate = new Generate();
        public final Behaviour behaviour = new Behaviour();
        public final Drop drop = new Drop();
        public final double maxHealth = 500;
        public final int eggBornTime = 7200;
        public final int maxPathingNodes = 5000;
        public final boolean villagersFear = true;
        public final boolean animalsFear = true;

        public static final class Generate {
            public final boolean skeletons = true;
            public final int skeletonChance = 300;
            public final int denChance = 260;
            public final int roostChance = 480;
            public final int denGoldAmount = 4;
            public final int oreRatio = 45;
        }

        public static final class Behaviour {
            public final int griefing = 0;
            public final boolean tamedGriefing = true;
            public final int flapNoiseDistance = 4;
            public final int fluteDistance = 8;
            public final int attackDamage = 17;
            public final double attackDamageFire = 2F;
            public final double attackDamageIce = 2.5F;
            public final double attackDamageLightning = 3.5F;
            public final int maxFlight = 256;
            public final int goldSearchLength = 30;
            public final boolean canHealFromBiting = false;
            public final boolean canDespawn = true;
            public final boolean sleep = true;
            public final boolean digWhenStuck = true;
            public final int breakBlockCooldown = 5;
            public final int targetSearchLength = 128;
            public final int wanderFromHomeDistance = 40;
            public final int hungerTickRate = 3000;
            public final boolean movedWronglyFix = false;
            public final double blockBreakingDropChance = 0.1D;
            public final boolean explosiveBreath = false;
            public final boolean chunkLoadSummonCrystal = true;
            public final double dragonFlightSpeedMod = 1F;
        }

        public static final class Drop {
            public final boolean skull = true;
            public final boolean heart = true;
            public final boolean blood = true;
        }
    }

    public static final class HippogryphsConfig {
        public final boolean spawn = true;
        public final int spawnWeight = 2;
        public final double fightSpeedMod = 1F;
    }

    public static final class PixieConfig {
        public final int spawnChance = 60;
        public final int size = 5;
        public final boolean stealItems = true;
    }

    public static final class CyclopsConfig {
        public final int spawnWanderingChance = 900;
        public final int spawnCaveChance = 170;
        public final int sheepSearchLength = 17;
        public final double maxHealth = 150;
        public final double attackStrength = 15;
        public final double biteStrength = 40;
        public final boolean griefing = true;
    }

    public static final class SirenConfig {
        public final double maxHealth = 50D;
        public final boolean shader = true;
        public final int maxSingTime = 12000;
        public final int timeBetweenSongs = 2000;
        public final int spawnChance = 400;
    }

    public static final class GorgonConfig {
        public final boolean generateTemple = true;
        public final double maxHealth = 100D;
    }

    public static final class DeathwormConfig {
        public final int targetSearchLength = 48;
        public final double maxHealth = 10D;
        public final double attackStrength = 3D;
        public final boolean attackMonsters = true;
        public final int spawnChance = 30;
    }

    public static final class CockatriceConfig {
        public final int chickenSearchLength = 32;
        public final int eggChance = 30;
        public final double maxHealth = 40.0D;
        public final boolean chickensLayRottenEggs = true;
        public final boolean spawn = true;
        public final int spawnWeight = 4;
    }

    public static final class StymphalianBirdConfig {
        public final int targetSearchLength = 48;
        public final int featherDropChance = 25;
        public final double featherAttackStrength = 1F;
        public final int flockLength = 40;
        public final int flightHeight = 80;
        public final boolean dataTagDrops = true;
        public final boolean attackAnimals = false;
        public final int spawnChance = 80;
    }

    public static final class TrollConfig {
        public final boolean spawn = true;
        public final int spawnWeight = 60;
        public final boolean dropWeapon = true;
        public final double maxHealth = 50;
        public final double attackStrength = 10;
    }

    public static final class MyrmexConfig {
        public final int pregnantTicks = 2500;
        public final int eggTicks = 3000;
        public final int larvaTicks = 35000;
        public final int colonyGenChance = 150;
        public final int colonySize = 80;
        public final int maximumWanderRadius = 50;
        public final boolean hiveIgnoreDaytime = false;
        public final double baseAttackStrength = 3.0D;
    }

    public static final class AmphithereConfig {
        public final boolean spawn = true;
        public final int spawnWeight = 50;
        public final float villagerSearchLength = 48;
        public final int tameTime = 400;
        public final double flightSpeed = 1.75F;
        public final double maxHealth = 50D;
        public final double attackStrength = 7D;
    }

    public static final class SeaSerpentConfig {
        public final int spawnChance = 250;
        public final boolean griefing = true;
        public final double baseHealth = 20D;
        public final double attackStrength = 4D;
    }

    public static final class LichConfig {
        public final boolean spawn = true;
        public final int spawnWeight = 4;
        public final int spawnChance = 30;
    }

    public static final class Hydra {
        public final double maxHealth = 250D;
        public final int spawnChance = 120;
    }

    public static final class GhostConfig {
        public final boolean generateGraveyards = true;
        public final double maxHealth = 30;
        public final double attackStrength = 3;
        public final boolean fromPlayerDeaths = true;
    }

    public static final class Armors {
        public final Dragon dragon = new Dragon();
        public final Dragonsteel dragonsteel = new Dragonsteel();

        public static final class Dragon {
            public final boolean fireAbility = true;
            public final boolean iceAbility = true;
            public final boolean lightningAbility = true;
        }

        public static final class Dragonsteel {
            public final double baseDamage = 25F;
            public final int baseArmor = 12;
            public final float baseArmorToughness = 6;
            public final int baseDurability = 8000;
            public final int baseDurabilityEquipment = 8000;
        }
    }

    public static final class WorldGenConfig {
        public final double dangerousDistanceLimit = 1000;
        public final double dangerousSeparationLimit = 300;
        public final boolean generateMausoleums = true;
        public final int villagerHouseWeight = 5;
    }

    public static IafConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = ConfigLoader.load(IafConfig.class, configPath, new IafConfig());
            if (INSTANCE.version != CURRENT_VERSION) {
                IceAndFire.LOGGER.warn("Wrong common config version {} for mod Ice And Fire! Automatically transform to version {} and backup old one.", INSTANCE.version, CURRENT_VERSION);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                ConfigLoader.copy(configPath, backupPath + "common_" + sdf.format(new Date()) + ".json");
                ConfigLoader.save(configPath, INSTANCE = new IafConfig());
            }
        }
        return INSTANCE;
    }
}
