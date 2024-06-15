package com.github.alexthe666.iceandfire.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemSeaSerpentScales extends ItemGeneric {

    private final Formatting color;
    private final String colorName;

    public ItemSeaSerpentScales(String colorName, Formatting color) {
        super();
        this.color = color;
        this.colorName = colorName;
    }


    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        tooltip.add(Text.translatable("sea_serpent." + this.colorName).formatted(this.color));
    }
}
