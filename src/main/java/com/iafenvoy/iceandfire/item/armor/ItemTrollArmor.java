package com.iafenvoy.iceandfire.item.armor;

import com.iafenvoy.uranus.server.item.CustomArmorMaterial;
import com.iafenvoy.iceandfire.enums.EnumTroll;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.Locale;

public class ItemTrollArmor extends ArmorItem {
    public final EnumTroll troll;

    public ItemTrollArmor(EnumTroll troll, CustomArmorMaterial material, Type slot) {
        super(material, slot, new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
        this.troll = troll;
    }

    public static String getName(EnumTroll troll, EquipmentSlot slot) {
        return "%s_troll_leather_%s".formatted(troll.name().toLowerCase(Locale.ROOT), getArmorPart(slot));
    }

    private static String getArmorPart(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> "helmet";
            case CHEST -> "chestplate";
            case LEGS -> "leggings";
            case FEET -> "boots";
            default -> "";
        };
    }

    @Override
    public ArmorMaterial getMaterial() {
        return this.troll.material;
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        tooltip.add(Text.translatable("item.iceandfire.troll_leather_armor_" + getArmorPart(this.type.getEquipmentSlot()) + ".desc").formatted(Formatting.GREEN));
    }
}
