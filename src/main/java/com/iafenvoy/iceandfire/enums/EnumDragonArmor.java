package com.iafenvoy.iceandfire.enums;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.item.armor.IafArmorMaterial;
import com.iafenvoy.iceandfire.item.armor.ItemDragonArmor;
import com.iafenvoy.iceandfire.item.armor.ItemScaleArmor;
import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.uranus.server.item.CustomArmorMaterial;
import com.iafenvoy.uranus.util.IdUtil;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;

import java.util.Locale;

public enum EnumDragonArmor {
    RED(12, EnumDragonColor.RED),
    BRONZE(13, EnumDragonColor.BRONZE),
    GREEN(14, EnumDragonColor.GREEN),
    GRAY(15, EnumDragonColor.GRAY),
    BLUE(12, EnumDragonColor.BLUE),
    WHITE(13, EnumDragonColor.WHITE),
    SAPPHIRE(14, EnumDragonColor.SAPPHIRE),
    SILVER(15, EnumDragonColor.SILVER),
    ELECTRIC(12, EnumDragonColor.ELECTRIC),
    AMETHYST(13, EnumDragonColor.AMETHYST),
    COPPER(14, EnumDragonColor.COPPER),
    BLACK(15, EnumDragonColor.BLACK);

    public final int armorId;
    public final EnumDragonColor color;
    public CustomArmorMaterial material;
    public Item helmet;
    public Item chestplate;
    public Item leggings;
    public Item boots;
    public CustomArmorMaterial armorMaterial;

    EnumDragonArmor(int armorId, EnumDragonColor color) {
        this.armorId = armorId;
        this.color = color;
    }

    public static void initArmors() {
        for (int i = 0; i < EnumDragonArmor.values().length; i++) {
            EnumDragonArmor value = EnumDragonArmor.values()[i];
            value.armorMaterial = new IafArmorMaterial(IdUtil.build(IceAndFire.MOD_ID, "armor_dragon_scales" + (i + 1)), 36, new int[]{5, 7, 9, 5}, 15, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 2);
            String sub = "armor_" + value.name().toLowerCase(Locale.ROOT);

            value.helmet = IafItems.register(sub + "_helmet", new ItemScaleArmor(value.color, value, value.armorMaterial, ArmorItem.Type.HELMET));
            value.chestplate = IafItems.register(sub + "_chestplate", new ItemScaleArmor(value.color, value, value.armorMaterial, ArmorItem.Type.CHESTPLATE));
            value.leggings = IafItems.register(sub + "_leggings", new ItemScaleArmor(value.color, value, value.armorMaterial, ArmorItem.Type.LEGGINGS));
            value.boots = IafItems.register(sub + "_boots", new ItemScaleArmor(value.color, value, value.armorMaterial, ArmorItem.Type.BOOTS));
        }
    }

    public static Item getScaleItem(EnumDragonArmor armor) {
        return switch (armor) {
            case BRONZE -> IafItems.DRAGONSCALES_BRONZE;
            case GREEN -> IafItems.DRAGONSCALES_GREEN;
            case GRAY -> IafItems.DRAGONSCALES_GRAY;
            case BLUE -> IafItems.DRAGONSCALES_BLUE;
            case WHITE -> IafItems.DRAGONSCALES_WHITE;
            case SAPPHIRE -> IafItems.DRAGONSCALES_SAPPHIRE;
            case SILVER -> IafItems.DRAGONSCALES_SILVER;
            case ELECTRIC -> IafItems.DRAGONSCALES_ELECTRIC;
            case AMETHYST -> IafItems.DRAGONSCALES_amethyst;
            case COPPER -> IafItems.DRAGONSCALES_COPPER;
            case BLACK -> IafItems.DRAGONSCALES_BLACK;
            default -> IafItems.DRAGONSCALES_RED;
        };
    }

    public static int getArmorOrdinal(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemDragonArmor armorItem)
            return armorItem.type.ordinal() + 1;
        return 0;
    }
}
