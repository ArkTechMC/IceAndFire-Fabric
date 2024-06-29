package com.iafenvoy.iceandfire.config;

import com.iafenvoy.iceandfire.IceAndFire;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = IceAndFire.MOD_ID)
public class IafConfig implements ConfigData {
    @ConfigEntry.Gui.Excluded
    public static final int CURRENT_VERSION = 1;
    @ConfigEntry.Gui.Excluded
    public static final String configPath = "./config/iceandfire/common.json";
    @ConfigEntry.Gui.Excluded
    public static final String backupPath = "./config/iceandfire/backup/";
    // Version key for identify
    @ConfigEntry.Gui.Excluded
    public int version = CURRENT_VERSION;
    @ConfigEntry.Gui.CollapsibleObject
    public ClientConfig client = new ClientConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public DragonConfig dragon = new DragonConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public HippogryphsConfig hippogryphs = new HippogryphsConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public PixieConfig pixie = new PixieConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public CyclopsConfig cyclops = new CyclopsConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public SirenConfig siren = new SirenConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public GorgonConfig gorgon = new GorgonConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public DeathwormConfig deathworm = new DeathwormConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public CockatriceConfig cockatrice = new CockatriceConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public StymphalianBirdConfig stymphalianBird = new StymphalianBirdConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public TrollConfig troll = new TrollConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public MyrmexConfig myrmex = new MyrmexConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public AmphithereConfig amphithere = new AmphithereConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public SeaSerpentConfig seaSerpent = new SeaSerpentConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public LichConfig lich = new LichConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public Hydra hydra = new Hydra();
    @ConfigEntry.Gui.CollapsibleObject
    public Hippocampus hippocampus = new Hippocampus();
    @ConfigEntry.Gui.CollapsibleObject
    public GhostConfig ghost = new GhostConfig();
    @ConfigEntry.Gui.CollapsibleObject
    public Armors armors = new Armors();
    @ConfigEntry.Gui.CollapsibleObject
    public WorldGenConfig worldGen = new WorldGenConfig();
    public double dreadQueenMaxHealth = 750;
    public boolean allowAttributeOverriding = true;

    public static class ClientConfig implements ConfigData {
        public boolean customMainMenu = true;
        //TODO
        public boolean dragonAuto3rdPerson = false;
    }

    public static class DragonConfig implements ConfigData {
        @ConfigEntry.Gui.CollapsibleObject
        public Generate generate = new Generate();
        @ConfigEntry.Gui.CollapsibleObject
        public Behaviour behaviour = new Behaviour();
        @ConfigEntry.Gui.CollapsibleObject
        public Drop drop = new Drop();
        public double maxHealth = 500;
        public int eggBornTime = 7200;
        public int maxPathingNodes = 5000;
        public boolean villagersFear = true;
        public boolean animalsFear = true;

        public static class Generate implements ConfigData {
            public boolean skeletons = true;
            public int skeletonChance = 300;
            public int denChance = 260;
            public int roostChance = 480;
            public int denGoldAmount = 4;
            public int oreRatio = 45;
        }

        public static class Behaviour implements ConfigData {
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

        public static class Drop implements ConfigData {
            public boolean skull = true;
            public boolean heart = true;
            public boolean blood = true;
        }
    }

    public static class HippogryphsConfig implements ConfigData {
        public boolean spawn = true;
        public int spawnWeight = 2;
        public double fightSpeedMod = 1F;
    }

    public static class PixieConfig implements ConfigData {
        public int spawnChance = 60;
        public int size = 5;
        public boolean stealItems = true;
    }

    public static class CyclopsConfig implements ConfigData {
        public int spawnWanderingChance = 900;
        public int spawnCaveChance = 170;
        public int sheepSearchLength = 17;
        public double maxHealth = 150;
        public double attackDamage = 15;
        public double biteDamage = 40;
        public boolean griefing = true;
    }

    public static class SirenConfig implements ConfigData {
        public int spawnChance = 400;
        public double maxHealth = 50D;
        public boolean shader = true;
        public int maxSingTime = 12000;
        public int timeBetweenSongs = 2000;
    }

    public static class GorgonConfig implements ConfigData {
        public boolean generateTemple = true;
        public double maxHealth = 100D;
    }

    public static class DeathwormConfig implements ConfigData {
        public int spawnChance = 30;
        public int targetSearchLength = 48;
        public double maxHealth = 10D;
        public double attackDamage = 3D;
        public boolean attackMonsters = true;
    }

    public static class CockatriceConfig implements ConfigData {
        public boolean spawn = true;
        public int spawnWeight = 4;
        public int chickenSearchLength = 32;
        public int eggChance = 30;
        public double maxHealth = 40.0D;
        public boolean chickensLayRottenEggs = true;
    }

    public static class StymphalianBirdConfig implements ConfigData {
        public int spawnChance = 80;
        public int targetSearchLength = 48;
        public int featherDropChance = 25;
        public double featherAttackDamage = 1F;
        public int flockLength = 40;
        public int flightHeight = 80;
        public boolean dataTagDrops = true;
        public boolean attackAnimals = false;
    }

    public static class TrollConfig implements ConfigData {
        public boolean spawn = true;
        public int spawnWeight = 60;
        public boolean dropWeapon = true;
        public double maxHealth = 50;
        public double attackDamage = 10;
    }

    public static class MyrmexConfig implements ConfigData {
        public int pregnantTicks = 2500;
        public int eggTicks = 3000;
        public int larvaTicks = 35000;
        public int colonyGenChance = 150;
        public int colonySize = 80;
        public int maximumWanderRadius = 50;
        public boolean hiveIgnoreDaytime = false;
        public double baseAttackDamage = 3.0D;
    }

    public static class AmphithereConfig implements ConfigData {
        public boolean spawn = true;
        public int spawnWeight = 50;
        public float villagerSearchLength = 48;
        public int tameTime = 400;
        public double flightSpeed = 1.75F;
        public double maxHealth = 50D;
        public double attackDamage = 7D;
    }

    public static class SeaSerpentConfig implements ConfigData {
        public int spawnChance = 250;
        public boolean griefing = true;
        public double baseHealth = 20D;
        public double attackDamage = 4D;
    }

    public static class LichConfig implements ConfigData {
        public boolean spawn = true;
        public int spawnWeight = 4;
        public int spawnChance = 30;
    }

    public static class Hydra implements ConfigData {
        public double maxHealth = 250D;
        public int spawnChance = 120;
    }

    public static class Hippocampus implements ConfigData {
        public int spawnChance = 40;
        public double swimSpeedMod = 1F;
    }

    public static class GhostConfig implements ConfigData {
        public boolean generateGraveyards = true;
        public double maxHealth = 30;
        public double attackDamage = 3;
        public boolean fromPlayerDeaths = true;
    }

    public static class Armors implements ConfigData {
        @ConfigEntry.Gui.CollapsibleObject
        public Dragon dragon = new Dragon();
        @ConfigEntry.Gui.CollapsibleObject
        public DragonSteel dragonsteel = new DragonSteel();

        public static class Dragon implements ConfigData {
            public boolean fireAbility = true;
            public boolean iceAbility = true;
            public boolean lightningAbility = true;
        }

        public static class DragonSteel implements ConfigData {
            public double baseDamage = 25F;
            public int baseArmor = 12;
            public float baseArmorToughness = 6;
            public int baseDurability = 8000;
            public int baseDurabilityEquipment = 8000;
        }
    }

    public static class WorldGenConfig implements ConfigData {
        public double dangerousDistanceLimit = 1000;
        public double dangerousSeparationLimit = 300;
        public boolean generateMausoleums = true;
        public int villagerHouseWeight = 5;
    }

    public static IafConfig getInstance() {
        return AutoConfig.getConfigHolder(IafConfig.class).getConfig();
    }
}
