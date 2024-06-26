package com.iafenvoy.iceandfire.item.armor;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.enums.EnumDragonArmorMaterial;
import com.iafenvoy.iceandfire.enums.EnumDragonArmorPart;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemDragonArmor extends Item {
    public final EnumDragonArmorMaterial type;
    public final EnumDragonArmorPart dragonSlot;
    public String name;
    private Pattern baseName = Pattern.compile("[a-z]+_[a-z]+");

    public ItemDragonArmor(EnumDragonArmorMaterial type, EnumDragonArmorPart dragonSlot) {
        super(new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/.maxCount(1));
        this.type = type;
        this.dragonSlot = dragonSlot;
        if (type == EnumDragonArmorMaterial.DRAGONSTEEL_FIRE || type == EnumDragonArmorMaterial.DRAGONSTEEL_ICE || type == EnumDragonArmorMaterial.DRAGONSTEEL_LIGHTNING)
            this.baseName = Pattern.compile("[a-z]+_[a-z]+_[a-z]+");
    }

    public static String getNameForSlot(EnumDragonArmorPart slot) {
        return slot.name().toLowerCase();
    }

    @Override
    public String getTranslationKey() {
        String fullName = Registries.ITEM.getId(this).getPath();
        Matcher matcher = this.baseName.matcher(fullName);
        this.name = matcher.find() ? matcher.group() : fullName;
        return "item." + IceAndFire.MOD_ID + "." + this.name;
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        String words = "dragon.armor_" + this.dragonSlot.name().toLowerCase();
        tooltip.add(Text.translatable(words).formatted(Formatting.GRAY));
    }
}
