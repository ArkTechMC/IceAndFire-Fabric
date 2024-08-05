package com.iafenvoy.iceandfire.enums;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.item.armor.IafArmorMaterial;
import com.iafenvoy.iceandfire.item.armor.ItemDragonArmor;
import com.iafenvoy.iceandfire.item.armor.ItemScaleArmor;
import com.iafenvoy.iceandfire.registry.IafItems;
import com.iafenvoy.uranus.object.IdUtil;
import com.iafenvoy.uranus.object.item.CustomArmorMaterial;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class EnumDragonArmor {
    public static final List<EnumDragonArmor> ARMORS = new ArrayList<>();
    public static final EnumDragonArmor RED = new EnumDragonArmor(EnumDragonColor.RED, () -> IafItems.DRAGONSCALES_RED);
    public static final EnumDragonArmor BRONZE = new EnumDragonArmor(EnumDragonColor.BRONZE, () -> IafItems.DRAGONSCALES_BRONZE);
    public static final EnumDragonArmor GREEN = new EnumDragonArmor(EnumDragonColor.GREEN, () -> IafItems.DRAGONSCALES_GREEN);
    public static final EnumDragonArmor GRAY = new EnumDragonArmor(EnumDragonColor.GRAY, () -> IafItems.DRAGONSCALES_GRAY);
    public static final EnumDragonArmor BLUE = new EnumDragonArmor(EnumDragonColor.BLUE, () -> IafItems.DRAGONSCALES_BLUE);
    public static final EnumDragonArmor WHITE = new EnumDragonArmor(EnumDragonColor.WHITE, () -> IafItems.DRAGONSCALES_WHITE);
    public static final EnumDragonArmor SAPPHIRE = new EnumDragonArmor(EnumDragonColor.SAPPHIRE, () -> IafItems.DRAGONSCALES_SAPPHIRE);
    public static final EnumDragonArmor SILVER = new EnumDragonArmor(EnumDragonColor.SILVER, () -> IafItems.DRAGONSCALES_SILVER);
    public static final EnumDragonArmor ELECTRIC = new EnumDragonArmor(EnumDragonColor.ELECTRIC, () -> IafItems.DRAGONSCALES_ELECTRIC);
    public static final EnumDragonArmor AMETHYST = new EnumDragonArmor(EnumDragonColor.AMETHYST, () -> IafItems.DRAGONSCALES_AMETHYST);
    public static final EnumDragonArmor COPPER = new EnumDragonArmor(EnumDragonColor.COPPER, () -> IafItems.DRAGONSCALES_COPPER);
    public static final EnumDragonArmor BLACK = new EnumDragonArmor(EnumDragonColor.BLACK, () -> IafItems.DRAGONSCALES_BLACK);

    private final EnumDragonColor color;
    private final Supplier<Item> repairItem;
    public CustomArmorMaterial material;
    public Item helmet;
    public Item chestplate;
    public Item leggings;
    public Item boots;
    public CustomArmorMaterial armorMaterial;

    public EnumDragonArmor(EnumDragonColor color, Supplier<Item> repairItem) {
        this.color = color;
        this.repairItem = repairItem;
        ARMORS.add(this);
    }

    public static void initArmors() {
        for (int i = 0; i < ARMORS.size(); i++) {
            EnumDragonArmor value = ARMORS.get(i);
            value.armorMaterial = new IafArmorMaterial(IdUtil.build(IceAndFire.MOD_ID, "armor_dragon_scales" + (i + 1)), 36, new int[]{5, 7, 9, 5}, 15, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 2);
            String sub = "armor_" + value.color.id().toLowerCase(Locale.ROOT);

            value.helmet = IafItems.register(sub + "_helmet", new ItemScaleArmor(value.color, value, value.armorMaterial, ArmorItem.Type.HELMET));
            value.chestplate = IafItems.register(sub + "_chestplate", new ItemScaleArmor(value.color, value, value.armorMaterial, ArmorItem.Type.CHESTPLATE));
            value.leggings = IafItems.register(sub + "_leggings", new ItemScaleArmor(value.color, value, value.armorMaterial, ArmorItem.Type.LEGGINGS));
            value.boots = IafItems.register(sub + "_boots", new ItemScaleArmor(value.color, value, value.armorMaterial, ArmorItem.Type.BOOTS));
        }
    }

    public Item getScaleItem() {
        return this.repairItem.get();
    }

    public EnumDragonColor getColor() {
        return this.color;
    }

    public static int getArmorOrdinal(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemDragonArmor armorItem)
            return EnumDragonArmorMaterial.MATERIALS.indexOf(armorItem.type) + 1;
        return 0;
    }
}
