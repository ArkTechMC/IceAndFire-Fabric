package com.iafenvoy.iceandfire.compat.delight;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DelightFoodItem extends Item {
    public DelightFoodItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (!FabricLoader.getInstance().isModLoaded("farmersdelight"))
            tooltip.add(Text.translatable("item.iceandfire.tooltip.require.delight"));
    }
}
