package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.citadel.server.item.CustomArmorMaterial;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.armor.IafArmorMaterial;
import com.github.alexthe666.iceandfire.item.armor.ItemScaleArmor;
import com.github.alexthe666.iceandfire.registry.IafItems;
import dev.arktechmc.iafextra.util.IdUtil;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvents;

public enum EnumDragonArmor {

    armor_red(12, EnumDragonEgg.RED),
    armor_bronze(13, EnumDragonEgg.BRONZE),
    armor_green(14, EnumDragonEgg.GREEN),
    armor_gray(15, EnumDragonEgg.GRAY),
    armor_blue(12, EnumDragonEgg.BLUE),
    armor_white(13, EnumDragonEgg.WHITE),
    armor_sapphire(14, EnumDragonEgg.SAPPHIRE),
    armor_silver(15, EnumDragonEgg.SILVER),
    armor_electric(12, EnumDragonEgg.ELECTRIC),
    armor_amythest(13, EnumDragonEgg.AMYTHEST),
    armor_copper(14, EnumDragonEgg.COPPER),
    armor_black(15, EnumDragonEgg.BLACK);

    public final int armorId;
    public final EnumDragonEgg eggType;
    public CustomArmorMaterial material;
    public Item helmet;
    public Item chestplate;
    public Item leggings;
    public Item boots;
    public CustomArmorMaterial armorMaterial;

    EnumDragonArmor(int armorId, EnumDragonEgg eggType) {
        this.armorId = armorId;
        this.eggType = eggType;
    }

    public static void initArmors() {
        for (int i = 0; i < EnumDragonArmor.values().length; i++) {
            EnumDragonArmor.values()[i].armorMaterial = new IafArmorMaterial(IdUtil.build(IceAndFire.MOD_ID, "armor_dragon_scales" + (i + 1)), 36, new int[]{5, 7, 9, 5}, 15, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 2);
            String sub = EnumDragonArmor.values()[i].name();

            int finalI = i;
            EnumDragonArmor.values()[finalI].helmet = IafItems.register(sub + "_helmet", new ItemScaleArmor(EnumDragonArmor.values()[finalI].eggType, EnumDragonArmor.values()[finalI], EnumDragonArmor.values()[finalI].armorMaterial, ArmorItem.Type.HELMET));
            EnumDragonArmor.values()[finalI].chestplate = IafItems.register(sub + "_chestplate", new ItemScaleArmor(EnumDragonArmor.values()[finalI].eggType, EnumDragonArmor.values()[finalI], EnumDragonArmor.values()[finalI].armorMaterial, ArmorItem.Type.CHESTPLATE));
            EnumDragonArmor.values()[finalI].leggings = IafItems.register(sub + "_leggings", new ItemScaleArmor(EnumDragonArmor.values()[finalI].eggType, EnumDragonArmor.values()[finalI], EnumDragonArmor.values()[finalI].armorMaterial, ArmorItem.Type.LEGGINGS));
            EnumDragonArmor.values()[finalI].boots = IafItems.register(sub + "_boots", new ItemScaleArmor(EnumDragonArmor.values()[finalI].eggType, EnumDragonArmor.values()[finalI], EnumDragonArmor.values()[finalI].armorMaterial, ArmorItem.Type.BOOTS));
        }
    }

    public static Item getScaleItem(EnumDragonArmor armor) {
        return switch (armor) {
            case armor_bronze -> IafItems.DRAGONSCALES_BRONZE;
            case armor_green -> IafItems.DRAGONSCALES_GREEN;
            case armor_gray -> IafItems.DRAGONSCALES_GRAY;
            case armor_blue -> IafItems.DRAGONSCALES_BLUE;
            case armor_white -> IafItems.DRAGONSCALES_WHITE;
            case armor_sapphire -> IafItems.DRAGONSCALES_SAPPHIRE;
            case armor_silver -> IafItems.DRAGONSCALES_SILVER;
            case armor_electric -> IafItems.DRAGONSCALES_ELECTRIC;
            case armor_amythest -> IafItems.DRAGONSCALES_AMYTHEST;
            case armor_copper -> IafItems.DRAGONSCALES_COPPER;
            case armor_black -> IafItems.DRAGONSCALES_BLACK;
            default -> IafItems.DRAGONSCALES_RED;
        };
    }
}
