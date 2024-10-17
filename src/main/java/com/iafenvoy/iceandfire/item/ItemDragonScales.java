package com.iafenvoy.iceandfire.item;

import com.iafenvoy.iceandfire.data.DragonColor;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;
import java.util.Locale;

public class ItemDragonScales extends Item {
    final DragonColor type;

    public ItemDragonScales(DragonColor type) {
        super(new Settings());
        this.type = type;
    }

    @Override
    public String getTranslationKey() {
        return "item.iceandfire.dragonscales";
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        tooltip.add(Text.translatable("dragon." + this.type.name().toLowerCase(Locale.ROOT)).formatted(this.type.color()));
    }
}
