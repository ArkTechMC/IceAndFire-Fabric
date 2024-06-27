package com.iafenvoy.iceandfire.item.tool;

import com.iafenvoy.iceandfire.registry.IafItems;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemHippocampusSlapper extends SwordItem {
    public ItemHippocampusSlapper() {
        super(IafItems.HIPPOCAMPUS_SWORD_TOOL_MATERIAL, 3, -2.4F, new Settings()/*.tab(IceAndFire.TAB_ITEMS)*/);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity targetEntity, LivingEntity attacker) {
        targetEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2));
        targetEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 2));
        targetEntity.playSound(SoundEvents.ENTITY_GUARDIAN_FLOP, 3, 1);

        return super.postHit(stack, targetEntity, attacker);
    }

    @Override
    public void appendTooltip(ItemStack stack, World worldIn, List<Text> tooltip, TooltipContext flagIn) {
        tooltip.add(Text.translatable("item.iceandfire.legendary_weapon.desc").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.iceandfire.hippocampus_slapper.desc_0").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.iceandfire.hippocampus_slapper.desc_1").formatted(Formatting.GRAY));
    }
}