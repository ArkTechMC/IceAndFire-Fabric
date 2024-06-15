package com.github.alexthe666.iceandfire.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemGeneric extends Item {
    int description = 0;

    public ItemGeneric() {
        super(new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
    }

    public ItemGeneric(int textLength) {
        super(new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
        this.description = textLength;
    }

    public ItemGeneric(int textLength, boolean hide) {
        super(new Settings());
        this.description = textLength;
    }

    public ItemGeneric(int textLength, int stacksize) {
        super(new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/.maxCount(stacksize));
        this.description = textLength;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        if (this == IafItemRegistry.CREATIVE_DRAGON_MEAL.get()) {
            return true;
        } else {
            return super.hasGlint(stack);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        if (this.description > 0) {
            for (int i = 0; i < this.description; i++) {
                tooltip.add(Text.translatable(this.getTranslationKey() + ".desc_" + i).formatted(Formatting.GRAY));
            }
        }
    }
}
