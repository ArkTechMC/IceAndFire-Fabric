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
    public DragonConfig dragon = new DragonConfig();
    public HippogryphsConfig hippogryphs = new HippogryphsConfig();
    public PixieConfig pixie = new PixieConfig();
    public CyclopsConfig cyclops = new CyclopsConfig();
    public SirenConfig siren = new SirenConfig();
    public GorgonConfig gorgon = new GorgonConfig();
    public DeathwormConfig deathworm = new DeathwormConfig();
    public CockatriceConfig cockatrice = new CockatriceConfig();
    public StymphalianBirdConfig stymphalianBird = new StymphalianBirdConfig();
    public TrollConfig troll = new TrollConfig();
    public MyrmexConfig myrmex = new MyrmexConfig();
    public AmphithereConfig amphithere = new AmphithereConfig();
    public SeaSerpentConfig seaSerpent = new SeaSerpentConfig();
    public LichConfig lich = new LichConfig();
    public Hydra hydra = new Hydra();
    public GhostConfig ghost = new GhostConfig();
    public Armors armors = new Armors();
    public WorldGenConfig worldGen = new WorldGenConfig();
    public double dreadQueenMaxHealth = 750;
    public int hippocampusSpawnChance = 40;
    public double hippocampusSwimSpeedMod = 1F;
    public boolean allowAttributeOverriding = true;

    public static class DragonConfig {
        public Generate generate = new Generate();
        public Behaviour behaviour = new Behaviour();
        public Drop drop = new Drop();
        public double maxHealth = 500;
        public int eggBornTime = 7200;
        public int maxPathingNodes = 5000;
        public boolean villagersFear = true;
        public boolean animalsFear = true;

        public static class Generate {
            public boolean skeletons = true;
            public int skeletonChance = 300;
            public int denChance = 260;
            public int roostChance = 480;
            public int denGoldAmount = 4;
            public int oreRatio = 45;
        }

        public static class Behaviour {
            public int griefing = 0;
            public boolean tamedGriefing = true;
            public int flapNoiseDistance = 4;
            public int fluteDistance = 8;
            public int attackDamage = 17;
            public double attackDamageFire = 2F;
            public double attackDamageIce = 2.5F;
            public double attackDamageLightning = 3.5F;
            public int maxFlight = 256;
            public int goldSearchLength = 30;
            public boolean canHealFromBiting = false;
            public boolean canDespawn = true;
            public boolean sleep = true;
            public boolean digWhenStuck = true;
            public int breakBlockCooldown = 5;
            public int targetSearchLength = 128;
            public int wanderFromHomeDistance = 40;
            public int hungerTickRate = 3000;
            public boolean movedWronglyFix = false;
            public double blockBreakingDropChance = 0.1D;
            public boolean explosiveBreath = false;
            public boolean chunkLoadSummonCrystal = true;
            public double dragonFlightSpeedMod = 1F;
        }

        public static class Drop {
            public boolean skull = true;
            public boolean heart = true;
            public boolean blood = true;
        }
    }

    public static class HippogryphsConfig {
        public boolean spawn = true;
        public int spawnWeight = 2;
        public double fightSpeedMod = 1F;
    }

    public static class PixieConfig {
        public int spawnChance = 60;
        public int size = 5;
        public boolean stealItems = true;
    }

    public static class CyclopsConfig {
        public int spawnWanderingChance = 900;
        public int spawnCaveChance = 170;
        public int sheepSearchLength = 17;
        public double maxHealth = 150;
        public double attackStrength = 15;
        public double biteStrength = 40;
        public boolean griefing = true;
    }

    public static class SirenConfig {
        public double maxHealth = 50D;
        public boolean shader = true;
        public int maxSingTime = 12000;
        public int timeBetweenSongs = 2000;
        public int spawnChance = 400;
    }

    public static class GorgonConfig {
        public boolean generateTemple = true;
        public double maxHealth = 100D;
    }

    public static class DeathwormConfig {
        public int targetSearchLength = 48;
        public double maxHealth = 10D;
        public double attackStrength = 3D;
        public boolean attackMonsters = true;
        public int spawnChance = 30;
    }

    public static class CockatriceConfig {
        public int chickenSearchLength = 32;
        public int eggChance = 30;
        public double maxHealth = 40.0D;
        public boolean chickensLayRottenEggs = true;
        public boolean spawn = true;
        public int spawnWeight = 4;
    }

    public static class StymphalianBirdConfig {
        public int targetSearchLength = 48;
        public int featherDropChance = 25;
        public double featherAttackStrength = 1F;
        public int flockLength = 40;
        public int flightHeight = 80;
        public boolean dataTagDrops = true;
        public boolean attackAnimals = false;
        public int spawnChance = 80;
    }

    public static class TrollConfig {
        public boolean spawn = true;
        public int spawnWeight = 60;
        public boolean dropWeapon = true;
        public double maxHealth = 50;
        public double attackStrength = 10;
    }

    public static class MyrmexConfig {
        public int pregnantTicks = 2500;
        public int eggTicks = 3000;
        public int larvaTicks = 35000;
        public int colonyGenChance = 150;
        public int colonySize = 80;
        public int maximumWanderRadius = 50;
        public boolean hiveIgnoreDaytime = false;
        public double baseAttackStrength = 3.0D;
    }

    public static class AmphithereConfig {
        public boolean spawn = true;
        public int spawnWeight = 50;
        public float villagerSearchLength = 48;
        public int tameTime = 400;
        public double flightSpeed = 1.75F;
        public double maxHealth = 50D;
        public double attackStrength = 7D;
    }

    public static class SeaSerpentConfig {
        public int spawnChance = 250;
        public boolean griefing = true;
        public double baseHealth = 20D;
        public double attackStrength = 4D;
    }

    public static class LichConfig {
        public boolean spawn = true;
        public int spawnWeight = 4;
        public int spawnChance = 30;
    }

    public static class Hydra {
        public double maxHealth = 250D;
        public int spawnChance = 120;
    }

    public static class GhostConfig {
        public boolean generateGraveyards = true;
        public double maxHealth = 30;
        public double attackStrength = 3;
        public boolean fromPlayerDeaths = true;
    }

    public static class Armors {
        public Dragon dragon = new Dragon();
        public Dragonsteel dragonsteel = new Dragonsteel();

        public static class Dragon {
            public boolean fireAbility = true;
            public boolean iceAbility = true;
            public boolean lightningAbility = true;
        }

        public static class Dragonsteel {
            public double baseDamage = 25F;
            public int baseArmor = 12;
            public float baseArmorToughness = 6;
            public int baseDurability = 8000;
            public int baseDurabilityEquipment = 8000;
        }
    }

    public static class WorldGenConfig {
        public double dangerousDistanceLimit = 1000;
        public double dangerousSeparationLimit = 300;
        public boolean generateMausoleums = true;
        public int villagerHouseWeight = 5;
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
