package com.iafenvoy.iceandfire.config;

import com.google.gson.JsonObject;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.jupiter.config.AutoInitConfigContainer;
import com.iafenvoy.jupiter.malilib.config.options.ConfigBoolean;
import com.iafenvoy.jupiter.malilib.config.options.ConfigDouble;
import com.iafenvoy.jupiter.malilib.config.options.ConfigInteger;
import net.minecraft.util.Identifier;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IafCommonConfig extends AutoInitConfigContainer {
    public static final IafCommonConfig INSTANCE = new IafCommonConfig();
    public static final int CURRENT_VERSION = 1;
    public static final String backupPath = "./config/iceandfire/";

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
    public HydraConfig hydra = new HydraConfig();
    public HippocampusConfig hippocampus = new HippocampusConfig();
    public GhostConfig ghost = new GhostConfig();
    public ArmorsConfig armors = new ArmorsConfig();
    public WorldGenConfig worldGen = new WorldGenConfig();
    public Misc misc = new Misc();

    public IafCommonConfig() {
        super(new Identifier("config.iceandfire.common"), "screen.iceandfire.common.title", "./config/iceandfire/iaf-common.json");
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected boolean shouldLoad(JsonObject obj) {
        int version = obj.get("version").getAsInt();
        if (version != CURRENT_VERSION) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                FileUtils.copyFile(new File(this.path), new File(IafCommonConfig.backupPath + "iceandfire_common_" + sdf.format(new Date()) + ".json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            IceAndFire.LOGGER.info("Wrong config version {} for mod {}! Automatically use version {} and backup old one.", version, IceAndFire.MOD_NAME, CURRENT_VERSION);
            return false;
        } else IceAndFire.LOGGER.info("{} config version match.", IceAndFire.MOD_NAME);
        return true;
    }

    @Override
    protected void writeCustomData(JsonObject obj) {
        obj.addProperty("version", CURRENT_VERSION);
    }

    public static class DragonConfig extends AutoInitConfigCategoryBase {
        public ConfigDouble maxHealth = new ConfigDouble("iceandfire.dragon.maxHealth", 500, 1, Double.MAX_VALUE);
        public ConfigInteger eggBornTime = new ConfigInteger("iceandfire.dragon.eggBornTime", 7200, 0, Integer.MAX_VALUE);
        public ConfigInteger maxPathingNodes = new ConfigInteger("iceandfire.dragon.maxPathingNodes", 5000, 0, Integer.MAX_VALUE);
        public ConfigBoolean villagersFear = new ConfigBoolean("iceandfire.dragon.villagersFear", true);
        public ConfigBoolean animalsFear = new ConfigBoolean("iceandfire.dragon.animalsFear", true);

        public ConfigBoolean generateSkeletons = new ConfigBoolean("iceandfire.dragon.generate.skeletons", true);
        public ConfigDouble generateSkeletonChance = new ConfigDouble("iceandfire.dragon.generate.skeletonChance", 1.0 / 300, 0, 1);
        public ConfigDouble generateDenChance = new ConfigDouble("iceandfire.dragon.generate.denChance", 1.0 / 260, 0, 1);
        public ConfigDouble generateRoostChance = new ConfigDouble("iceandfire.dragon.generate.roostChance", 1.0 / 480, 0, 1);
        public ConfigDouble generateDenGoldChance = new ConfigDouble("iceandfire.dragon.generate.denGoldAmount", 1.0 / 4, 0, 1);
        public ConfigDouble generateOreRatio = new ConfigDouble("iceandfire.dragon.generate.oreRatio", 1.0 / 45, 0, 1);

        public ConfigBoolean griefing = new ConfigBoolean("iceandfire.dragon.griefing", true);
        public ConfigBoolean tamedGriefing = new ConfigBoolean("iceandfire.dragon.tamedGriefing", true);
        public ConfigInteger flapNoiseDistance = new ConfigInteger("iceandfire.dragon.flapNoiseDistance", 4, 0, 32);
        public ConfigInteger fluteDistance = new ConfigInteger("iceandfire.dragon.fluteDistance", 8, 0, 32);
        public ConfigInteger attackDamage = new ConfigInteger("iceandfire.dragon.attackDamage", 17, 0, 100);
        public ConfigDouble attackDamageFire = new ConfigDouble("iceandfire.dragon.attackDamageFire", 2, 0, 5);
        public ConfigDouble attackDamageIce = new ConfigDouble("iceandfire.dragon.attackDamageIce", 2.5, 0, 5);
        public ConfigDouble attackDamageLightning = new ConfigDouble("iceandfire.dragon.attackDamageLightning", 3.5, 0, 5);
        public ConfigInteger maxFlight = new ConfigInteger("iceandfire.dragon.maxFlight", 256, 0, 384);
        public ConfigInteger goldSearchLength = new ConfigInteger("iceandfire.dragon.goldSearchLength", 30, 0, 100);
        public ConfigBoolean canHealFromBiting = new ConfigBoolean("iceandfire.dragon.canHealFromBiting", false);
        public ConfigBoolean canDespawn = new ConfigBoolean("iceandfire.dragon.despawn", true);
        public ConfigBoolean sleep = new ConfigBoolean("iceandfire.dragon.sleep", true);
        public ConfigBoolean digWhenStuck = new ConfigBoolean("iceandfire.dragon.digWhenStuck", true);
        public ConfigInteger breakBlockCooldown = new ConfigInteger("iceandfire.dragon.breakBlockCooldown", 0, 5, Integer.MAX_VALUE);
        public ConfigInteger targetSearchLength = new ConfigInteger("iceandfire.dragon.breakBlockCooldown", 128, 0, 1024);
        public ConfigInteger wanderFromHomeDistance = new ConfigInteger("iceandfire.dragon.wanderFromHomeDistance", 40, 0, 100);
        public ConfigInteger hungerTickRate = new ConfigInteger("iceandfire.dragon.hungerTickRate", 3000, 0, 10000);
        public ConfigBoolean movedWronglyFix = new ConfigBoolean("iceandfire.dragon.movedWronglyFix", false);
        public ConfigDouble blockBreakingDropChance = new ConfigDouble("iceandfire.dragon.blockBreakingDropChance", 0.1, 0, 1);
        public ConfigBoolean explosiveBreath = new ConfigBoolean("iceandfire.dragon.explosiveBreath", false);
        public ConfigBoolean chunkLoadSummonCrystal = new ConfigBoolean("iceandfire.dragon.chunkLoadSummonCrystal", true);
        public ConfigDouble dragonFlightSpeedMod = new ConfigDouble("iceandfire.dragon.dragonFlightSpeedMod", 1, 0.0001, 5);

        public ConfigBoolean lootSkull = new ConfigBoolean("iceandfire.dragon.loot.skull", true);
        public ConfigBoolean lootHeart = new ConfigBoolean("iceandfire.dragon.loot.heart", true);
        public ConfigBoolean lootBlood = new ConfigBoolean("iceandfire.dragon.loot.blood", true);

        public DragonConfig() {
            super("dragon", "iceandfire.category.dragon");
        }
    }

    public static class HippogryphsConfig extends AutoInitConfigCategoryBase {
        public ConfigBoolean spawn = new ConfigBoolean("iceandfire.hippogryphs.spawn", true);
        public ConfigInteger spawnWeight = new ConfigInteger("iceandfire.hippogryphs.spawnWeight", 2, 0, 10);
        public ConfigDouble fightSpeedMod = new ConfigDouble("iceandfire.hippogryphs.fightSpeedMod", 1, 0.0001, 5);

        public HippogryphsConfig() {
            super("hippogryphs", "iceandfire.category.hippogryphs");
        }
    }

    public static class PixieConfig extends AutoInitConfigCategoryBase {
        public ConfigDouble spawnChance = new ConfigDouble("iceandfire.pixie.spawnChance", 1.0 / 60, 0, 1);
        public ConfigInteger size = new ConfigInteger("iceandfire.pixie.size", 5, 0, 10);
        public ConfigBoolean stealItems = new ConfigBoolean("iceandfire.pixie.stealItems", true);

        public PixieConfig() {
            super("pixie", "iceandfire.category.pixie");
        }
    }

    public static class CyclopsConfig extends AutoInitConfigCategoryBase {
        public ConfigDouble spawnWanderingChance = new ConfigDouble("iceandfire.cyclops.spawnWanderingChance", 1.0 / 900, 0, 1);
        public ConfigDouble spawnCaveChance = new ConfigDouble("iceandfire.cyclops.spawnCaveChance", 1.0 / 170, 0, 1);
        public ConfigInteger sheepSearchLength = new ConfigInteger("iceandfire.cyclops.sheepSearchLength", 17, 0, 128);
        public ConfigDouble maxHealth = new ConfigDouble("iceandfire.cyclops.maxHealth", 150, 1, Double.MAX_VALUE);
        public ConfigDouble attackDamage = new ConfigDouble("iceandfire.cyclops.attackDamage", 15, 0, 50);
        public ConfigDouble biteDamage = new ConfigDouble("iceandfire.cyclops.biteDamage", 40, 0, 100);
        public ConfigBoolean griefing = new ConfigBoolean("iceandfire.cyclops.griefing", true);

        public CyclopsConfig() {
            super("cyclops", "iceandfire.category.cyclops");
        }
    }

    public static class SirenConfig extends AutoInitConfigCategoryBase {
        public ConfigDouble spawnChance = new ConfigDouble("iceandfire.siren.spawnChance", 1.0 / 400, 0, 1);
        public ConfigDouble maxHealth = new ConfigDouble("iceandfire.siren.maxHealth", 50, 1, Double.MAX_VALUE);
        public ConfigBoolean shader = new ConfigBoolean("iceandfire.siren.shader", true);
        public ConfigInteger maxSingTime = new ConfigInteger("iceandfire.siren.maxSingTime", 12000, 0, 48000);
        public ConfigInteger timeBetweenSongs = new ConfigInteger("iceandfire.siren.timeBetweenSongs", 2000, 0, 10000);

        public SirenConfig() {
            super("siren", "iceandfire.category.siren");
        }
    }

    public static class GorgonConfig extends AutoInitConfigCategoryBase {
        public ConfigBoolean generateTemple = new ConfigBoolean("iceandfire.gorgon.generateTemple", true);
        public ConfigDouble maxHealth = new ConfigDouble("iceandfire.gorgon.maxHealth", 100, 1, Double.MAX_VALUE);

        public GorgonConfig() {
            super("gorgon", "iceandfire.category.gorgon");
        }
    }

    public static class DeathwormConfig extends AutoInitConfigCategoryBase {
        public ConfigDouble spawnChance = new ConfigDouble("iceandfire.deathworm.spawnChance", 1.0 / 30, 0, 1);
        public ConfigInteger targetSearchLength = new ConfigInteger("iceandfire.deathworm.targetSearchLength", 48, 0, 128);
        public ConfigDouble maxHealth = new ConfigDouble("iceandfire.deathworm.maxHealth", 10, 1, Double.MAX_VALUE);
        public ConfigDouble attackDamage = new ConfigDouble("iceandfire.deathworm.attackDamage", 3, 0, 30);
        public ConfigBoolean attackMonsters = new ConfigBoolean("iceandfire.deathworm.attackMonsters", true);

        public DeathwormConfig() {
            super("deathworm", "iceandfire.category.deathworm");
        }
    }

    public static class CockatriceConfig extends AutoInitConfigCategoryBase {
        public ConfigBoolean spawn = new ConfigBoolean("iceandfire.cockatrice.spawn", true);
        public ConfigInteger spawnWeight = new ConfigInteger("iceandfire.cockatrice.spawnWeight", 4, 0, 10);
        public ConfigInteger chickenSearchLength = new ConfigInteger("iceandfire.cockatrice.chickenSearchLength", 32, 0, 128);
        public ConfigDouble eggChance = new ConfigDouble("iceandfire.cockatrice.eggChance", 1.0 / 30, 0, 1);
        public ConfigDouble maxHealth = new ConfigDouble("iceandfire.cockatrice.maxHealth", 40, 1, Double.MAX_VALUE);
        public ConfigBoolean chickensLayRottenEggs = new ConfigBoolean("iceandfire.cockatrice.chickensLayRottenEggs", true);

        public CockatriceConfig() {
            super("cockatrice", "iceandfire.category.cockatrice");
        }
    }

    public static class StymphalianBirdConfig extends AutoInitConfigCategoryBase {
        public ConfigDouble spawnChance = new ConfigDouble("iceandfire.bird.spawnChance", 1.0 / 80, 0, 1);
        public ConfigInteger targetSearchLength = new ConfigInteger("iceandfire.bird.targetSearchLength", 48, 0, 128);
        public ConfigDouble featherDropChance = new ConfigDouble("iceandfire.bird.featherDropChance", 1.0 / 25, 0, 1);
        public ConfigDouble featherAttackDamage = new ConfigDouble("iceandfire.bird.featherAttackDamage", 1, 0, 5);
        public ConfigInteger flockLength = new ConfigInteger("iceandfire.bird.flockLength", 40, 0, 100);
        public ConfigInteger flightHeight = new ConfigInteger("iceandfire.bird.flightHeight", 80, 64, 384);
        public ConfigBoolean dataTagDrops = new ConfigBoolean("iceandfire.bird.dataTagDrops", true);
        public ConfigBoolean attackAnimals = new ConfigBoolean("iceandfire.bird.attackAnimals", false);

        public StymphalianBirdConfig() {
            super("bird", "iceandfire.category.bird");
        }
    }

    public static class TrollConfig extends AutoInitConfigCategoryBase {
        public ConfigBoolean spawn = new ConfigBoolean("iceandfire.troll.spawn", true);
        public ConfigInteger spawnWeight = new ConfigInteger("iceandfire.troll.spawnWeight", 60, 0, 100);
        public ConfigBoolean dropWeapon = new ConfigBoolean("iceandfire.troll.dropWeapon", true);
        public ConfigDouble maxHealth = new ConfigDouble("iceandfire.troll.maxHealth", 50, 1, Double.MAX_VALUE);
        public ConfigDouble attackDamage = new ConfigDouble("iceandfire.troll.attackDamage", 10, 0, 50);

        public TrollConfig() {
            super("troll", "iceandfire.category.troll");
        }
    }

    public static class MyrmexConfig extends AutoInitConfigCategoryBase {
        public ConfigInteger pregnantTicks = new ConfigInteger("iceandfire.myrmex.pregnantTicks", 2500, 0, 5000);
        public ConfigInteger eggTicks = new ConfigInteger("iceandfire.myrmex.eggTicks", 3000, 0, 5000);
        public ConfigInteger larvaTicks = new ConfigInteger("iceandfire.myrmex.larvaTicks", 35000, 0, 50000);
        public ConfigDouble colonyGenChance = new ConfigDouble("iceandfire.myrmex.colonyGenChance", 1.0 / 50, 0, 1);
        public ConfigInteger colonySize = new ConfigInteger("iceandfire.myrmex.colonySize", 80, 0, 200);
        public ConfigInteger maximumWanderRadius = new ConfigInteger("iceandfire.myrmex.maximumWanderRadius", 50, 0, 200);
        public ConfigBoolean hiveIgnoreDaytime = new ConfigBoolean("iceandfire.myrmex.hiveIgnoreDaytime", false);
        public ConfigDouble baseAttackDamage = new ConfigDouble("iceandfire.myrmex.baseAttackDamage", 3, 0, 5);

        public MyrmexConfig() {
            super("myrmex", "iceandfire.category.myrmex");
        }
    }

    public static class AmphithereConfig extends AutoInitConfigCategoryBase {
        public ConfigBoolean spawn = new ConfigBoolean("iceandfire.amphithere.spawn", true);
        public ConfigInteger spawnWeight = new ConfigInteger("iceandfire.amphithere.spawnWeight", 50, 0, 200);
        public ConfigDouble villagerSearchLength = new ConfigDouble("iceandfire.amphithere.villagerSearchLength", 48, 0, 128);
        public ConfigInteger tameTime = new ConfigInteger("iceandfire.amphithere.tameTime", 400, 0, 1000);
        public ConfigDouble flightSpeed = new ConfigDouble("iceandfire.amphithere.flightSpeed", 1.75, 0, 10);
        public ConfigDouble maxHealth = new ConfigDouble("iceandfire.amphithere.maxHealth", 50, 1, Double.MAX_VALUE);
        public ConfigDouble attackDamage = new ConfigDouble("iceandfire.amphithere.attackDamage", 7, 0, 20);

        public AmphithereConfig() {
            super("amphithere", "iceandfire.category.amphithere");
        }
    }

    public static class SeaSerpentConfig extends AutoInitConfigCategoryBase {
        public ConfigDouble spawnChance = new ConfigDouble("iceandfire.seaSerpent.spawnChance", 1.0 / 250, 0, 1);
        public ConfigBoolean griefing = new ConfigBoolean("iceandfire.seaSerpent.griefing", true);
        public ConfigDouble baseHealth = new ConfigDouble("iceandfire.seaSerpent.baseHealth", 20, 0, 50);
        public ConfigDouble attackDamage = new ConfigDouble("iceandfire.seaSerpent.baseHealth", 4, 0, 10);

        public SeaSerpentConfig() {
            super("seaSerpent", "iceandfire.category.seaSerpent");
        }
    }

    public static class LichConfig extends AutoInitConfigCategoryBase {
        public ConfigBoolean spawn = new ConfigBoolean("iceandfire.lich.spawn", true);
        public ConfigInteger spawnWeight = new ConfigInteger("iceandfire.lich.spawnWeight", 4, 0, 10);
        public ConfigDouble spawnChance = new ConfigDouble("iceandfire.lich.spawnChance", 1.0 / 30, 0, 1);

        public LichConfig() {
            super("lich", "iceandfire.category.lich");
        }
    }

    public static class HydraConfig extends AutoInitConfigCategoryBase {
        public ConfigDouble maxHealth = new ConfigDouble("iceandfire.hydra.maxHealth", 250, 1, Double.MAX_VALUE);
        public ConfigDouble spawnChance = new ConfigDouble("iceandfire.hydra.spawnChance", 1.0 / 120, 0, 1);

        public HydraConfig() {
            super("hydra", "iceandfire.category.hydra");
        }
    }

    public static class HippocampusConfig extends AutoInitConfigCategoryBase {
        public ConfigDouble spawnChance = new ConfigDouble("iceandfire.hippocampus.spawnChance", 1.0 / 40, 0, 1);
        public ConfigDouble swimSpeedMod = new ConfigDouble("iceandfire.hippocampus.swimSpeedMod", 1, 0, 5);

        public HippocampusConfig() {
            super("hippocampus", "iceandfire.category.hippocampus");
        }
    }

    public static class GhostConfig extends AutoInitConfigCategoryBase {
        public ConfigBoolean generateGraveyards = new ConfigBoolean("iceandfire.ghost.generateGraveyards", true);
        public ConfigDouble maxHealth = new ConfigDouble("iceandfire.ghost.maxHealth", 30, 1, Double.MAX_VALUE);
        public ConfigDouble attackDamage = new ConfigDouble("iceandfire.ghost.attackDamage", 3, 0, 40);
        public ConfigBoolean fromPlayerDeaths = new ConfigBoolean("iceandfire.ghost.fromPlayerDeaths", true);

        public GhostConfig() {
            super("ghost", "iceandfire.category.ghost");
        }
    }

    public static class ArmorsConfig extends AutoInitConfigCategoryBase {
        public ConfigBoolean dragonFireAbility = new ConfigBoolean("iceandfire.armors.dragonFireAbility", true);
        public ConfigBoolean dragonIceAbility = new ConfigBoolean("iceandfire.armors.dragonIceAbility", true);
        public ConfigBoolean dragonLightningAbility = new ConfigBoolean("iceandfire.armors.dragonLightningAbility", true);

        public ConfigDouble dragonSteelBaseDamage = new ConfigDouble("iceandfire.armors.dragonSteelBaseDamage", 25, 0, 100);
        public ConfigInteger dragonSteelBaseArmor = new ConfigInteger("iceandfire.armors.dragonSteelBaseArmor", 12, 0, 100);
        public ConfigDouble dragonSteelBaseArmorToughness = new ConfigDouble("iceandfire.armors.dragonSteelBaseArmorToughness", 6, 0, 50);
        public ConfigInteger dragonSteelBaseDurability = new ConfigInteger("iceandfire.armors.dragonSteelBaseDurability", 8000, 0, 20000);
        public ConfigInteger dragonSteelBaseDurabilityEquipment = new ConfigInteger("iceandfire.armors.dragonSteelBaseDurabilityEquipment", 8000, 0, 20000);

        public ArmorsConfig() {
            super("armors", "iceandfire.category.armors");
        }
    }

    public static class WorldGenConfig extends AutoInitConfigCategoryBase {
        public ConfigDouble dangerousDistanceLimit = new ConfigDouble("iceandfire.worldgen.dangerousDistanceLimit", 1000, 0, 10000);
        public ConfigDouble dangerousSeparationLimit = new ConfigDouble("iceandfire.worldgen.dangerousSeparationLimit", 300, 0, 10000);
        public ConfigBoolean generateMausoleums = new ConfigBoolean("iceandfire.worldgen.generateMausoleums", true);
        public ConfigInteger villagerHouseWeight = new ConfigInteger("iceandfire.worldgen.villagerHouseWeight", 5, 0, 10);

        public WorldGenConfig() {
            super("worldgen", "iceandfire.category.worldgen");
        }
    }

    public static class Misc extends AutoInitConfigCategoryBase {
        public ConfigBoolean enableDragonSeeker = new ConfigBoolean("iceandfire.misc.enableDragonSeeker", true);
        public ConfigDouble dreadQueenMaxHealth = new ConfigDouble("iceandfire.misc.dreadQueenMaxHealth", 750, 0, 1000);
        public ConfigBoolean allowAttributeOverriding = new ConfigBoolean("iceandfire.misc.allowAttributeOverriding", true);

        public Misc() {
            super("misc", "iceandfire.category.misc");
        }
    }
}
