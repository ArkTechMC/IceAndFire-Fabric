package com.iafenvoy.iceandfire.config;

import com.google.gson.JsonObject;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.jupiter.config.container.AutoInitConfigContainer;
import com.iafenvoy.jupiter.config.entry.BooleanEntry;
import com.iafenvoy.jupiter.config.entry.DoubleEntry;
import com.iafenvoy.jupiter.config.entry.IntegerEntry;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
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
    protected boolean shouldLoad(JsonObject obj) {
        if (!obj.has("version")) return true;
        int version = obj.get("version").getAsInt();
        if (version != CURRENT_VERSION) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                FileUtils.copyFile(new File(this.path), new File(IafCommonConfig.backupPath + "iceandfire_common_" + sdf.format(new Date()) + ".json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            IceAndFire.LOGGER.info("Wrong common config version {} for mod {}! Automatically use version {} and backup old one.", version, IceAndFire.MOD_NAME, CURRENT_VERSION);
            return false;
        } else IceAndFire.LOGGER.info("{} common config version match.", IceAndFire.MOD_NAME);
        return true;
    }

    @Override
    protected void writeCustomData(JsonObject obj) {
        obj.addProperty("version", CURRENT_VERSION);
    }

    public static class DragonConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Double> maxHealth = new DoubleEntry("iceandfire.dragon.maxHealth", 500, 1, Integer.MAX_VALUE);
        public IConfigEntry<Integer> eggBornTime = new IntegerEntry("iceandfire.dragon.eggBornTime", 7200, 0, Integer.MAX_VALUE);
        public IConfigEntry<Integer> maxPathingNodes = new IntegerEntry("iceandfire.dragon.maxPathingNodes", 5000, 0, Integer.MAX_VALUE);
        public IConfigEntry<Boolean> villagersFear = new BooleanEntry("iceandfire.dragon.villagersFear", true);
        public IConfigEntry<Boolean> animalsFear = new BooleanEntry("iceandfire.dragon.animalsFear", true);

        public IConfigEntry<Boolean> generateSkeletons = new BooleanEntry("iceandfire.dragon.generate.skeletons", true);
        public IConfigEntry<Double> generateSkeletonChance = new DoubleEntry("iceandfire.dragon.generate.skeletonChance", 1.0 / 300, 0, 1);
        public IConfigEntry<Double> generateRoostChance = new DoubleEntry("iceandfire.dragon.generate.roostChance", 1.0 / 480, 0, 1);
        public IConfigEntry<Double> generateDenGoldChance = new DoubleEntry("iceandfire.dragon.generate.denGoldAmount", 1.0 / 4, 0, 1);
        public IConfigEntry<Double> generateOreRatio = new DoubleEntry("iceandfire.dragon.generate.oreRatio", 1.0 / 45, 0, 1);

        public IConfigEntry<Boolean> griefing = new BooleanEntry("iceandfire.dragon.griefing", true);
        public IConfigEntry<Boolean> tamedGriefing = new BooleanEntry("iceandfire.dragon.tamedGriefing", true);
        public IConfigEntry<Integer> flapNoiseDistance = new IntegerEntry("iceandfire.dragon.flapNoiseDistance", 4, 0, 32);
        public IConfigEntry<Integer> fluteDistance = new IntegerEntry("iceandfire.dragon.fluteDistance", 8, 0, 512);
        public IConfigEntry<Integer> attackDamage = new IntegerEntry("iceandfire.dragon.attackDamage", 17, 0, Integer.MAX_VALUE);
        public IConfigEntry<Double> attackDamageFire = new DoubleEntry("iceandfire.dragon.attackDamageFire", 2, 0, Integer.MAX_VALUE);
        public IConfigEntry<Double> attackDamageIce = new DoubleEntry("iceandfire.dragon.attackDamageIce", 2.5, 0, Integer.MAX_VALUE);
        public IConfigEntry<Double> attackDamageLightning = new DoubleEntry("iceandfire.dragon.attackDamageLightning", 3.5, 0, Integer.MAX_VALUE);
        public IConfigEntry<Integer> maxFlight = new IntegerEntry("iceandfire.dragon.maxFlight", 256, 0, 384);
        public IConfigEntry<Integer> goldSearchLength = new IntegerEntry("iceandfire.dragon.goldSearchLength", 30, 0, 256);
        public IConfigEntry<Boolean> canHealFromBiting = new BooleanEntry("iceandfire.dragon.canHealFromBiting", false);
        public IConfigEntry<Boolean> canDespawn = new BooleanEntry("iceandfire.dragon.canDespawn", true);
        public IConfigEntry<Boolean> sleep = new BooleanEntry("iceandfire.dragon.sleep", true);
        public IConfigEntry<Boolean> digWhenStuck = new BooleanEntry("iceandfire.dragon.digWhenStuck", true);
        public IConfigEntry<Integer> breakBlockCooldown = new IntegerEntry("iceandfire.dragon.breakBlockCooldown", 5, 0, Integer.MAX_VALUE);
        public IConfigEntry<Integer> targetSearchLength = new IntegerEntry("iceandfire.dragon.breakBlockCooldown", 128, 0, 1024);
        public IConfigEntry<Integer> wanderFromHomeDistance = new IntegerEntry("iceandfire.dragon.wanderFromHomeDistance", 40, 0, 1024);
        public IConfigEntry<Integer> hungerTickRate = new IntegerEntry("iceandfire.dragon.hungerTickRate", 3000, 0, Integer.MAX_VALUE);
        public IConfigEntry<Double> blockBreakingDropChance = new DoubleEntry("iceandfire.dragon.blockBreakingDropChance", 0.1, 0, 1);
        public IConfigEntry<Boolean> explosiveBreath = new BooleanEntry("iceandfire.dragon.explosiveBreath", false);
        public IConfigEntry<Boolean> chunkLoadSummonCrystal = new BooleanEntry("iceandfire.dragon.chunkLoadSummonCrystal", true);
        public IConfigEntry<Double> dragonFlightSpeedMod = new DoubleEntry("iceandfire.dragon.dragonFlightSpeedMod", 1, 0.0001, 50);
        public IConfigEntry<Integer> maxTamedDragonAge = new IntegerEntry("iceandfire.dragon.maxTamedDragonAge", 128, 0, 128);

        public IConfigEntry<Boolean> lootSkull = new BooleanEntry("iceandfire.dragon.loot.skull", true);
        public IConfigEntry<Boolean> lootHeart = new BooleanEntry("iceandfire.dragon.loot.heart", true);
        public IConfigEntry<Boolean> lootBlood = new BooleanEntry("iceandfire.dragon.loot.blood", true);

        public DragonConfig() {
            super("dragon", "iceandfire.category.dragon");
        }
    }

    public static class HippogryphsConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Boolean> spawn = new BooleanEntry("iceandfire.hippogryphs.spawn", true);
        public IConfigEntry<Integer> spawnWeight = new IntegerEntry("iceandfire.hippogryphs.spawnWeight", 2, 0, 20);
        public IConfigEntry<Double> fightSpeedMod = new DoubleEntry("iceandfire.hippogryphs.fightSpeedMod", 1, 0.0001, 50);

        public HippogryphsConfig() {
            super("hippogryphs", "iceandfire.category.hippogryphs");
        }
    }

    public static class PixieConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Double> spawnChance = new DoubleEntry("iceandfire.pixie.spawnChance", 1.0 / 60, 0, 1);
        public IConfigEntry<Integer> size = new IntegerEntry("iceandfire.pixie.size", 5, 0, 100);
        public IConfigEntry<Boolean> stealItems = new BooleanEntry("iceandfire.pixie.stealItems", true);

        public PixieConfig() {
            super("pixie", "iceandfire.category.pixie");
        }
    }

    public static class CyclopsConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Double> spawnWanderingChance = new DoubleEntry("iceandfire.cyclops.spawnWanderingChance", 1.0 / 900, 0, 1);
        public IConfigEntry<Double> spawnCaveChance = new DoubleEntry("iceandfire.cyclops.spawnCaveChance", 1.0 / 170, 0, 1);
        public IConfigEntry<Integer> sheepSearchLength = new IntegerEntry("iceandfire.cyclops.sheepSearchLength", 17, 0, 1024);
        public IConfigEntry<Double> maxHealth = new DoubleEntry("iceandfire.cyclops.maxHealth", 150, 1, Integer.MAX_VALUE);
        public IConfigEntry<Double> attackDamage = new DoubleEntry("iceandfire.cyclops.attackDamage", 15, 0, Integer.MAX_VALUE);
        public IConfigEntry<Double> biteDamage = new DoubleEntry("iceandfire.cyclops.biteDamage", 40, 0, Integer.MAX_VALUE);
        public IConfigEntry<Boolean> griefing = new BooleanEntry("iceandfire.cyclops.griefing", true);

        public CyclopsConfig() {
            super("cyclops", "iceandfire.category.cyclops");
        }
    }

    public static class SirenConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Double> spawnChance = new DoubleEntry("iceandfire.siren.spawnChance", 1.0 / 400, 0, 1);
        public IConfigEntry<Double> maxHealth = new DoubleEntry("iceandfire.siren.maxHealth", 50, 1, Integer.MAX_VALUE);
        public IConfigEntry<Integer> maxSingTime = new IntegerEntry("iceandfire.siren.maxSingTime", 12000, 0, Integer.MAX_VALUE);
        public IConfigEntry<Integer> timeBetweenSongs = new IntegerEntry("iceandfire.siren.timeBetweenSongs", 2000, 0, Integer.MAX_VALUE);

        public SirenConfig() {
            super("siren", "iceandfire.category.siren");
        }
    }

    public static class GorgonConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Boolean> generateTemple = new BooleanEntry("iceandfire.gorgon.generateTemple", true);
        public IConfigEntry<Double> maxHealth = new DoubleEntry("iceandfire.gorgon.maxHealth", 100, 1, Integer.MAX_VALUE);

        public GorgonConfig() {
            super("gorgon", "iceandfire.category.gorgon");
        }
    }

    public static class DeathwormConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Double> spawnChance = new DoubleEntry("iceandfire.deathworm.spawnChance", 1.0 / 30, 0, 1);
        public IConfigEntry<Integer> targetSearchLength = new IntegerEntry("iceandfire.deathworm.targetSearchLength", 48, 0, 1024);
        public IConfigEntry<Double> maxHealth = new DoubleEntry("iceandfire.deathworm.maxHealth", 10, 1, Integer.MAX_VALUE);
        public IConfigEntry<Double> attackDamage = new DoubleEntry("iceandfire.deathworm.attackDamage", 3, 0, 30);
        public IConfigEntry<Boolean> attackMonsters = new BooleanEntry("iceandfire.deathworm.attackMonsters", true);

        public DeathwormConfig() {
            super("deathworm", "iceandfire.category.deathworm");
        }
    }

    public static class CockatriceConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Boolean> spawn = new BooleanEntry("iceandfire.cockatrice.spawn", true);
        public IConfigEntry<Integer> spawnWeight = new IntegerEntry("iceandfire.cockatrice.spawnWeight", 4, 0, 20);
        public IConfigEntry<Integer> chickenSearchLength = new IntegerEntry("iceandfire.cockatrice.chickenSearchLength", 32, 0, 1024);
        public IConfigEntry<Double> eggChance = new DoubleEntry("iceandfire.cockatrice.eggChance", 1.0 / 30, 0, 1);
        public IConfigEntry<Double> maxHealth = new DoubleEntry("iceandfire.cockatrice.maxHealth", 40, 1, Integer.MAX_VALUE);
        public IConfigEntry<Boolean> chickensLayRottenEggs = new BooleanEntry("iceandfire.cockatrice.chickensLayRottenEggs", true);

        public CockatriceConfig() {
            super("cockatrice", "iceandfire.category.cockatrice");
        }
    }

    public static class StymphalianBirdConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Double> spawnChance = new DoubleEntry("iceandfire.bird.spawnChance", 1.0 / 80, 0, 1);
        public IConfigEntry<Integer> targetSearchLength = new IntegerEntry("iceandfire.bird.targetSearchLength", 48, 0, 1024);
        public IConfigEntry<Double> featherDropChance = new DoubleEntry("iceandfire.bird.featherDropChance", 1.0 / 25, 0, 1);
        public IConfigEntry<Double> featherAttackDamage = new DoubleEntry("iceandfire.bird.featherAttackDamage", 1, 0, Integer.MAX_VALUE);
        public IConfigEntry<Integer> flockLength = new IntegerEntry("iceandfire.bird.flockLength", 40, 0, 200);
        public IConfigEntry<Integer> flightHeight = new IntegerEntry("iceandfire.bird.flightHeight", 80, 64, 384);
        public IConfigEntry<Boolean> attackAnimals = new BooleanEntry("iceandfire.bird.attackAnimals", false);

        public StymphalianBirdConfig() {
            super("bird", "iceandfire.category.bird");
        }
    }

    public static class TrollConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Boolean> spawn = new BooleanEntry("iceandfire.troll.spawn", true);
        public IConfigEntry<Integer> spawnWeight = new IntegerEntry("iceandfire.troll.spawnWeight", 60, 0, 200);
        public IConfigEntry<Boolean> dropWeapon = new BooleanEntry("iceandfire.troll.dropWeapon", true);
        public IConfigEntry<Double> maxHealth = new DoubleEntry("iceandfire.troll.maxHealth", 50, 1, Integer.MAX_VALUE);
        public IConfigEntry<Double> attackDamage = new DoubleEntry("iceandfire.troll.attackDamage", 10, 0, Integer.MAX_VALUE);

        public TrollConfig() {
            super("troll", "iceandfire.category.troll");
        }
    }

    public static class MyrmexConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Integer> pregnantTicks = new IntegerEntry("iceandfire.myrmex.pregnantTicks", 2500, 0, Integer.MAX_VALUE);
        public IConfigEntry<Integer> eggTicks = new IntegerEntry("iceandfire.myrmex.eggTicks", 3000, 0, Integer.MAX_VALUE);
        public IConfigEntry<Integer> larvaTicks = new IntegerEntry("iceandfire.myrmex.larvaTicks", 35000, 0, Integer.MAX_VALUE);
        public IConfigEntry<Double> colonyGenChance = new DoubleEntry("iceandfire.myrmex.colonyGenChance", 1.0 / 50, 0, 1);
        public IConfigEntry<Integer> colonySize = new IntegerEntry("iceandfire.myrmex.colonySize", 80, 0, 400);
        public IConfigEntry<Integer> maximumWanderRadius = new IntegerEntry("iceandfire.myrmex.maximumWanderRadius", 50, 0, 400);
        public IConfigEntry<Boolean> hiveIgnoreDaytime = new BooleanEntry("iceandfire.myrmex.hiveIgnoreDaytime", false);
        public IConfigEntry<Double> baseAttackDamage = new DoubleEntry("iceandfire.myrmex.baseAttackDamage", 3, 0, Integer.MAX_VALUE);

        public MyrmexConfig() {
            super("myrmex", "iceandfire.category.myrmex");
        }
    }

    public static class AmphithereConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Boolean> spawn = new BooleanEntry("iceandfire.amphithere.spawn", true);
        public IConfigEntry<Integer> spawnWeight = new IntegerEntry("iceandfire.amphithere.spawnWeight", 50, 0, 400);
        public IConfigEntry<Double> villagerSearchLength = new DoubleEntry("iceandfire.amphithere.villagerSearchLength", 48, 0, 1024);
        public IConfigEntry<Integer> tameTime = new IntegerEntry("iceandfire.amphithere.tameTime", 400, 0, Integer.MAX_VALUE);
        public IConfigEntry<Double> flightSpeed = new DoubleEntry("iceandfire.amphithere.flightSpeed", 1.75, 0, 20);
        public IConfigEntry<Double> maxHealth = new DoubleEntry("iceandfire.amphithere.maxHealth", 50, 1, Integer.MAX_VALUE);
        public IConfigEntry<Double> attackDamage = new DoubleEntry("iceandfire.amphithere.attackDamage", 7, 0, Integer.MAX_VALUE);

        public AmphithereConfig() {
            super("amphithere", "iceandfire.category.amphithere");
        }
    }

    public static class SeaSerpentConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Double> spawnChance = new DoubleEntry("iceandfire.seaSerpent.spawnChance", 1.0 / 250, 0, 1);
        public IConfigEntry<Boolean> griefing = new BooleanEntry("iceandfire.seaSerpent.griefing", true);
        public IConfigEntry<Double> baseHealth = new DoubleEntry("iceandfire.seaSerpent.baseHealth", 20, 0, Integer.MAX_VALUE);
        public IConfigEntry<Double> attackDamage = new DoubleEntry("iceandfire.seaSerpent.attackDamage", 4, 0, Integer.MAX_VALUE);

        public SeaSerpentConfig() {
            super("seaSerpent", "iceandfire.category.seaSerpent");
        }
    }

    public static class LichConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Boolean> spawn = new BooleanEntry("iceandfire.lich.spawn", true);
        public IConfigEntry<Integer> spawnWeight = new IntegerEntry("iceandfire.lich.spawnWeight", 4, 0, 20);
        public IConfigEntry<Double> spawnChance = new DoubleEntry("iceandfire.lich.spawnChance", 1.0 / 30, 0, 1);

        public LichConfig() {
            super("lich", "iceandfire.category.lich");
        }
    }

    public static class HydraConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Double> maxHealth = new DoubleEntry("iceandfire.hydra.maxHealth", 250, 1, Integer.MAX_VALUE);
        public IConfigEntry<Double> spawnChance = new DoubleEntry("iceandfire.hydra.spawnChance", 1.0 / 120, 0, 1);

        public HydraConfig() {
            super("hydra", "iceandfire.category.hydra");
        }
    }

    public static class HippocampusConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Double> spawnChance = new DoubleEntry("iceandfire.hippocampus.spawnChance", 1.0 / 40, 0, 1);
        public IConfigEntry<Double> swimSpeedMod = new DoubleEntry("iceandfire.hippocampus.swimSpeedMod", 1, 0.0001, 10);

        public HippocampusConfig() {
            super("hippocampus", "iceandfire.category.hippocampus");
        }
    }

    public static class GhostConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Boolean> generateGraveyards = new BooleanEntry("iceandfire.ghost.generateGraveyards", true);
        public IConfigEntry<Double> maxHealth = new DoubleEntry("iceandfire.ghost.maxHealth", 30, 1, Integer.MAX_VALUE);
        public IConfigEntry<Double> attackDamage = new DoubleEntry("iceandfire.ghost.attackDamage", 3, 0, Integer.MAX_VALUE);
        public IConfigEntry<Boolean> fromPlayerDeaths = new BooleanEntry("iceandfire.ghost.fromPlayerDeaths", true);

        public GhostConfig() {
            super("ghost", "iceandfire.category.ghost");
        }
    }

    public static class ArmorsConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Boolean> dragonFireAbility = new BooleanEntry("iceandfire.armors.dragonFireAbility", true);
        public IConfigEntry<Boolean> dragonIceAbility = new BooleanEntry("iceandfire.armors.dragonIceAbility", true);
        public IConfigEntry<Boolean> dragonLightningAbility = new BooleanEntry("iceandfire.armors.dragonLightningAbility", true);

        public IConfigEntry<Double> dragonSteelBaseDamage = new DoubleEntry("iceandfire.armors.dragonSteelBaseDamage", 25, 0, Integer.MAX_VALUE);
        public IConfigEntry<Integer> dragonSteelBaseArmor = new IntegerEntry("iceandfire.armors.dragonSteelBaseArmor", 12, 0, Integer.MAX_VALUE);
        public IConfigEntry<Double> dragonSteelBaseArmorToughness = new DoubleEntry("iceandfire.armors.dragonSteelBaseArmorToughness", 6, 0, Integer.MAX_VALUE);
        public IConfigEntry<Integer> dragonSteelBaseDurability = new IntegerEntry("iceandfire.armors.dragonSteelBaseDurability", 8000, 0, Integer.MAX_VALUE);
        public IConfigEntry<Integer> dragonSteelBaseDurabilityEquipment = new IntegerEntry("iceandfire.armors.dragonSteelBaseDurabilityEquipment", 8000, 0, Integer.MAX_VALUE);

        public ArmorsConfig() {
            super("armors", "iceandfire.category.armors");
        }
    }

    public static class WorldGenConfig extends AutoInitConfigCategoryBase {
        public IConfigEntry<Double> dangerousDistanceLimit = new DoubleEntry("iceandfire.worldgen.dangerousDistanceLimit", 1000, 0, Integer.MAX_VALUE);
        public IConfigEntry<Double> dangerousSeparationLimit = new DoubleEntry("iceandfire.worldgen.dangerousSeparationLimit", 300, 0, Integer.MAX_VALUE);
        public IConfigEntry<Boolean> generateMausoleums = new BooleanEntry("iceandfire.worldgen.generateMausoleums", true);

        public WorldGenConfig() {
            super("worldgen", "iceandfire.category.worldgen");
        }
    }

    public static class Misc extends AutoInitConfigCategoryBase {
        public IConfigEntry<Boolean> enableDragonSeeker = new BooleanEntry("iceandfire.misc.enableDragonSeeker", true);
        public IConfigEntry<Double> dreadQueenMaxHealth = new DoubleEntry("iceandfire.misc.dreadQueenMaxHealth", 750, 0, Integer.MAX_VALUE);

        public Misc() {
            super("misc", "iceandfire.category.misc");
        }
    }
}
