package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.compat.delight.DelightFoodItem;
import com.iafenvoy.iceandfire.config.IafCommonConfig;
import com.iafenvoy.iceandfire.enums.*;
import com.iafenvoy.iceandfire.item.*;
import com.iafenvoy.iceandfire.item.armor.*;
import com.iafenvoy.iceandfire.item.food.ItemAmbrosia;
import com.iafenvoy.iceandfire.item.food.ItemCannoli;
import com.iafenvoy.iceandfire.item.food.ItemDragonFlesh;
import com.iafenvoy.iceandfire.item.food.ItemPixieDust;
import com.iafenvoy.iceandfire.item.tool.*;
import com.iafenvoy.iceandfire.registry.tag.BannerPatternTags;
import com.iafenvoy.iceandfire.registry.tag.CommonTags;
import com.iafenvoy.iceandfire.registry.tag.IafItemTags;
import com.iafenvoy.uranus.object.item.CustomArmorMaterial;
import com.iafenvoy.uranus.object.item.CustomToolMaterial;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import static com.iafenvoy.iceandfire.item.tool.DragonSteelToolMaterial.createMaterialWithRepairItem;

@SuppressWarnings("unused")
public class IafItems {
    public static final CustomArmorMaterial SILVER_ARMOR_MATERIAL = new IafArmorMaterial("silver", 15, new int[]{1, 4, 5, 2}, 20, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0);
    public static final CustomArmorMaterial COPPER_ARMOR_MATERIAL = new IafArmorMaterial("copper", 10, new int[]{1, 3, 4, 2}, 15, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0);
    public static final CustomArmorMaterial BLINDFOLD_ARMOR_MATERIAL = new IafArmorMaterial("blindfold", 5, new int[]{1, 1, 1, 1}, 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
    public static final CustomArmorMaterial SHEEP_ARMOR_MATERIAL = new IafArmorMaterial("sheep", 5, new int[]{1, 3, 2, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
    public static final CustomArmorMaterial MYRMEX_DESERT_ARMOR_MATERIAL = new IafArmorMaterial("myrmexdesert", 20, new int[]{3, 5, 8, 4}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
    public static final CustomArmorMaterial MYRMEX_JUNGLE_ARMOR_MATERIAL = new IafArmorMaterial("myrmexjungle", 20, new int[]{3, 5, 8, 4}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
    public static final CustomArmorMaterial EARPLUGS_ARMOR_MATERIAL = new IafArmorMaterial("earplugs", 5, new int[]{1, 1, 1, 1}, 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
    public static final CustomArmorMaterial DEATHWORM_0_ARMOR_MATERIAL = new IafArmorMaterial("yellow_seathworm", 15, new int[]{2, 5, 7, 3}, 5, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.5F);
    public static final CustomArmorMaterial DEATHWORM_1_ARMOR_MATERIAL = new IafArmorMaterial("white_seathworm", 15, new int[]{2, 5, 7, 3}, 5, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.5F);
    public static final CustomArmorMaterial DEATHWORM_2_ARMOR_MATERIAL = new IafArmorMaterial("red_deathworm", 15, new int[]{2, 5, 7, 3}, 5, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.5F);
    public static final CustomArmorMaterial TROLL_MOUNTAIN_ARMOR_MATERIAL = new IafArmorMaterial("mountain_troll", 20, new int[]{2, 5, 7, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1F);
    public static final CustomArmorMaterial TROLL_FOREST_ARMOR_MATERIAL = new IafArmorMaterial("forest_troll", 20, new int[]{2, 5, 7, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1F);
    public static final CustomArmorMaterial TROLL_FROST_ARMOR_MATERIAL = new IafArmorMaterial("frost_troll", 20, new int[]{2, 5, 7, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1F);
    public static final CustomArmorMaterial DRAGONSTEEL_FIRE_ARMOR_MATERIAL;

    static {
        DRAGONSTEEL_FIRE_ARMOR_MATERIAL = new DragonSteelArmorMaterial("dragonsteel_fire", (int) (0.02D * IafCommonConfig.INSTANCE.armors.dragonsteel.baseDurabilityEquipment), new int[]{IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmor - 6, IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmor - 3, IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmor, IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmor - 5}, 30, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmorToughness);
    }

    public static final CustomArmorMaterial DRAGONSTEEL_ICE_ARMOR_MATERIAL;

    static {
        DRAGONSTEEL_ICE_ARMOR_MATERIAL = new DragonSteelArmorMaterial("dragonsteel_ice", (int) (0.02D * IafCommonConfig.INSTANCE.armors.dragonsteel.baseDurabilityEquipment), new int[]{IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmor - 6, IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmor - 3, IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmor, IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmor - 5}, 30, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmorToughness);
    }

    public static final CustomArmorMaterial DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL;

    static {
        DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL = new DragonSteelArmorMaterial("dragonsteel_lightning", (int) (0.02D * IafCommonConfig.INSTANCE.armors.dragonsteel.baseDurabilityEquipment), new int[]{IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmor - 6, IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmor - 3, IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmor, IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmor - 5}, 30, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, IafCommonConfig.INSTANCE.armors.dragonsteel.baseArmorToughness);
    }

    public static final CustomToolMaterial SILVER_TOOL_MATERIAL = new CustomToolMaterial("silver", 2, 460, 1.0F, 11.0F, 18);
    public static final CustomToolMaterial COPPER_TOOL_MATERIAL = new CustomToolMaterial("copper", 2, 300, 0.0F, 0.7F, 10);
    public static final CustomToolMaterial DRAGONBONE_TOOL_MATERIAL = new CustomToolMaterial("dragon_bone", 3, 1660, 4.0F, 10.0F, 22);
    public static final CustomToolMaterial FIRE_DRAGONBONE_TOOL_MATERIAL = new CustomToolMaterial("fire_dragon_bone", 3, 2000, 5.5F, 10F, 22);
    public static final CustomToolMaterial ICE_DRAGONBONE_TOOL_MATERIAL = new CustomToolMaterial("ice_dragon_bone", 3, 2000, 5.5F, 10F, 22);
    public static final CustomToolMaterial LIGHTNING_DRAGONBONE_TOOL_MATERIAL = new CustomToolMaterial("lightening_dragon_bone", 3, 2000, 5.5F, 10F, 22);
    public static final CustomToolMaterial TROLL_WEAPON_TOOL_MATERIAL = new CustomToolMaterial("troll_weapon", 2, 300, 1F, 10F, 1);
    public static final CustomToolMaterial MYRMEX_CHITIN_TOOL_MATERIAL = new CustomToolMaterial("myrmex_chitin", 3, 600, 1.0F, 6.0F, 8);
    public static final CustomToolMaterial HIPPOGRYPH_SWORD_TOOL_MATERIAL = new CustomToolMaterial("hippogryph_sword", 2, 500, 2.5F, 10F, 10);
    public static final CustomToolMaterial STYMHALIAN_SWORD_TOOL_MATERIAL = new CustomToolMaterial("stymphalian_sword", 2, 500, 2, 10.0F, 10);
    public static final CustomToolMaterial AMPHITHERE_SWORD_TOOL_MATERIAL = new CustomToolMaterial("amphithere_sword", 2, 500, 1F, 10F, 10);
    public static final CustomToolMaterial HIPPOCAMPUS_SWORD_TOOL_MATERIAL = new CustomToolMaterial("hippocampus_sword", 0, 500, -2F, 0F, 50);
    public static final CustomToolMaterial DREAD_SWORD_TOOL_MATERIAL = new CustomToolMaterial("dread_sword", 0, 100, 1F, 10F, 0);
    public static final CustomToolMaterial DREAD_KNIGHT_TOOL_MATERIAL = new CustomToolMaterial("dread_knight_sword", 0, 1200, 13F, 0F, 10);
    public static final CustomToolMaterial GHOST_SWORD_TOOL_MATERIAL = new CustomToolMaterial("ghost_sword", 2, 3000, 5, 10.0F, 25);

    public static final Item BESTIARY = register("bestiary", new ItemBestiary());
    public static final Item MANUSCRIPT = register("manuscript", new ItemGeneric());
    public static final Item SAPPHIRE_GEM = register("sapphire_gem", new ItemGeneric());
    public static final Item SILVER_INGOT = register("silver_ingot", new ItemGeneric());
    public static final Item SILVER_NUGGET = register("silver_nugget", new ItemGeneric());
    public static final Item RAW_SILVER = register("raw_silver", new ItemGeneric());
    public static final Item COPPER_NUGGET = register("copper_nugget", new ItemGeneric());
    public static final Item SILVER_HELMET = register("armor_silver_metal_helmet", new ArmorItem(SILVER_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item SILVER_CHESTPLATE = register("armor_silver_metal_chestplate", new ArmorItem(SILVER_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item SILVER_LEGGINGS = register("armor_silver_metal_leggings", new ArmorItem(SILVER_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
    public static final Item SILVER_BOOTS = register("armor_silver_metal_boots", new ArmorItem(SILVER_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new FabricItemSettings()));
    public static final Item SILVER_SWORD = register("silver_sword", new ItemModSword(SILVER_TOOL_MATERIAL));
    public static final Item SILVER_SHOVEL = register("silver_shovel", new ItemModShovel(SILVER_TOOL_MATERIAL));
    public static final Item SILVER_PICKAXE = register("silver_pickaxe", new ItemModPickaxe(SILVER_TOOL_MATERIAL));
    public static final Item SILVER_AXE = register("silver_axe", new ItemModAxe(SILVER_TOOL_MATERIAL));
    public static final Item SILVER_HOE = register("silver_hoe", new ItemModHoe(SILVER_TOOL_MATERIAL));

    public static final Item COPPER_HELMET = register("armor_copper_metal_helmet", new ArmorItem(COPPER_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item COPPER_CHESTPLATE = register("armor_copper_metal_chestplate", new ArmorItem(COPPER_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item COPPER_LEGGINGS = register("armor_copper_metal_leggings", new ArmorItem(COPPER_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
    public static final Item COPPER_BOOTS = register("armor_copper_metal_boots", new ArmorItem(COPPER_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new FabricItemSettings()));
    public static final Item COPPER_SWORD = register("copper_sword", new ItemModSword(COPPER_TOOL_MATERIAL));
    public static final Item COPPER_SHOVEL = register("copper_shovel", new ItemModShovel(COPPER_TOOL_MATERIAL));
    public static final Item COPPER_PICKAXE = register("copper_pickaxe", new ItemModPickaxe(COPPER_TOOL_MATERIAL));
    public static final Item COPPER_AXE = register("copper_axe", new ItemModAxe(COPPER_TOOL_MATERIAL));
    public static final Item COPPER_HOE = register("copper_hoe", new ItemModHoe(COPPER_TOOL_MATERIAL));

    public static final Item FIRE_STEW = register("fire_stew", new ItemGeneric());
    public static final Item FROST_STEW = register("frost_stew", new ItemGeneric());
    public static final Item LIGHTNING_STEW = register("lightning_stew", new ItemGeneric());
    public static final Item DRAGONEGG_RED = register("dragonegg_red", new ItemDragonEgg(EnumDragonColor.RED));
    public static final Item DRAGONEGG_GREEN = register("dragonegg_green", new ItemDragonEgg(EnumDragonColor.GREEN));
    public static final Item DRAGONEGG_BRONZE = register("dragonegg_bronze", new ItemDragonEgg(EnumDragonColor.BRONZE));
    public static final Item DRAGONEGG_GRAY = register("dragonegg_gray", new ItemDragonEgg(EnumDragonColor.GRAY));
    public static final Item DRAGONEGG_BLUE = register("dragonegg_blue", new ItemDragonEgg(EnumDragonColor.BLUE));
    public static final Item DRAGONEGG_WHITE = register("dragonegg_white", new ItemDragonEgg(EnumDragonColor.WHITE));
    public static final Item DRAGONEGG_SAPPHIRE = register("dragonegg_sapphire", new ItemDragonEgg(EnumDragonColor.SAPPHIRE));
    public static final Item DRAGONEGG_SILVER = register("dragonegg_silver", new ItemDragonEgg(EnumDragonColor.SILVER));
    public static final Item DRAGONEGG_ELECTRIC = register("dragonegg_electric", new ItemDragonEgg(EnumDragonColor.ELECTRIC));
    public static final Item DRAGONEGG_amethyst = register("dragonegg_amethyst", new ItemDragonEgg(EnumDragonColor.AMETHYST));
    public static final Item DRAGONEGG_COPPER = register("dragonegg_copper", new ItemDragonEgg(EnumDragonColor.COPPER));
    public static final Item DRAGONEGG_BLACK = register("dragonegg_black", new ItemDragonEgg(EnumDragonColor.BLACK));
    public static final Item DRAGONSCALES_RED = register("dragonscales_red", new ItemDragonScales(EnumDragonColor.RED));
    public static final Item DRAGONSCALES_GREEN = register("dragonscales_green", new ItemDragonScales(EnumDragonColor.GREEN));
    public static final Item DRAGONSCALES_BRONZE = register("dragonscales_bronze", new ItemDragonScales(EnumDragonColor.BRONZE));
    public static final Item DRAGONSCALES_GRAY = register("dragonscales_gray", new ItemDragonScales(EnumDragonColor.GRAY));
    public static final Item DRAGONSCALES_BLUE = register("dragonscales_blue", new ItemDragonScales(EnumDragonColor.BLUE));
    public static final Item DRAGONSCALES_WHITE = register("dragonscales_white", new ItemDragonScales(EnumDragonColor.WHITE));
    public static final Item DRAGONSCALES_SAPPHIRE = register("dragonscales_sapphire", new ItemDragonScales(EnumDragonColor.SAPPHIRE));
    public static final Item DRAGONSCALES_SILVER = register("dragonscales_silver", new ItemDragonScales(EnumDragonColor.SILVER));
    public static final Item DRAGONSCALES_ELECTRIC = register("dragonscales_electric", new ItemDragonScales(EnumDragonColor.ELECTRIC));
    public static final Item DRAGONSCALES_amethyst = register("dragonscales_amethyst", new ItemDragonScales(EnumDragonColor.AMETHYST));
    public static final Item DRAGONSCALES_COPPER = register("dragonscales_copper", new ItemDragonScales(EnumDragonColor.COPPER));
    public static final Item DRAGONSCALES_BLACK = register("dragonscales_black", new ItemDragonScales(EnumDragonColor.BLACK));
    public static final Item DRAGON_BONE = register("dragonbone", new Item(new FabricItemSettings()));
    public static final Item WITHERBONE = register("witherbone", new ItemGeneric());
    public static final Item FISHING_SPEAR = register("fishing_spear", new Item(new FabricItemSettings().maxDamage(64)));
    public static final Item WITHER_SHARD = register("wither_shard", new ItemGeneric());
    public static final Item DRAGONBONE_SWORD = register("dragonbone_sword", new ItemModSword(DRAGONBONE_TOOL_MATERIAL));
    public static final Item DRAGONBONE_SHOVEL = register("dragonbone_shovel", new ItemModShovel(DRAGONBONE_TOOL_MATERIAL));
    public static final Item DRAGONBONE_PICKAXE = register("dragonbone_pickaxe", new ItemModPickaxe(DRAGONBONE_TOOL_MATERIAL));
    public static final Item DRAGONBONE_AXE = register("dragonbone_axe", new ItemModAxe(DRAGONBONE_TOOL_MATERIAL));
    public static final Item DRAGONBONE_HOE = register("dragonbone_hoe", new ItemModHoe(DRAGONBONE_TOOL_MATERIAL));
    public static final Item DRAGONBONE_SWORD_FIRE = register("dragonbone_sword_fire", new ItemAlchemySword(FIRE_DRAGONBONE_TOOL_MATERIAL));
    public static final Item DRAGONBONE_SWORD_ICE = register("dragonbone_sword_ice", new ItemAlchemySword(ICE_DRAGONBONE_TOOL_MATERIAL));
    public static final Item DRAGONBONE_SWORD_LIGHTNING = register("dragonbone_sword_lightning", new ItemAlchemySword(LIGHTNING_DRAGONBONE_TOOL_MATERIAL));
    public static final Item DRAGONBONE_ARROW = register("dragonbone_arrow", new ItemDragonArrow());
    public static final Item DRAGON_BOW = register("dragonbone_bow", new ItemDragonBow());
    public static final Item DRAGON_SKULL_FIRE = register(ItemDragonSkull.getName(0), new ItemDragonSkull(0));
    public static final Item DRAGON_SKULL_ICE = register(ItemDragonSkull.getName(1), new ItemDragonSkull(1));
    public static final Item DRAGON_SKULL_LIGHTNING = register(ItemDragonSkull.getName(2), new ItemDragonSkull(2));
    public static final ItemDragonArmor DRAGONARMOR_IRON_HEAD = buildDragonArmor(EnumDragonArmorPart.HEAD, EnumDragonArmorMaterial.IRON);
    public static final ItemDragonArmor DRAGONARMOR_IRON_NECK = buildDragonArmor(EnumDragonArmorPart.NECK, EnumDragonArmorMaterial.IRON);
    public static final ItemDragonArmor DRAGONARMOR_IRON_BODY = buildDragonArmor(EnumDragonArmorPart.BODY, EnumDragonArmorMaterial.IRON);
    public static final ItemDragonArmor DRAGONARMOR_IRON_TAIL = buildDragonArmor(EnumDragonArmorPart.TAIL, EnumDragonArmorMaterial.IRON);
    public static final ItemDragonArmor DRAGONARMOR_COPPER_HEAD = buildDragonArmor(EnumDragonArmorPart.HEAD, EnumDragonArmorMaterial.COPPER);
    public static final ItemDragonArmor DRAGONARMOR_COPPER_NECK = buildDragonArmor(EnumDragonArmorPart.NECK, EnumDragonArmorMaterial.COPPER);
    public static final ItemDragonArmor DRAGONARMOR_COPPER_BODY = buildDragonArmor(EnumDragonArmorPart.BODY, EnumDragonArmorMaterial.COPPER);
    public static final ItemDragonArmor DRAGONARMOR_COPPER_TAIL = buildDragonArmor(EnumDragonArmorPart.TAIL, EnumDragonArmorMaterial.COPPER);
    public static final ItemDragonArmor DRAGONARMOR_SILVER_HEAD = buildDragonArmor(EnumDragonArmorPart.HEAD, EnumDragonArmorMaterial.SILVER);
    public static final ItemDragonArmor DRAGONARMOR_SILVER_NECK = buildDragonArmor(EnumDragonArmorPart.NECK, EnumDragonArmorMaterial.SILVER);
    public static final ItemDragonArmor DRAGONARMOR_SILVER_BODY = buildDragonArmor(EnumDragonArmorPart.BODY, EnumDragonArmorMaterial.SILVER);
    public static final ItemDragonArmor DRAGONARMOR_SILVER_TAIL = buildDragonArmor(EnumDragonArmorPart.TAIL, EnumDragonArmorMaterial.SILVER);
    public static final ItemDragonArmor DRAGONARMOR_GOLD_HEAD = buildDragonArmor(EnumDragonArmorPart.HEAD, EnumDragonArmorMaterial.GOLD);
    public static final ItemDragonArmor DRAGONARMOR_GOLD_NECK = buildDragonArmor(EnumDragonArmorPart.NECK, EnumDragonArmorMaterial.GOLD);
    public static final ItemDragonArmor DRAGONARMOR_GOLD_BODY = buildDragonArmor(EnumDragonArmorPart.BODY, EnumDragonArmorMaterial.GOLD);
    public static final ItemDragonArmor DRAGONARMOR_GOLD_TAIL = buildDragonArmor(EnumDragonArmorPart.TAIL, EnumDragonArmorMaterial.GOLD);
    public static final ItemDragonArmor DRAGONARMOR_DIAMOND_HEAD = buildDragonArmor(EnumDragonArmorPart.HEAD, EnumDragonArmorMaterial.DIAMOND);
    public static final ItemDragonArmor DRAGONARMOR_DIAMOND_NECK = buildDragonArmor(EnumDragonArmorPart.NECK, EnumDragonArmorMaterial.DIAMOND);
    public static final ItemDragonArmor DRAGONARMOR_DIAMOND_BODY = buildDragonArmor(EnumDragonArmorPart.BODY, EnumDragonArmorMaterial.DIAMOND);
    public static final ItemDragonArmor DRAGONARMOR_DIAMOND_TAIL = buildDragonArmor(EnumDragonArmorPart.TAIL, EnumDragonArmorMaterial.DIAMOND);
    public static final ItemDragonArmor DRAGONARMOR_NETHERITE_HEAD = buildDragonArmor(EnumDragonArmorPart.HEAD, EnumDragonArmorMaterial.NETHERITE);
    public static final ItemDragonArmor DRAGONARMOR_NETHERITE_NECK = buildDragonArmor(EnumDragonArmorPart.NECK, EnumDragonArmorMaterial.NETHERITE);
    public static final ItemDragonArmor DRAGONARMOR_NETHERITE_BODY = buildDragonArmor(EnumDragonArmorPart.BODY, EnumDragonArmorMaterial.NETHERITE);
    public static final ItemDragonArmor DRAGONARMOR_NETHERITE_TAIL = buildDragonArmor(EnumDragonArmorPart.TAIL, EnumDragonArmorMaterial.NETHERITE);
    public static final ItemDragonArmor DRAGONARMOR_DRAGONSTEEL_FIRE_HEAD = buildDragonArmor(EnumDragonArmorPart.HEAD, EnumDragonArmorMaterial.DRAGON_STEEL_FIRE);
    public static final ItemDragonArmor DRAGONARMOR_DRAGONSTEEL_FIRE_NECK = buildDragonArmor(EnumDragonArmorPart.NECK, EnumDragonArmorMaterial.DRAGON_STEEL_FIRE);
    public static final ItemDragonArmor DRAGONARMOR_DRAGONSTEEL_FIRE_BODY = buildDragonArmor(EnumDragonArmorPart.BODY, EnumDragonArmorMaterial.DRAGON_STEEL_FIRE);
    public static final ItemDragonArmor DRAGONARMOR_DRAGONSTEEL_FIRE_TAIL = buildDragonArmor(EnumDragonArmorPart.TAIL, EnumDragonArmorMaterial.DRAGON_STEEL_FIRE);
    public static final ItemDragonArmor DRAGONARMOR_DRAGONSTEEL_ICE_HEAD = buildDragonArmor(EnumDragonArmorPart.HEAD, EnumDragonArmorMaterial.DRAGON_STEEL_ICE);
    public static final ItemDragonArmor DRAGONARMOR_DRAGONSTEEL_ICE_NECK = buildDragonArmor(EnumDragonArmorPart.NECK, EnumDragonArmorMaterial.DRAGON_STEEL_ICE);
    public static final ItemDragonArmor DRAGONARMOR_DRAGONSTEEL_ICE_BODY = buildDragonArmor(EnumDragonArmorPart.BODY, EnumDragonArmorMaterial.DRAGON_STEEL_ICE);
    public static final ItemDragonArmor DRAGONARMOR_DRAGONSTEEL_ICE_TAIL = buildDragonArmor(EnumDragonArmorPart.TAIL, EnumDragonArmorMaterial.DRAGON_STEEL_ICE);
    public static final ItemDragonArmor DRAGONARMOR_DRAGONSTEEL_LIGHTNING_HEAD = buildDragonArmor(EnumDragonArmorPart.HEAD, EnumDragonArmorMaterial.DRAGON_STEEL_LIGHTNING);
    public static final ItemDragonArmor DRAGONARMOR_DRAGONSTEEL_LIGHTNING_NECK = buildDragonArmor(EnumDragonArmorPart.NECK, EnumDragonArmorMaterial.DRAGON_STEEL_LIGHTNING);
    public static final ItemDragonArmor DRAGONARMOR_DRAGONSTEEL_LIGHTNING_BODY = buildDragonArmor(EnumDragonArmorPart.BODY, EnumDragonArmorMaterial.DRAGON_STEEL_LIGHTNING);
    public static final ItemDragonArmor DRAGONARMOR_DRAGONSTEEL_LIGHTNING_TAIL = buildDragonArmor(EnumDragonArmorPart.TAIL, EnumDragonArmorMaterial.DRAGON_STEEL_LIGHTNING);
    public static final Item DRAGON_MEAL = register("dragon_meal", new ItemGeneric());
    public static final Item SICKLY_DRAGON_MEAL = register("sickly_dragon_meal", new ItemGeneric(1));
    public static final Item CREATIVE_DRAGON_MEAL = register("creative_dragon_meal", new ItemGeneric(2));
    public static final Item FIRE_DRAGON_FLESH = register(ItemDragonFlesh.getNameForType(0), new ItemDragonFlesh(0));
    public static final Item ICE_DRAGON_FLESH = register(ItemDragonFlesh.getNameForType(1), new ItemDragonFlesh(1));
    public static final Item LIGHTNING_DRAGON_FLESH = register(ItemDragonFlesh.getNameForType(2), new ItemDragonFlesh(2));
    public static final Item FIRE_DRAGON_HEART = register("fire_dragon_heart", new ItemGeneric());
    public static final Item ICE_DRAGON_HEART = register("ice_dragon_heart", new ItemGeneric());
    public static final Item LIGHTNING_DRAGON_HEART = register("lightning_dragon_heart", new ItemGeneric());
    public static final Item FIRE_DRAGON_BLOOD = register("fire_dragon_blood", new ItemGeneric());
    public static final Item ICE_DRAGON_BLOOD = register("ice_dragon_blood", new ItemGeneric());
    public static final Item LIGHTNING_DRAGON_BLOOD = register("lightning_dragon_blood", new ItemGeneric());
    public static final Item DRAGON_STAFF = register("dragon_stick", new Item(new FabricItemSettings().maxCount(1)));
    public static final Item DRAGON_HORN = register("dragon_horn", new ItemDragonHorn());
    public static final Item DRAGON_FLUTE = register("dragon_flute", new ItemDragonFlute());
    public static final Item SUMMONING_CRYSTAL_FIRE = register("summoning_crystal_fire", new ItemSummoningCrystal());
    public static final Item SUMMONING_CRYSTAL_ICE = register("summoning_crystal_ice", new ItemSummoningCrystal());
    public static final Item SUMMONING_CRYSTAL_LIGHTNING = register("summoning_crystal_lightning", new ItemSummoningCrystal());
    public static final Item HIPPOGRYPH_EGG = register("hippogryph_egg", new ItemHippogryphEgg());
    public static final Item IRON_HIPPOGRYPH_ARMOR = register("iron_hippogryph_armor", new ItemGeneric(0, 1));
    public static final Item GOLD_HIPPOGRYPH_ARMOR = register("gold_hippogryph_armor", new ItemGeneric(0, 1));
    public static final Item DIAMOND_HIPPOGRYPH_ARMOR = register("diamond_hippogryph_armor", new ItemGeneric(0, 1));
    public static final Item HIPPOGRYPH_TALON = register("hippogryph_talon", new ItemGeneric(1));
    public static final Item HIPPOGRYPH_SWORD = register("hippogryph_sword", new ItemHippogryphSword());
    public static final Item GORGON_HEAD = register("gorgon_head", new ItemGorgonHead());
    public static final Item STONE_STATUE = register("stone_statue", new ItemStoneStatue());
    public static final Item BLINDFOLD = register("blindfold", new ItemBlindfold());
    public static final Item PIXIE_DUST = register("pixie_dust", new ItemPixieDust());
    public static final Item PIXIE_WINGS = register("pixie_wings", new ItemGeneric(1));
    public static final Item PIXIE_WAND = register("pixie_wand", new ItemPixieWand());
    public static final Item AMBROSIA = register("ambrosia", new ItemAmbrosia());
    public static final Item CYCLOPS_EYE = register("cyclops_eye", new ItemCyclopsEye());
    public static final Item SHEEP_HELMET = register("sheep_helmet", new ItemModArmor(SHEEP_ARMOR_MATERIAL, ArmorItem.Type.HELMET));
    public static final Item SHEEP_CHESTPLATE = register("sheep_chestplate", new ItemModArmor(SHEEP_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE));
    public static final Item SHEEP_LEGGINGS = register("sheep_leggings", new ItemModArmor(SHEEP_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS));
    public static final Item SHEEP_BOOTS = register("sheep_boots", new ItemModArmor(SHEEP_ARMOR_MATERIAL, ArmorItem.Type.BOOTS));
    public static final Item SHINY_SCALES = register("shiny_scales", new ItemGeneric());
    public static final Item SIREN_TEAR = register("siren_tear", new ItemGeneric(1));
    public static final Item SIREN_FLUTE = register("siren_flute", new ItemSirenFlute());
    public static final Item HIPPOCAMPUS_FIN = register("hippocampus_fin", new ItemGeneric(1));
    public static final Item HIPPOCAMPUS_SLAPPER = register("hippocampus_slapper", new ItemHippocampusSlapper());
    public static final Item EARPLUGS = register("earplugs", new ItemModArmor(EARPLUGS_ARMOR_MATERIAL, ArmorItem.Type.HELMET));
    public static final Item DEATH_WORM_CHITIN_YELLOW = register("deathworm_chitin_yellow", new ItemGeneric());
    public static final Item DEATH_WORM_CHITIN_WHITE = register("deathworm_chitin_white", new ItemGeneric());
    public static final Item DEATH_WORM_CHITIN_RED = register("deathworm_chitin_red", new ItemGeneric());
    public static final Item DEATHWORM_YELLOW_HELMET = register("deathworm_yellow_helmet", new ArmorItem(DEATHWORM_0_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item DEATHWORM_YELLOW_CHESTPLATE = register("deathworm_yellow_chestplate", new ArmorItem(DEATHWORM_0_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item DEATHWORM_YELLOW_LEGGINGS = register("deathworm_yellow_leggings", new ArmorItem(DEATHWORM_0_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
    public static final Item DEATHWORM_YELLOW_BOOTS = register("deathworm_yellow_boots", new ArmorItem(DEATHWORM_0_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new FabricItemSettings()));
    public static final Item DEATHWORM_WHITE_HELMET = register("deathworm_white_helmet", new ArmorItem(DEATHWORM_1_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item DEATHWORM_WHITE_CHESTPLATE = register("deathworm_white_chestplate", new ArmorItem(DEATHWORM_1_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item DEATHWORM_WHITE_LEGGINGS = register("deathworm_white_leggings", new ArmorItem(DEATHWORM_1_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
    public static final Item DEATHWORM_WHITE_BOOTS = register("deathworm_white_boots", new ArmorItem(DEATHWORM_1_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new FabricItemSettings()));
    public static final Item DEATHWORM_RED_HELMET = register("deathworm_red_helmet", new ArmorItem(DEATHWORM_2_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item DEATHWORM_RED_CHESTPLATE = register("deathworm_red_chestplate", new ArmorItem(DEATHWORM_2_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item DEATHWORM_RED_LEGGINGS = register("deathworm_red_leggings", new ArmorItem(DEATHWORM_2_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
    public static final Item DEATHWORM_RED_BOOTS = register("deathworm_red_boots", new ArmorItem(DEATHWORM_2_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new FabricItemSettings()));
    public static final Item DEATHWORM_EGG = register("deathworm_egg", new ItemDeathwormEgg(false));
    public static final Item DEATHWORM_EGG_GIGANTIC = register("deathworm_egg_giant", new ItemDeathwormEgg(true));
    public static final Item DEATHWORM_TOUNGE = register("deathworm_tounge", new ItemGeneric(1));
    public static final Item DEATHWORM_GAUNTLET_YELLOW = register("deathworm_gauntlet_yellow", new ItemDeathwormGauntlet());
    public static final Item DEATHWORM_GAUNTLET_WHITE = register("deathworm_gauntlet_white", new ItemDeathwormGauntlet());
    public static final Item DEATHWORM_GAUNTLET_RED = register("deathworm_gauntlet_red", new ItemDeathwormGauntlet());
    public static final Item ROTTEN_EGG = register("rotten_egg", new ItemRottenEgg());
    public static final Item COCKATRICE_EYE = register("cockatrice_eye", new ItemGeneric(1));
    public static final Item ITEM_COCKATRICE_SCEPTER = register("cockatrice_scepter", new ItemCockatriceScepter());
    public static final Item STYMPHALIAN_BIRD_FEATHER = register("stymphalian_bird_feather", new ItemGeneric());
    public static final Item STYMPHALIAN_ARROW = register("stymphalian_arrow", new ItemStymphalianArrow());
    public static final Item STYMPHALIAN_FEATHER_BUNDLE = register("stymphalian_feather_bundle", new ItemStymphalianFeatherBundle());
    public static final Item STYMPHALIAN_DAGGER = register("stymphalian_bird_dagger", new ItemStymphalianDagger());
    public static final Item TROLL_TUSK = register("troll_tusk", new ItemGeneric());
    public static final Item MYRMEX_DESERT_EGG = register("myrmex_desert_egg", new ItemMyrmexEgg(false));
    public static final Item MYRMEX_JUNGLE_EGG = register("myrmex_jungle_egg", new ItemMyrmexEgg(true));
    public static final Item MYRMEX_DESERT_RESIN = register("myrmex_desert_resin", new ItemGeneric());
    public static final Item MYRMEX_JUNGLE_RESIN = register("myrmex_jungle_resin", new ItemGeneric());
    public static final Item MYRMEX_DESERT_CHITIN = register("myrmex_desert_chitin", new ItemGeneric());
    public static final Item MYRMEX_JUNGLE_CHITIN = register("myrmex_jungle_chitin", new ItemGeneric());
    public static final Item MYRMEX_STINGER = register("myrmex_stinger", new ItemGeneric());
    public static final Item MYRMEX_DESERT_SWORD = register("myrmex_desert_sword", new ItemModSword(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final Item MYRMEX_DESERT_SWORD_VENOM = register("myrmex_desert_sword_venom", new ItemModSword(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final Item MYRMEX_DESERT_SHOVEL = register("myrmex_desert_shovel", new ItemModShovel(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final Item MYRMEX_DESERT_PICKAXE = register("myrmex_desert_pickaxe", new ItemModPickaxe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final Item MYRMEX_DESERT_AXE = register("myrmex_desert_axe", new ItemModAxe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final Item MYRMEX_DESERT_HOE = register("myrmex_desert_hoe", new ItemModHoe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final Item MYRMEX_JUNGLE_SWORD = register("myrmex_jungle_sword", new ItemModSword(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final Item MYRMEX_JUNGLE_SWORD_VENOM = register("myrmex_jungle_sword_venom", new ItemModSword(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final Item MYRMEX_JUNGLE_SHOVEL = register("myrmex_jungle_shovel", new ItemModShovel(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final Item MYRMEX_JUNGLE_PICKAXE = register("myrmex_jungle_pickaxe", new ItemModPickaxe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final Item MYRMEX_JUNGLE_AXE = register("myrmex_jungle_axe", new ItemModAxe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final Item MYRMEX_JUNGLE_HOE = register("myrmex_jungle_hoe", new ItemModHoe(MYRMEX_CHITIN_TOOL_MATERIAL));
    public static final Item MYRMEX_DESERT_STAFF = register("myrmex_desert_staff", new ItemMyrmexStaff(false));
    public static final Item MYRMEX_JUNGLE_STAFF = register("myrmex_jungle_staff", new ItemMyrmexStaff(true));
    public static final Item MYRMEX_DESERT_HELMET = register("myrmex_desert_helmet", new ItemModArmor(MYRMEX_DESERT_ARMOR_MATERIAL, ArmorItem.Type.HELMET));
    public static final Item MYRMEX_DESERT_CHESTPLATE = register("myrmex_desert_chestplate", new ItemModArmor(MYRMEX_DESERT_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE));
    public static final Item MYRMEX_DESERT_LEGGINGS = register("myrmex_desert_leggings", new ItemModArmor(MYRMEX_DESERT_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS));
    public static final Item MYRMEX_DESERT_BOOTS = register("myrmex_desert_boots", new ItemModArmor(MYRMEX_DESERT_ARMOR_MATERIAL, ArmorItem.Type.BOOTS));
    public static final Item MYRMEX_JUNGLE_HELMET = register("myrmex_jungle_helmet", new ItemModArmor(MYRMEX_JUNGLE_ARMOR_MATERIAL, ArmorItem.Type.HELMET));
    public static final Item MYRMEX_JUNGLE_CHESTPLATE = register("myrmex_jungle_chestplate", new ItemModArmor(MYRMEX_JUNGLE_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE));
    public static final Item MYRMEX_JUNGLE_LEGGINGS = register("myrmex_jungle_leggings", new ItemModArmor(MYRMEX_JUNGLE_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS));
    public static final Item MYRMEX_JUNGLE_BOOTS = register("myrmex_jungle_boots", new ItemModArmor(MYRMEX_JUNGLE_ARMOR_MATERIAL, ArmorItem.Type.BOOTS));
    public static final Item MYRMEX_DESERT_SWARM = register("myrmex_desert_swarm", new ItemMyrmexSwarm(false));
    public static final Item MYRMEX_JUNGLE_SWARM = register("myrmex_jungle_swarm", new ItemMyrmexSwarm(true));
    public static final Item AMPHITHERE_FEATHER = register("amphithere_feather", new ItemGeneric());
    public static final Item AMPHITHERE_ARROW = register("amphithere_arrow", new ItemAmphithereArrow());
    public static final Item AMPHITHERE_MACUAHUITL = register("amphithere_macuahuitl", new ItemAmphithereMacuahuitl());
    public static final Item SERPENT_FANG = register("sea_serpent_fang", new ItemGeneric());
    public static final Item SEA_SERPENT_ARROW = register("sea_serpent_arrow", new ItemSeaSerpentArrow());
    public static final Item TIDE_TRIDENT_INVENTORY = register("tide_trident_inventory", new ItemGeneric(0, true));
    public static final Item TIDE_TRIDENT = register("tide_trident", new ItemTideTrident());
    public static final Item CHAIN = register("chain", new ItemChain(false));
    public static final Item CHAIN_STICKY = register("chain_sticky", new ItemChain(true));
    public static final Item DRAGONSTEEL_FIRE_INGOT = register("dragonsteel_fire_ingot", new ItemGeneric());
    public static final Item DRAGONSTEEL_FIRE_SWORD = register("dragonsteel_fire_sword", new ItemModSword(createMaterialWithRepairItem(DRAGONSTEEL_FIRE_INGOT, "dragonsteel_tier_fire")));
    public static final Item DRAGONSTEEL_FIRE_PICKAXE = register("dragonsteel_fire_pickaxe", new ItemModPickaxe(createMaterialWithRepairItem(DRAGONSTEEL_FIRE_INGOT, "dragonsteel_tier_fire")));
    public static final Item DRAGONSTEEL_FIRE_AXE = register("dragonsteel_fire_axe", new ItemModAxe(createMaterialWithRepairItem(DRAGONSTEEL_FIRE_INGOT, "dragonsteel_tier_fire")));
    public static final Item DRAGONSTEEL_FIRE_SHOVEL = register("dragonsteel_fire_shovel", new ItemModShovel(createMaterialWithRepairItem(DRAGONSTEEL_FIRE_INGOT, "dragonsteel_tier_fire")));
    public static final Item DRAGONSTEEL_FIRE_HOE = register("dragonsteel_fire_hoe", new ItemModHoe(createMaterialWithRepairItem(DRAGONSTEEL_FIRE_INGOT, "dragonsteel_tier_fire")));
    public static final Item DRAGONSTEEL_FIRE_HELMET = register("dragonsteel_fire_helmet", new ItemDragonSteelArmor(DRAGONSTEEL_FIRE_ARMOR_MATERIAL, 0, ArmorItem.Type.HELMET));
    public static final Item DRAGONSTEEL_FIRE_CHESTPLATE = register("dragonsteel_fire_chestplate", new ItemDragonSteelArmor(DRAGONSTEEL_FIRE_ARMOR_MATERIAL, 1, ArmorItem.Type.CHESTPLATE));
    public static final Item DRAGONSTEEL_FIRE_LEGGINGS = register("dragonsteel_fire_leggings", new ItemDragonSteelArmor(DRAGONSTEEL_FIRE_ARMOR_MATERIAL, 2, ArmorItem.Type.LEGGINGS));
    public static final Item DRAGONSTEEL_FIRE_BOOTS = register("dragonsteel_fire_boots", new ItemDragonSteelArmor(DRAGONSTEEL_FIRE_ARMOR_MATERIAL, 3, ArmorItem.Type.BOOTS));
    public static final Item DRAGONSTEEL_ICE_INGOT = register("dragonsteel_ice_ingot", new ItemGeneric());
    public static final Item DRAGONSTEEL_ICE_SWORD = register("dragonsteel_ice_sword", new ItemModSword(createMaterialWithRepairItem(DRAGONSTEEL_ICE_INGOT, "dragonsteel_tier_ice")));
    public static final Item DRAGONSTEEL_ICE_PICKAXE = register("dragonsteel_ice_pickaxe", new ItemModPickaxe(createMaterialWithRepairItem(DRAGONSTEEL_ICE_INGOT, "dragonsteel_tier_ice")));
    public static final Item DRAGONSTEEL_ICE_AXE = register("dragonsteel_ice_axe", new ItemModAxe(createMaterialWithRepairItem(DRAGONSTEEL_ICE_INGOT, "dragonsteel_tier_ice")));
    public static final Item DRAGONSTEEL_ICE_SHOVEL = register("dragonsteel_ice_shovel", new ItemModShovel(createMaterialWithRepairItem(DRAGONSTEEL_ICE_INGOT, "dragonsteel_tier_ice")));
    public static final Item DRAGONSTEEL_ICE_HOE = register("dragonsteel_ice_hoe", new ItemModHoe(createMaterialWithRepairItem(DRAGONSTEEL_ICE_INGOT, "dragonsteel_tier_ice")));
    public static final Item DRAGONSTEEL_ICE_HELMET = register("dragonsteel_ice_helmet", new ItemDragonSteelArmor(DRAGONSTEEL_ICE_ARMOR_MATERIAL, 0, ArmorItem.Type.HELMET));
    public static final Item DRAGONSTEEL_ICE_CHESTPLATE = register("dragonsteel_ice_chestplate", new ItemDragonSteelArmor(DRAGONSTEEL_ICE_ARMOR_MATERIAL, 1, ArmorItem.Type.CHESTPLATE));
    public static final Item DRAGONSTEEL_ICE_LEGGINGS = register("dragonsteel_ice_leggings", new ItemDragonSteelArmor(DRAGONSTEEL_ICE_ARMOR_MATERIAL, 2, ArmorItem.Type.LEGGINGS));
    public static final Item DRAGONSTEEL_ICE_BOOTS = register("dragonsteel_ice_boots", new ItemDragonSteelArmor(DRAGONSTEEL_ICE_ARMOR_MATERIAL, 3, ArmorItem.Type.BOOTS));

    public static final Item DRAGONSTEEL_LIGHTNING_INGOT = register("dragonsteel_lightning_ingot", new ItemGeneric());
    public static final Item DRAGONSTEEL_LIGHTNING_SWORD = register("dragonsteel_lightning_sword", new ItemModSword(createMaterialWithRepairItem(DRAGONSTEEL_LIGHTNING_INGOT, "dragonsteel_tier_lightning")));
    public static final Item DRAGONSTEEL_LIGHTNING_PICKAXE = register("dragonsteel_lightning_pickaxe", new ItemModPickaxe(createMaterialWithRepairItem(DRAGONSTEEL_LIGHTNING_INGOT, "dragonsteel_tier_lightning")));
    public static final Item DRAGONSTEEL_LIGHTNING_AXE = register("dragonsteel_lightning_axe", new ItemModAxe(createMaterialWithRepairItem(DRAGONSTEEL_LIGHTNING_INGOT, "dragonsteel_tier_lightning")));
    public static final Item DRAGONSTEEL_LIGHTNING_SHOVEL = register("dragonsteel_lightning_shovel", new ItemModShovel(createMaterialWithRepairItem(DRAGONSTEEL_LIGHTNING_INGOT, "dragonsteel_tier_lightning")));
    public static final Item DRAGONSTEEL_LIGHTNING_HOE = register("dragonsteel_lightning_hoe", new ItemModHoe(createMaterialWithRepairItem(DRAGONSTEEL_LIGHTNING_INGOT, "dragonsteel_tier_lightning")));
    public static final Item DRAGONSTEEL_LIGHTNING_HELMET = register("dragonsteel_lightning_helmet", new ItemDragonSteelArmor(DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL, 0, ArmorItem.Type.HELMET));
    public static final Item DRAGONSTEEL_LIGHTNING_CHESTPLATE = register("dragonsteel_lightning_chestplate", new ItemDragonSteelArmor(DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL, 1, ArmorItem.Type.CHESTPLATE));
    public static final Item DRAGONSTEEL_LIGHTNING_LEGGINGS = register("dragonsteel_lightning_leggings", new ItemDragonSteelArmor(DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL, 2, ArmorItem.Type.LEGGINGS));
    public static final Item DRAGONSTEEL_LIGHTNING_BOOTS = register("dragonsteel_lightning_boots", new ItemDragonSteelArmor(DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL, 3, ArmorItem.Type.BOOTS));

    public static final Item WEEZER_BLUE_ALBUM = register("weezer_blue_album", new ItemGeneric(1, true));
    public static final Item DRAGON_DEBUG_STICK = register("dragon_debug_stick", new ItemGeneric(1, true), false);
    public static final Item DREAD_SWORD = register("dread_sword", new ItemModSword(DREAD_SWORD_TOOL_MATERIAL));
    public static final Item DREAD_KNIGHT_SWORD = register("dread_knight_sword", new ItemModSword(DREAD_KNIGHT_TOOL_MATERIAL));
    public static final Item LICH_STAFF = register("lich_staff", new ItemLichStaff());
    public static final Item DREAD_QUEEN_SWORD = register("dread_queen_sword", new ItemModSword(createMaterialWithRepairItem(Items.AIR, "dragonsteel_tier_dread_queen")));
    public static final Item DREAD_QUEEN_STAFF = register("dread_queen_staff", new ItemDreadQueenStaff());
    public static final Item DREAD_SHARD = register("dread_shard", new ItemGeneric(0));
    public static final Item DREAD_KEY = register("dread_key", new ItemGeneric(0));
    public static final Item HYDRA_FANG = register("hydra_fang", new ItemGeneric(0));
    public static final Item HYDRA_HEART = register("hydra_heart", new ItemHydraHeart());
    public static final Item HYDRA_ARROW = register("hydra_arrow", new ItemHydraArrow());
    public static final Item CANNOLI = register("cannoli", new ItemCannoli(), false);
    public static final Item ECTOPLASM = register("ectoplasm", new ItemGeneric());
    public static final Item GHOST_INGOT = register("ghost_ingot", new ItemGeneric(1));
    public static final Item GHOST_SWORD = register("ghost_sword", new ItemGhostSword());
    public static final Item DRAGON_SEEKER = register("dragon_seeker", new ItemDragonSeeker(ItemDragonSeeker.SeekerType.NORMAL));
    public static final Item EPIC_DRAGON_SEEKER = register("epic_dragon_seeker", new ItemDragonSeeker(ItemDragonSeeker.SeekerType.EPIC));
    public static final Item LEGENDARY_DRAGON_SEEKER = register("legendary_dragon_seeker", new ItemDragonSeeker(ItemDragonSeeker.SeekerType.LEGENDARY));
    public static final Item GODLY_DRAGON_SEEKER = register("godly_dragon_seeker", new ItemDragonSeeker(ItemDragonSeeker.SeekerType.GODLY));

    public static final BannerPatternItem PATTERN_FIRE = register("banner_pattern_fire", new BannerPatternItem(BannerPatternTags.FIRE_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));
    public static final BannerPatternItem PATTERN_ICE = register("banner_pattern_ice", new BannerPatternItem(BannerPatternTags.ICE_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));
    public static final BannerPatternItem PATTERN_LIGHTNING = register("banner_pattern_lightning", new BannerPatternItem(BannerPatternTags.LIGHTNING_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));
    public static final BannerPatternItem PATTERN_FIRE_HEAD = register("banner_pattern_fire_head", new BannerPatternItem(BannerPatternTags.FIRE_HEAD_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));
    public static final BannerPatternItem PATTERN_ICE_HEAD = register("banner_pattern_ice_head", new BannerPatternItem(BannerPatternTags.ICE_HEAD_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));
    public static final BannerPatternItem PATTERN_LIGHTNING_HEAD = register("banner_pattern_lightning_head", new BannerPatternItem(BannerPatternTags.LIGHTNING_HEAD_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));
    public static final BannerPatternItem PATTERN_AMPHITHERE = register("banner_pattern_amphithere", new BannerPatternItem(BannerPatternTags.AMPHITHERE_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));
    public static final BannerPatternItem PATTERN_BIRD = register("banner_pattern_bird", new BannerPatternItem(BannerPatternTags.BIRD_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));
    public static final BannerPatternItem PATTERN_EYE = register("banner_pattern_eye", new BannerPatternItem(BannerPatternTags.EYE_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));
    public static final BannerPatternItem PATTERN_FAE = register("banner_pattern_fae", new BannerPatternItem(BannerPatternTags.FAE_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));
    public static final BannerPatternItem PATTERN_FEATHER = register("banner_pattern_feather", new BannerPatternItem(BannerPatternTags.FEATHER_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));
    public static final BannerPatternItem PATTERN_GORGON = register("banner_pattern_gorgon", new BannerPatternItem(BannerPatternTags.GORGON_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));
    public static final BannerPatternItem PATTERN_HIPPOCAMPUS = register("banner_pattern_hippocampus", new BannerPatternItem(BannerPatternTags.HIPPOCAMPUS_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));
    public static final BannerPatternItem PATTERN_HIPPOGRYPH_HEAD = register("banner_pattern_hippogryph_head", new BannerPatternItem(BannerPatternTags.HIPPOGRYPH_HEAD_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));
    public static final BannerPatternItem PATTERN_MERMAID = register("banner_pattern_mermaid", new BannerPatternItem(BannerPatternTags.MERMAID_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));
    public static final BannerPatternItem PATTERN_SEA_SERPENT = register("banner_pattern_sea_serpent", new BannerPatternItem(BannerPatternTags.SEA_SERPENT_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));
    public static final BannerPatternItem PATTERN_TROLL = register("banner_pattern_troll", new BannerPatternItem(BannerPatternTags.TROLL_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));
    public static final BannerPatternItem PATTERN_WEEZER = register("banner_pattern_weezer", new BannerPatternItem(BannerPatternTags.WEEZER_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));
    public static final BannerPatternItem PATTERN_DREAD = register("banner_pattern_dread", new BannerPatternItem(BannerPatternTags.DREAD_BANNER_PATTERN, new FabricItemSettings().maxCount(1)));

    public static final DelightFoodItem COOKED_RICE_WITH_FIRE_DRAGON_MEAT = register("cooked_rice_with_fire_dragon_meat", new DelightFoodItem(new Item.Settings().maxCount(1).food(new FoodComponent.Builder().meat().hunger(4).saturationModifier(0.6f).statusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 20 * 5), 1).statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 20 * 60 * 2), 1).build())));
    public static final DelightFoodItem COOKED_RICE_WITH_ICE_DRAGON_MEAT = register("cooked_rice_with_ice_dragon_meat", new DelightFoodItem(new Item.Settings().maxCount(1).food(new FoodComponent.Builder().meat().hunger(4).saturationModifier(0.6f).statusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 20 * 5), 1).statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 20 * 60 * 2, 2), 1).build())));
    public static final DelightFoodItem COOKED_RICE_WITH_LIGHTNING_DRAGON_MEAT = register("cooked_rice_with_lightning_dragon_meat", new DelightFoodItem(new Item.Settings().maxCount(1).food(new FoodComponent.Builder().meat().hunger(4).saturationModifier(0.6f).statusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 20 * 5), 1).statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 60 * 2, 2), 1).build())));
    public static final DelightFoodItem GHOST_CREAM = register("ghost_cream", new DelightFoodItem(new Item.Settings().maxCount(1).food(new FoodComponent.Builder().snack().hunger(4).saturationModifier(0.6f).statusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 20 * 20), 1).build())));
    public static final DelightFoodItem PIXIE_DUST_MILKY_TEA = register("pixie_dust_milky_tea", new DelightFoodItem(new Item.Settings().maxCount(1).food(new FoodComponent.Builder().snack().hunger(4).saturationModifier(0.6f).statusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 20 * 60 * 2), 1).build())));

    static {
        EnumDragonArmor.initArmors();
        EnumSeaSerpent.initArmors();
        EnumSkullType.initItems();
        EnumTroll.initArmors();
    }

    public static void init() {
        registerItems();
        setRepairMaterials();
    }

    public static void registerItems() {
        //spawn Eggs
        register("spawn_egg_fire_dragon", new SpawnEggItem(IafEntities.FIRE_DRAGON, 0X340000, 0XA52929, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_ice_dragon", new SpawnEggItem(IafEntities.ICE_DRAGON, 0XB5DDFB, 0X7EBAF0, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_lightning_dragon", new SpawnEggItem(IafEntities.LIGHTNING_DRAGON, 0X422367, 0X725691, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_hippogryph", new SpawnEggItem(IafEntities.HIPPOGRYPH, 0XD8D8D8, 0XD1B55D, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_gorgon", new SpawnEggItem(IafEntities.GORGON, 0XD0D99F, 0X684530, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_pixie", new SpawnEggItem(IafEntities.PIXIE, 0XFF7F89, 0XE2CCE2, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_cyclops", new SpawnEggItem(IafEntities.CYCLOPS, 0XB0826E, 0X3A1F0F, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_siren", new SpawnEggItem(IafEntities.SIREN, 0X8EE6CA, 0XF2DFC8, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_hippocampus", new SpawnEggItem(IafEntities.HIPPOCAMPUS, 0X4491C7, 0X4FC56B, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_death_worm", new SpawnEggItem(IafEntities.DEATH_WORM, 0XD1CDA3, 0X423A3A, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_cockatrice", new SpawnEggItem(IafEntities.COCKATRICE, 0X8F5005, 0X4F5A23, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_stymphalian_bird", new SpawnEggItem(IafEntities.STYMPHALIAN_BIRD, 0X744F37, 0X9E6C4B, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_troll", new SpawnEggItem(IafEntities.TROLL, 0X3D413D, 0X58433A, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_myrmex_worker", new SpawnEggItem(IafEntities.MYRMEX_WORKER, 0XA16026, 0X594520, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_myrmex_soldier", new SpawnEggItem(IafEntities.MYRMEX_SOLDIER, 0XA16026, 0X7D622D, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_myrmex_sentinel", new SpawnEggItem(IafEntities.MYRMEX_SENTINEL, 0XA16026, 0XA27F3A, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_myrmex_royal", new SpawnEggItem(IafEntities.MYRMEX_ROYAL, 0XA16026, 0XC79B48, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_myrmex_queen", new SpawnEggItem(IafEntities.MYRMEX_QUEEN, 0XA16026, 0XECB855, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_amphithere", new SpawnEggItem(IafEntities.AMPHITHERE, 0X597535, 0X00AA98, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_sea_serpent", new SpawnEggItem(IafEntities.SEA_SERPENT, 0X008299, 0XC5E6E7, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_dread_thrall", new SpawnEggItem(IafEntities.DREAD_THRALL, 0XE0E6E6, 0X00FFFF, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_dread_ghoul", new SpawnEggItem(IafEntities.DREAD_GHOUL, 0XE0E6E6, 0X7B838A, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_dread_beast", new SpawnEggItem(IafEntities.DREAD_BEAST, 0XE0E6E6, 0X38373C, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_dread_scuttler", new SpawnEggItem(IafEntities.DREAD_SCUTTLER, 0XE0E6E6, 0X4D5667, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_lich", new SpawnEggItem(IafEntities.DREAD_LICH, 0XE0E6E6, 0X274860, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_dread_knight", new SpawnEggItem(IafEntities.DREAD_KNIGHT, 0XE0E6E6, 0X4A6C6E, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_dread_horse", new SpawnEggItem(IafEntities.DREAD_HORSE, 0XE0E6E6, 0XACACAC, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_hydra", new SpawnEggItem(IafEntities.HYDRA, 0X8B8B78, 0X2E372B, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
        register("spawn_egg_ghost", new SpawnEggItem(IafEntities.GHOST, 0XB9EDB8, 0X73B276, new Item.Settings()/*.tab(IceAndFire.TAB_ITEMS)*/));
    }

    public static ItemDragonArmor buildDragonArmor(EnumDragonArmorPart type, EnumDragonArmorMaterial material) {
        return register(String.format("dragonarmor_%s_%s", material.getId(), type.getId()), new ItemDragonArmor(material, type));
    }

    public static <T extends Item> T register(String name, T item) {
        return register(name, item, true);
    }

    public static <T extends Item> T register(String name, T item, boolean putInTab) {
        if (putInTab) IafItemGroups.TAB_ITEMS_LIST.add(item);
        return Registry.register(Registries.ITEM, new Identifier(IceAndFire.MOD_ID, name), item);
    }

    public static void setRepairMaterials() {
        IafItems.BLINDFOLD_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromTag(CommonTags.Items.STRING));
        IafItems.SILVER_ARMOR_MATERIAL.setRepairMaterial(Ingredient.fromTag(IafItemTags.INGOTS_SILVER));
        IafItems.SILVER_TOOL_MATERIAL.setRepairMaterial(Ingredient.fromTag(IafItemTags.INGOTS_SILVER));
        IafItems.DRAGONBONE_TOOL_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.DRAGON_BONE));
        IafItems.FIRE_DRAGONBONE_TOOL_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.DRAGON_BONE));
        IafItems.ICE_DRAGONBONE_TOOL_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.DRAGON_BONE));
        IafItems.LIGHTNING_DRAGONBONE_TOOL_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.DRAGON_BONE));
        for (EnumDragonArmor armor : EnumDragonArmor.values())
            armor.armorMaterial.setRepairMaterial(Ingredient.ofItems(EnumDragonArmor.getScaleItem(armor)));
        IafItems.DRAGONSTEEL_FIRE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.DRAGONSTEEL_FIRE_INGOT));
        IafItems.DRAGONSTEEL_ICE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.DRAGONSTEEL_ICE_INGOT));
        IafItems.DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.DRAGONSTEEL_LIGHTNING_INGOT));
        IafItems.SHEEP_ARMOR_MATERIAL.setRepairMaterial(Ingredient.ofItems(Blocks.WHITE_WOOL));
        IafItems.EARPLUGS_ARMOR_MATERIAL.setRepairMaterial(Ingredient.ofItems(Blocks.OAK_BUTTON));
        IafItems.DEATHWORM_0_ARMOR_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.DEATH_WORM_CHITIN_YELLOW));
        IafItems.DEATHWORM_1_ARMOR_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.DEATH_WORM_CHITIN_RED));
        IafItems.DEATHWORM_2_ARMOR_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.DEATH_WORM_CHITIN_WHITE));
        IafItems.TROLL_WEAPON_TOOL_MATERIAL.setRepairMaterial(Ingredient.ofItems(Items.STONE));
        IafItems.TROLL_MOUNTAIN_ARMOR_MATERIAL.setRepairMaterial(Ingredient.ofItems(EnumTroll.MOUNTAIN.leather));
        IafItems.TROLL_FOREST_ARMOR_MATERIAL.setRepairMaterial(Ingredient.ofItems(EnumTroll.FOREST.leather));
        IafItems.TROLL_FROST_ARMOR_MATERIAL.setRepairMaterial(Ingredient.ofItems(EnumTroll.FROST.leather));
        IafItems.HIPPOGRYPH_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.HIPPOGRYPH_TALON));
        IafItems.HIPPOCAMPUS_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.SHINY_SCALES));
        IafItems.AMPHITHERE_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.AMPHITHERE_FEATHER));
        IafItems.STYMHALIAN_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.STYMPHALIAN_BIRD_FEATHER));
        IafItems.MYRMEX_CHITIN_TOOL_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.MYRMEX_DESERT_CHITIN));
        IafItems.MYRMEX_DESERT_ARMOR_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.MYRMEX_DESERT_CHITIN));
        IafItems.MYRMEX_JUNGLE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.MYRMEX_JUNGLE_CHITIN));
        IafItems.DREAD_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.DREAD_SHARD));
        IafItems.DREAD_KNIGHT_TOOL_MATERIAL.setRepairMaterial(Ingredient.ofItems(IafItems.DREAD_SHARD));
        for (EnumSeaSerpent serpent : EnumSeaSerpent.values())
            serpent.armorMaterial.setRepairMaterial(Ingredient.ofItems(serpent.scale));
    }

    public static void registerModelPredicates() {
        ModelPredicateProviderRegistry.register(IafItems.DRAGON_BOW, new Identifier("pulling"), (itemStack, clientWorld, livingEntity, seed) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1 : 0);
        ModelPredicateProviderRegistry.register(IafItems.DRAGON_BOW, new Identifier("pull"), (itemStack, clientWorld, livingEntity, seed) -> livingEntity == null ? 0 : livingEntity.getActiveItem() != itemStack ? 0 : (float) (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / 20);

        ModelPredicateProviderRegistry.register(IafItems.DRAGON_HORN, new Identifier("iceorfire"), (stack, level, entity, p) -> ItemDragonHorn.getDragonType(stack) * 0.25F);
        ModelPredicateProviderRegistry.register(IafItems.SUMMONING_CRYSTAL_FIRE, new Identifier("has_dragon"), (stack, level, entity, p) -> ItemSummoningCrystal.hasDragon(stack) ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(IafItems.SUMMONING_CRYSTAL_ICE, new Identifier("has_dragon"), (stack, level, entity, p) -> ItemSummoningCrystal.hasDragon(stack) ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(IafItems.SUMMONING_CRYSTAL_LIGHTNING, new Identifier("has_dragon"), (stack, level, entity, p) -> ItemSummoningCrystal.hasDragon(stack) ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(IafItems.TIDE_TRIDENT, new Identifier("throwing"), (stack, level, entity, p) -> entity != null && entity.isUsingItem() && entity.getMainHandStack() == stack ? 1.0F : 0.0F);
    }
}
