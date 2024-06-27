package com.iafenvoy.iceandfire.config;

import com.iafenvoy.iceandfire.IceAndFire;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IafConfig {
    private static IafConfig INSTANCE = null;
    private static final int CURRENT_VERSION = 1;
    private static final String configPath = "./config/iceandfire/common.json";
    private static final String backupPath = "./config/iceandfire/backup/";
    // Version key for identify
    public int version = CURRENT_VERSION;

    public DragonConfig dragon = new DragonConfig();

    public double dreadQueenMaxHealth = 750;

    public boolean spawnHippogryphs = true;
    public int hippogryphSpawnRate = 2;
    public boolean generateGorgonTemple = true;
    public double gorgonMaxHealth = 100D;
    public int spawnPixiesChance = 60;
    public int pixieVillageSize = 5;
    public boolean pixiesStealItems = true;
    public int spawnWanderingCyclopsChance = 900;
    public int spawnCyclopsCaveChance = 170;
    public int cyclopesSheepSearchLength = 17;
    public double cyclopsMaxHealth = 150;
    public double cyclopsAttackStrength = 15;
    public double cyclopsBiteStrength = 40;
    public boolean cyclopsGriefing = true;
    public double sirenMaxHealth = 50D;
    public boolean sirenShader = true;
    public int sirenMaxSingTime = 12000;
    public int sirenTimeBetweenSongs = 2000;
    public int generateSirenChance = 400;
    public int hippocampusSpawnChance = 40;
    public int deathWormTargetSearchLength = 48;
    public double deathWormMaxHealth = 10D;
    public double deathWormAttackStrength = 3D;
    public boolean deathWormAttackMonsters = true;
    public int deathWormSpawnRate = 30;
    public int cockatriceChickenSearchLength = 32;
    public int cockatriceEggChance = 30;
    public double cockatriceMaxHealth = 40.0D;
    public boolean chickensLayRottenEggs = true;
    public boolean spawnCockatrices = true;
    public int cockatriceSpawnRate = 4;
    public int stymphalianBirdTargetSearchLength = 48;
    public int stymphalianBirdFeatherDropChance = 25;
    public double stymphalianBirdFeatherAttackStength = 1F;
    public int stymphalianBirdFlockLength = 40;
    public int stymphalianBirdFlightHeight = 80;
    public boolean stymphalianBirdsDataTagDrops = true;
    public boolean stympahlianBirdAttackAnimals = false;
    public int stymphalianBirdSpawnChance = 80;
    public boolean spawnTrolls = true;
    public int trollSpawnRate = 60;
    public boolean trollsDropWeapon = true;
    public double trollMaxHealth = 50;
    public double trollAttackStrength = 10;
    public boolean villagersFearDragons = true;
    public boolean animalsFearDragons = true;
    public int myrmexPregnantTicks = 2500;
    public int myrmexEggTicks = 3000;
    public int myrmexLarvaTicks = 35000;
    public int myrmexColonyGenChance = 150;
    public int myrmexColonySize = 80;
    public int myrmexMaximumWanderRadius = 50;
    public boolean myrmexHiveIgnoreDaytime = false;
    public double myrmexBaseAttackStrength = 3.0D;
    public boolean spawnAmphitheres = true;
    public int amphithereSpawnRate = 50;
    public float amphithereVillagerSearchLength = 48;
    public int amphithereTameTime = 400;
    public double amphithereFlightSpeed = 1.75F;
    public double amphithereMaxHealth = 50D;
    public double amphithereAttackStrength = 7D;
    public int seaSerpentSpawnChance = 250;
    public boolean seaSerpentGriefing = true;
    public double seaSerpentBaseHealth = 20D;
    public double seaSerpentAttackStrength = 4D;
    public double dragonsteelBaseDamage = 25F;
    public int dragonsteelBaseArmor = 12;
    public float dragonsteelBaseArmorToughness = 6;
    public int dragonsteelBaseDurability = 8000;
    public int dragonsteelBaseDurabilityEquipment = 8000;
    public boolean dragonMovedWronglyFix = false;
    public boolean weezerTinkers = true;
    public double dragonBlockBreakingDropChance = 0.1D;
    public boolean generateMausoleums = true;
    public boolean spawnLiches = true;
    public int lichSpawnRate = 4;
    public int lichSpawnChance = 30;
    public double hydraMaxHealth = 250D;
    public int generateHydraChance = 120;
    public boolean explosiveDragonBreath = false;
    public double weezerTinkersDisarmChance = 0.2F;
    public boolean chunkLoadSummonCrystal = true;
    public double dangerousWorldGenDistanceLimit = 1000;
    public double dangerousWorldGenSeparationLimit = 300;
    public double dragonFlightSpeedMod = 1F;
    public double hippogryphFlightSpeedMod = 1F;
    public double hippocampusSwimSpeedMod = 1F;
    public boolean generateGraveyards = true;
    public double ghostMaxHealth = 30;
    public double ghostAttackStrength = 3;
    public boolean ghostsFromPlayerDeaths = true;

    public boolean dragonWeaponFireAbility = true;
    public boolean dragonWeaponIceAbility = true;
    public boolean dragonWeaponLightningAbility = true;
    public int villagerHouseWeight = 5;
    public boolean allowAttributeOverriding = true;

    public static class DragonConfig {
        public Generate generate = new Generate();
        public Behaviour behaviour = new Behaviour();
        public Drop drop = new Drop();
        public double maxHealth = 500;
        public int eggBornTime = 7200;
        public int maxPathingNodes = 5000;

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
        }

        public static class Drop {
            public boolean skull = true;
            public boolean heart = true;
            public boolean blood = true;
        }
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
