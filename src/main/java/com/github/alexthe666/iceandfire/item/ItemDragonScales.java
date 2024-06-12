package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemDragonScales extends Item {
    final EnumDragonEgg type;

    public ItemDragonScales(EnumDragonEgg type) {
        super(new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
        this.type = type;
    }

    @Override
    public @NotNull String getTranslationKey() {
        return "item.iceandfire.dragonscales";
    }

    @Override
    public void appendTooltip(@NotNull ItemStack stack, World worldIn, List<Text> tooltip, @NotNull TooltipContext flagIn) {
        tooltip.add(Text.translatable("dragon." + this.type.toString().toLowerCase()).formatted(this.type.color));
    }

}
