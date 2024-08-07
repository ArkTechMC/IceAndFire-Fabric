package com.iafenvoy.iceandfire.data;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.item.ItemSeaSerpentScales;
import com.iafenvoy.iceandfire.item.armor.IafArmorMaterial;
import com.iafenvoy.iceandfire.item.armor.ItemSeaSerpentArmor;
import com.iafenvoy.iceandfire.item.block.BlockSeaSerpentScales;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.uranus.object.IdUtil;
import com.iafenvoy.uranus.object.item.CustomArmorMaterial;
import net.minecraft.block.Block;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SeaSerpent {
    public static final SeaSerpent BLUE = new SeaSerpent("blue", Formatting.BLUE);
    public static final SeaSerpent BRONZE = new SeaSerpent("bronze", Formatting.GOLD);
    public static final SeaSerpent DEEPBLUE = new SeaSerpent("deepblue", Formatting.DARK_BLUE);
    public static final SeaSerpent GREEN = new SeaSerpent("green", Formatting.DARK_GREEN);
    public static final SeaSerpent PURPLE = new SeaSerpent("purple", Formatting.DARK_PURPLE);
    public static final SeaSerpent RED = new SeaSerpent("red", Formatting.DARK_RED);
    public static final SeaSerpent TEAL = new SeaSerpent("teal", Formatting.AQUA);
    private static final List<SeaSerpent> TYPES = new ArrayList<>();
    public final String resourceName;
    public final Formatting color;
    public CustomArmorMaterial armorMaterial;
    public Item scale;
    public Item helmet;
    public Item chestplate;
    public Item leggings;
    public Item boots;
    public Block scaleBlock;

    public SeaSerpent(String resourceName, Formatting color) {
        this.resourceName = resourceName.toLowerCase(Locale.ROOT);
        this.color = color;
        TYPES.add(this);
    }

    public static List<SeaSerpent> values() {
        return ImmutableList.copyOf(TYPES);
    }

    public static void initArmors() {
        for (SeaSerpent color : SeaSerpent.values()) {
            color.armorMaterial = new IafArmorMaterial(IdUtil.build(IceAndFire.MOD_ID, "sea_serpent_scales_") + color.resourceName, 30, new int[]{4, 8, 7, 4}, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 2.5F);
            color.scaleBlock = IafBlocks.register("sea_serpent_scale_block_" + color.resourceName, new BlockSeaSerpentScales(color.resourceName, color.color));
            color.scale = IafItems.register("sea_serpent_scales_" + color.resourceName, new ItemSeaSerpentScales(color.resourceName, color.color));
            color.helmet = IafItems.register("tide_" + color.resourceName + "_helmet", new ItemSeaSerpentArmor(color, color.armorMaterial, ArmorItem.Type.HELMET));
            color.chestplate = IafItems.register("tide_" + color.resourceName + "_chestplate", new ItemSeaSerpentArmor(color, color.armorMaterial, ArmorItem.Type.CHESTPLATE));
            color.leggings = IafItems.register("tide_" + color.resourceName + "_leggings", new ItemSeaSerpentArmor(color, color.armorMaterial, ArmorItem.Type.LEGGINGS));
            color.boots = IafItems.register("tide_" + color.resourceName + "_boots", new ItemSeaSerpentArmor(color, color.armorMaterial, ArmorItem.Type.BOOTS));
        }
    }
}
