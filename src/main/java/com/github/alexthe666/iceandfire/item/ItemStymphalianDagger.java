package com.github.alexthe666.iceandfire.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemStymphalianDagger extends SwordItem {

    public ItemStymphalianDagger() {
        super(IafItemRegistry.STYMHALIAN_SWORD_TOOL_MATERIAL, 3, -1.0F, new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
    }


    @Override
    public boolean postHit(ItemStack stack, LivingEntity targetEntity, LivingEntity attacker) {
        return super.postHit(stack, targetEntity, attacker);
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        tooltip.add(Text.translatable("item.iceandfire.legendary_weapon.desc").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.iceandfire.stymphalian_bird_dagger.desc_0").formatted(Formatting.GRAY));
    }
}