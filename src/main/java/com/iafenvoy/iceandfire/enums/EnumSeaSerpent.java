package com.iafenvoy.iceandfire.enums;

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

public class EnumSeaSerpent {
    private static final List<EnumSeaSerpent> TYPES = new ArrayList<>();
    public static final EnumSeaSerpent BLUE = new EnumSeaSerpent("blue", Formatting.BLUE);
    public static final EnumSeaSerpent BRONZE = new EnumSeaSerpent("bronze", Formatting.GOLD);
    public static final EnumSeaSerpent DEEPBLUE = new EnumSeaSerpent("deepblue", Formatting.DARK_BLUE);
    public static final EnumSeaSerpent GREEN = new EnumSeaSerpent("green", Formatting.DARK_GREEN);
    public static final EnumSeaSerpent PURPLE = new EnumSeaSerpent("purple", Formatting.DARK_PURPLE);
    public static final EnumSeaSerpent RED = new EnumSeaSerpent("red", Formatting.DARK_RED);
    public static final EnumSeaSerpent TEAL = new EnumSeaSerpent("teal", Formatting.AQUA);

    public final String resourceName;
    public final Formatting color;
    public CustomArmorMaterial armorMaterial;
    public Item scale;
    public Item helmet;
    public Item chestplate;
    public Item leggings;
    public Item boots;
    public Block scaleBlock;

    public EnumSeaSerpent(String resourceName, Formatting color) {
        this.resourceName = resourceName.toLowerCase(Locale.ROOT);
        this.color = color;
        TYPES.add(this);
    }

    public static List<EnumSeaSerpent> values() {
        return ImmutableList.copyOf(TYPES);
    }

    public static void initArmors() {
        for (EnumSeaSerpent color : EnumSeaSerpent.values()) {
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
