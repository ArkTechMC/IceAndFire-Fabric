package com.github.alexthe666.iceandfire.item.tool;

import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class ItemDragonBow extends BowItem {
    private static final Predicate<ItemStack> DRAGON_ARROWS = stack -> stack.isIn(IafItemTags.DRAGON_ARROWS);

    public ItemDragonBow() {
        super(new Settings().maxDamage(584));
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return DRAGON_ARROWS;
    }
}
