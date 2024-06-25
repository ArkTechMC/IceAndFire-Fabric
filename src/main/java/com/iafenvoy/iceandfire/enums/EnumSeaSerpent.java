package com.iafenvoy.iceandfire.enums;

import com.iafenvoy.citadel.server.item.CustomArmorMaterial;
import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.block.BlockSeaSerpentScales;
import com.iafenvoy.iceandfire.item.ItemSeaSerpentScales;
import com.iafenvoy.iceandfire.item.armor.IafArmorMaterial;
import com.iafenvoy.iceandfire.item.armor.ItemSeaSerpentArmor;
import com.iafenvoy.iceandfire.registry.IafBlocks;
import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.iceandfire.util.IdUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;

import java.util.Locale;

public enum EnumSeaSerpent {
    BLUE(Formatting.BLUE),
    BRONZE(Formatting.GOLD),
    DEEPBLUE(Formatting.DARK_BLUE),
    GREEN(Formatting.DARK_GREEN),
    PURPLE(Formatting.DARK_PURPLE),
    RED(Formatting.DARK_RED),
    TEAL(Formatting.AQUA);

    public final String resourceName;
    public final Formatting color;
    public CustomArmorMaterial armorMaterial;
    public Item scale;
    public Item helmet;
    public Item chestplate;
    public Item leggings;
    public Item boots;
    public Block scaleBlock;

    EnumSeaSerpent(Formatting color) {
        this.resourceName = this.name().toLowerCase(Locale.ROOT);
        this.color = color;
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
